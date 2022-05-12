package com.triquang.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.common.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
