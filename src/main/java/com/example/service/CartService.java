package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.repository.CartItemRepository;
import com.example.repository.ProductRepository;

@Service
public class CartService {
    @Autowired
    CartItemRepository cartItemRepository;
    
    @Autowired
    ProductRepository productRepository;
    
    public List<CartItem> getCartItemsByUser(User user){
        return cartItemRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public Optional<CartItem> getCartItemsByUserAndProduct(User user, Product product) {
        return cartItemRepository.findByUserAndProduct(user, product);
    }
    
    public void addCartItem(User user, Long productId,int quantity) throws Exception {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new Exception("商品が存在しません。");
        }
        
        Product product = productOpt.get();

        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setCount(cartItem.getCount() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setCount(quantity);
            cartItemRepository.save(cartItem);
        }

    }

    public void removeCartItem(Long cartitemId, User user) throws Exception {
        Optional<CartItem> cartitemOpt = cartItemRepository.findById(cartitemId);
        if (!cartitemOpt.isPresent()) {
            throw new Exception("カートに商品が存在しません。");
        }

        CartItem cartitem = cartitemOpt.get();

        if (!cartitem.getUser().equals(user)) {
            throw new Exception("他のユーザーの商品を削除することはできません。");
        }

        cartItemRepository.delete(cartitem);
    }

    public boolean isCartItem(User user, Product product) {
        return cartItemRepository.findByUserAndProduct(user, product).isPresent();
    }

}
