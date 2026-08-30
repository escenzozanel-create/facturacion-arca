package com.facturacion.facturacionarca.Service;

import com.facturacion.facturacionarca.Dto.CredencialesArca;

import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;

import java.io.FileInputStream;
import java.io.StringReader;

import java.nio.charset.StandardCharsets;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;

import java.security.cert.X509Certificate;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import java.util.Base64;

@Service
public class ArcaAuthService {

    @Value("${arca.p12.password}")
    private String passwordP12;

    private CredencialesArca credencialesActuales;
    private Instant vencimientoActual;

    public ArcaAuthService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Genera el XML que se envía a WSAA para pedir acceso a wsfe
    private String generarTRA() {

        long uniqueId = System.currentTimeMillis() / 1000;

        Instant ahora = Instant.now();

        Instant generacion =
                ahora.minus(10, ChronoUnit.MINUTES);

        Instant expiracion =
                ahora.plus(1, ChronoUnit.HOURS);

        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <loginTicketRequest version="1.0">
                    <header>
                        <uniqueId>%d</uniqueId>
                        <generationTime>%s</generationTime>
                        <expirationTime>%s</expirationTime>
                    </header>
                    <service>wsfe</service>
                </loginTicketRequest>
                """.formatted(
                uniqueId,
                generacion,
                expiracion
        );
    }

    // Firma el TRA utilizando el certificado .p12
    private String firmarTRA(String tra) throws Exception {

        KeyStore keyStore =
                KeyStore.getInstance("PKCS12");

        try (FileInputStream archivo =
                     new FileInputStream(
                             "certificados/MiCertificado.p12"
                     )) {

            keyStore.load(
                    archivo,
                    passwordP12.toCharArray()
            );
        }

        String alias =
                keyStore.aliases().nextElement();

        PrivateKey clavePrivada =
                (PrivateKey) keyStore.getKey(
                        alias,
                        passwordP12.toCharArray()
                );

        X509Certificate certificado =
                (X509Certificate)
                        keyStore.getCertificate(alias);

        CMSSignedDataGenerator generador =
                new CMSSignedDataGenerator();

        ContentSigner firmador =
                new JcaContentSignerBuilder(
                        "SHA256withRSA"
                )
                        .setProvider("BC")
                        .build(clavePrivada);

        generador.addSignerInfoGenerator(
                new JcaSignerInfoGeneratorBuilder(
                        new JcaDigestCalculatorProviderBuilder()
                                .setProvider("BC")
                                .build()
                )
                        .build(
                                firmador,
                                certificado
                        )
        );

        generador.addCertificate(
                new JcaX509CertificateHolder(
                        certificado
                )
        );

        CMSProcessableByteArray contenido =
                new CMSProcessableByteArray(
                        tra.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        CMSSignedData cms =
                generador.generate(
                        contenido,
                        true
                );

        return Base64
                .getEncoder()
                .encodeToString(
                        cms.getEncoded()
                );
    }

    // Pide un Token + Sign nuevos a WSAA
    private CredencialesArca solicitarCredenciales()
            throws Exception {

        String tra = generarTRA();

        String cmsBase64 =
                firmarTRA(tra);

        String soap = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soapenv:Envelope
                    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:wsaa="http://wsaa.view.sua.dvadac.desein.afip.gov">

                    <soapenv:Header/>

                    <soapenv:Body>
                        <wsaa:loginCms>
                            <wsaa:in0>%s</wsaa:in0>
                        </wsaa:loginCms>
                    </soapenv:Body>

                </soapenv:Envelope>
                """.formatted(cmsBase64);

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.TEXT_XML
        );

        headers.add(
                "SOAPAction",
                "\"\""
        );

        HttpEntity<String> request =
                new HttpEntity<>(
                        soap,
                        headers
                );

        RestTemplate restTemplate =
                new RestTemplate();

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "https://wsaahomo.afip.gov.ar/ws/services/LoginCms",
                        request,
                        String.class
                );

        return procesarRespuestaWSAA(
                response.getBody()
        );
    }

    // Extrae Token, Sign y vencimiento de la respuesta de WSAA
    private CredencialesArca procesarRespuestaWSAA(
            String respuesta) throws Exception {

        Document documento =
                DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(
                                new InputSource(
                                        new StringReader(
                                                respuesta
                                        )
                                )
                        );

        if (documento
                .getElementsByTagName(
                        "loginCmsReturn"
                )
                .getLength() > 0) {

            String xmlInterno =
                    documento
                            .getElementsByTagName(
                                    "loginCmsReturn"
                            )
                            .item(0)
                            .getTextContent();

            documento =
                    DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(
                                    new InputSource(
                                            new StringReader(
                                                    xmlInterno
                                            )
                                    )
                            );
        }

        String token =
                documento
                        .getElementsByTagName("token")
                        .item(0)
                        .getTextContent();

        String sign =
                documento
                        .getElementsByTagName("sign")
                        .item(0)
                        .getTextContent();

        String vencimiento =
                documento
                        .getElementsByTagName(
                                "expirationTime"
                        )
                        .item(0)
                        .getTextContent();

        vencimientoActual =
                OffsetDateTime
                        .parse(vencimiento)
                        .toInstant();

        credencialesActuales =
                new CredencialesArca(
                        token,
                        sign
                );

        return credencialesActuales;
    }

    // Este es el método que va a usar ArcaFacturacionService
    public CredencialesArca obtenerCredenciales() {

    try {

        if (credencialesActuales != null
                && vencimientoActual != null
                && Instant.now().isBefore(
                        vencimientoActual.minus(
                                5,
                                ChronoUnit.MINUTES
                        )
                )) {

            System.out.println("Token vigente. Reutilizando credenciales ARCA.");

            return credencialesActuales;
        }

        System.out.println("Autenticando con WSAA homologacion...");

        CredencialesArca nuevasCredenciales =
                solicitarCredenciales();

        System.out.println("Credenciales ARCA obtenidas correctamente.");

        return nuevasCredenciales;

    } catch (Exception e) {

        System.out.println("Error al autenticar con ARCA.");

        throw new RuntimeException(
                "No se pudo autenticar con ARCA",
                e
        );
    }
}
}