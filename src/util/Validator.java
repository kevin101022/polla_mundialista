package util;

import java.util.regex.Pattern;

/**
 * Métodos de validación reutilizables para formularios.
 * Retornan String con el mensaje de error, o null si es válido.
 */
public final class Validator {

    private Validator() {}

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // ─────────────────────────────────────────────────────────────────────────

    /** Verifica que el texto no sea nulo ni vacío. */
    public static String requerido(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            return nombreCampo + " es requerido.";
        }
        return null;
    }

    /** Verifica formato de email. */
    public static String email(String valor) {
        if (valor == null || !EMAIL_PATTERN.matcher(valor.trim()).matches()) {
            return Constants.MSG_EMAIL_INVALIDO;
        }
        return null;
    }

    /** Verifica longitud mínima. */
    public static String longitudMinima(String valor, int minimo, String nombreCampo) {
        if (valor == null || valor.trim().length() < minimo) {
            return nombreCampo + " debe tener al menos " + minimo + " caracteres.";
        }
        return null;
    }

    /** Verifica que dos campos coincidan (ej. contraseñas). */
    public static String coinciden(String a, String b, String nombreCampo) {
        if (a == null || !a.equals(b)) {
            return nombreCampo + " no coinciden.";
        }
        return null;
    }

    /** Verifica que la cadena sea un número entero válido. */
    public static String esEntero(String valor, String nombreCampo) {
        try {
            Integer.parseInt(valor.trim());
            return null;
        } catch (NumberFormatException e) {
            return nombreCampo + " debe ser un número entero.";
        }
    }

    /** Verifica que la cadena sea un número decimal válido. */
    public static String esDecimal(String valor, String nombreCampo) {
        try {
            Double.parseDouble(valor.trim());
            return null;
        } catch (NumberFormatException e) {
            return nombreCampo + " debe ser un número válido.";
        }
    }
}
