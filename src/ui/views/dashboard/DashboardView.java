package ui.views.dashboard;

import model.Usuario;
import ui.MainFrame;
import ui.theme.AppColors;
import ui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

public class DashboardView extends JPanel {

    private final MainFrame mainFrame;
    private JPanel rankingTablePanel;
    private JButton btnRefresh;
    
    private JLabel titleLabel;
    private JLabel ptsLabel;

    public DashboardView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppColors.BACKGROUND);
        content.setBorder(new EmptyBorder(32, 32, 32, 32));

        content.add(buildHeroCard());
        content.add(Box.createVerticalStrut(24));

        JPanel midPanel = new JPanel(new GridLayout(1, 2, 24, 0));
        midPanel.setOpaque(false);
        midPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        midPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        midPanel.add(buildRulesCard());
        midPanel.add(buildStatsCard());

        content.add(midPanel);
        content.add(Box.createVerticalStrut(24));
        content.add(buildRankingCard());
        content.add(Box.createVerticalGlue());

        content.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (content.getWidth() < 800) {
                    midPanel.setLayout(new GridLayout(2, 1, 0, 24));
                    midPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
                } else {
                    midPanel.setLayout(new GridLayout(1, 2, 24, 0));
                    midPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
                }
                midPanel.revalidate();
            }
        });

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(AppColors.BACKGROUND);
        scroll.getViewport().setBackground(AppColors.BACKGROUND);

        add(scroll, BorderLayout.CENTER);
    }

    public JButton getBtnRefresh() {
        return btnRefresh;
    }

    private JLabel predictionsCountLabel;

    public void actualizarSaludo(Usuario u, int predictionsCount) {
        if (u != null) {
            titleLabel.setText("Hola, " + u.getUsername());
            ptsLabel.setText("Tus Puntos: " + u.getPuntosTotales());
            predictionsCountLabel.setText("Partidos Pronosticados: " + predictionsCount);
        }
    }

    public void actualizarRanking(List<Usuario> usuarios) {
        if (rankingTablePanel == null) return;
        
        rankingTablePanel.removeAll();
        rankingTablePanel.add(buildTableHeader());
        
        // Renderizamos todos los usuarios. Si queremos límite, Math.min(usuarios.size(), N)
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            rankingTablePanel.add(buildRankRow(i + 1, u.getUsername(), String.valueOf(u.getPuntosTotales()), i == 0));
        }
        
        rankingTablePanel.revalidate();
        rankingTablePanel.repaint();
    }

    // ─── Hero Card ────────────────────────────────────────────────────────────

    private JPanel buildHeroCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout(24, 0));
        card.setBorder(new EmptyBorder(32, 32, 32, 32));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Hola, ...");
        titleLabel.setFont(AppFonts.headlineLg());
        titleLabel.setForeground(AppColors.ON_SURFACE);

        JLabel subtitle = new JLabel("Bienvenido de vuelta a la competencia.");
        subtitle.setFont(AppFonts.bodyLg());
        subtitle.setForeground(AppColors.ON_SURFACE_VARIANT);

        JPanel badgePanel = buildPointsBadge();

        left.add(titleLabel);
        left.add(Box.createVerticalStrut(4));
        left.add(subtitle);
        left.add(Box.createVerticalStrut(16));
        left.add(badgePanel);

        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);
        right.add(buildPronosticarButton());

        card.add(left,  BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel buildPointsBadge() {
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_CONTAINER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(AppColors.OUTLINE_VARIANT);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setOpaque(false);

        JLabel star = new JLabel("★");
        star.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        star.setForeground(AppColors.SECONDARY_CONTAINER);

        ptsLabel = new JLabel("Tus Puntos: 0");
        ptsLabel.setFont(AppFonts.headlineMd());
        ptsLabel.setForeground(AppColors.PRIMARY);

        badge.add(star);
        badge.add(ptsLabel);
        return badge;
    }

    private JButton buildPronosticarButton() {
        JButton btn = new JButton("⚽  Ir a Pronosticar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? AppColors.PRIMARY_CONTAINER : AppColors.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(AppFonts.labelMd());
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 48));
        btn.addActionListener(e -> mainFrame.showView(MainFrame.VIEW_PREDICTIONS));
        return btn;
    }

    // ─── Rules & Stats Cards ──────────────────────────────────────────────────

    private JPanel buildRulesCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("📝 Sistema de Puntos");
        title.setFont(AppFonts.headlineMd());
        title.setForeground(AppColors.ON_SURFACE);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT));
        header.add(title, BorderLayout.WEST);
        
        JPanel body = new JPanel(new GridLayout(3, 1, 0, 8));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(12, 0, 0, 0));
        
        body.add(createRuleLabel("✅ Acierto Exacto (Resultado perfecto):", "3 Pts", AppColors.PRIMARY));
        body.add(createRuleLabel("✅ Acierto Tendencia (Gana o empata):", "1 Pt", AppColors.SECONDARY));
        body.add(createRuleLabel("❌ Fallo en Predicción:", "0 Pts", AppColors.ERROR));

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createRuleLabel(String text, String pts, Color ptsColor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lText = new JLabel(text);
        lText.setFont(AppFonts.bodyMd());
        lText.setForeground(AppColors.ON_SURFACE_VARIANT);
        JLabel lPts = new JLabel(pts);
        lPts.setFont(AppFonts.labelMd());
        lPts.setForeground(ptsColor);
        p.add(lText, BorderLayout.WEST);
        p.add(lPts, BorderLayout.EAST);
        return p;
    }

    private JPanel buildStatsCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("📊 Mis Pronósticos");
        title.setFont(AppFonts.headlineMd());
        title.setForeground(AppColors.ON_SURFACE);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT));
        header.add(title, BorderLayout.WEST);
        
        JPanel body = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 24));
        body.setOpaque(false);
        
        predictionsCountLabel = new JLabel("Partidos Pronosticados: 0");
        predictionsCountLabel.setFont(AppFonts.headlineMd());
        predictionsCountLabel.setForeground(AppColors.PRIMARY);
        
        body.add(predictionsCountLabel);

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // ─── Ranking Card ─────────────────────────────────────────────────────────

    private JPanel buildRankingCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 8, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT));

        JPanel leftH = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftH.setOpaque(false);
        JLabel trophy = new JLabel("🏆");
        trophy.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        JLabel title = new JLabel("Ranking Global");
        title.setFont(AppFonts.headlineMd());
        title.setForeground(AppColors.ON_SURFACE);
        leftH.add(trophy);
        leftH.add(title);

        btnRefresh = new JButton("🔄 Refrescar");
        btnRefresh.setFont(AppFonts.labelMd());
        btnRefresh.setForeground(AppColors.PRIMARY);
        btnRefresh.setContentAreaFilled(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        header.add(leftH, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);

        JPanel body = buildRankingTable();

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JButton link = new JButton("Ver Ranking Completo");
        link.setFont(AppFonts.labelMd());
        link.setForeground(AppColors.PRIMARY);
        link.setContentAreaFilled(false);
        link.setBorderPainted(false);
        link.setFocusPainted(false);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addActionListener(e -> mainFrame.showView(MainFrame.VIEW_LEADERBOARD));
        footer.add(link);

        card.add(header, BorderLayout.NORTH);
        card.add(body,   BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildRankingTable() {
        rankingTablePanel = new JPanel();
        rankingTablePanel.setOpaque(false);
        rankingTablePanel.setLayout(new BoxLayout(rankingTablePanel, BoxLayout.Y_AXIS));
        rankingTablePanel.setBorder(new EmptyBorder(12, 0, 12, 0));
        rankingTablePanel.add(buildTableHeader());
        return rankingTablePanel;
    }

    private JPanel buildTableHeader() {
        JPanel header = new JPanel(new GridLayout(1, 3));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        header.setBorder(new MatteBorder(0, 0, 2, 0, AppColors.OUTLINE_VARIANT));

        JLabel pos = new JLabel("POSICIÓN", SwingConstants.CENTER);
        pos.setFont(new Font("SansSerif", Font.BOLD, 11));
        pos.setForeground(AppColors.ON_SURFACE_VARIANT);
        pos.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel usr = new JLabel("USUARIO");
        usr.setFont(new Font("SansSerif", Font.BOLD, 11));
        usr.setForeground(AppColors.ON_SURFACE_VARIANT);
        usr.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel pts = new JLabel("PUNTOS", SwingConstants.RIGHT);
        pts.setFont(new Font("SansSerif", Font.BOLD, 11));
        pts.setForeground(AppColors.ON_SURFACE_VARIANT);
        pts.setBorder(new EmptyBorder(10, 0, 10, 0));

        header.add(pos);
        header.add(usr);
        header.add(pts);
        return header;
    }

    private JPanel buildRankRow(int pos, String name, String points, boolean isFirst) {
        JPanel row = new JPanel(new GridLayout(1, 3));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        row.setBorder(new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT));

        if (isFirst) {
            row.setBackground(new Color(AppColors.SECONDARY_CONTAINER.getRGB() & 0x00FFFFFF | (25 << 24), true));
        } else {
            row.setOpaque(false);
        }

        JPanel posCell = new JPanel(new GridBagLayout());
        posCell.setOpaque(false);
        if (isFirst) {
            JLabel badge = new JLabel(String.valueOf(pos), SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AppColors.SECONDARY_CONTAINER);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            badge.setFont(AppFonts.labelMd());
            badge.setForeground(AppColors.ON_SECONDARY_CONTAINER);
            badge.setPreferredSize(new Dimension(32, 32));
            posCell.add(badge);
        } else {
            JLabel posLbl = new JLabel(String.valueOf(pos), SwingConstants.CENTER);
            posLbl.setFont(AppFonts.bodyMd());
            posLbl.setForeground(AppColors.OUTLINE);
            posCell.add(posLbl);
        }

        JPanel userCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        userCell.setOpaque(false);
        JPanel avatar = buildMiniAvatar();
        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(isFirst ? AppFonts.labelMd() : AppFonts.bodyMd());
        nameLbl.setForeground(AppColors.ON_SURFACE);
        userCell.add(avatar);
        userCell.add(nameLbl);

        JLabel ptsLbl = new JLabel(points + " pts", SwingConstants.RIGHT);
        ptsLbl.setFont(isFirst ? AppFonts.labelMd() : AppFonts.bodyMd());
        ptsLbl.setForeground(isFirst ? AppColors.PRIMARY : AppColors.ON_SURFACE);
        ptsLbl.setBorder(new EmptyBorder(0, 0, 0, 8));

        row.add(posCell);
        row.add(userCell);
        row.add(ptsLbl);
        return row;
    }

    private JPanel buildMiniAvatar() {
        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_VARIANT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(AppColors.OUTLINE);
                g2.fillOval(6, 2, 12, 12);
                g2.fillArc(2, 14, 20, 14, 0, 180);
                g2.dispose();
            }
        };
        av.setPreferredSize(new Dimension(32, 32));
        av.setMinimumSize(new Dimension(32, 32));
        av.setMaximumSize(new Dimension(32, 32));
        av.setOpaque(false);
        return av;
    }

    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fill(new RoundRectangle2D.Float(2, 4, getWidth()-2, getHeight(), 12, 12));
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(AppColors.OUTLINE_VARIANT);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return card;
    }
}
