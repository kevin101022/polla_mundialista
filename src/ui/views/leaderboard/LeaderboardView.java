package ui.views.leaderboard;

import ui.MainFrame;
import ui.theme.AppColors;
import ui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import model.Usuario;
import util.Session;
import java.util.List;
import java.util.ArrayList;

/**
 * LeaderboardView — Ranking Global Completo
 *
 * Fiel al diseño polla_mundialista_2026_ranking_completo:
 *  - Título + tabs Global/Friends/Weekly
 *  - Podio top-3: #1 centro (fondo dorado, más alto), #2 izq, #3 der
 *  - Tabla: POS / PLAYER / RECENT FORM (iconos ✓✗–) / POINTS
 *  - La fila #5 (usuario actual) resaltada en verde suave
 *  - Botón "Load More"
 */
public class LeaderboardView extends JPanel {

    public LeaderboardView(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);
        actualizarRanking(new ArrayList<>());
    }

    public void actualizarRanking(List<Usuario> ranking) {
        removeAll();
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppColors.BACKGROUND);
        content.setBorder(new EmptyBorder(32, 32, 32, 32));

        // Header
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel titleArea = new JPanel();
        titleArea.setOpaque(false);
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Ranking Global");
        title.setFont(AppFonts.headlineLg());
        title.setForeground(AppColors.ON_SURFACE);
        JLabel sub = new JLabel("Mira tu posición entre todos los jugadores del torneo.");
        sub.setFont(AppFonts.bodyMd());
        sub.setForeground(AppColors.ON_SURFACE_VARIANT);
        titleArea.add(title);
        titleArea.add(sub);

        headerRow.add(titleArea, BorderLayout.WEST);

        content.add(headerRow);
        content.add(Box.createVerticalStrut(24));
        content.add(buildPodium(ranking));
        content.add(Box.createVerticalStrut(16));
        content.add(buildTable(ranking));
        content.add(Box.createVerticalStrut(16));
        content.add(buildLoadMore());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(AppColors.BACKGROUND);
        scroll.getViewport().setBackground(AppColors.BACKGROUND);

        add(scroll, BorderLayout.CENTER);
        revalidate();
        repaint();
    }



    // ─── Podio Top-3 ──────────────────────────────────────────────────────────

    private JPanel buildPodium(List<Usuario> ranking) {
        JPanel podium = new JPanel(new GridBagLayout());
        podium.setOpaque(false);
        podium.setAlignmentX(Component.LEFT_ALIGNMENT);
        podium.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy   = 0;
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.insets  = new Insets(0, 8, 0, 8);

        // #2 — Izquierda (gris, más bajo)
        c.gridx = 0; c.weightx = 1;
        if (ranking.size() > 1) {
            podium.add(buildPodiumCard(2, ranking.get(1).getUsername(), ranking.get(1).getPuntosTotales() + " pt", false, 220), c);
        } else {
            podium.add(Box.createRigidArea(new Dimension(100, 220)), c);
        }

        // #1 — Centro (dorado, más alto)
        c.gridx = 1;
        if (ranking.size() > 0) {
            podium.add(buildPodiumCard(1, ranking.get(0).getUsername(), ranking.get(0).getPuntosTotales() + " pt", true, 260), c);
        } else {
            podium.add(Box.createRigidArea(new Dimension(100, 260)), c);
        }

        // #3 — Derecha (gris, más bajo)
        c.gridx = 2;
        if (ranking.size() > 2) {
            podium.add(buildPodiumCard(3, ranking.get(2).getUsername(), ranking.get(2).getPuntosTotales() + " pt", false, 210), c);
        } else {
            podium.add(Box.createRigidArea(new Dimension(100, 210)), c);
        }

        return podium;
    }

    private JPanel buildPodiumCard(int pos, String name, String pts, boolean isFirst, int height) {
        boolean isGold = isFirst;

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isGold ? AppColors.SECONDARY_CONTAINER : AppColors.SURFACE_CONTAINER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, height));
        card.setMinimumSize(new Dimension(0, height));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;

        // Avatar con badge de posición
        JPanel avatarWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        avatarWrap.setOpaque(false);
        JPanel avatar = buildLargeAvatar(pos);
        avatarWrap.add(avatar);

        c.gridy = 0; c.insets = new Insets(16, 16, 8, 16);
        card.add(avatarWrap, c);

        // Nombre
        JLabel nameLbl = new JLabel(name, SwingConstants.CENTER);
        nameLbl.setFont(AppFonts.labelMd());
        nameLbl.setForeground(isGold ? AppColors.SECONDARY : AppColors.ON_SURFACE);
        c.gridy = 1; c.insets = new Insets(0, 8, 8, 8);
        card.add(nameLbl, c);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(isGold ? AppColors.SECONDARY_FIXED_DIM : AppColors.OUTLINE_VARIANT);
        c.gridy = 2; c.insets = new Insets(0, 16, 8, 16);
        card.add(sep, c);

        // Puntos
        JLabel ptsLbl = new JLabel(pts, SwingConstants.CENTER);
        ptsLbl.setFont(AppFonts.headlineMd());
        ptsLbl.setForeground(isGold ? AppColors.SECONDARY : AppColors.ON_SURFACE);
        c.gridy = 3; c.insets = new Insets(0, 8, 16, 8);
        card.add(ptsLbl, c);

        return card;
    }

    private JPanel buildLargeAvatar(int pos) {
        JPanel wrap = new JPanel(null);
        wrap.setOpaque(false);
        wrap.setPreferredSize(new Dimension(64, 72));

        JPanel circle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_CONTAINER_HIGHEST);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(AppColors.OUTLINE);
                g2.fillOval(14, 6, 28, 28);
                g2.fillArc(6, 36, 44, 28, 0, 180);
                g2.dispose();
            }
        };
        circle.setBounds(0, 0, 60, 60);
        circle.setOpaque(false);

        JPanel badge = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = pos == 1 ? AppColors.SECONDARY_CONTAINER
                         : pos == 2 ? AppColors.SURFACE_CONTAINER_HIGH
                         : new Color(0xCD7F32); // bronce
                g2.setColor(bg);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setOpaque(false);
        badge.setBounds(40, 46, 22, 22);

        JLabel badgeLbl = new JLabel(pos == 1 ? "🏆" : "#" + pos);
        badgeLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, pos == 1 ? 11 : 9));
        badgeLbl.setForeground(AppColors.ON_SURFACE);
        badge.add(badgeLbl);

        wrap.add(circle);
        wrap.add(badge);
        return wrap;
    }

    // ─── Tabla ────────────────────────────────────────────────────────────────

    private JPanel buildTable(List<Usuario> ranking) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fill(new RoundRectangle2D.Float(2, 3, getWidth()-2, getHeight(), 12, 12));
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
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Header
        card.add(buildTableHeader());

        // Filas (a partir de la posición 4)
        for (int i = 3; i < ranking.size(); i++) {
            Usuario u = ranking.get(i);
            boolean isYou = Session.getCurrentUser() != null && u.getId() == Session.getCurrentUser().getId();
            String name = u.getUsername() + (isYou ? " (Tú)" : "");
            card.add(buildPlayerRow(String.valueOf(i + 1), name, "–––", String.valueOf(u.getPuntosTotales()), isYou));
        }

        return card;
    }

    private JPanel buildTableHeader() {
        JPanel row = new JPanel(new GridLayout(1, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT),
                new EmptyBorder(12, 24, 12, 24)
        ));

        String[] cols = {"POS", "JUGADOR", "FORMA RECIENTE", "PUNTOS"};
        int[] aligns  = {SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.RIGHT};
        for (int i = 0; i < cols.length; i++) {
            JLabel lbl = new JLabel(cols[i], aligns[i]);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
            lbl.setForeground(AppColors.ON_SURFACE_VARIANT);
            row.add(lbl);
        }
        return row;
    }

    private JPanel buildPlayerRow(String pos, String name, String form, String pts, boolean isYou) {
        JPanel row = new JPanel(new GridLayout(1, 4));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        row.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT),
                new EmptyBorder(0, 24, 0, 24)
        ));

        if (isYou) {
            row.setBackground(new Color(AppColors.PRIMARY_FIXED.getRGB() & 0x00FFFFFF | (40 << 24), true));
        } else {
            row.setOpaque(false);
        }

        // POS
        JLabel posLbl = new JLabel(pos, SwingConstants.CENTER);
        posLbl.setFont(AppFonts.bodyMd());
        posLbl.setForeground(AppColors.OUTLINE);
        row.add(posLbl);

        // PLAYER
        JPanel playerCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        playerCell.setOpaque(false);
        playerCell.add(buildSmallAvatar());
        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(isYou ? AppFonts.labelMd() : AppFonts.bodyMd());
        nameLbl.setForeground(AppColors.ON_SURFACE);
        playerCell.add(nameLbl);
        row.add(playerCell);

        // FORM
        row.add(buildFormIndicators(form));

        // POINTS
        JLabel ptsLbl = new JLabel(pts, SwingConstants.RIGHT);
        ptsLbl.setFont(AppFonts.labelMd());
        ptsLbl.setForeground(AppColors.ON_SURFACE);
        row.add(ptsLbl);

        return row;
    }

    private JPanel buildFormIndicators(String form) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        p.setOpaque(false);

        for (char c : form.toCharArray()) {
            JLabel dot = new JLabel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color col = c == '✓' ? new Color(0x1E7D34)
                              : c == '✗' ? AppColors.ERROR
                              : AppColors.OUTLINE;
                    g2.setColor(col);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                    FontMetrics fm = g2.getFontMetrics();
                    String sym = c == '✓' ? "✓" : c == '✗' ? "✗" : "–";
                    g2.drawString(sym,
                            (getWidth()  - fm.stringWidth(sym)) / 2,
                            (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2.dispose();
                }
            };
            dot.setPreferredSize(new Dimension(20, 20));
            p.add(dot);
        }
        return p;
    }

    private JPanel buildSmallAvatar() {
        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_VARIANT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(AppColors.OUTLINE);
                g2.fillOval(7, 3, 10, 10);
                g2.fillArc(3, 14, 18, 12, 0, 180);
                g2.dispose();
            }
        };
        av.setPreferredSize(new Dimension(32, 32));
        av.setMinimumSize(new Dimension(32, 32));
        av.setMaximumSize(new Dimension(32, 32));
        av.setOpaque(false);
        return av;
    }

    // ─── Load More ────────────────────────────────────────────────────────────

    private JPanel buildLoadMore() {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JButton btn = new JButton("Cargar más  ∨");
        btn.setFont(AppFonts.labelMd());
        btn.setForeground(AppColors.ON_SURFACE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(btn);
        return card;
    }
}
