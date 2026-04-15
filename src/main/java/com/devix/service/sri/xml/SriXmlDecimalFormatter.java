package com.devix.service.sri.xml;

import java.util.Locale;

/**
 * Formato numérico para XML del SRI: tipo {@code xs:decimal} exige punto como separador decimal y sin agrupación de miles.
 * No usar {@code String.format("%.2f", x)} sin locale: en JVM con configuración regional española produce {@code 1000,00}.
 */
public final class SriXmlDecimalFormatter {

    private SriXmlDecimalFormatter() {}

    public static String twoDecimals(Double value) {
        if (value == null) return "0.00";
        return String.format(Locale.ROOT, "%.2f", value);
    }

    public static String sixDecimals(Double value) {
        if (value == null) return "0.000000";
        return String.format(Locale.ROOT, "%.6f", value);
    }
}
