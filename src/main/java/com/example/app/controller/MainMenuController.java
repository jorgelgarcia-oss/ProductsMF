package com.example.app.controller;
packacka
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainMenuController {

  @GetMapping("/")
  public String mainMenu() {
    return "main-menu";
  }
}