package com.mermershop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mermershop.dto.CartItem;
import com.mermershop.model.Product;
import com.mermershop.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CartService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private HttpSession session;
    
    private static final String CART_SESSION_KEY = "cart";
    
    @SuppressWarnings("unchecked")
    private List<CartItem> getCart() {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }
    
    public List<CartItem> getCartItems() {
        return getCart();
    }
    
    public void addToCart(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produkt nicht gefunden"));
        
        List<CartItem> cart = getCart();
        
        Optional<CartItem> existingItem = cart.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst();
        
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                quantity
            );
            cart.add(newItem);
        }
        
        session.setAttribute(CART_SESSION_KEY, cart);
    }
    
    public void updateQuantity(Long productId, Integer quantity) {
        List<CartItem> cart = getCart();
        
        cart.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst()
            .ifPresent(item -> item.setQuantity(quantity));
        
        session.setAttribute(CART_SESSION_KEY, cart);
    }
    
    public void removeFromCart(Long productId) {
        List<CartItem> cart = getCart();
        cart.removeIf(item -> item.getProductId().equals(productId));
        session.setAttribute(CART_SESSION_KEY, cart);
    }
    
    public void clearCart() {
        session.setAttribute(CART_SESSION_KEY, new ArrayList<CartItem>());
    }
    
    public Double getTotal() {
        return getCart().stream()
            .mapToDouble(CartItem::getSubtotal)
            .sum();
    }
    
    public Integer getItemCount() {
        return getCart().stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
}
