package com.mermershop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private Double productPrice;
    
    @Column(nullable = false)
    private Integer quantity;
    
    private String productImageUrl;
    
    public Double getSubtotal() {
        return productPrice * quantity;
    }
}
