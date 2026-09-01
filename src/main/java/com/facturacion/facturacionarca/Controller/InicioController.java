package com.facturacion.facturacionarca.Controller;
import com.facturacion.facturacionarca.Service.ArcaAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;
import com.facturacion.facturacionarca.Service.ArcaFacturacionService;
import com.facturacion.facturacionarca.Dto.FacturaRequest;
import com.facturacion.facturacionarca.Dto.FacturaResultado;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class InicioController {

    private final ArcaAuthService arcaAuthService;
    private final ArcaFacturacionService arcaFacturacionService;

   public InicioController(
        ArcaAuthService arcaAuthService,
        ArcaFacturacionService arcaFacturacionService) {

    this.arcaAuthService = arcaAuthService;
    this.arcaFacturacionService = arcaFacturacionService;
}

    @GetMapping("/")
    public String inicio() {
        return "index";
    }







@GetMapping("/arca/credenciales")
@ResponseBody
public String probarCredenciales() throws Exception {

    var credenciales = arcaAuthService.obtenerCredenciales();

    return "Token obtenido: " + (credenciales.getToken() != null)
            + "<br>Sign obtenido: " + (credenciales.getSign() != null);
}

@GetMapping(value = "/arca/puntos-venta",
        produces = MediaType.APPLICATION_XML_VALUE)
@ResponseBody
public String puntosVenta() {
    return arcaFacturacionService.consultarPuntosVenta();
}

@GetMapping(value = "/arca/probar-factura",
        produces = MediaType.APPLICATION_XML_VALUE)
@ResponseBody
public String probarFactura() {
    return arcaFacturacionService.probarFacturaC();
}
@GetMapping(value = "/arca/condiciones-iva",
        produces = MediaType.APPLICATION_XML_VALUE)
@ResponseBody
public String condicionesIva() {
    return arcaFacturacionService.consultarCondicionesIva();
}

@GetMapping("/factura")
public String factura() {
    return "factura";
}
@GetMapping("/arca/ultimo-comprobante")
@ResponseBody
public String ultimoComprobante() {

    int ultimo =
            arcaFacturacionService.obtenerUltimoComprobante();

    return "Ultimo comprobante: " + ultimo;
}
@PostMapping("/factura/emitir")
public String emitirFactura(
        FacturaRequest factura,
        Model model) {

    FacturaResultado resultado =
            arcaFacturacionService.emitirFactura(factura);

    model.addAttribute("factura", factura);
    model.addAttribute("resultado", resultado);

    return "resultado-factura";
}



}