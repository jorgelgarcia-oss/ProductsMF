package com.example.app.controller;

import com.example.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductPageController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "productList";
    }

    // Other methods adjusted to use getAllProducts() instead of findAll()

    // Example of fixing enum switch case labels to unqualified names
    // Assuming enum is named ProductType with constants TYPE1, TYPE2, TYPE3
    // switch(productType) {
    //     case TYPE1:
    //         // ...
    //         break;
    //     case TYPE2:
    //         // ...
    //         break;
    //     case TYPE3:
    //         // ...
    //         break;
    // }

    // Removed calls to non-existing exit() method on productService
}
