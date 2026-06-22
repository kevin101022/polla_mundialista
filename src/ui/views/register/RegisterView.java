package ui.views.register;

import ui.MainFrame;
import ui.theme.AppColors;
import ui.theme.AppFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Vista de Registro — Polla Mundialista 2026
 *
 * Idéntica estructura a LoginView pero con:
 *  - Círculo dorado (secondary-container #fcd400)
 *  - Subtítulo "Crear nueva cuenta"
 *  - Botón "Registrarme y Jugar ✓"
 *  - Link "← Volver al Login"
 */
public class RegisterView extends JPanel {

    private static final int CARD_W  = 448;
    private static final int PAD     = 40;
    private static final int INNER_W = CARD_W - PAD * 2;
    private static final int INPUT_H = 46;
    private static final int BTN_H   = 48;
    private static final int RADIUS  = 8;

    private final MainFrame mainFrame;
    private JTextField      userField;
    private JPasswordField  passField;
    private JLabel          userError;
    private JLabel          passError;
    private JButton         btnRegistrar;

    public RegisterView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(AppColors.SURFACE_CONTAINER_LOWEST);
        add(buildCard());
    }

    // ─── Fondo con grilla ─────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(AppColors.SURFACE_CONTAINER_LOWEST);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(0, 0, 0, 8));
        for (int x = 0; x <= getWidth();  x += 40) g2.drawLine(x, 0, x, getHeight());
        for (int y = 0; y <= getHeight(); y += 40) g2.drawLine(0, y, getWidth(), y);
        g2.dispose();
    }

    // ─── Card ─────────────────────────────────────────────────────────────────

    private JPanel buildCard() {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (int i = 8; i >= 1; i--) {
                    g2.setColor(new Color(0, 0, 0, i * 3));
                    g2.fill(new RoundRectangle2D.Float(-i, i, getWidth() + i * 2f, getHeight(), 12, 12));
                }
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(new Color(0xBFC9C4));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2f, getHeight() - 2f, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);

        int y = PAD;

        // Fila 0: Círculo dorado
        JPanel circle = buildCircle(AppColors.SECONDARY_CONTAINER, "+", AppColors.ON_SECONDARY_CONTAINER);
        int circleX = PAD + (INNER_W - 64) / 2;
        circle.setBounds(circleX, y, 64, 64);
        card.add(circle);
        y += 64 + 16;

        // Fila 1: Título
        JLabel title = new JLabel("Polla Mundialista 2026", SwingConstants.CENTER);
        title.setFont(AppFonts.headlineLg());
        title.setForeground(AppColors.ON_SURFACE);
        title.setBounds(PAD, y, INNER_W, 40);
        card.add(title);
        y += 40 + 4;

        // Fila 2: Subtítulo
        JLabel sub = new JLabel("Crear nueva cuenta", SwingConstants.CENTER);
        sub.setFont(AppFonts.bodyMd());
        sub.setForeground(AppColors.ON_SURFACE_VARIANT);
        sub.setBounds(PAD, y, INNER_W, 24);
        card.add(sub);
        y += 24 + 24;

        // Fila 3: Label usuario
        JLabel lblUser = new JLabel("Nombre de usuario");
        lblUser.setFont(AppFonts.labelMd());
        lblUser.setForeground(AppColors.ON_SURFACE);
        lblUser.setBounds(PAD, y, INNER_W, 20);
        card.add(lblUser);
        y += 20 + 4;

        // Fila 4: Input usuario
        userField = new JTextField();
        JPanel userInput = buildInput(userField, "Ingrese su nombre de usuario", false, "\uD83D\uDC64");
        userInput.setBounds(PAD, y, INNER_W, INPUT_H);
        card.add(userInput);
        y += INPUT_H + 4;

        // Fila 5: Error usuario
        userError = buildErrorLabel();
        userError.setBounds(PAD, y, INNER_W, 16);
        card.add(userError);
        y += 16 + 12;

        // Fila 6: Label contraseña
        JLabel lblPass = new JLabel("Contrase\u00f1a");
        lblPass.setFont(AppFonts.labelMd());
        lblPass.setForeground(AppColors.ON_SURFACE);
        lblPass.setBounds(PAD, y, INNER_W, 20);
        card.add(lblPass);
        y += 20 + 4;

        // Fila 7: Input contraseña
        passField = new JPasswordField();
        JPanel passInput = buildInput(passField, "Ingrese su contrase\u00f1a", true, "\uD83D\uDD12");
        passInput.setBounds(PAD, y, INNER_W, INPUT_H);
        card.add(passInput);
        y += INPUT_H + 4;

        // Fila 8: Error contraseña
        passError = buildErrorLabel();
        passError.setBounds(PAD, y, INNER_W, 16);
        card.add(passError);
        y += 16 + 24;

        // Fila 9: Botón Registrar
        btnRegistrar = buildButton("Registrarme y Jugar  \u2713");
        JPanel btnPanel = new JPanel(null);
        btnPanel.setOpaque(false);
        btnPanel.add(btnRegistrar);
        btnPanel.setBounds(PAD, y, INNER_W, BTN_H);
        card.add(btnPanel);
        y += BTN_H + 24;

        // Fila 10: Link volver
        JPanel linkPanel = buildLink("\u2190 Volver al Login",
                e -> mainFrame.showView(MainFrame.VIEW_LOGIN));
        linkPanel.setBounds(PAD, y, INNER_W, 24);
        card.add(linkPanel);
        y += 24 + PAD;

        card.setPreferredSize(new Dimension(CARD_W, y));
        return card;
    }

    // ─── Círculo ícono ────────────────────────────────────────────────────────

    private JPanel buildCircle(Color bg, String symbol, Color fg) {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        JLabel lbl = new JLabel(symbol);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbl.setForeground(fg);
        p.add(lbl);
        return p;
    }

    // ─── Input con ícono ─────────────────────────────────────────────────────

    private JPanel buildInput(JTextField field, String placeholder,
                               boolean isPassword, String iconText) {

        final boolean[] focused = {false};

        JPanel wrapper = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), RADIUS, RADIUS));
                float sw = focused[0] ? 2f : 1f;
                g2.setColor(focused[0] ? AppColors.PRIMARY : new Color(0xBFC9C4));
                g2.setStroke(new BasicStroke(sw));
                float off = sw / 2f;
                g2.draw(new RoundRectangle2D.Float(off, off, getWidth() - sw, getHeight() - sw, RADIUS, RADIUS));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        wrapper.setOpaque(false);

        JLabel icon = new JLabel(iconText);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        icon.setForeground(new Color(0x3F4945));
        icon.setBounds(12, (INPUT_H - 20) / 2, 20, 20);
        wrapper.add(icon);

        field.setFont(AppFonts.bodyMd());
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
        field.setBounds(40, 0, INNER_W - 40 - 2, INPUT_H);
        field.setForeground(new Color(0x3F4945));
        field.setText(placeholder);
        if (isPassword) ((JPasswordField) field).setEchoChar((char) 0);

        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                focused[0] = true;
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(AppColors.ON_SURFACE);
                    if (isPassword) ((JPasswordField) field).setEchoChar('\u25CF');
                }
                wrapper.repaint();
            }
            @Override public void focusLost(FocusEvent e) {
                focused[0] = false;
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(0x3F4945));
                    if (isPassword) ((JPasswordField) field).setEchoChar((char) 0);
                }
                wrapper.repaint();
            }
        });

        wrapper.add(field);
        return wrapper;
    }

    // ─── Botón ────────────────────────────────────────────────────────────────

    private JButton buildButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x004D40) : AppColors.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), RADIUS, RADIUS));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(AppFonts.labelMd());
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBounds(0, 0, INNER_W, BTN_H);
        return btn;
    }

    // ─── Link ─────────────────────────────────────────────────────────────────

    private JPanel buildLink(String text, ActionListener action) {
        JButton link = new JButton(text);
        link.setFont(AppFonts.labelMd());
        link.setForeground(AppColors.TERTIARY);
        link.setContentAreaFilled(false);
        link.setBorderPainted(false);
        link.setFocusPainted(false);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { link.setForeground(AppColors.TERTIARY_CONTAINER); }
            @Override public void mouseExited(MouseEvent e)  { link.setForeground(AppColors.TERTIARY); }
        });
        link.addActionListener(action);

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrap.setOpaque(false);
        wrap.add(link);
        return wrap;
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private JLabel buildErrorLabel() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(AppColors.ERROR);
        return lbl;
    }



    public JTextField getUserField() { return userField; }
    public JPasswordField getPassField() { return passField; }
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JLabel getUserError() { return userError; }
    public JLabel getPassError() { return passError; }
}
