package ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Métodos de fábrica para aplicar estilos del Design System a componentes Swing.
 *
 * Reglas de uso:
 *  - Los estilos PRIMARY/SECONDARY/GHOST corresponden a los 3 tipos de botón del DS.
 *  - Siempre usar AppColors y AppFonts en lugar de valores hardcodeados.
 *  - El radio de esquinas estándar es 8px; cards usan 16px.
 */
public final class AppStyles {

    private AppStyles() {}

    // ─── Spacing constants (8px unit scale) ───────────────────────────────────
    public static final int SPACING_XS  = 4;
    public static final int SPACING_SM  = 8;
    public static final int SPACING_MD  = 16;
    public static final int SPACING_LG  = 24;
    public static final int SPACING_XL  = 40;
    public static final int CONTAINER_PADDING = 32;

    // ─── Border radius ────────────────────────────────────────────────────────
    public static final int RADIUS_SM   = 4;
    public static final int RADIUS_MD   = 8;
    public static final int RADIUS_LG   = 16;
    public static final int RADIUS_FULL = 9999;

    // ─── Panels ───────────────────────────────────────────────────────────────

    /**
     * Aplica estilo de "card" Level-1 al panel:
     * fondo blanco, borde sutil 1px, padding interior.
     */
    public static void styleCard(JPanel panel) {
        panel.setBackground(AppColors.CARD_BG);
        panel.setBorder(new CompoundBorder(
            new LineBorder(AppColors.BORDER, 1, true),
            new EmptyBorder(SPACING_LG, SPACING_LG, SPACING_LG, SPACING_LG)
        ));
    }

    /**
     * Aplica el fondo de pantalla principal (APP_BG).
     */
    public static void styleScreenPanel(JPanel panel) {
        panel.setBackground(AppColors.APP_BG);
        panel.setBorder(new EmptyBorder(CONTAINER_PADDING, CONTAINER_PADDING,
                                         CONTAINER_PADDING, CONTAINER_PADDING));
    }

    /**
     * Panel de navegación lateral — Stadium Deep Green.
     */
    public static void styleNavPanel(JPanel panel) {
        panel.setBackground(AppColors.PRIMARY);
        panel.setBorder(new EmptyBorder(SPACING_LG, SPACING_MD, SPACING_LG, SPACING_MD));
    }

    // ─── Labels ───────────────────────────────────────────────────────────────

    public static void styleDisplayLabel(JLabel label) {
        label.setFont(AppFonts.displayLg());
        label.setForeground(AppColors.TEXT_PRIMARY);
    }

    public static void styleHeadlineLabel(JLabel label) {
        label.setFont(AppFonts.headlineLg());
        label.setForeground(AppColors.TEXT_PRIMARY);
    }

    public static void styleHeadlineMdLabel(JLabel label) {
        label.setFont(AppFonts.headlineMd());
        label.setForeground(AppColors.TEXT_PRIMARY);
    }

    public static void styleBodyLabel(JLabel label) {
        label.setFont(AppFonts.bodyMd());
        label.setForeground(AppColors.TEXT_SECONDARY);
    }

    public static void styleLabelOnPrimary(JLabel label) {
        label.setFont(AppFonts.labelMd());
        label.setForeground(AppColors.ON_PRIMARY);
    }

    // ─── Inputs ───────────────────────────────────────────────────────────────

    /**
     * Estado default: fondo blanco, borde gris 1px, radio 8px.
     */
    public static void styleTextField(JTextField field) {
        field.setFont(AppFonts.bodyMd());
        field.setForeground(AppColors.TEXT_PRIMARY);
        field.setBackground(AppColors.SURFACE_CONTAINER_LOWEST);
        field.setBorder(new CompoundBorder(
            new LineBorder(AppColors.BORDER, 1, true),
            new EmptyBorder(SPACING_SM, SPACING_MD, SPACING_SM, SPACING_MD)
        ));
        field.setCaretColor(AppColors.PRIMARY);
    }

    /**
     * Estado error: borde rojo 2px.
     */
    public static void styleTextFieldError(JTextField field) {
        field.setBorder(new CompoundBorder(
            new LineBorder(AppColors.ERROR, 2, true),
            new EmptyBorder(SPACING_SM, SPACING_MD, SPACING_SM, SPACING_MD)
        ));
    }

    // ─── Buttons ──────────────────────────────────────────────────────────────

    /**
     * Botón Primary: fondo Stadium Green, texto blanco, radio 8px.
     */
    public static void stylePrimaryButton(JButton btn) {
        btn.setFont(AppFonts.labelMd());
        btn.setForeground(AppColors.ON_PRIMARY);
        btn.setBackground(AppColors.PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(SPACING_SM + 2, SPACING_LG, SPACING_SM + 2, SPACING_LG));
    }

    /**
     * Botón Secondary: fondo transparente, borde verde 2px, texto verde.
     */
    public static void styleSecondaryButton(JButton btn) {
        btn.setFont(AppFonts.labelMd());
        btn.setForeground(AppColors.PRIMARY);
        btn.setBackground(AppColors.SURFACE_CONTAINER_LOWEST);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
            new LineBorder(AppColors.PRIMARY, 2, true),
            new EmptyBorder(SPACING_SM, SPACING_LG, SPACING_SM, SPACING_LG)
        ));
    }

    /**
     * Botón Ghost: sin borde, texto Action Blue — links y acciones terciarias.
     */
    public static void styleGhostButton(JButton btn) {
        btn.setFont(AppFonts.bodyMd());
        btn.setForeground(AppColors.BLUE_ACTION);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(SPACING_XS, SPACING_SM, SPACING_XS, SPACING_SM));
    }

    // ─── Tables ───────────────────────────────────────────────────────────────

    public static void styleTable(JTable table) {
        table.setFont(AppFonts.bodyMd());
        table.setForeground(AppColors.TEXT_PRIMARY);
        table.setBackground(AppColors.CARD_BG);
        table.setRowHeight(44);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(AppColors.BORDER);
        table.getTableHeader().setFont(AppFonts.labelMd());
        table.getTableHeader().setBackground(AppColors.SURFACE_CONTAINER_LOW);
        table.getTableHeader().setForeground(AppColors.TEXT_SECONDARY);
        table.setSelectionBackground(AppColors.PRIMARY_FIXED);
        table.setSelectionForeground(AppColors.ON_PRIMARY_FIXED);
    }

    // ─── Separators / Dividers ────────────────────────────────────────────────

    public static Border cardBorder() {
        return new CompoundBorder(
            new LineBorder(AppColors.BORDER, 1, true),
            new EmptyBorder(SPACING_LG, SPACING_LG, SPACING_LG, SPACING_LG)
        );
    }

    public static Border inputBorder() {
        return new CompoundBorder(
            new LineBorder(AppColors.BORDER, 1, true),
            new EmptyBorder(SPACING_SM, SPACING_MD, SPACING_SM, SPACING_MD)
        );
    }

    public static Border inputFocusBorder() {
        return new CompoundBorder(
            new LineBorder(AppColors.PRIMARY, 2, true),
            new EmptyBorder(SPACING_SM - 1, SPACING_MD - 1, SPACING_SM - 1, SPACING_MD - 1)
        );
    }
}
