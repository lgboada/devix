package com.devix.service.sri;

import com.devix.domain.Cliente;
import com.devix.web.rest.errors.BadRequestAlertException;

/**
 * Valida la identificación del comprador para comprobantes SRI según el tipo de documento del {@link Cliente}.
 * Convención Devix: {@code tipoDocumento} = {@code C} (cédula, 10 dígitos) o {@code R} (RUC, 13 dígitos).
 */
public final class SriCompradorIdentificacionValidator {

    private static final String ENTITY_FACTURA = "factura";

    private SriCompradorIdentificacionValidator() {}

    public static String normalizarSoloDigitos(String identificacion) {
        if (identificacion == null) {
            return "";
        }
        return identificacion.replaceAll("\\D", "");
    }

    /**
     * Valida y devuelve la identificación solo numérica. Lanza {@link BadRequestAlertException} (400) si no cumple.
     */
    public static String validarYObtenerIdentificacionComprador(Cliente cliente, String identificacionFactura) {
        final String consumidorFinal = "9999999999999";
        String id = normalizarSoloDigitos(identificacionFactura);
        if (consumidorFinal.equals(id)) {
            return consumidorFinal;
        }
        if (id.isEmpty()) {
            throw new BadRequestAlertException(
                "La identificación del comprador es obligatoria",
                ENTITY_FACTURA,
                "sriCompradorSinIdentificacion"
            );
        }

        if (cliente == null || cliente.getTipoDocumento() == null || cliente.getTipoDocumento().isBlank()) {
            throw new BadRequestAlertException(
                "El cliente de la factura no tiene tipo de documento (C/R) configurado",
                ENTITY_FACTURA,
                "sriCompradorSinTipoDocumento"
            );
        }

        String td = cliente.getTipoDocumento().trim().toUpperCase();
        switch (td) {
            case "C" -> {
                if (id.length() != 10) {
                    throw new BadRequestAlertException(
                        "La cédula del comprador debe tener 10 dígitos",
                        ENTITY_FACTURA,
                        "sriCompradorCedulaInvalida"
                    );
                }
                return id;
            }
            case "R" -> {
                if (id.length() != 13) {
                    throw new BadRequestAlertException(
                        "El RUC del comprador debe tener 13 dígitos",
                        ENTITY_FACTURA,
                        "sriCompradorRucInvalido"
                    );
                }
                return id;
            }
            default -> throw new BadRequestAlertException(
                "Tipo de documento del cliente no soportado para SRI: " + cliente.getTipoDocumento(),
                ENTITY_FACTURA,
                "sriCompradorTipoDocumentoInvalido"
            );
        }
    }
}
