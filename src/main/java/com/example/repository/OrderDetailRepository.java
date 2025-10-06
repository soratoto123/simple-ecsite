package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
