package com.facturacion.facturacionarca.Service;

import com.facturacion.facturacionarca.Dto.CredencialesArca;
import com.facturacion.facturacionarca.Dto.FacturaRequest;
import com.facturacion.facturacionarca.Dto.FacturaResultado;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

@Service
public class ArcaFacturacionService {

    private final ArcaAuthService arcaAuthService;

    public ArcaFacturacionService(ArcaAuthService arcaAuthService) {
        this.arcaAuthService = arcaAuthService;
    }

    public String consultarPuntosVenta() {

        CredencialesArca credenciales =
                arcaAuthService.obtenerCredenciales();

        String cuit = "20403647897";

        String soap = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soapenv:Envelope
                    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:ar="http://ar.gov.afip.dif.FEV1/">

                    <soapenv:Header/>

                    <soapenv:Body>
                        <ar:FEParamGetPtosVenta>
                            <ar:Auth>
                                <ar:Token>%s</ar:Token>
                                <ar:Sign>%s</ar:Sign>
                                <ar:Cuit>%s</ar:Cuit>
                            </ar:Auth>
                        </ar:FEParamGetPtosVenta>
                    </soapenv:Body>

                </soapenv:Envelope>
                """.formatted(
                credenciales.getToken(),
                credenciales.getSign(),
                cuit
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        headers.add(
                "SOAPAction",
                "\"http://ar.gov.afip.dif.FEV1/FEParamGetPtosVenta\""
        );

        HttpEntity<String> request =
                new HttpEntity<>(soap, headers);

        RestTemplate restTemplate =
                new RestTemplate();

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "https://wswhomo.afip.gov.ar/wsfev1/service.asmx",
                        request,
                        String.class
                );

        return response.getBody();
    }

    public String probarFacturaC() {

    CredencialesArca credenciales = arcaAuthService.obtenerCredenciales();

    String cuit = "20403647897";

    String soap = """
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:ar="http://ar.gov.afip.dif.FEV1/">

                <soapenv:Header/>

                <soapenv:Body>
                    <ar:FECAESolicitar>

                        <ar:Auth>
                            <ar:Token>%s</ar:Token>
                            <ar:Sign>%s</ar:Sign>
                            <ar:Cuit>%s</ar:Cuit>
                        </ar:Auth>

                        <ar:FeCAEReq>

                            <ar:FeCabReq>
                                <ar:CantReg>1</ar:CantReg>
                                <ar:PtoVta>1</ar:PtoVta>
                                <ar:CbteTipo>11</ar:CbteTipo>
                            </ar:FeCabReq>

                            <ar:FeDetReq>
                                <ar:FECAEDetRequest>

                                    <ar:Concepto>1</ar:Concepto>

                                    <ar:DocTipo>99</ar:DocTipo>
                                    <ar:DocNro>0</ar:DocNro>
                                    <ar:CondicionIVAReceptorId>5</ar:CondicionIVAReceptorId>

                                    <ar:CbteDesde>1</ar:CbteDesde>
                                    <ar:CbteHasta>1</ar:CbteHasta>
                                    <ar:CbteFch>20260830</ar:CbteFch>
                                    <ar:ImpTotal>100</ar:ImpTotal>
                                    <ar:ImpTotConc>0</ar:ImpTotConc>
                                    <ar:ImpNeto>100</ar:ImpNeto>
                                    <ar:ImpOpEx>0</ar:ImpOpEx>
                                    <ar:ImpTrib>0</ar:ImpTrib>
                                    <ar:ImpIVA>0</ar:ImpIVA>

                                    <ar:MonId>PES</ar:MonId>
                                    <ar:MonCotiz>1</ar:MonCotiz>

                                </ar:FECAEDetRequest>
                            </ar:FeDetReq>

                        </ar:FeCAEReq>

                    </ar:FECAESolicitar>
                </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(
            credenciales.getToken(),
            credenciales.getSign(),
            cuit
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_XML);

    headers.add(
            "SOAPAction",
            "\"http://ar.gov.afip.dif.FEV1/FECAESolicitar\""
    );

    HttpEntity<String> request =
            new HttpEntity<>(soap, headers);

    RestTemplate restTemplate =
            new RestTemplate();

    ResponseEntity<String> response =
            restTemplate.postForEntity(
                    "https://wswhomo.afip.gov.ar/wsfev1/service.asmx",
                    request,
                    String.class
            );

    return response.getBody();
}

public String consultarCondicionesIva() {

    CredencialesArca credenciales = arcaAuthService.obtenerCredenciales();

    String cuit = "20403647897";

    String soap = """
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:ar="http://ar.gov.afip.dif.FEV1/">

                <soapenv:Header/>

                <soapenv:Body>
                    <ar:FEParamGetCondicionIvaReceptor>
                        <ar:Auth>
                            <ar:Token>%s</ar:Token>
                            <ar:Sign>%s</ar:Sign>
                            <ar:Cuit>%s</ar:Cuit>
                        </ar:Auth>
                    </ar:FEParamGetCondicionIvaReceptor>
                </soapenv:Body>

            </soapenv:Envelope>
            """.formatted(
            credenciales.getToken(),
            credenciales.getSign(),
            cuit
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_XML);

    headers.add(
            "SOAPAction",
            "\"http://ar.gov.afip.dif.FEV1/FEParamGetCondicionIvaReceptor\""
    );

    HttpEntity<String> request =
            new HttpEntity<>(soap, headers);

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response =
            restTemplate.postForEntity(
                    "https://wswhomo.afip.gov.ar/wsfev1/service.asmx",
                    request,
                    String.class
            );

    return response.getBody();
}

public int obtenerUltimoComprobante() {

    CredencialesArca credenciales =
            arcaAuthService.obtenerCredenciales();

    String cuit = "20403647897";

    System.out.println("Consultando ultimo comprobante autorizado en ARCA...");

    String soap = """
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:ar="http://ar.gov.afip.dif.FEV1/">

                <soapenv:Header/>

                <soapenv:Body>
                    <ar:FECompUltimoAutorizado>

                        <ar:Auth>
                            <ar:Token>%s</ar:Token>
                            <ar:Sign>%s</ar:Sign>
                            <ar:Cuit>%s</ar:Cuit>
                        </ar:Auth>

                        <ar:PtoVta>1</ar:PtoVta>
                        <ar:CbteTipo>11</ar:CbteTipo>

                    </ar:FECompUltimoAutorizado>
                </soapenv:Body>

            </soapenv:Envelope>
            """.formatted(
            credenciales.getToken(),
            credenciales.getSign(),
            cuit
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_XML);

    headers.add(
            "SOAPAction",
            "\"http://ar.gov.afip.dif.FEV1/FECompUltimoAutorizado\""
    );

    HttpEntity<String> request =
            new HttpEntity<>(soap, headers);

    RestTemplate restTemplate =
            new RestTemplate();

    String respuesta =
            restTemplate.postForObject(
                    "https://wswhomo.afip.gov.ar/wsfev1/service.asmx",
                    request,
                    String.class
            );

    try {

        Document documento =
                DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(
                                new InputSource(
                                        new StringReader(respuesta)
                                )
                        );

        String numero =
                documento
                        .getElementsByTagName("CbteNro")
                        .item(0)
                        .getTextContent();

        int ultimo = Integer.parseInt(numero);

        System.out.println(
                "Ultimo comprobante autorizado: " + ultimo
        );

        return ultimo;

    } catch (Exception e) {

        System.out.println(
                "Error al consultar el ultimo comprobante en ARCA."
        );

        throw new RuntimeException(
                "No se pudo obtener el ultimo comprobante",
                e
        );
    }
}

public FacturaResultado emitirFactura(FacturaRequest factura) {

    CredencialesArca credenciales = arcaAuthService.obtenerCredenciales();
    String docTipo = factura.getTipoDocumento();

long docNro = factura.getNumeroDocumento();

String condicionIva =
        factura.getCondicionIvaCliente();
    String cuit = "20403647897";

    int ultimo = obtenerUltimoComprobante();
    int siguiente = ultimo + 1;
    System.out.println("Proximo comprobante: " + siguiente);
    String fecha = java.time.LocalDate.now()
            .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);

    String total = factura.getTotal().toPlainString();

    String soap = """
            <?xml version="1.0" encoding="UTF-8"?>
            <soapenv:Envelope
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:ar="http://ar.gov.afip.dif.FEV1/">

                <soapenv:Header/>

                <soapenv:Body>
                    <ar:FECAESolicitar>

                        <ar:Auth>
                            <ar:Token>%s</ar:Token>
                            <ar:Sign>%s</ar:Sign>
                            <ar:Cuit>%s</ar:Cuit>
                        </ar:Auth>

                        <ar:FeCAEReq>

                            <ar:FeCabReq>
                                <ar:CantReg>1</ar:CantReg>
                                <ar:PtoVta>1</ar:PtoVta>
                                <ar:CbteTipo>11</ar:CbteTipo>
                            </ar:FeCabReq>

                            <ar:FeDetReq>
                                <ar:FECAEDetRequest>

                                    <ar:Concepto>1</ar:Concepto>

                                    <ar:DocTipo>%s</ar:DocTipo>
                                    <ar:DocNro>%d</ar:DocNro>

                                    <ar:CondicionIVAReceptorId>%s</ar:CondicionIVAReceptorId>

                                    <ar:CbteDesde>%d</ar:CbteDesde>
                                    <ar:CbteHasta>%d</ar:CbteHasta>

                                    <ar:CbteFch>%s</ar:CbteFch>

                                    <ar:ImpTotal>%s</ar:ImpTotal>
                                    <ar:ImpTotConc>0</ar:ImpTotConc>
                                    <ar:ImpNeto>%s</ar:ImpNeto>
                                    <ar:ImpOpEx>0</ar:ImpOpEx>
                                    <ar:ImpTrib>0</ar:ImpTrib>
                                    <ar:ImpIVA>0</ar:ImpIVA>

                                    <ar:MonId>PES</ar:MonId>
                                    <ar:MonCotiz>1</ar:MonCotiz>

                                </ar:FECAEDetRequest>
                            </ar:FeDetReq>

                        </ar:FeCAEReq>

                    </ar:FECAESolicitar>
                </soapenv:Body>

            </soapenv:Envelope>
            """.formatted(
        credenciales.getToken(),
        credenciales.getSign(),
        cuit,
        docTipo,
        docNro,
        condicionIva,
        siguiente,
        siguiente,
        fecha,
        total,
        total
);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_XML);

    headers.add(
            "SOAPAction",
            "\"http://ar.gov.afip.dif.FEV1/FECAESolicitar\""
    );

    HttpEntity<String> request =
            new HttpEntity<>(soap, headers);

    RestTemplate restTemplate = new RestTemplate();

    String respuesta = restTemplate.postForObject(
            "https://wswhomo.afip.gov.ar/wsfev1/service.asmx",
            request,
            String.class
    );

    try {

        Document documento =
                DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(
                                new InputSource(
                                        new StringReader(respuesta)
                                )
                        );

        String resultado =
                documento
                        .getElementsByTagName("Resultado")
                        .item(0)
                        .getTextContent();

        String cae = "";

        String vencimiento = "";

        if (documento.getElementsByTagName("CAE").item(0) != null) {
            cae = documento
                    .getElementsByTagName("CAE")
                    .item(0)
                    .getTextContent();
        }

        if (documento.getElementsByTagName("CAEFchVto").item(0) != null) {
            vencimiento = documento
                    .getElementsByTagName("CAEFchVto")
                    .item(0)
                    .getTextContent();
        }

        return new FacturaResultado(
                siguiente,
                cae,
                vencimiento,
                factura.getTotal(),
                resultado
        );

    } catch (Exception e) {

        throw new RuntimeException(
                "No se pudo procesar la respuesta de ARCA",
                e
        );
    }
}


}