package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Product;
import com.example.entity.User;
import com.example.security.CustomUserDetails;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.PurchaseService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PurchaseService purchaseService;
    
    @Autowired
    private CartService cartService;

    // 商品一覧表示
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/list";
    }

    // 商品詳細表示
    @GetMapping("/{id}")
    public String showProduct(@PathVariable Long id,
                              @AuthenticationPrincipal CustomUserDetails userDetails, 
                              Model model) {
        Product product = productService.getProductById(id)
                            .orElseThrow(() -> new IllegalArgumentException("無効な商品ID: " + id));

        // カート判定
        boolean isCartItem = false;
        if (userDetails != null) {
            User user = purchaseService.getUserByEmail(userDetails.getUsername());
            isCartItem = cartService.isCartItem(user, product);
        }

        model.addAttribute("product", product);
        model.addAttribute("isCartItem", isCartItem);
        return "products/detail";
    }
}
