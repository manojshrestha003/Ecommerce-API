package org.example.store.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest
{
    @NotBlank(message= "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Enter a valid Email")
    private String customerEmail;


    @Valid
    @NotEmpty(message = "Order must contain at least one item ")
    private List<OrderItemRequest> items;
}
