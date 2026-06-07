package org.example.store.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.store.dto.OrderItemRequest;
import org.example.store.dto.OrderRequest;
import org.example.store.entities.Order;
import org.example.store.entities.OrderItem;
import org.example.store.entities.Product;
import org.example.store.repositories.OrderItemRepository;
import org.example.store.repositories.OrderRepository;
import org.example.store.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setStatus("CONFIRMED");

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


    public Order getOrderById(Long id ){
      return orderRepository.findById(id)
              .orElseThrow(()-> new RuntimeException("Order not found  for id " +id));
    }
}