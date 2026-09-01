package com.facturacion.facturacionarca.Dto;

public class CredencialesArca {

    private String token;
    private String sign;

    public CredencialesArca(String token, String sign) {
        this.token = token;
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public String getSign() {
        return sign;
    }
}
