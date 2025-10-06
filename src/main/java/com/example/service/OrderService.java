package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Order;
import com.example.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // ユーザーの注文を取得
    public List<Order> getOrdersByUser(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    // 特定の注文を取得（ユーザー用）
    public Optional<Order> getOrderByIdAndUser(Long id, String email) {
        return orderRepository.findByIdAndUserEmail(id, email);
    }

    // すべての注文を取得（管理者用）
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    // 特定の注文を取得（管理者用）
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // 注文を保存
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
