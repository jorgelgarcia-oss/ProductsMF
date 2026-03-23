package com.example.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotBlank;

public class ProductDto {

  @NotNull(message = "ID must not be null")
  private Long id;

  @NotBlank(message = "Name must not be blank")
  private String name;

  @NotBlank(message = "Description must not be blank")
  private String description;

  @NotNull(message = "Price must not be null")
  @Positive(message = "Price must be positive")
  private Double price;

  public ProductDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }
}