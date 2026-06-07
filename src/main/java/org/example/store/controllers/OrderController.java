package org.example.store.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store.dto.OrderRequest;
import org.example.store.entities.Order;
import org.example.store.services.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Order createOrder( @Valid @RequestBody OrderRequest orderRequest){
        return  orderService.createOrder(orderRequest);

    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }



}

