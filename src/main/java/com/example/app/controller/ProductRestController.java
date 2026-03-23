package com.example.app.controller;

import com.example.app.dto.ProductDto;
import com.example.app.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Tag(name = "Product API", description = "CRUD operations for products")
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductRestController {

  private final ProductService productService;

  public ProductRestController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "Get all products")
  @GetMapping
  public ResponseEntity<List<ProductDto>> getAll() {
    List<ProductDto> products = productService.findAll();
    return ResponseEntity.ok(products);
  }

  @Operation(summary = "Get product by ID")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getById(@PathVariable("id") Long id) {
    try {
      ProductDto dto = productService.findById(id);
      return ResponseEntity.ok(dto);
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Create a new product")
  @PostMapping
  public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
    productService.enterAddMode();
    try {
      ProductDto saved = productService.save(dto);
      return new ResponseEntity<>(saved, HttpStatus.CREATED);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Update an existing product")
  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> update(@PathVariable("id") Long id, @Valid @RequestBody ProductDto dto) {
    productService.enterEditMode();
    if (!id.equals(dto.getId())) {
      return ResponseEntity.badRequest().build();
    }
    try {
      ProductDto updated = productService.save(dto);
      return ResponseEntity.ok(updated);
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.notFound().build();
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Delete a product")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    productService.enterDeleteMode();
    try {
      productService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }
}