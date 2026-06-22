package ui.components;

import ui.theme.AppColors;
import ui.theme.AppFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Pastilla (pill) de estado de solo lectura.
 * Completamente redondeada, con colores semánticos.
 *
 * Variantes disponibles:
 *   SUCCESS  → verde (completado, activo)
 *   WARNING  → dorado (pendiente, en progreso)
 *   DANGER   → rojo  (error, inactivo)
 *   INFO     → azul  (informativo)
 *   NEUTRAL  → gris  (default)
 */
public class StatusPill extends JLabel {

    public enum Variant { SUCCESS, WARNING, DANGER, INFO, NEUTRAL }

    private Color pillBg;
    private Color pillFg;

    // ─────────────────────────────────────────────────────────────────────────

    public StatusPill(String text, Variant variant) {
        super(text, SwingConstants.CENTER);
        setFont(AppFonts.labelMd());
        setOpaque(false);
        setPreferredSize(new Dimension(getPreferredSize().width + 24, 26));
        applyVariant(variant);
    }

    public StatusPill(String text) {
        this(text, Variant.NEUTRAL);
    }

    // ─── Colores por variante ─────────────────────────────────────────────────

    private void applyVariant(Variant variant) {
        switch (variant) {
            case SUCCESS:
                pillBg = AppColors.PRIMARY_FIXED;
                pillFg = AppColors.ON_PRIMARY_FIXED;
                break;
            case WARNING:
                pillBg = AppColors.SECONDARY_FIXED;
                pillFg = AppColors.ON_SECONDARY_FIXED;
                break;
            case DANGER:
                pillBg = AppColors.ERROR_CONTAINER;
                pillFg = AppColors.ON_ERROR_CONTAINER;
                break;
            case INFO:
                pillBg = AppColors.TERTIARY_FIXED;
                pillFg = AppColors.ON_TERTIARY_FIXED;
                break;
            default:
                pillBg = AppColors.SURFACE_CONTAINER_HIGH;
                pillFg = AppColors.TEXT_SECONDARY;
                break;
        }
        setForeground(pillFg);
    }

    // ─── Pintado ──────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo pill completamente redondeado
        g2.setColor(pillBg);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Insets getInsets() {
        return new Insets(4, 12, 4, 12);
    }
}
