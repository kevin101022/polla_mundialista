package ui.components;

import ui.theme.AppColors;
import ui.theme.AppStyles;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel con esquinas redondeadas, fondo y borde del Design System.
 * Reemplaza al JPanel estándar para cards y contenedores elevados.
 *
 * Niveles de elevación:
 *   LEVEL_0 → fondo APP_BG, sin sombra
 *   LEVEL_1 → fondo CARD_BG (blanco), sombra suave (default)
 *   LEVEL_2 → fondo CARD_BG, sombra más pronunciada (hover/modal)
 */
public class RoundedPanel extends JPanel {

    public enum Elevation { LEVEL_0, LEVEL_1, LEVEL_2 }

    private int radius;
    private Color bgColor;
    private Elevation elevation;
    private boolean showBorder;

    // ─────────────────────────────────────────────────────────────────────────

    public RoundedPanel(int radius, Elevation elevation, boolean showBorder) {
        this.radius      = radius;
        this.elevation   = elevation;
        this.showBorder  = showBorder;
        this.bgColor     = (elevation == Elevation.LEVEL_0)
                           ? AppColors.APP_BG
                           : AppColors.CARD_BG;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(
            AppStyles.SPACING_LG, AppStyles.SPACING_LG,
            AppStyles.SPACING_LG, AppStyles.SPACING_LG));
    }

    /** Card estándar (LEVEL_1, radio 16px, con borde). */
    public RoundedPanel() {
        this(AppStyles.RADIUS_LG, Elevation.LEVEL_1, true);
    }

    /** Card con radio personalizado. */
    public RoundedPanel(int radius) {
        this(radius, Elevation.LEVEL_1, true);
    }

    // ─── Pintado ──────────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        // Sombra
        paintShadow(g2, w, h);

        // Fondo redondeado
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, radius, radius));

        // Borde sutil
        if (showBorder) {
            g2.setColor(AppColors.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, radius, radius));
        }

        g2.dispose();
        super.paintComponent(g);
    }

    private void paintShadow(Graphics2D g2, int w, int h) {
        switch (elevation) {
            case LEVEL_1:
                // 0px 4px 12px rgba(0,0,0,0.05)
                for (int i = 1; i <= 6; i++) {
                    float alpha = 0.008f * (7 - i);
                    g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2.fill(new RoundRectangle2D.Float(-i, i, w + i * 2, h + i, radius + i, radius + i));
                }
                break;
            case LEVEL_2:
                // 0px 8px 20px rgba(0,0,0,0.10)
                for (int i = 1; i <= 10; i++) {
                    float alpha = 0.012f * (11 - i);
                    g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2.fill(new RoundRectangle2D.Float(-i, i, w + i * 2, h + i, radius + i, radius + i));
                }
                break;
            default:
                break;
        }
    }

    // ─── Setters ──────────────────────────────────────────────────────────────

    public void setRadius(int radius)          { this.radius    = radius;    repaint(); }
    public void setBgColor(Color color)        { this.bgColor   = color;     repaint(); }
    public void setElevation(Elevation level)  { this.elevation = level;     repaint(); }
    public void setShowBorder(boolean show)    { this.showBorder = show;     repaint(); }
}
