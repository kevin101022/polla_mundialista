package ui.theme;

import java.awt.Color;

/**
 * Paleta de colores del Design System "Pitch & Podium".
 * Todas las vistas y componentes deben referenciar estas constantes.
 */
public final class AppColors {

    private AppColors() {}

    // ─── Primary ────────────────────────────────────────────────────────────
    public static final Color PRIMARY                  = new Color(0x00342B);
    public static final Color ON_PRIMARY               = new Color(0xFFFFFF);
    public static final Color PRIMARY_CONTAINER        = new Color(0x004D40);
    public static final Color ON_PRIMARY_CONTAINER     = new Color(0x7EBDAC);
    public static final Color INVERSE_PRIMARY          = new Color(0x94D3C1);
    public static final Color PRIMARY_FIXED            = new Color(0xAFEFDD);
    public static final Color PRIMARY_FIXED_DIM        = new Color(0x94D3C1);
    public static final Color ON_PRIMARY_FIXED         = new Color(0x00201A);
    public static final Color ON_PRIMARY_FIXED_VARIANT = new Color(0x065043);

    // ─── Secondary (Trophy Gold) ─────────────────────────────────────────────
    public static final Color SECONDARY                  = new Color(0x705D00);
    public static final Color ON_SECONDARY               = new Color(0xFFFFFF);
    public static final Color SECONDARY_CONTAINER        = new Color(0xFCD400);
    public static final Color ON_SECONDARY_CONTAINER     = new Color(0x6E5C00);
    public static final Color SECONDARY_FIXED            = new Color(0xFFE16D);
    public static final Color SECONDARY_FIXED_DIM        = new Color(0xE9C400);
    public static final Color ON_SECONDARY_FIXED         = new Color(0x221B00);
    public static final Color ON_SECONDARY_FIXED_VARIANT = new Color(0x544600);

    // ─── Tertiary (Action Blue) ──────────────────────────────────────────────
    public static final Color TERTIARY                  = new Color(0x002E56);
    public static final Color ON_TERTIARY               = new Color(0xFFFFFF);
    public static final Color TERTIARY_CONTAINER        = new Color(0x00457B);
    public static final Color ON_TERTIARY_CONTAINER     = new Color(0x76B4FF);
    public static final Color TERTIARY_FIXED            = new Color(0xD3E4FF);
    public static final Color TERTIARY_FIXED_DIM        = new Color(0xA2C9FF);
    public static final Color ON_TERTIARY_FIXED         = new Color(0x001C38);
    public static final Color ON_TERTIARY_FIXED_VARIANT = new Color(0x004881);

    // ─── Error ───────────────────────────────────────────────────────────────
    public static final Color ERROR              = new Color(0xBA1A1A);
    public static final Color ON_ERROR           = new Color(0xFFFFFF);
    public static final Color ERROR_CONTAINER    = new Color(0xFFDAD6);
    public static final Color ON_ERROR_CONTAINER = new Color(0x93000A);

    // ─── Surface & Neutrals ──────────────────────────────────────────────────
    public static final Color SURFACE                   = new Color(0xF8FAFB);
    public static final Color SURFACE_DIM               = new Color(0xD8DADB);
    public static final Color SURFACE_BRIGHT            = new Color(0xF8FAFB);
    public static final Color SURFACE_CONTAINER_LOWEST  = new Color(0xFFFFFF);
    public static final Color SURFACE_CONTAINER_LOW     = new Color(0xF2F4F5);
    public static final Color SURFACE_CONTAINER         = new Color(0xECEEEF);
    public static final Color SURFACE_CONTAINER_HIGH    = new Color(0xE6E8E9);
    public static final Color SURFACE_CONTAINER_HIGHEST = new Color(0xE1E3E4);
    public static final Color ON_SURFACE                = new Color(0x191C1D);
    public static final Color ON_SURFACE_VARIANT        = new Color(0x3F4945);
    public static final Color INVERSE_SURFACE           = new Color(0x2E3132);
    public static final Color INVERSE_ON_SURFACE        = new Color(0xEFF1F2);
    public static final Color SURFACE_TINT              = new Color(0x29695B);
    public static final Color SURFACE_VARIANT           = new Color(0xE1E3E4);

    // ─── Outline ─────────────────────────────────────────────────────────────
    public static final Color OUTLINE         = new Color(0x707975);
    public static final Color OUTLINE_VARIANT = new Color(0xBFC9C4);

    // ─── Background ──────────────────────────────────────────────────────────
    public static final Color BACKGROUND    = new Color(0xF8FAFB);
    public static final Color ON_BACKGROUND = new Color(0x191C1D);

    // ─── Alias semánticos (uso rápido en componentes) ─────────────────────────
    /** Fondo de la app */
    public static final Color APP_BG         = BACKGROUND;
    /** Verde oscuro — nav, botón primario */
    public static final Color GREEN_DARK     = PRIMARY;
    /** Dorado — highlights, awards */
    public static final Color GOLD           = SECONDARY_CONTAINER;
    /** Azul acción — links secundarios */
    public static final Color BLUE_ACTION    = TERTIARY;
    /** Blanco puro — cards */
    public static final Color CARD_BG        = SURFACE_CONTAINER_LOWEST;
    /** Borde sutil 1px */
    public static final Color BORDER         = new Color(0xE0E0E0);
    /** Texto principal */
    public static final Color TEXT_PRIMARY   = ON_SURFACE;
    /** Texto secundario */
    public static final Color TEXT_SECONDARY = ON_SURFACE_VARIANT;
    /** Sombra suave */
    public static final Color SHADOW         = new Color(0, 0, 0, 13); // ~5% opacidad
}
