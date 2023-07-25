package com.sugon.ofdconverter;

import cn.hutool.core.codec.Base64Encoder;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ofdrw.converter.ofdconverter.PDFConverter;
import org.ofdrw.converter.ConvertHelper;

@SpringBootApplication
public class ConvertedApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConvertedApplication.class, args);
    }




}
