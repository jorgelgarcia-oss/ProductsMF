package com.example.app.controller;

import com.example.app.dto.ProductDto;
import com.example.app.service.ProductService;
import com.example.app.service.ProductService.Mode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
@Tag(name = "Product API", description = "CRUD operations for products")
public class ProductRestController {

  private final ProductService productService;

  public ProductRestController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "List all products")
  @GetMapping
  public ResponseEntity<List<ProductDto>> listAll() {
    List<ProductDto> products = productService.listAll();
    return ResponseEntity.ok(products);
  }

  @Operation(summary = "Get product by ID")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getById(@PathVariable("id") Long id) {
    try {
      ProductDto dto = productService.findById(id);
      return ResponseEntity.ok(dto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Enter Add mode")
  @PostMapping("/mode/add")
  public ResponseEntity<Void> enterAddMode() {
    productService.enterAddMode();
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Enter Edit mode")
  @PostMapping("/mode/edit")
  public ResponseEntity<Void> enterEditMode() {
    productService.enterEditMode();
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Enter Delete mode")
  @PostMapping("/mode/delete")
  public ResponseEntity<Void> enterDeleteMode() {
    productService.enterDeleteMode();
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Save product (Add/Edit/Delete)")
  @PostMapping
  public ResponseEntity<?> save(@Valid @RequestBody ProductDto dto) {
    try {
      ProductDto saved = productService.save(dto);
      if (productService.getCurrentMode() == Mode.Idle) {
        return ResponseEntity.ok(saved);
      }
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid state after save");
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @Operation(summary = "Exit application (sets mode to Idle)")
  @PostMapping("/mode/exit")
  public ResponseEntity<Void> exitApplication() {
    productService.exitMode();
    return ResponseEntity.ok().build();
  }
}