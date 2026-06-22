package ui.shell;

import ui.MainFrame;
import ui.theme.AppColors;
import ui.theme.AppFonts;
import util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * SidebarPanel — Panel lateral fijo 240px
 *
 * Estructura fiel al diseño:
 *  - Logo "Polla Mundialista 2026" (verde oscuro, bold)
 *  - Card de usuario (avatar circular + nombre + puntos)
 *  - Botón "Place Bets" (verde primario, ancho completo)
 *  - Navegación: Home, Predictions, Leaderboard, Groups, Store
 *  - Separador + Settings / Help
 *
 * El item activo se resalta con fondo SECONDARY_CONTAINER (amarillo).
 */
public class SidebarPanel extends JPanel {

    private static final int SIDEBAR_W = 240;

    private final MainFrame mainFrame;
    private String activeView = MainFrame.VIEW_DASHBOARD;

    // Nav items
    private JPanel navHome;
    private JPanel navPredictions;
    private JPanel navMyPredictions;
    private JPanel navLeaderboard;
    private JPanel navGroups;
    private JPanel navAdmin;
    private JPanel navPlaceBets;

    private JLabel nameLbl;
    private JLabel ptsLbl;

    public SidebarPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(SIDEBAR_W, 0));
        setBackground(AppColors.SURFACE_CONTAINER_LOW);
        setBorder(new MatteBorder(0, 0, 0, 1, AppColors.OUTLINE_VARIANT));
        setLayout(new BorderLayout());
        build();
    }

    public void setActiveView(String viewName) {
        this.activeView = viewName;
        actualizarInfo();
        repaintNavItems();
    }

    public void actualizarInfo() {
        if (Session.getCurrentUser() != null) {
            if (nameLbl != null) nameLbl.setText(Session.getCurrentUser().getUsername());
            if (ptsLbl != null) ptsLbl.setText(Session.getCurrentUser().getPuntosTotales() + " Puntos");
            
            boolean isAdmin = "admin".equalsIgnoreCase(Session.getCurrentUser().getUsername());
            if (navAdmin != null) navAdmin.setVisible(isAdmin);
            
            // Ocultar opciones de apuestas para el admin
            if (navHome != null) navHome.setVisible(!isAdmin);
            if (navPredictions != null) navPredictions.setVisible(!isAdmin);
            if (navMyPredictions != null) navMyPredictions.setVisible(!isAdmin);
            if (navGroups != null) navGroups.setVisible(!isAdmin);
            if (navPlaceBets != null) navPlaceBets.setVisible(!isAdmin);
        }
    }

    // ─── Construcción ─────────────────────────────────────────────────────────

    private void build() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(AppColors.SURFACE_CONTAINER_LOW);
        top.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Logo
        top.add(buildLogo());
        top.add(Box.createVerticalStrut(24));

        // Card usuario
        top.add(buildUserCard());
        top.add(Box.createVerticalStrut(16));

        // Botón Place Bets
        navPlaceBets = buildPlaceBetsButton();
        top.add(navPlaceBets);
        top.add(Box.createVerticalStrut(8));

        // Nav items
        navHome        = buildNavItem("🏠", "Inicio",        MainFrame.VIEW_DASHBOARD);
        navPredictions = buildNavItem("⚽", "Pronósticos",  MainFrame.VIEW_PREDICTIONS);
        navMyPredictions = buildNavItem("📝", "Mis Pronósticos", MainFrame.VIEW_MY_PREDICTIONS);
        navLeaderboard = buildNavItem("🏆", "Ranking",  MainFrame.VIEW_LEADERBOARD);
        navGroups      = buildNavItem("👥", "Grupos",       MainFrame.VIEW_GROUPS);

        navAdmin = buildAdminButton();
        navAdmin.setVisible(false); // Oculto por defecto

        top.add(navHome);
        top.add(Box.createVerticalStrut(2));
        top.add(navPredictions);
        top.add(Box.createVerticalStrut(2));
        top.add(navMyPredictions);
        top.add(Box.createVerticalStrut(2));
        top.add(navLeaderboard);
        top.add(Box.createVerticalStrut(2));
        top.add(navGroups);
        top.add(Box.createVerticalStrut(2));
        top.add(navAdmin);

        add(top, BorderLayout.NORTH);
        add(Box.createVerticalGlue(), BorderLayout.CENTER);

        // Panel inferior para Logout
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(AppColors.SURFACE_CONTAINER_LOW);
        bottom.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel navLogout = buildLogoutButton();
        
        bottom.add(navLogout);

        add(bottom, BorderLayout.SOUTH);

        repaintNavItems();
    }

    // ─── Logo ─────────────────────────────────────────────────────────────────

    private JPanel buildLogo() {
        JLabel label = new JLabel("<html>Polla Mundialista<br>2026</html>");
        label.setFont(AppFonts.headlineMd());
        label.setForeground(AppColors.PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(label);
        return p;
    }

    // ─── Card de usuario ──────────────────────────────────────────────────────

    private JPanel buildUserCard() {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_CONTAINER_HIGHEST);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 12, 12, 12));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar circular
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.SURFACE_VARIANT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                // Ícono persona simbólico
                g2.setColor(AppColors.OUTLINE);
                g2.fillOval(8, 4, 16, 16);
                g2.fillArc(2, 20, 28, 20, 0, 180);
                g2.dispose();
            }
        };
        Dimension avatarSz = new Dimension(48, 48);
        avatar.setPreferredSize(avatarSz);
        avatar.setMinimumSize(avatarSz);
        avatar.setMaximumSize(avatarSz);
        avatar.setOpaque(false);

        // Texto
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        nameLbl = new JLabel("Bienvenido");
        nameLbl.setFont(AppFonts.labelMd());
        nameLbl.setForeground(AppColors.ON_SURFACE);

        ptsLbl = new JLabel("0 Puntos");
        ptsLbl.setFont(AppFonts.labelMd());
        ptsLbl.setForeground(AppColors.PRIMARY);

        text.add(nameLbl);
        text.add(Box.createVerticalStrut(2));
        text.add(ptsLbl);

        card.add(avatar, BorderLayout.WEST);
        card.add(text,   BorderLayout.CENTER);
        return card;
    }

    // ─── Botón Place Bets ─────────────────────────────────────────────────────

    private JPanel buildPlaceBetsButton() {
        JButton btn = new JButton("Pronosticar") {
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
        btn.setForeground(AppColors.ON_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.addActionListener(e -> mainFrame.showView(MainFrame.VIEW_PREDICTIONS));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        wrap.add(btn);
        return wrap;
    }

    // ─── Nav item ─────────────────────────────────────────────────────────────

    private JPanel buildNavItem(String iconText, String label, String targetView) {
        final boolean[] hovered = {false};

        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                if (targetView != null && targetView.equals(activeView)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AppColors.SECONDARY_CONTAINER);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                    g2.dispose();
                } else if (hovered[0]) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AppColors.SURFACE_CONTAINER_HIGH);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(208, 40));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setBorder(new EmptyBorder(0, 8, 0, 8));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boolean isActive = targetView != null && targetView.equals(activeView);

        JLabel icon = new JLabel(iconText);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        icon.setForeground(isActive ? AppColors.ON_SECONDARY_CONTAINER : AppColors.ON_SURFACE_VARIANT);
        icon.setPreferredSize(new Dimension(24, 24));
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lbl = new JLabel(label);
        lbl.setFont(isActive ? AppFonts.labelMd() : AppFonts.bodyMd());
        lbl.setForeground(isActive ? AppColors.ON_SECONDARY_CONTAINER : AppColors.ON_SURFACE_VARIANT);

        item.add(icon);
        item.add(lbl);

        if (targetView != null) {
            item.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered[0] = true;  item.repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered[0] = false; item.repaint(); }
                @Override public void mouseClicked(MouseEvent e) { mainFrame.showView(targetView); }
            });
        }

        return item;
    }

    // ─── Logout Botón ─────────────────────────────────────────────────────────

    private JPanel buildLogoutButton() {
        final boolean[] hovered = {false};

        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                if (hovered[0]) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AppColors.ERROR.darker());
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(208, 40));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setBorder(new EmptyBorder(0, 8, 0, 8));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel icon = new JLabel("🚪");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        icon.setForeground(AppColors.ON_SURFACE_VARIANT);

        JLabel lbl = new JLabel("Cerrar Sesión");
        lbl.setFont(AppFonts.bodyMd());
        lbl.setForeground(AppColors.ON_SURFACE_VARIANT);

        item.add(icon);
        item.add(lbl);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { 
                hovered[0] = true;  
                icon.setForeground(AppColors.ON_ERROR);
                lbl.setForeground(AppColors.ON_ERROR);
                item.repaint(); 
            }
            @Override public void mouseExited(MouseEvent e)  { 
                hovered[0] = false; 
                icon.setForeground(AppColors.ON_SURFACE_VARIANT);
                lbl.setForeground(AppColors.ON_SURFACE_VARIANT);
                item.repaint(); 
            }
            @Override public void mouseClicked(MouseEvent e) { 
                Session.logout();
                mainFrame.showView(MainFrame.VIEW_LOGIN); 
            }
        });

        return item;
    }

    private void repaintNavItems() {
        // Se maneja visualmente en paintComponent basado en activeView
        if (navHome != null)        { navHome.repaint(); navHome.revalidate(); }
        if (navPredictions != null) { navPredictions.repaint(); }
        if (navMyPredictions != null) { navMyPredictions.repaint(); }
        if (navLeaderboard != null) { navLeaderboard.repaint(); }
        if (navGroups != null)      { navGroups.repaint(); }
    }

    private JPanel buildAdminButton() {
        final boolean[] hovered = {false};

        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                if (hovered[0]) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 230, 230));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(208, 40));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setBorder(new EmptyBorder(0, 8, 0, 8));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel icon = new JLabel("⚙️");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        icon.setForeground(AppColors.ERROR);
        icon.setPreferredSize(new Dimension(24, 24));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lbl = new JLabel("Panel de Control");
        lbl.setFont(AppFonts.bodyMd());
        lbl.setForeground(AppColors.ERROR);

        item.add(icon);
        item.add(lbl);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered[0] = true;  item.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered[0] = false; item.repaint(); }
            @Override public void mouseClicked(MouseEvent e) {
                // Instanciar panel de admin en ventana separada
                ui.views.admin.AdminPanelView adminView = new ui.views.admin.AdminPanelView();
                new controller.AdminController(
                    adminView, 
                    new dao.impl.PartidoDAOImpl(), 
                    new dao.impl.PronosticoDAOImpl(), 
                    new dao.impl.UsuarioDAOImpl()
                );
                adminView.setVisible(true);
            }
        });

        return item;
    }
}
