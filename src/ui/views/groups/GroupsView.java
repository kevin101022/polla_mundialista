package ui.views.groups;

import ui.MainFrame;
import ui.theme.AppColors;
import ui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import model.EstadisticaEquipo;
import model.Partido;
import util.FlagUtil;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Vista de Grupos
 * Muestra:
 *    · Tabs (WrapLayout)
 *    · Tabla Posiciones
 *    · Partidos Recientes (mini cards)
 *    · Group Insights (fondo verde oscuro: Total Goals + Top Scorer)
 */
public class GroupsView extends JPanel {

    private int activeGroup = 0; // 0=A, 1=B...
    private List<String> groupNames = new ArrayList<>();
    private List<EstadisticaEquipo> currentStats = new ArrayList<>();
    private List<Partido> currentMatches = new ArrayList<>();
    
    private Consumer<Integer> onGroupSelectedListener;

    public GroupsView(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);
    }
    
    public void setOnGroupSelectedListener(Consumer<Integer> listener) {
        this.onGroupSelectedListener = listener;
    }
    
    public void actualizarTabs(List<String> grupos, int activeIndex) {
        this.groupNames = grupos;
        this.activeGroup = activeIndex;
        rebuild(); // Muestra solo el cascarón con tabs, luego se llamarán los renderizadores
    }
    
    public void renderizarClasificacion(List<EstadisticaEquipo> stats) {
        this.currentStats = stats;
        rebuild();
    }
    
    public void renderizarPartidosRecientes(List<Partido> partidos) {
        this.currentMatches = partidos;
        rebuild();
    }
    
    public void renderizarEstadisticas(List<EstadisticaEquipo> stats) {
        // En GroupsController, esto también actualizará la información necesaria, pero en rebuild() ya se usa currentStats.
        // Solo repintamos.
        rebuild();
    }

    private void rebuild() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(AppColors.BACKGROUND);
        content.setBorder(new EmptyBorder(32, 32, 32, 32));

        // Título
        JLabel title = new JLabel("Fase de Grupos");
        title.setFont(AppFonts.headlineLg());
        title.setForeground(AppColors.ON_SURFACE);

        JLabel sub = new JLabel("Mira las posiciones, puntos y el historial de partidos para el Mundial 2026.");
        sub.setFont(AppFonts.bodyLg());
        sub.setForeground(AppColors.ON_SURFACE_VARIANT);
        sub.setBorder(new EmptyBorder(4, 0, 20, 0));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(title);
        top.add(sub);
        top.add(buildGroupTabs());
        top.add(Box.createVerticalStrut(16));

        // Split: tabla izquierda + panel derecho
        JPanel split = new JPanel(new GridBagLayout());
        split.setOpaque(false);
        
        JPanel leftPanel = buildStandingsCard();
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(buildRecentMatchesCard());
        rightPanel.add(Box.createVerticalStrut(12));
        rightPanel.add(buildInsightsCard());

        GridBagConstraints cLeft = new GridBagConstraints();
        cLeft.gridx = 0; cLeft.gridy = 0; 
        cLeft.fill = GridBagConstraints.BOTH; 
        cLeft.weightx = 0.6; cLeft.weighty = 1.0; 
        cLeft.insets = new Insets(0, 0, 0, 16);
        split.add(leftPanel, cLeft);

        GridBagConstraints cRight = new GridBagConstraints();
        cRight.gridx = 1; cRight.gridy = 0; 
        cRight.fill = GridBagConstraints.BOTH; 
        cRight.weightx = 0.4; cRight.weighty = 1.0; 
        cRight.insets = new Insets(0, 0, 0, 0);
        split.add(rightPanel, cRight);

        // Listener para volverlo apilado en pantallas estrechas
        split.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                boolean narrow = split.getWidth() < 800;
                GridBagLayout gbl = (GridBagLayout) split.getLayout();
                
                if (narrow) {
                    cLeft.gridx = 0; cLeft.gridy = 0; cLeft.weightx = 1.0; cLeft.insets = new Insets(0, 0, 16, 0);
                    cRight.gridx = 0; cRight.gridy = 1; cRight.weightx = 1.0; cRight.insets = new Insets(0, 0, 0, 0);
                } else {
                    cLeft.gridx = 0; cLeft.gridy = 0; cLeft.weightx = 0.6; cLeft.insets = new Insets(0, 0, 0, 16);
                    cRight.gridx = 1; cRight.gridy = 0; cRight.weightx = 0.4; cRight.insets = new Insets(0, 0, 0, 0);
                }
                
                gbl.setConstraints(leftPanel, cLeft);
                gbl.setConstraints(rightPanel, cRight);
                split.revalidate();
            }
        });

        content.add(top,   BorderLayout.NORTH);
        content.add(split, BorderLayout.CENTER);

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

    // ─── Tabs de grupos ───────────────────────────────────────────────────────

    private JPanel buildGroupTabs() {
        JPanel tabs = new JPanel(new util.WrapLayout(FlowLayout.LEFT, 8, 8));
        tabs.setOpaque(false);

        for (int i = 0; i < groupNames.size(); i++) {
            final int idx = i;
            boolean active = i == activeGroup;
            JLabel tab = new JLabel("Grupo " + groupNames.get(i), SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (active) {
                        g2.setColor(AppColors.PRIMARY);
                        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                    } else {
                        g2.setColor(Color.WHITE);
                        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                        g2.setColor(AppColors.OUTLINE_VARIANT);
                        g2.setStroke(new BasicStroke(1f));
                        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 20, 20));
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            tab.setFont(AppFonts.labelMd());
            tab.setForeground(active ? Color.WHITE : AppColors.ON_SURFACE);
            tab.setPreferredSize(new Dimension(80, 36));
            tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tab.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (onGroupSelectedListener != null) {
                        onGroupSelectedListener.accept(idx);
                    }
                }
            });
            tabs.add(tab);
        }
        return tabs;
    }

    // ─── Tabla de posiciones ──────────────────────────────────────────────────

    private JPanel buildStandingsCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header card
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));

        String groupStr = (groupNames != null && groupNames.size() > activeGroup) ? "Grupo " + groupNames.get(activeGroup) : "";
        JLabel title = new JLabel(groupStr + " - Clasificación");
        title.setFont(AppFonts.headlineMd());
        title.setForeground(AppColors.ON_SURFACE);

        header.add(title, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        // Tabla
        String[] cols = {"#", "Equipo", "PJ", "G", "E", "P", "GF", "GC", "DG", "Pts"};
        int[]    ws   = {30, 160, 40, 40, 40, 40, 40, 40, 50, 40};

        JPanel table = new JPanel();
        table.setOpaque(false);
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));

        // Encabezado tabla
        table.add(buildStandingsHeader(cols, ws));

        // Filas de datos
        if (currentStats != null) {
            for (int i = 0; i < currentStats.size(); i++) {
                EstadisticaEquipo est = currentStats.get(i);
                table.add(buildStandingsRow(est, i + 1, ws));
            }
        }

        // Leyenda
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        legend.setOpaque(false);
        legend.setBorder(new EmptyBorder(12, 0, 0, 0));
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(AppColors.PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(10, 10));
        dot.setOpaque(false);
        JLabel legLbl = new JLabel("Avanza a la Siguiente Ronda");
        legLbl.setFont(AppFonts.caption());
        legLbl.setForeground(AppColors.ON_SURFACE_VARIANT);
        legend.add(dot);
        legend.add(legLbl);

        card.add(table,  BorderLayout.CENTER);
        card.add(legend, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildStandingsHeader(String[] cols, int[] ws) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.setBorder(new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT));

        for (int i = 0; i < cols.length; i++) {
            JLabel lbl = new JLabel(cols[i], i <= 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
            lbl.setForeground(AppColors.ON_SURFACE_VARIANT);
            lbl.setPreferredSize(new Dimension(ws[i], 40));
            row.add(lbl);
        }
        return row;
    }

    private JPanel buildStandingsRow(EstadisticaEquipo est, int pos, int[] ws) {
        String team    = est.getEquipo().getNombre();
        int mp         = est.getPartidosJugados();
        int w          = est.getPartidosGanados();
        int d          = est.getPartidosEmpatados();
        int l          = est.getPartidosPerdidos();
        int gf         = est.getGolesFavor();
        int ga         = est.getGolesContra();
        int gdNum      = est.getDiferenciaGoles();
        String gd      = (gdNum > 0 ? "+" : "") + gdNum;
        int pts        = est.getPuntos();
        boolean adv    = pos <= 2; // Avanzan los dos primeros
        boolean redGD  = gdNum < 0;

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        row.setBorder(new MatteBorder(0, 0, 1, 0, AppColors.OUTLINE_VARIANT));

        if (adv) {
            row.setBackground(new Color(AppColors.PRIMARY_FIXED.getRGB() & 0x00FFFFFF | (25 << 24), true));
        } else {
            row.setOpaque(false);
        }

        // Primera celda: posición. Segunda celda: Panel con la bandera y el nombre
        JPanel pnlTeam = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlTeam.setOpaque(false);
        JLabel lblFlag = new JLabel();
        FlagUtil.aplicarBandera(lblFlag, est.getEquipo().getCodigoIso(), 24, 18);
        pnlTeam.add(lblFlag);
        JLabel lblTeamName = new JLabel(team);
        lblTeamName.setFont(AppFonts.bodyMd());
        lblTeamName.setForeground(AppColors.ON_SURFACE);
        pnlTeam.add(lblTeamName);

        Object[] cells = {pos, pnlTeam, mp, w, d, l, gf, ga, gd, pts};
        for (int i = 0; i < cells.length; i++) {
            if (i == 1) {
                // Agregar el panel del equipo directamente
                pnlTeam.setPreferredSize(new Dimension(ws[i], 48));
                row.add(pnlTeam);
            } else {
                JLabel lbl = new JLabel(String.valueOf(cells[i]),
                        i <= 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
                lbl.setFont(AppFonts.bodyMd());
                boolean isGD = i == 8;
                lbl.setForeground(isGD && redGD ? AppColors.ERROR : AppColors.ON_SURFACE);
                lbl.setPreferredSize(new Dimension(ws[i], 48));
                row.add(lbl);
            }
        }
        return row;
    }

    // ─── Recent Matches ───────────────────────────────────────────────────────

    private JPanel buildRecentMatchesCard() {
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Partidos Recientes");
        title.setFont(AppFonts.headlineMd());
        title.setForeground(AppColors.ON_SURFACE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(AppColors.OUTLINE_VARIANT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        if (currentMatches != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM");
            for (Partido p : currentMatches) {
                card.add(Box.createVerticalStrut(12));
                card.add(sep);
                card.add(Box.createVerticalStrut(12));
                
                String fecha = p.getFechaHora() != null ? p.getFechaHora().format(fmt) : "Por def.";
                String t1 = p.getEquipoLocal().getCodigoIso();
                String t2 = p.getEquipoVisitante().getCodigoIso();
                String score = (p.getGolesLocalReal() != null ? p.getGolesLocalReal() : "-") + " : " +
                               (p.getGolesVisitanteReal() != null ? p.getGolesVisitanteReal() : "-");
                               
                Color bg1 = AppColors.ON_SURFACE;
                Color bg2 = AppColors.ON_SURFACE;
                if ("FINALIZADO".equals(p.getEstado())) {
                    if (p.getGolesLocalReal() != null && p.getGolesVisitanteReal() != null) {
                        if (p.getGolesLocalReal() > p.getGolesVisitanteReal()) { bg1 = AppColors.PRIMARY; bg2 = AppColors.ON_SURFACE_VARIANT; }
                        else if (p.getGolesVisitanteReal() > p.getGolesLocalReal()) { bg2 = AppColors.PRIMARY; bg1 = AppColors.ON_SURFACE_VARIANT; }
                        else { bg1 = AppColors.ON_SURFACE_VARIANT; bg2 = AppColors.ON_SURFACE_VARIANT; }
                    }
                }
                
                card.add(buildMatchResult(fecha, p.getEstado(), t1, score, t2, bg1, bg2));
            }
        }
        card.add(Box.createVerticalStrut(16));

        JButton viewAll = new JButton("Ver todos los partidos del grupo →");
        viewAll.setFont(AppFonts.labelMd());
        viewAll.setForeground(AppColors.ON_SURFACE);
        viewAll.setContentAreaFilled(false);
        viewAll.setBorderPainted(false);
        viewAll.setFocusPainted(false);
        viewAll.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(viewAll);

        return card;
    }

    private JPanel buildMatchResult(String matchday, String status,
                                     String t1, String score, String t2,
                                     Color c1, Color c2) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel md = new JLabel(matchday);
        md.setFont(AppFonts.caption());
        md.setForeground(AppColors.ON_SURFACE_VARIANT);

        JLabel st = new JLabel(status);
        st.setFont(AppFonts.caption());
        st.setForeground(AppColors.ON_SURFACE_VARIANT);

        JPanel meta = new JPanel(new BorderLayout());
        meta.setOpaque(false);
        meta.add(md, BorderLayout.WEST);
        meta.add(st, BorderLayout.EAST);

        // Score center
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        scorePanel.setOpaque(false);

        JLabel team1 = new JLabel(t1);
        team1.setFont(AppFonts.labelMd());
        team1.setForeground(c1);

        JLabel scoreBox = new JLabel(score, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        scoreBox.setFont(AppFonts.labelMd());
        scoreBox.setForeground(Color.WHITE);
        scoreBox.setPreferredSize(new Dimension(52, 28));
        scoreBox.setOpaque(false);

        JLabel team2 = new JLabel(t2);
        team2.setFont(AppFonts.labelMd());
        team2.setForeground(c2);

        scorePanel.add(team1);
        scorePanel.add(scoreBox);
        scorePanel.add(team2);

        JPanel full = new JPanel(new BorderLayout());
        full.setOpaque(false);
        full.add(meta,       BorderLayout.NORTH);
        full.add(scorePanel, BorderLayout.CENTER);
        return full;
    }

    // ─── Group Insights ───────────────────────────────────────────────────────

    private JPanel buildInsightsCard() {
        JPanel card = new JPanel(new GridLayout(1, 2, 12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        // Título arriba
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JLabel insTitle = new JLabel("Estadísticas del Grupo");
        insTitle.setFont(AppFonts.headlineMd());
        insTitle.setForeground(Color.WHITE);

        JPanel stats = new JPanel(new GridLayout(1, 2, 12, 0));
        stats.setOpaque(false);
        int golesTotales = 0;
        String mejorAtaque = "N/A";
        int maxGolesFavor = -1;
        
        if (currentStats != null) {
            for (EstadisticaEquipo est : currentStats) {
                golesTotales += est.getGolesFavor(); // Aquí sumamos solo los de favor, que representan todos los goles si vemos todo el grupo.
                // Corrección: golesTotales se calcula mejor en base a partidos, o simplemente la mitad de (suma GF + suma GC).
                if (est.getGolesFavor() > maxGolesFavor) {
                    maxGolesFavor = est.getGolesFavor();
                    mejorAtaque = est.getEquipo().getCodigoIso() + " (" + maxGolesFavor + ")";
                }
            }
        }
        
        stats.add(buildStat("GOLES TOTALES", String.valueOf(golesTotales)));
        stats.add(buildStat("MEJOR ATAQUE", mejorAtaque));

        wrap.add(insTitle, BorderLayout.NORTH);
        wrap.add(stats,    BorderLayout.CENTER);

        // La card es wrap con fondo verde
        JPanel outer = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.PRIMARY_CONTAINER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));
        outer.setAlignmentX(Component.LEFT_ALIGNMENT);
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        outer.add(wrap, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildStat(String label, String value) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(AppColors.INVERSE_PRIMARY);

        JLabel val = new JLabel(value);
        val.setFont(AppFonts.headlineLg());
        val.setForeground(Color.WHITE);

        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(val);
        return p;
    }

    // ─── Helper card ─────────────────────────────────────────────────────────

    private JPanel createCard() {
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
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }
}
