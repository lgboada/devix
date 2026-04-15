package com.devix.service.sri;

import com.devix.domain.Compania;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 * Genera la clave de acceso de 49 dígitos requerida por el SRI Ecuador.
 *
 * Estructura:
 *   fechaEmision(8) + tipoComprobante(2) + ruc(13) + ambiente(1)
 *   + establecimiento(3) + puntoEmision(3) + secuencial(9)
 *   + codigoNumerico(8) + tipoEmision(1) + digitoVerificador(1)
 */
@Service
public class SriClaveAccesoService {

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final ZoneId ZONA_EC = ZoneId.of("America/Guayaquil");
    private static final Random RANDOM = new Random();

    /**
     * @param fecha           Fecha de emisión (Instant)
     * @param tipoComprobante Código SRI: "01"=Factura, "03"=Liquidación, "04"=NotaCredito,
     *                        "05"=NotaDebito, "06"=GuiaRemision, "07"=Retencion
     * @param serie           establecimiento+puntoEmision (6 dígitos, ej: "001001")
     * @param secuencial      número secuencial (noFisico), se rellena con ceros hasta 9 dígitos
     * @param compania        datos del emisor
     * @return clave de acceso de 48 dígitos sin el dígito verificador + dígito verificador
     */
    public String generar(java.time.Instant fecha, String tipoComprobante, String serie, String secuencial, Compania compania) {
        String fechaStr = FECHA_FMT.format(fecha.atZone(ZONA_EC).toLocalDate());
        String ruc = compania.getDni();
        String ambiente = String.valueOf(compania.getAmbienteSri());
        String estab = serie.substring(0, 3);
        String ptoEmi = serie.substring(3, 6);
        String seq = String.format("%09d", Long.parseLong(secuencial.replaceAll("\\D", "")));
        String codigoNumerico = String.format("%08d", RANDOM.nextInt(99_999_999));
        String tipoEmision = "1";

        String clave48 = fechaStr + tipoComprobante + ruc + ambiente + estab + ptoEmi + seq + codigoNumerico + tipoEmision;

        return clave48 + calcularDigitoVerificador(clave48);
    }

    /**
     * Módulo 11 descendente (pesos 2..7 cíclicos de derecha a izquierda).
     */
    public int calcularDigitoVerificador(String clave48) {
        int[] pesos = { 2, 3, 4, 5, 6, 7 };
        int suma = 0;
        int indicePeso = 0;
        for (int i = clave48.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(clave48.charAt(i)) * pesos[indicePeso % 6];
            indicePeso++;
        }
        int residuo = suma % 11;
        if (residuo == 0) return 0;
        if (residuo == 1) return 1;
        return 11 - residuo;
    }
}
