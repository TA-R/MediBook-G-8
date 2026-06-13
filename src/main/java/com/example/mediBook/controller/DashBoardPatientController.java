package com.example.mediBook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class DashBoardPatientController {
    @GetMapping("/dashboardPatient")
    public String dashboard(Model model) {
        return "patient/dashboardPatient";
    }
}