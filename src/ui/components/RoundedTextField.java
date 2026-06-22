package ui.components;

import ui.theme.AppColors;
import ui.theme.AppFonts;
import ui.theme.AppStyles;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Campo de texto estilizado con el Design System.
 *
 * Comportamiento:
 *  - Por defecto: borde gris 1px
 *  - Focus: borde verde Primary 2px
 *  - Error: borde rojo 2px + mensaje helper debajo
 */
public class RoundedTextField extends JPanel {

    private final JTextField field;
    private final JLabel    errorLabel;
    private boolean hasError = false;

    // ─────────────────────────────────────────────────────────────────────────

    public RoundedTextField(String placeholder) {
        setLayout(new BorderLayout(0, AppStyles.SPACING_XS));
        setOpaque(false);

        field = new JTextField();
        field.setFont(AppFonts.bodyMd());
        field.setForeground(AppColors.TEXT_PRIMARY);
        field.setBackground(AppColors.SURFACE_CONTAINER_LOWEST);
        field.setCaretColor(AppColors.PRIMARY);
        applyDefaultBorder();

        // Placeholder hint
        if (placeholder != null && !placeholder.isEmpty()) {
            field.putClientProperty("placeholder", placeholder);
            setPlaceholder(placeholder);
        }

        // Focus listeners
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { if (!hasError) applyFocusBorder(); }
            @Override public void focusLost(FocusEvent e)   { if (!hasError) applyDefaultBorder(); }
        });

        // Error label (oculto por defecto)
        errorLabel = new JLabel();
        errorLabel.setFont(AppFonts.caption());
        errorLabel.setForeground(AppColors.ERROR);
        errorLabel.setVisible(false);

        add(field,       BorderLayout.CENTER);
        add(errorLabel,  BorderLayout.SOUTH);
    }

    public RoundedTextField() {
        this(null);
    }

    // ─── Borders ─────────────────────────────────────────────────────────────

    private void applyDefaultBorder() {
        field.setBorder(new CompoundBorder(
            new LineBorder(AppColors.BORDER, 1, true),
            new EmptyBorder(AppStyles.SPACING_SM, AppStyles.SPACING_MD,
                            AppStyles.SPACING_SM, AppStyles.SPACING_MD)
        ));
    }

    private void applyFocusBorder() {
        field.setBorder(new CompoundBorder(
            new LineBorder(AppColors.PRIMARY, 2, true),
            new EmptyBorder(AppStyles.SPACING_SM - 1, AppStyles.SPACING_MD - 1,
                            AppStyles.SPACING_SM - 1, AppStyles.SPACING_MD - 1)
        ));
    }

    private void applyErrorBorder() {
        field.setBorder(new CompoundBorder(
            new LineBorder(AppColors.ERROR, 2, true),
            new EmptyBorder(AppStyles.SPACING_SM - 1, AppStyles.SPACING_MD - 1,
                            AppStyles.SPACING_SM - 1, AppStyles.SPACING_MD - 1)
        ));
    }

    // ─── Placeholder ──────────────────────────────────────────────────────────

    private void setPlaceholder(String hint) {
        field.setForeground(AppColors.TEXT_SECONDARY);
        field.setText(hint);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(AppColors.TEXT_PRIMARY);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(AppColors.TEXT_SECONDARY);
                    field.setText(hint);
                }
            }
        });
    }

    // ─── API Pública ──────────────────────────────────────────────────────────

    public String getText() { return field.getText(); }
    public void setText(String t) { field.setText(t); }
    public JTextField getField() { return field; }

    /** Muestra un mensaje de error y aplica el borde rojo. */
    public void showError(String message) {
        hasError = true;
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        applyErrorBorder();
    }

    /** Limpia el estado de error. */
    public void clearError() {
        hasError = false;
        errorLabel.setText("");
        errorLabel.setVisible(false);
        applyDefaultBorder();
    }

    public void setEditable(boolean editable) { field.setEditable(editable); }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        field.setEnabled(enabled);
    }
}
