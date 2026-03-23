package com.example.app.controller;

import com.example.app.dto.ProductDto;
import com.example.app.service.ProductService;
import com.example.app.service.ProductService.Mode;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/product")
public class ProductWebController {

  private final ProductService productService;

  public ProductWebController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public String showProductPage(Model model) {
    if (!model.containsAttribute("productDto")) {
      model.addAttribute("productDto", new ProductDto());
    }
    model.addAttribute("mode", productService.getCurrentMode());
    model.addAttribute("products", productService.listAll());
    return "product";
  }

  @PostMapping("/add")
  public String enterAddMode(RedirectAttributes redirectAttributes) {
    productService.enterAddMode();
    redirectAttributes.addFlashAttribute("productDto", new ProductDto());
    return "redirect:/product";
  }

  @PostMapping("/edit")
  public String enterEditMode(RedirectAttributes redirectAttributes) {
    productService.enterEditMode();
    redirectAttributes.addFlashAttribute("productDto", new ProductDto());
    return "redirect:/product";
  }

  @PostMapping("/delete")
  public String enterDeleteMode(RedirectAttributes redirectAttributes) {
    productService.enterDeleteMode();
    redirectAttributes.addFlashAttribute("productDto", new ProductDto());
    return "redirect:/product";
  }

  @PostMapping("/save")
  public String save(@Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult bindingResult,
                     RedirectAttributes redirectAttributes, Model model) {
    Mode mode = productService.getCurrentMode();

    if (mode == Mode.Idle) {
      redirectAttributes.addFlashAttribute("errorMessage", "Not in Add, Edit or Delete mode");
      return "redirect:/product";
    }

    // In Edit or Delete mode, load product details by ID if ID is entered
    if ((mode == Mode.Edit || mode == Mode.Delete) && productDto.getId() != null && !bindingResult.hasFieldErrors("id")) {
      try {
        ProductDto loaded = productService.findById(productDto.getId());
        redirectAttributes.addFlashAttribute("productDto", loaded);
        redirectAttributes.addFlashAttribute("mode", mode);
        return "redirect:/product";
      } catch (EntityNotFoundException e) {
        bindingResult.rejectValue("id", "notfound", "Product not found with ID: " + productDto.getId());
      }
    }

    if (bindingResult.hasErrors()) {
      model.addAttribute("mode", mode);
      model.addAttribute("products", productService.listAll());
      return "product";
    }

    try {
      productService.save(productDto);
      redirectAttributes.addFlashAttribute("successMessage", "Operation successful");
    } catch (EntityNotFoundException e) {
      bindingResult.rejectValue("id", "notfound", e.getMessage());
      model.addAttribute("mode", mode);
      model.addAttribute("products", productService.listAll());
      return "product";
    } catch (IllegalStateException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/product";
    }

    return "redirect:/product";
  }

  @PostMapping("/exit")
  public String exitApplication(RedirectAttributes redirectAttributes) {
    productService.exitMode();
    redirectAttributes.addFlashAttribute("successMessage", "Exited application mode");
    return "redirect:/product";
  }
}