package org.example.store.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.example.store.dto.OrderRequest;
import org.example.store.entities.Order;
import org.example.store.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Order createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            Authentication authentication
    ) {
        String email = null;

        if (authentication != null && authentication.getName() != null) {
            email = authentication.getName();
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            email = auth.getName();
        }

        return orderService.createOrder(orderRequest, email);
    }

    @GetMapping
    public List<Order> getOrder(){
        return orderService.getOrder();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }



}

