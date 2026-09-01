package com.facturacion.facturacionarca.Dto;

import java.math.BigDecimal;

public class FacturaResultado {

    private int numeroComprobante;
    private String cae;
    private String vencimientoCae;
    private BigDecimal total;
    private String resultado;

    public FacturaResultado(
            int numeroComprobante,
            String cae,
            String vencimientoCae,
            BigDecimal total,
            String resultado) {

        this.numeroComprobante = numeroComprobante;
        this.cae = cae;
        this.vencimientoCae = vencimientoCae;
        this.total = total;
        this.resultado = resultado;
    }

    public int getNumeroComprobante() {
        return numeroComprobante;
    }

    public String getCae() {
        return cae;
    }

    public String getVencimientoCae() {
        return vencimientoCae;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getResultado() {
        return resultado;
    }
}
