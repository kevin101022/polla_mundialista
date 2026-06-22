package ui.components;

import ui.theme.AppColors;
import ui.theme.AppFonts;
import ui.theme.AppStyles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Botón con esquinas redondeadas y estilos del Design System.
 *
 * Tipos disponibles:
 *   PRIMARY   → fondo verde, texto blanco
 *   SECONDARY → borde verde, texto verde, fondo blanco
 *   GHOST     → sin borde, texto azul
 */
public class RoundedButton extends JButton {

    public enum Style { PRIMARY, SECONDARY, GHOST }

    private Style buttonStyle;
    private int radius;
    private Color bgColor;
    private Color hoverColor;
    private Color textColor;
    private Color borderColor;
    private int borderWidth;

    // ─────────────────────────────────────────────────────────────────────────

    public RoundedButton(String text, Style style) {
        super(text);
        this.buttonStyle = style;
        this.radius      = AppStyles.RADIUS_MD;
        init();
    }

    public RoundedButton(String text) {
        this(text, Style.PRIMARY);
    }

    // ─── Inicialización ───────────────────────────────────────────────────────

    private void init() {
        setFont(AppFonts.labelMd());
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(
            AppStyles.SPACING_SM + 2, AppStyles.SPACING_LG,
            AppStyles.SPACING_SM + 2, AppStyles.SPACING_LG));

        switch (buttonStyle) {
            case PRIMARY:
                bgColor     = AppColors.PRIMARY;
                hoverColor  = AppColors.PRIMARY_CONTAINER;
                textColor   = AppColors.ON_PRIMARY;
                borderColor = AppColors.PRIMARY;
                borderWidth = 0;
                break;
            case SECONDARY:
                bgColor     = AppColors.SURFACE_CONTAINER_LOWEST;
                hoverColor  = AppColors.PRIMARY_FIXED;
                textColor   = AppColors.PRIMARY;
                borderColor = AppColors.PRIMARY;
                borderWidth = 2;
                break;
            case GHOST:
                bgColor     = new Color(0, 0, 0, 0);
                hoverColor  = AppColors.SURFACE_CONTAINER_LOW;
                textColor   = AppColors.BLUE_ACTION;
                borderColor = new Color(0, 0, 0, 0);
                borderWidth = 0;
                break;
        }
        setForeground(textColor);

        // Hover effect
        addMouseListener(new MouseAdapter() {
            Color originalBg = bgColor;
            @Override public void mouseEntered(MouseEvent e) { bgColor = hoverColor; repaint(); }
            @Override public void mouseExited(MouseEvent e)  { bgColor = originalBg;  repaint(); }
        });
    }

    // ─── Pintado personalizado ────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        // Sombra suave (solo PRIMARY)
        if (buttonStyle == Style.PRIMARY) {
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fill(new RoundRectangle2D.Float(2, 3, w - 4, h - 2, radius, radius));
        }

        // Fondo
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, radius, radius));

        // Borde (SECONDARY)
        if (borderWidth > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.draw(new RoundRectangle2D.Float(
                borderWidth / 2f, borderWidth / 2f,
                w - borderWidth, h - borderWidth,
                radius, radius));
        }

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) { /* Manejado en paintComponent */ }

    // ─── Setters ──────────────────────────────────────────────────────────────

    public void setRadius(int radius) { this.radius = radius; repaint(); }
}
