package com.sugon.ofdconverter.controller;

import com.sugon.ofdconverter.config.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    @GetMapping(value = "/", produces = "application/json;charset=UTF-8")
    public JsonResult<Map> Helle() {
        return new JsonResult(0, "wecoome to  application");
    }
}