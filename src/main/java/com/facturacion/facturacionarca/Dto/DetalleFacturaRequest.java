package com.facturacion.facturacionarca.Dto;

import java.math.BigDecimal;

public class DetalleFacturaRequest {

    private String codigo;
    private String descripcion;
    private int cantidad;
    private String unidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;

    public BigDecimal getSubtotal() {
        BigDecimal bruto =
                precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        if (descuento == null) {
            return bruto;
        }

        return bruto.subtract(descuento);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
}
