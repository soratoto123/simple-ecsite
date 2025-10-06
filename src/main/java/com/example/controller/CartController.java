package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.CartItem;
import com.example.entity.User;
import com.example.security.CustomUserDetails;
import com.example.service.CartService;
import com.example.service.PurchaseService;

@Controller
@RequestMapping("/cartitems")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public String listCartItems(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = purchaseService.getUserByEmail(userDetails.getUsername());
        List<CartItem> cartItems = cartService.getCartItemsByUser(user);
        int total=0;
        for(CartItem cartItem:cartItems) {
            total+=cartItem.getProduct().getPrice()*cartItem.getCount();
        }
        model.addAttribute("cartitems", cartItems);
        model.addAttribute("total", total);
        return "cartitems/list";
    }

    @PostMapping("/add")
    public String addToCartItems(@RequestParam Long productId,
                                 @RequestParam int quantity,
                                 @AuthenticationPrincipal CustomUserDetails userDetails, 
                                 Model model) {
        User user = purchaseService.getUserByEmail(userDetails.getUsername());
        try {
            cartService.addCartItem(user, productId, quantity);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/products/" + productId;
    }

    @PostMapping("/remove/{id}")
    public String removeFromCartItems(@PathVariable Long id, 
                                      @AuthenticationPrincipal CustomUserDetails userDetails, 
                                      Model model) {
        User user = purchaseService.getUserByEmail(userDetails.getUsername());
        try {
            cartService.removeCartItem(id, user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/cartitems";
    }

}
