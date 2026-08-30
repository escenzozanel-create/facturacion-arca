package com.facturacion.facturacionarca.Dto;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class FacturaRequest {

    // DATOS DEL EMISOR
    private String nombreEmisor;
    private String domicilioEmisor;
    private String condicionIvaEmisor;
    private String ingresosBrutos;
    private String inicioActividades;

    // DATOS DEL CLIENTE
    private String nombreCliente;
    private String domicilioCliente;
    private String condicionIvaCliente;
    private String tipoDocumento;
    private Long numeroDocumento;

  private List<DetalleFacturaRequest> detalles = new ArrayList<>();

    public String getNombreEmisor() {
        return nombreEmisor;
    }

    public void setNombreEmisor(String nombreEmisor) {
        this.nombreEmisor = nombreEmisor;
    }

    public String getDomicilioEmisor() {
        return domicilioEmisor;
    }

    public void setDomicilioEmisor(String domicilioEmisor) {
        this.domicilioEmisor = domicilioEmisor;
    }

    public String getCondicionIvaEmisor() {
        return condicionIvaEmisor;
    }

    public void setCondicionIvaEmisor(String condicionIvaEmisor) {
        this.condicionIvaEmisor = condicionIvaEmisor;
    }

    public String getIngresosBrutos() {
        return ingresosBrutos;
    }

    public void setIngresosBrutos(String ingresosBrutos) {
        this.ingresosBrutos = ingresosBrutos;
    }

    public String getInicioActividades() {
        return inicioActividades;
    }

    public void setInicioActividades(String inicioActividades) {
        this.inicioActividades = inicioActividades;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDomicilioCliente() {
        return domicilioCliente;
    }

    public void setDomicilioCliente(String domicilioCliente) {
        this.domicilioCliente = domicilioCliente;
    }

    public String getCondicionIvaCliente() {
        return condicionIvaCliente;
    }

    public void setCondicionIvaCliente(String condicionIvaCliente) {
        this.condicionIvaCliente = condicionIvaCliente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }


    public List<DetalleFacturaRequest> getDetalles() {
    return detalles;
}

public void setDetalles(List<DetalleFacturaRequest> detalles) {
    this.detalles = detalles;
}

public BigDecimal getTotal() {

    return detalles.stream()
            .map(DetalleFacturaRequest::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}
}