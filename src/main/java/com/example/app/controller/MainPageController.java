package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

  @GetMapping("/")
  public String mainMenu(Model model) {
    return "mainmenu";
  }
}