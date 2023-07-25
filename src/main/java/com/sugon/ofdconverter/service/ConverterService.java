package com.sugon.ofdconverter.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.FileUtil;
import com.sugon.ofdconverter.config.BusinessException;
import com.sugon.ofdconverter.config.JsonResult;
import com.sugon.ofdconverter.domain.InfoVo;
import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.ofdconverter.ImageConverter;
import org.ofdrw.converter.ofdconverter.PDFConverter;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.PageLayout;
import org.ofdrw.layout.VirtualPage;
import org.ofdrw.layout.element.Div;
import org.ofdrw.layout.element.Img;
import org.ofdrw.layout.element.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@Service
public class ConverterService {


    public InfoVo Image2OFD(MultipartFile[] files) throws BusinessException, IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<Path> tempFiles = new ArrayList<>();
        try (OFDDoc ofdDoc = new OFDDoc(output)) {
            for (MultipartFile file : files) {
                Path tempFile = Files.createTempFile("temp", null);
                Files.createDirectories(tempFile.getParent());
                Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

                PageLayout pageLayout = ofdDoc.getPageLayout();
                VirtualPage vPage = new VirtualPage(pageLayout);
                BufferedImage image = Img.readImage(tempFile.toFile());

                float height = image.getHeight();
                float width = image.getWidth();
                Img img;

                if (height / width > pageLayout.getHeight() / pageLayout.getWidth()) {
                    img = new Img((pageLayout.getHeight()) * width / height, pageLayout.getHeight(), tempFile);
                } else {
                    img = new Img((pageLayout.getWidth()), (pageLayout.getWidth()) / width * height, tempFile);
                }

                double x = (pageLayout.getWidth() - img.getWidth()) / 2;
                double y = (pageLayout.getHeight() - img.getHeight()) / 2;
                img.setPosition(Position.Absolute).setX(x).setY(y);

                vPage.add(img);
                ofdDoc.addVPage(vPage);

                tempFiles.add(tempFile);
            }
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        } finally {
            for (Path tempFile : tempFiles) {
                Files.delete(tempFile);
            }
        }

        InfoVo info = new InfoVo();
        info.setData(Base64Encoder.encode(output.toByteArray()));
        return info;
    }


    public InfoVo OFD2PDF(MultipartFile file) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConvertHelper.toPdf(file.getInputStream(), output);
        InfoVo info = new InfoVo();
        info.setData(Base64Encoder.encode(output.toByteArray()));
        return info;
    }

    private static BufferedImage convertCMYK2RGB(ImageReader reader) throws IOException {
        // 读取Raster (没有颜色的转换) CMYK
        Raster raster = reader.readRaster(0, null);
        int w = raster.getWidth();
        int h = raster.getHeight();
        byte[] rgb = new byte[w * h * 3];

        // 彩色空间转换
        float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);
        float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);
        float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);
        float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);

        for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {
            float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i],
                    cr = 255 - Cr[i];

            double val = y + 1.402 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);

            val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);

            val = y + 1.772 * (cb - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);
        }
        raster = Raster.createInterleavedRaster(
                new DataBufferByte(rgb, rgb.length),
                w, h,
                w * 3,
                3,
                new int[]{0, 1, 2},
                null);
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, true,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
    }
}
