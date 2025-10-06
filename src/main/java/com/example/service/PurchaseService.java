package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.CartItem;
import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.entity.User;
import com.example.repository.OrderDetailRepository;
import com.example.repository.OrderRepository;

@Service
public class PurchaseService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserService userService;

    // ユーザー情報取得
    public User getUserByEmail(String email) {
        return userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));
    }

    // 購入処理
    @Transactional
    public void processPurchase(User user, List<CartItem> cartItems) throws Exception {

        Integer totalPrice = 0;
        
        // 注文を作成
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
                totalPrice += cartItem.getProduct().getPrice()*cartItem.getCount();
            // 注文詳細を作成
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setPrice(cartItem.getProduct().getPrice()*cartItem.getCount());
            orderDetail.setCount(cartItem.getCount());
            orderDetailRepository.save(orderDetail);
        }
        
        // 注文を更新
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }
}
