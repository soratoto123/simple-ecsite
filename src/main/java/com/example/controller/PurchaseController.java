package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.CartItem;
import com.example.entity.User;
import com.example.security.CustomUserDetails;
import com.example.service.CartService;
import com.example.service.PurchaseService;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private CartService cartService;

    // まとめて購入処理
    @PostMapping("/all")
    public String bulkPurchase(@AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {
        User user = purchaseService.getUserByEmail(userDetails.getUsername());
        List<CartItem> cartItems = cartService.getCartItemsByUser(user);
        try {
            purchaseService.processPurchase(user, cartItems);
            for(CartItem cartItem : cartItems) {
                cartService.removeCartItem(cartItem.getId(), user);
            }
            return "purchase/complete";
        } catch (Exception e) {
            int total=0;
            for(CartItem cartItem:cartItems) {
                total+=cartItem.getProduct().getPrice()*cartItem.getCount();
            }
            model.addAttribute("cartitems", cartItems);
            model.addAttribute("total", total);
            return "cartitems/list";
        }
    }
}
