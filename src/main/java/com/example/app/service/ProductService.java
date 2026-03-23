package com.example.app.service;

import com.example.app.dto.ProductDto;
import com.example.app.entity.Product;
import com.example.app.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

  private final ProductRepository productRepository;

  public enum Mode {
    IDLE,
    ADD,
    EDIT,
    DELETE
  }

  private Mode currentMode = Mode.IDLE;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductDto> findAll() {
    return productRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public ProductDto findById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    return toDto(product);
  }

  public ProductDto save(ProductDto dto) {
    validateDto(dto);
    Product product = toEntity(dto);
    productRepository.save(product);
    currentMode = Mode.IDLE;
    return toDto(product);
  }

  public void delete(Long id) {
    if (!productRepository.existsById(id)) {
      throw new EntityNotFoundException("Product not found with ID: " + id);
    }
    productRepository.deleteById(id);
    currentMode = Mode.IDLE;
  }

  public Mode getCurrentMode() {
    return currentMode;
  }

  public void enterAddMode() {
    currentMode = Mode.ADD;
  }

  public void enterEditMode() {
    currentMode = Mode.EDIT;
  }

  public void enterDeleteMode() {
    currentMode = Mode.DELETE;
  }

  public void exit() {
    currentMode = Mode.IDLE;
  }

  private void validateDto(ProductDto dto) {
    if (dto.getId() == null) {
      throw new IllegalArgumentException("ID must not be null");
    }
    if (dto.getName() == null || dto.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Name must not be blank");
    }
    if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
      throw new IllegalArgumentException("Description must not be blank");
    }
    if (dto.getPrice() == null) {
      throw new IllegalArgumentException("Price must not be null");
    }
    if (dto.getPrice().doubleValue() <= 0) {
      throw new IllegalArgumentException("Price must be positive");
    }
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