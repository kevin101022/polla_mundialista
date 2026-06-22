package util;

/**
 * Constantes globales de la aplicación.
 * Agrega aquí cualquier valor que se use en múltiples clases.
 */
public final class Constants {

    private Constants() {}

    // ─── Aplicación ───────────────────────────────────────────────────────────
    public static final String APP_NAME    = "Mi Aplicación";  // ← Actualizar
    public static final String APP_VERSION = "1.0.0";

    // ─── Base de datos ────────────────────────────────────────────────────────
    public static final String DB_NAME     = "app.db";

    // ─── Mensajes comunes ─────────────────────────────────────────────────────
    public static final String MSG_CAMPO_REQUERIDO  = "Este campo es requerido.";
    public static final String MSG_EMAIL_INVALIDO   = "Ingresa un email válido.";
    public static final String MSG_ERROR_CONEXION   = "Error de conexión a la base de datos.";
    public static final String MSG_GUARDADO_OK      = "Registro guardado correctamente.";
    public static final String MSG_ELIMINADO_OK     = "Registro eliminado correctamente.";
    public static final String MSG_ERROR_GENERAL    = "Ocurrió un error inesperado.";
}
