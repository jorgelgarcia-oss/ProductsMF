package com.example.app.service;

import com.example.app.dto.ProductDto;
import com.example.app.entity.Product;
import com.example.app.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

  public enum Mode {
    Idle,
    Add,
    Edit,
    Delete
  }

  private final ProductRepository productRepository;

  private Mode currentMode = Mode.Idle;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mode getCurrentMode() {
    return currentMode;
  }

  public void enterAddMode() {
    currentMode = Mode.Add;
  }

  public void enterEditMode() {
    currentMode = Mode.Edit;
  }

  public void enterDeleteMode() {
    currentMode = Mode.Delete;
  }

  public void exitMode() {
    currentMode = Mode.Idle;
  }

  public List<ProductDto> listAll() {
    List<Product> products = productRepository.findAll();
    return products.stream().map(this::toDto).collect(Collectors.toList());
  }

  public ProductDto findById(Long id) {
    Optional<Product> product = productRepository.findById(id);
    if (!product.isPresent()) {
      throw new EntityNotFoundException("Product not found with ID: " + id);
    }
    return toDto(product.get());
  }

  public ProductDto save(ProductDto dto) {
    if (currentMode == Mode.Idle) {
      throw new IllegalStateException("Cannot save product when not in Add, Edit or Delete mode");
    }
    if (currentMode == Mode.Delete) {
      delete(dto.getId());
      exitMode();
      return null;
    }
    Product product = toEntity(dto);
    productRepository.save(product);
    exitMode();
    return toDto(product);
  }

  public void delete(Long id) {
    if (!productRepository.existsById(id)) {
      throw new EntityNotFoundException("Product not found with ID: " + id);
    }
    productRepository.deleteById(id);
  }

  private ProductDto toDto(Product product) {
    ProductDto dto = new ProductDto();
    dto.setId(product.getId());
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    return dto;
  }

  private Product toEntity(ProductDto dto) {
    Product product = new Product();
    product.setId(dto.getId());
    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    return product;
  }
}