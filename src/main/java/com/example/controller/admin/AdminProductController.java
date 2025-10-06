package com.example.controller.admin;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Product;
import com.example.service.ProductService;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    // 商品一覧表示
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products/list";
    }

    // 商品作成フォーム表示
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/products/form";
    }

    // 商品作成処理
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/products/form";
        }
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    // 商品編集フォーム表示
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                            .orElseThrow(() -> new IllegalArgumentException("無効な商品ID: " + id));
        model.addAttribute("product", product);
        return "admin/products/form";
    }

    // 商品更新処理
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct) {
        Product product = productService.getProductById(id)
                            .orElseThrow(() -> new IllegalArgumentException("無効な商品ID: " + id));
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setCategory(updatedProduct.getCategory());
        product.setImageUrl(updatedProduct.getImageUrl());
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    // 商品削除処理
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        try {
            productService.deleteProduct(id);
            return "redirect:/admin/products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "注文済みの商品のため削除できません。");
            model.addAttribute("products", productService.getAllProducts());
            return "admin/products/list";
        }
    }
}
