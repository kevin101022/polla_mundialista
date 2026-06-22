package ui.theme;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

/**
 * Gestiona las fuentes del Design System "Pitch & Podium".
 *
 * Fuentes usadas:
 *  - Plus Jakarta Sans → Headlines / Display
 *  - Inter             → Body / Labels / Data
 *
 * Las fuentes .ttf deben estar en: src/resources/fonts/
 * Si no se encuentran, se usa una fuente del sistema como fallback.
 */
public final class AppFonts {

    private AppFonts() {}

    // ─── Nombres de familia ───────────────────────────────────────────────────

    // ─── Fuentes base registradas ─────────────────────────────────────────────
    private static Font jakartaBase;
    private static Font interBase;
    private static boolean loaded = false;

    // ─── Carga e inicialización ───────────────────────────────────────────────

    /**
     * Carga las fuentes personalizadas desde resources/fonts/.
     * Llamar una sola vez al iniciar la aplicación (en App.java o MainFrame).
     */
    public static void load() {
        if (loaded) return;
        jakartaBase = loadFont("/resources/fonts/PlusJakartaSans-Bold.ttf",     new Font("SansSerif", Font.BOLD, 12));
        interBase   = loadFont("/resources/fonts/Inter-Regular.ttf",             new Font("SansSerif", Font.PLAIN, 12));
        loaded = true;
    }

    private static Font loadFont(String resourcePath, Font fallback) {
        try {
            InputStream is = AppFonts.class.getResourceAsStream(resourcePath);
            if (is == null) {
                System.out.println("[AppFonts] No se encontró: " + resourcePath + " → usando fallback");
                return fallback;
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (FontFormatException | java.io.IOException e) {
            System.out.println("[AppFonts] Error cargando fuente: " + resourcePath);
            return fallback;
        }
    }

    // ─── Métodos de acceso por escala tipográfica ─────────────────────────────

    /** Display LG → 48px / ExtraBold (800) */
    public static Font displayLg() {
        ensureLoaded();
        return jakartaBase.deriveFont(Font.BOLD, 48f);
    }

    /** Headline LG → 32px / Bold (700) */
    public static Font headlineLg() {
        ensureLoaded();
        return jakartaBase.deriveFont(Font.BOLD, 32f);
    }

    /** Headline LG Mobile → 24px / Bold (700) */
    public static Font headlineLgMobile() {
        ensureLoaded();
        return jakartaBase.deriveFont(Font.BOLD, 24f);
    }

    /** Headline MD → 24px / SemiBold (600) */
    public static Font headlineMd() {
        ensureLoaded();
        return jakartaBase.deriveFont(Font.BOLD, 20f);
    }

    /** Body LG → 18px / Regular */
    public static Font bodyLg() {
        ensureLoaded();
        return interBase.deriveFont(Font.PLAIN, 18f);
    }

    /** Body MD → 16px / Regular */
    public static Font bodyMd() {
        ensureLoaded();
        return interBase.deriveFont(Font.PLAIN, 16f);
    }

    /** Label MD → 14px / SemiBold — botones, tags */
    public static Font labelMd() {
        ensureLoaded();
        return interBase.deriveFont(Font.BOLD, 14f);
    }

    /** Caption → 12px / Regular — textos auxiliares */
    public static Font caption() {
        ensureLoaded();
        return interBase.deriveFont(Font.PLAIN, 12f);
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    private static void ensureLoaded() {
        if (!loaded) load();
    }
}
