package com.mermershop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mermershop.dto.CartItem;
import com.mermershop.model.Order;
import com.mermershop.model.OrderItem;
import com.mermershop.model.User;
import com.mermershop.repository.OrderRepository;
import com.mermershop.repository.UserRepository;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartService cartService;
    
    @Transactional
    public Order createOrder(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
        
        List<CartItem> cartItems = cartService.getCartItems();
        
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Warenkorb ist leer");
        }
        
        // Erstelle Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cartService.getTotal());
        
        // Erstelle OrderItems
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setProductPrice(cartItem.getProductPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductImageUrl(cartItem.getProductImageUrl());
            
            order.getItems().add(orderItem);
        }
        
        // Speichere Order (Items werden durch CASCADE auch gespeichert)
        Order savedOrder = orderRepository.save(order);
        
        // Leere Warenkorb
        cartService.clearCart();
        
        return savedOrder;
    }
    
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }
}
