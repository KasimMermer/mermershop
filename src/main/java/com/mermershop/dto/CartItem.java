package com.mermershop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImageUrl;
    private Integer quantity;
    
    public Double getSubtotal() {
        return productPrice * quantity;
    }
}
