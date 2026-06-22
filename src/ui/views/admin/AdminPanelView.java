package ui.views.admin;

import model.Partido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminPanelView extends JFrame {

    private final Color SURFACE_LOW = new Color(242, 244, 245);
    private final Color SURFACE_LOWEST = new Color(255, 255, 255);
    private final Color SURFACE_HIGH = new Color(230, 232, 233);
    private final Color ERROR_COLOR = new Color(186, 26, 26);
    private final Color TEXT_MUTED = new Color(63, 73, 69);
    private final Color OUTLINE = new Color(112, 121, 117);
    private final Color PRIMARY = new Color(0, 52, 43);

    private JComboBox<String> cmbFases;
    private JComboBox<PartidoItem> matchSelect;
    private JTextField txtHome;
    private JTextField txtAway;
    private JButton btnFinalize;

    public AdminPanelView() {
        setTitle("Panel de Administración - Polla Mundialista 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SURFACE_LOW);

        JPanel rootContainer = new JPanel(new BorderLayout());
        rootContainer.setBackground(SURFACE_LOW);
        rootContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        RoundedPanel card = new RoundedPanel(15, SURFACE_LOWEST);
        card.setLayout(new BorderLayout());

        card.add(createHeader(), BorderLayout.NORTH);
        card.add(createBody(), BorderLayout.CENTER);
        card.add(createFooter(), BorderLayout.SOUTH);

        rootContainer.add(card, BorderLayout.CENTER);
        add(rootContainer);
    }

    public JComboBox<String> getCmbFases() { return cmbFases; }
    public JComboBox<PartidoItem> getMatchSelect() { return matchSelect; }
    public JTextField getTxtHome() { return txtHome; }
    public JTextField getTxtAway() { return txtAway; }
    public JButton getBtnFinalize() { return btnFinalize; }

    public void setOnFinalizarListener(ActionListener listener) {
        // Quitamos cualquier listener previo y agregamos el del Controlador
        for (ActionListener al : btnFinalize.getActionListeners()) {
            btnFinalize.removeActionListener(al);
        }
        btnFinalize.addActionListener(listener);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SURFACE_HIGH);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 227, 228)),
                new EmptyBorder(20, 25, 20, 25)
        ));

        JLabel lblTitle = new JLabel("🛡️ PANEL DE ADMINISTRACIÓN - ACCESO RESTRINGIDO");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(ERROR_COLOR);

        JLabel lblVersion = new JLabel("V1.0.4");
        lblVersion.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblVersion.setForeground(TEXT_MUTED);
        lblVersion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(OUTLINE, 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblVersion, BorderLayout.EAST);
        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblMatch = new JLabel("Seleccionar Partido en Vivo");
        lblMatch.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMatch.setForeground(TEXT_MUTED);
        lblMatch.setAlignmentX(Component.LEFT_ALIGNMENT);

        matchSelect = new JComboBox<>();
        matchSelect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        matchSelect.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
        matchSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        matchSelect.setBackground(SURFACE_LOWEST);
        matchSelect.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblMatchHint = new JLabel("Solo se muestran partidos con estado 'Pendiente' o 'En Progreso'.");
        lblMatchHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblMatchHint.setForeground(TEXT_MUTED);
        lblMatchHint.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel goalsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        goalsPanel.setOpaque(false);
        goalsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        goalsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        goalsPanel.add(createGoalInput("Goles Oficiales Local"));
        goalsPanel.add(createGoalInput("Goles Oficiales Visitante"));

        JPanel vsPanel = createVSGraphic();
        vsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFase = new JLabel("Fase / Jornada");
        lblFase.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblFase.setForeground(TEXT_MUTED);
        lblFase.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbFases = new JComboBox<>();
        cmbFases.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cmbFases.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
        cmbFases.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbFases.setBackground(SURFACE_LOWEST);
        cmbFases.setFont(new Font("SansSerif", Font.PLAIN, 14));

        body.add(lblFase);
        body.add(Box.createRigidArea(new Dimension(0, 5)));
        body.add(cmbFases);
        body.add(Box.createRigidArea(new Dimension(0, 20)));

        body.add(lblMatch);
        body.add(Box.createRigidArea(new Dimension(0, 10)));
        body.add(matchSelect);
        body.add(Box.createRigidArea(new Dimension(0, 5)));
        body.add(lblMatchHint);
        body.add(Box.createRigidArea(new Dimension(0, 30)));
        body.add(goalsPanel);
        body.add(Box.createRigidArea(new Dimension(0, 40)));
        body.add(vsPanel);

        return body;
    }

    private JPanel createGoalInput(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel inputWrapper = new JPanel(new BorderLayout(10, 0));
        inputWrapper.setOpaque(false);
        inputWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputWrapper.setBorder(new EmptyBorder(5,0,0,0));

        JLabel iconLabel = new JLabel("⚽");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
        
        JTextField txtGoal = new JTextField();
        txtGoal.setFont(new Font("SansSerif", Font.BOLD, 28));
        txtGoal.setHorizontalAlignment(JTextField.CENTER);
        txtGoal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(OUTLINE, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        if (title.contains("Local")) txtHome = txtGoal;
        else txtAway = txtGoal;

        inputWrapper.add(iconLabel, BorderLayout.WEST);
        inputWrapper.add(txtGoal, BorderLayout.CENTER);

        panel.add(lbl);
        panel.add(inputWrapper);

        return panel;
    }

    private JPanel createVSGraphic() {
        RoundedPanel panel = new RoundedPanel(15, new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 12));
        panel.setLayout(new GridBagLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 140));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);

        JPanel pnlLocal = createTeamIcon("LOCAL", PRIMARY);
        gbc.gridx = 0;
        panel.add(pnlLocal, gbc);

        JLabel lblVS = new JLabel("VS");
        lblVS.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblVS.setForeground(new Color(PRIMARY.getRed(), PRIMARY.getGreen(), PRIMARY.getBlue(), 50));
        gbc.gridx = 1;
        panel.add(lblVS, gbc);

        JPanel pnlAway = createTeamIcon("VISITANTE", OUTLINE);
        gbc.gridx = 2;
        panel.add(pnlAway, gbc);

        return panel;
    }

    private JPanel createTeamIcon(String teamName, Color ringColor) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        JPanel circle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE_LOWEST);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(ringColor);
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(2, 2, getWidth() - 5, getHeight() - 5);
                g2.setColor(new Color(200, 200, 200));
                g2.fillRoundRect(getWidth()/2 - 12, getHeight()/2 - 12, 24, 24, 4, 4);
                g2.dispose();
            }
        };
        circle.setPreferredSize(new Dimension(72, 72));
        circle.setOpaque(false);

        JLabel lbl = new JLabel(teamName, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        lbl.setBorder(new EmptyBorder(8, 0, 0, 0));

        container.add(circle, BorderLayout.CENTER);
        container.add(lbl, BorderLayout.SOUTH);
        return container;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(new Color(236, 238, 239));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(225, 227, 228)),
                new EmptyBorder(25, 30, 25, 30)
        ));

        btnFinalize = new JButton("⚠️ Finalizar Partido y Calcular Puntos ⚠️");
        btnFinalize.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnFinalize.setBackground(ERROR_COLOR);
        btnFinalize.setForeground(Color.WHITE);
        btnFinalize.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnFinalize.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFinalize.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnFinalize.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        btnFinalize.setFocusPainted(false);
        btnFinalize.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        bottomRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JButton btnClose = new JButton("❌ Cerrar Panel");
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setForeground(TEXT_MUTED);
        btnClose.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());

        JLabel lblSecure = new JLabel("🔒 Sesión Segura Encriptada");
        lblSecure.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSecure.setForeground(OUTLINE);

        bottomRow.add(btnClose, BorderLayout.WEST);
        bottomRow.add(lblSecure, BorderLayout.EAST);

        footer.add(btnFinalize);
        footer.add(Box.createRigidArea(new Dimension(0, 20)));
        footer.add(bottomRow);

        return footer;
    }

    // --- Wrapper Class for JComboBox ---
    public static class PartidoItem {
        private final Partido partido;
        private final String label;

        public PartidoItem(Partido partido, String label) {
            this.partido = partido;
            this.label = label;
        }

        public Partido getPartido() {
            return partido;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // Main de prueba para abrirlo directo si es necesario
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> {
            AdminPanelView view = new AdminPanelView();
            // Para usar el controlador:
            dao.PartidoDAO pDao = new dao.impl.PartidoDAOImpl();
            dao.PronosticoDAO prDao = new dao.impl.PronosticoDAOImpl();
            dao.UsuarioDAO uDao = new dao.impl.UsuarioDAOImpl();
            new controller.AdminController(view, pDao, prDao, uDao);
            view.setVisible(true);
        });
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.setColor(bgColor);
            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
            graphics.setColor(new Color(225, 227, 228));
            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
        }
    }
}
