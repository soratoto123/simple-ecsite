package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.entity.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserOrderByCreatedAtDesc(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);

}
