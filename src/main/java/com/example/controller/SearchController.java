package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Product;
import com.example.repository.ProductRepository;

@Controller
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    ProductRepository productRepository;
    
    @GetMapping
    public String showSearchPage() {
        return "search/list";
    }
    
    @GetMapping("/{category}")
    public String searchProduct(@PathVariable String category, Model model) {
        List<Product> products = productRepository.findByCategory(category);
        model.addAttribute("products", products);
        return "products/list";
    }

}
