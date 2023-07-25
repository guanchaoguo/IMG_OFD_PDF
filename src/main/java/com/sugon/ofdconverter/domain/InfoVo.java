package com.sugon.ofdconverter.domain;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class InfoVo {
    String Data;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
