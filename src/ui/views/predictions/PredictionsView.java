package ui.views.predictions;

import model.Partido;
import ui.MainFrame;
import ui.theme.AppColors;
import ui.theme.AppFonts;
import util.FlagUtil;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PredictionsView extends JPanel {

    private final MainFrame mainFrame;
    private final JComboBox<String> cmbFases;
    private final JPanel centerPanel;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a");

    // Interfaz para inyectar la lógica del botón Guardar desde el Controlador
    public interface OnGuardarPronosticoListener {
        void onGuardar(Partido partido, JTextField txtLocal, JTextField txtVis, JButton btnGuardar);
    }
    
    private OnGuardarPronosticoListener guardarListener;

    public void setOnGuardarPronosticoListener(OnGuardarPronosticoListener listener) {
        this.guardarListener = listener;
    }

    public PredictionsView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppColors.BACKGROUND);
        topPanel.setBorder(new EmptyBorder(32, 32, 16, 32));
        
        topPanel.add(buildHeader(), BorderLayout.CENTER);
        
        cmbFases = new JComboBox<>();
        cmbFases.setFont(AppFonts.bodyMd());
        cmbFases.setPreferredSize(new Dimension(200, 36));
        cmbFases.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        JLabel lblFase = new JLabel("Fase: ");
        lblFase.setFont(AppFonts.labelMd());
        lblFase.setForeground(AppColors.ON_SURFACE);
        filterPanel.add(lblFase);
        filterPanel.add(cmbFases);
        
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(AppColors.BACKGROUND);
        centerPanel.setBorder(new EmptyBorder(0, 32, 32, 32));

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(AppColors.BACKGROUND);

        add(scroll, BorderLayout.CENTER);
    }

    public JComboBox<String> getCmbFases() {
        return cmbFases;
    }

    public void renderizarPartidos(Map<Partido, Boolean> partidosConEdicion, Map<Partido, model.Pronostico> pronosticosUsuario) {
        centerPanel.removeAll();
        
        for (Map.Entry<Partido, Boolean> entry : partidosConEdicion.entrySet()) {
            Partido p = entry.getKey();
            boolean editable = entry.getValue();
            model.Pronostico pron = pronosticosUsuario.get(p);
            
            JPanel card;
            if ("FINALIZADO".equalsIgnoreCase(p.getEstado())) {
                card = buildFinishedCard(p, pron);
            } else if ("EN_JUEGO".equalsIgnoreCase(p.getEstado())) {
                card = buildLiveCard(p, pron);
            } else {
                card = buildPendingCard(p, editable, pron);
            }
            
            centerPanel.add(card);
            centerPanel.add(Box.createVerticalStrut(16));
        }
        
        centerPanel.add(Box.createVerticalGlue());
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        backPanel.setOpaque(false);
        backPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel backIcon = new JLabel("←");
        backIcon.setFont(AppFonts.labelMd());
        backIcon.setForeground(AppColors.PRIMARY);
        
        JLabel backText = new JLabel("Volver al Ranking");
        backText.setFont(AppFonts.labelMd());
        backText.setForeground(AppColors.PRIMARY);
        backText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backText.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                mainFrame.showView(MainFrame.VIEW_LEADERBOARD);
            }
        });

        backPanel.add(backIcon);
        backPanel.add(backText);

        JLabel title = new JLabel("Predicciones de Partidos");
        title.setFont(AppFonts.headlineLg());
        title.setForeground(AppColors.ON_SURFACE);
        title.setBorder(new EmptyBorder(16, 0, 4, 0));

        JLabel sub = new JLabel("Ingresa tus pronósticos antes del pitazo inicial.");
        sub.setFont(AppFonts.bodyLg());
        sub.setForeground(AppColors.ON_SURFACE_VARIANT);

        header.add(backPanel);
        header.add(title);
        header.add(sub);
        
        return header;
    }

    // ─── Tarjetas Dinámicas ───────────────────────────────────────────────────

    private JPanel buildPendingCard(Partido p, boolean editable, model.Pronostico pron) {
        JPanel card = createBaseCard(null, null, null);
        
        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        String dateStr = p.getFechaHora() != null ? p.getFechaHora().format(DATE_FMT) : "Por def.";
        String timeStr = p.getFechaHora() != null ? p.getFechaHora().format(TIME_FMT) : "Por def.";
        String groupStr = p.getEquipoLocal().getGrupo() != null ? "GRUPO " + p.getEquipoLocal().getGrupo() : p.getFase();
        
        content.add(buildMatchInfo(groupStr, dateStr, timeStr), BorderLayout.WEST);
        
        JPanel centerBlock = createInnerBox();
        
        JPanel inputs = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        inputs.setOpaque(false);
        
        JTextField txtLocal = createTextField(editable);
        inputs.add(wrapScoreInput(txtLocal));
        
        JLabel dash = new JLabel("-");
        dash.setFont(AppFonts.headlineMd());
        dash.setForeground(AppColors.OUTLINE_VARIANT);
        inputs.add(dash);
        
        JTextField txtVis = createTextField(editable);
        inputs.add(wrapScoreInput(txtVis));
        
        if (pron != null) {
            txtLocal.setText(String.valueOf(pron.getGolesLocalPred()));
            txtVis.setText(String.valueOf(pron.getGolesVisitantePred()));
        }
        
        centerBlock.add(buildTeam(p.getEquipoLocal().getCodigoIso(), p.getEquipoLocal().getNombre()));
        centerBlock.add(Box.createHorizontalStrut(16));
        centerBlock.add(inputs);
        centerBlock.add(Box.createHorizontalStrut(16));
        centerBlock.add(buildTeam(p.getEquipoVisitante().getCodigoIso(), p.getEquipoVisitante().getNombre()));
        content.add(centerBlock, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        btnPanel.setPreferredSize(new Dimension(140, 0));
        
        JButton btnGuardar = buildGuardarBtn(editable);
        if (editable) {
            btnGuardar.addActionListener(e -> {
                if (guardarListener != null) {
                    guardarListener.onGuardar(p, txtLocal, txtVis, btnGuardar);
                }
            });
        }
        btnPanel.add(btnGuardar);
        
        content.add(btnPanel, BorderLayout.EAST);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildLiveCard(Partido p, model.Pronostico pron) {
        JPanel card = createBaseCard("EN JUEGO", AppColors.ERROR, AppColors.ON_ERROR);
        
        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        String dateStr = p.getFechaHora() != null ? p.getFechaHora().format(DATE_FMT) : "Por def.";
        String timeStr = p.getFechaHora() != null ? p.getFechaHora().format(TIME_FMT) : "Por def.";
        String groupStr = p.getEquipoLocal().getGrupo() != null ? "GRUPO " + p.getEquipoLocal().getGrupo() : p.getFase();

        content.add(buildMatchInfo(groupStr, dateStr, timeStr), BorderLayout.WEST);
        
        JPanel centerBlock = createInnerBox();
        centerBlock.add(buildTeam(p.getEquipoLocal().getCodigoIso(), p.getEquipoLocal().getNombre()));
        
        JPanel scores = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        scores.setOpaque(false);
        String s1 = pron != null ? String.valueOf(pron.getGolesLocalPred()) : "-";
        String s2 = pron != null ? String.valueOf(pron.getGolesVisitantePred()) : "-";
        
        scores.add(buildLiveScoreBox(s1));
        JLabel dash = new JLabel("-");
        dash.setFont(AppFonts.headlineMd());
        dash.setForeground(AppColors.OUTLINE_VARIANT);
        scores.add(dash);
        scores.add(buildLiveScoreBox(s2));
        centerBlock.add(scores);

        centerBlock.add(buildTeam(p.getEquipoVisitante().getCodigoIso(), p.getEquipoVisitante().getNombre()));
        content.add(centerBlock, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        btnPanel.setPreferredSize(new Dimension(140, 0));
        btnPanel.add(buildGuardarBtn(false));
        content.add(btnPanel, BorderLayout.EAST);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildFinishedCard(Partido p, model.Pronostico pron) {
        JPanel card = createBaseCard("FINALIZADO", AppColors.SURFACE_CONTAINER_HIGHEST, AppColors.ON_SURFACE_VARIANT);
        
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        String dateStr = p.getFechaHora() != null ? p.getFechaHora().format(DATE_FMT) : "Por def.";
        String timeStr = p.getFechaHora() != null ? p.getFechaHora().format(TIME_FMT) : "Por def.";
        String groupStr = p.getEquipoLocal().getGrupo() != null ? "GRUPO " + p.getEquipoLocal().getGrupo() : p.getFase();

        content.add(buildMatchInfo(groupStr, dateStr, timeStr), BorderLayout.WEST);
        
        JPanel centerBlock = createInnerBox();
        centerBlock.add(buildTeam(p.getEquipoLocal().getCodigoIso(), p.getEquipoLocal().getNombre()));
        
        JPanel scores = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        scores.setOpaque(false);
        String s1 = pron != null ? String.valueOf(pron.getGolesLocalPred()) : "-";
        String s2 = pron != null ? String.valueOf(pron.getGolesVisitantePred()) : "-";
        scores.add(buildFinishedScoreBox(s1));
        JLabel dash = new JLabel("-");
        dash.setFont(AppFonts.headlineMd());
        dash.setForeground(AppColors.OUTLINE_VARIANT);
        scores.add(dash);
        scores.add(buildFinishedScoreBox(s2));
        centerBlock.add(scores);

        centerBlock.add(buildTeam(p.getEquipoVisitante().getCodigoIso(), p.getEquipoVisitante().getNombre()));
        content.add(centerBlock, BorderLayout.CENTER);

        JPanel emptyRight = new JPanel();
        emptyRight.setOpaque(false);
        emptyRight.setPreferredSize(new Dimension(140, 0));
        content.add(emptyRight, BorderLayout.EAST);

        mainContent.add(content, BorderLayout.CENTER);

        JPanel banner = new JPanel(new BorderLayout());
        banner.setOpaque(false);
        banner.setBorder(new EmptyBorder(0, 24, 24, 24));
        
        JPanel bannerInner = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_CONTAINER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(AppColors.OUTLINE_VARIANT);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 8, 8));
                g2.dispose();
            }
        };
        bannerInner.setOpaque(false);
        bannerInner.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel resLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        resLeft.setOpaque(false);
        JLabel resLbl = new JLabel("Resultado Real:");
        resLbl.setFont(AppFonts.labelMd());
        resLbl.setForeground(AppColors.ON_SURFACE_VARIANT);
        
        String r1 = p.getGolesLocalReal() != null ? String.valueOf(p.getGolesLocalReal()) : "0";
        String r2 = p.getGolesVisitanteReal() != null ? String.valueOf(p.getGolesVisitanteReal()) : "0";
        JLabel resVal = new JLabel(r1 + " - " + r2);
        resVal.setFont(AppFonts.headlineMd());
        resVal.setForeground(AppColors.ON_SURFACE);
        resLeft.add(resLbl);
        resLeft.add(resVal);

        JPanel resRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        resRight.setOpaque(false);
        JLabel ptsIcon = new JLabel("⊗");
        ptsIcon.setFont(AppFonts.labelMd());
        ptsIcon.setForeground(AppColors.OUTLINE);
        String ptsTxt = pron != null && pron.getPuntosObtenidos() != null ? "Ganaste " + pron.getPuntosObtenidos() + " Pts" : "Sin pronóstico / Pts pdte";
        JLabel ptsLbl = new JLabel(ptsTxt);
        ptsLbl.setFont(AppFonts.labelMd());
        ptsLbl.setForeground(AppColors.ON_SURFACE_VARIANT);
        resRight.add(ptsIcon);
        resRight.add(ptsLbl);

        bannerInner.add(resLeft, BorderLayout.WEST);
        bannerInner.add(resRight, BorderLayout.EAST);
        
        banner.add(bannerInner, BorderLayout.CENTER);
        mainContent.add(banner, BorderLayout.SOUTH);

        card.add(mainContent, BorderLayout.CENTER);
        return card;
    }

    // ─── Helpers Visuales ─────────────────────────────────────────────────────

    private JPanel createBaseCard(String badgeText, Color badgeBg, Color badgeFg) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(AppColors.OUTLINE_VARIANT);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 12, 12));
                
                if (badgeText != null) {
                    g2.setColor(badgeBg);
                    g2.fill(new RoundRectangle2D.Float(getWidth() - 100, 0, 100, 24, 0, 0));
                    g2.setColor(badgeFg);
                    g2.setFont(new Font("Inter", Font.BOLD, 10));
                    FontMetrics fm = g2.getFontMetrics();
                    int x = getWidth() - 100 + (100 - fm.stringWidth(badgeText)) / 2;
                    int y = 16;
                    g2.drawString(badgeText, x, y);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        return card;
    }

    private JPanel buildMatchInfo(String group, String date, String time) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 0));

        JLabel gl = new JLabel(group);
        gl.setFont(new Font("Inter", Font.BOLD, 10));
        gl.setForeground(AppColors.OUTLINE);
        
        JLabel dl = new JLabel(date);
        dl.setFont(AppFonts.bodyMd());
        dl.setForeground(AppColors.ON_SURFACE);
        
        JLabel tl = new JLabel(time);
        tl.setFont(AppFonts.bodyMd());
        tl.setForeground(AppColors.ON_SURFACE_VARIANT);

        p.add(gl);
        p.add(Box.createVerticalStrut(4));
        p.add(dl);
        p.add(tl);
        return p;
    }

    private JPanel createInnerBox() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 16)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_BRIGHT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(AppColors.SURFACE_VARIANT);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 8, 8));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JPanel buildTeam(String codigoIso, String name) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 80));

        JLabel f = new JLabel();
        FlagUtil.aplicarBandera(f, codigoIso, 32, 24);
        f.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel n = new JLabel(name, SwingConstants.CENTER);
        n.setFont(AppFonts.labelMd());
        n.setForeground(AppColors.ON_SURFACE);
        n.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(f);
        p.add(Box.createVerticalStrut(8));
        p.add(n);
        return p;
    }

    private JTextField createTextField(boolean enabled) {
        JTextField field = new JTextField();
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setFont(new Font("Plus Jakarta Sans", Font.BOLD, 28));
        field.setOpaque(false);
        field.setBorder(null);
        field.setEnabled(enabled);
        return field;
    }

    private JPanel wrapScoreInput(JTextField field) {
        final boolean[] focused = {false};

        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean isEnabled = field.isEnabled();
                
                g2.setColor(isEnabled ? Color.WHITE : AppColors.SURFACE_VARIANT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                
                float sw = focused[0] ? 2f : 1f;
                g2.setColor(focused[0] ? AppColors.PRIMARY : AppColors.OUTLINE);
                g2.setStroke(new BasicStroke(sw));
                float off = sw / 2f;
                g2.draw(new RoundRectangle2D.Float(off, off, getWidth()-sw, getHeight()-sw, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(56, 56));
        
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { focused[0] = true; wrapper.repaint(); }
            @Override public void focusLost(FocusEvent e) { focused[0] = false; wrapper.repaint(); }
        });
        
        // Listen to dynamic enable/disable updates
        field.addPropertyChangeListener("enabled", evt -> {
            field.setForeground(field.isEnabled() ? AppColors.ON_SURFACE : AppColors.ON_SURFACE_VARIANT);
            wrapper.repaint();
        });
        
        // Initial color setup
        field.setForeground(field.isEnabled() ? AppColors.ON_SURFACE : AppColors.ON_SURFACE_VARIANT);

        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildLiveScoreBox(String score) {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.PRIMARY_CONTAINER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(56, 56));

        JLabel l = new JLabel(score);
        l.setFont(new Font("Plus Jakarta Sans", Font.BOLD, 28));
        l.setForeground(AppColors.SECONDARY_CONTAINER);
        p.add(l);
        return p;
    }

    private JPanel buildFinishedScoreBox(String score) {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(AppColors.OUTLINE_VARIANT);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 8, 8));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(56, 56));

        JLabel l = new JLabel(score);
        l.setFont(new Font("Plus Jakarta Sans", Font.BOLD, 28));
        l.setForeground(AppColors.ON_SURFACE_VARIANT);
        p.add(l);
        return p;
    }

    private JButton buildGuardarBtn(boolean enabled) {
        JButton btn = new JButton("Guardar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isEnabled()) {
                    g2.setColor(getModel().isRollover() ? AppColors.PRIMARY_CONTAINER : AppColors.PRIMARY);
                } else {
                    g2.setColor(AppColors.SURFACE_VARIANT);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(AppFonts.labelMd());
        btn.setForeground(enabled ? AppColors.ON_PRIMARY : AppColors.OUTLINE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setEnabled(enabled);
        
        // Update color on disable dynamically
        btn.addPropertyChangeListener("enabled", evt -> {
            btn.setForeground(btn.isEnabled() ? AppColors.ON_PRIMARY : AppColors.OUTLINE);
        });

        if (enabled) {
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        btn.setPreferredSize(new Dimension(100, 40));
        return btn;
    }
}
