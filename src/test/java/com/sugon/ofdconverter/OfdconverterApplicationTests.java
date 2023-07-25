package com.sugon.ofdconverter;

import org.junit.jupiter.api.Test;
import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.ofdconverter.PDFConverter;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class OfdconverterApplicationTests {

    @Test
    void contextLoads() {

        ConvertHelper.toPdf(
                Paths.get("/Users/jimogangdan/IdeaProjects/test_pro/src/test/resources/IMAGE-PAGES.ofd"),
                Paths.get("/Users/jimogangdan/IdeaProjects/ofd_pdf/src/test/resources/IMAGE-PAGES.ofd.pdf")
        );


    }

}
