package com.sugon.ofdconverter.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.FileUtil;
import com.sugon.ofdconverter.config.BusinessException;
import com.sugon.ofdconverter.config.JsonResult;
import com.sugon.ofdconverter.domain.InfoVo;
import com.sugon.ofdconverter.service.ConverterService;
import org.json.JSONObject;
import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.ofdconverter.PDFConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class Converter {


    @Autowired
    private ConverterService service;


    @PostMapping(path = "/api/v1/converter", produces = "application/json")
    public JsonResult<InfoVo> ConverterODF(@RequestParam("files") MultipartFile[] files) throws BusinessException, IOException {
        InfoVo data = service.Image2OFD(files);
        return new JsonResult<InfoVo>(data);
    }

    @PostMapping(path = "/api/v1/restore", produces = "application/json")
    public JsonResult<InfoVo> RestoreToODF(@RequestParam("file") MultipartFile file) throws IOException {
        InfoVo data = service.OFD2PDF(file);
        return new JsonResult<InfoVo>(data);
    }


}
