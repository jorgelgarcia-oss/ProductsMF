package com.example.app.controller;

import com.example.app.dto.ProductDto;
import com.example.app.entity.Product;
import com.example.app.service.ProductService;
import com.example.app.service.ProductService.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductPageController {

  private final ProductService productService;

  public ProductPageController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public String listProducts(Model model) {
    List<ProductDto> products = productService.findAll();
    model.addAttribute("products", products);
    model.addAttribute("mode", productService.getCurrentMode().name());
    model.addAttribute("productDto", new ProductDto());
    return "product";
  }

  @PostMapping(params = "action=add")
  public String enterAddMode(Model model) {
    productService.enterAddMode();
    model.addAttribute("mode", productService.getCurrentMode().name());
    model.addAttribute("productDto", new ProductDto());
    model.addAttribute("products", productService.findAll());
    return "product";
  }

  @PostMapping(params = "action=edit")
  public String enterEditMode(Model model) {
    productService.enterEditMode();
    model.addAttribute("mode", productService.getCurrentMode().name());
    model.addAttribute("productDto", new ProductDto());
    model.addAttribute("products", productService.findAll());
    return "product";
  }

  @PostMapping(params = "action=delete")
  public String enterDeleteMode(Model model) {
    productService.enterDeleteMode();
    model.addAttribute("mode", productService.getCurrentMode().name());
    model.addAttribute("productDto", new ProductDto());
    model.addAttribute("products", productService.findAll());
    return "product";
  }

  @PostMapping(params = "action=save")
  public String saveProduct(@Valid @ModelAttribute("productDto") ProductDto productDto,
                            BindingResult bindingResult,
                            Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("mode", productService.getCurrentMode().name());
      model.addAttribute("products", productService.findAll());
      return "product";
    }

    try {
      switch (productService.getCurrentMode()) {
        case ADD:
          productService.save(productDto);
          break;
        case EDIT:
          productService.save(productDto);
          break;
        case DELETE:
          productService.delete(productDto.getId());
          break;
        default:
          break;
      }
      productService.exit();
      return "redirect:/product";
    } catch (EntityNotFoundException ex) {
      bindingResult.rejectValue("id", "error.productDto", "Product not found");
      model.addAttribute("mode", productService.getCurrentMode().name());
      model.addAttribute("products", productService.findAll());
      return "product";
    } catch (IllegalArgumentException ex) {
      bindingResult.reject("error.productDto", ex.getMessage());
      model.addAttribute("mode", productService.getCurrentMode().name());
      model.addAttribute("products", productService.findAll());
      return "product";
    }
  }

  @PostMapping(params = "action=exit")
  public String exitApplication() {
    productService.exit();
    return "redirect:/";
  }

  @PostMapping(params = "action=load")
  public String loadProductById(@RequestParam("id") Long id, Model model) {
    try {
      ProductDto dto = productService.findById(id);
      model.addAttribute("productDto", dto);
    } catch (EntityNotFoundException ex) {
      model.addAttribute("productDto", new ProductDto());
      model.addAttribute("loadError", "Product not found with ID: " + id);
    }
    model.addAttribute("mode", productService.getCurrentMode().name());
    model.addAttribute("products", productService.findAll());
    return "product";
  }
}