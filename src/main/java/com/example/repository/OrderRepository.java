package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // ユーザーの注文を取得
    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);

    // 特定の注文をユーザーでフィルタリング
    Optional<Order> findByIdAndUserEmail(Long id, String email);

    // すべての注文を取得（管理者用）
    List<Order> findAllByOrderByCreatedAtDesc();
}

