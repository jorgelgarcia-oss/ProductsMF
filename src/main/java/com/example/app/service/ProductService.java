package com.example.app.service;

import com.example.app.dto.ProductDto;
import java.util.List;

public class ProductService {

    public enum Mode {
        ADD, EDIT, DELETE, NONE
    }

    private Mode currentMode = Mode.NONE;

    public void enterAddMode() {
        currentMode = Mode.ADD;
    }

    public void enterEditMode() {
        currentMode = Mode.EDIT;
    }

    public void enterDeleteMode() {
        currentMode = Mode.DELETE;
    }

    public void exitMode() {
        currentMode = Mode.NONE;
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public List<ProductDto> listAll() {
        // stub method
        return null;
    }

    public ProductDto findById(Long id) {
        // stub method
        return null;
    }

    public void save(ProductDto productDto) {
        // stub method
    }
}
