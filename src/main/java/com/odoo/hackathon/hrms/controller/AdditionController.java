package com.odoo.hackathon.hrms.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class AdditionController {

    @PostMapping("/add")
    public int add(@RequestParam int num1, @RequestParam int num2) {
        log.info("num1:{}|num2:{}", num1, num2);

        int sumResult = num1 + num2;
        log.info("sumResult:{}", sumResult);

        return sumResult;
    }
}