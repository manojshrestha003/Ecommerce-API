package org.example.store.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.store.dto.OrderItemRequest;
import org.example.store.dto.OrderRequest;
import org.example.store.entities.Order;
import org.example.store.entities.OrderItem;
import org.example.store.entities.Product;
import org.example.store.repositories.OrderRepository;
import org.example.store.repositories.ProductRepository;
import org.example.store.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
        private final UserRepository userRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest, String name) {

        // resolve user by email (name) and attach to order
        var user = userRepository.findByEmail(name)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + name));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("CONFIRMED");
        order.setTotalPrice(BigDecimal.ZERO);
        order.setOrderItems(new ArrayList<>());

        // Save order first to get ID for cascading items
        order = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // IMPORTANT: make sure OrderRequest has getItems()
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found with id " + itemRequest.getProductId()
                            )
                    );

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            BigDecimal itemTotalPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalPrice = totalPrice.add(itemTotalPrice);

            // reduce stock
            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.getQuantity()
            );

            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    public List<Order> getOrder(){
        return orderRepository.findAll();
    }


    public Order getOrderById(Long id ){
      return orderRepository.findById(id)
              .orElseThrow(()-> new RuntimeException("Order not found  for id " +id));
    }
}