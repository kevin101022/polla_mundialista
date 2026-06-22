package ui;

import ui.shell.SidebarPanel;
import ui.theme.AppColors;
import ui.theme.AppFonts;
import ui.views.login.LoginView;
import ui.views.register.RegisterView;
import ui.views.dashboard.DashboardView;
import ui.views.predictions.PredictionsView;
import ui.views.mypredictions.MyPredictionsView;
import ui.views.leaderboard.LeaderboardView;
import ui.views.groups.GroupsView;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal — Polla Mundialista 2026
 *
 * Estructura:
 *   BorderLayout.WEST  → SidebarPanel (visible solo cuando hay sesión activa)
 *   BorderLayout.CENTER → CardLayout con todas las vistas
 *
 * Vistas de autenticación (sin sidebar): LOGIN, REGISTER
 * Vistas con sidebar:  DASHBOARD, PREDICTIONS, LEADERBOARD, GROUPS
 */
public class MainFrame extends JFrame {

    // ─── Claves de vistas ─────────────────────────────────────────────────────
    public static final String VIEW_LOGIN       = "LOGIN";
    public static final String VIEW_REGISTER    = "REGISTER";
    public static final String VIEW_DASHBOARD   = "DASHBOARD";
    public static final String VIEW_PREDICTIONS = "PREDICTIONS";
    public static final String VIEW_MY_PREDICTIONS = "MY_PREDICTIONS";
    public static final String VIEW_LEADERBOARD = "LEADERBOARD";
    public static final String VIEW_GROUPS      = "GROUPS";

    // ─── Layout ───────────────────────────────────────────────────────────────
    private final CardLayout  cardLayout = new CardLayout();
    private final JPanel      cards      = new JPanel(cardLayout);
    
    // Referencias para el Controlador
    private LoginView loginView;
    private RegisterView registerView;
    private PredictionsView predictionsView;
    private MyPredictionsView myPredictionsView;
    private DashboardView dashboardView;
    private LeaderboardView leaderboardView;
    private GroupsView groupsView;
    
    private controller.DashboardController dashboardController;
    private controller.LeaderboardController leaderboardController;
    private controller.GroupsController groupsController;
    private controller.PartidoController partidoController;
    private controller.MyPredictionsController myPredictionsController;
    
    private java.util.List<SidebarPanel> sidebars = new java.util.ArrayList<>();

    public MainFrame() {
        AppFonts.load();
        initWindow();
        initViews();
    }

    // ─── Ventana ─────────────────────────────────────────────────────────────

    private void initWindow() {
        setTitle("Polla Mundialista 2026");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 680));
        setPreferredSize(new Dimension(1280, 800));
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);
        setLayout(new BorderLayout());

        cards.setBackground(AppColors.BACKGROUND);
        add(cards, BorderLayout.CENTER);

        pack();
    }

    // ─── Registro de vistas ───────────────────────────────────────────────────

    private void initViews() {
        loginView = new LoginView(this);
        registerView = new RegisterView(this);
        predictionsView = new PredictionsView(this);

        // Vistas de auth (pantalla completa, sin sidebar)
        cards.add(loginView,    VIEW_LOGIN);
        cards.add(registerView, VIEW_REGISTER);

        // Vistas con sidebar — se embeben en un shell de dos paneles
        dashboardView = new DashboardView(this);
        myPredictionsView = new MyPredictionsView(this);
        leaderboardView = new LeaderboardView(this);
        groupsView = new GroupsView(this);
        cards.add(buildShell(dashboardView, VIEW_DASHBOARD),   VIEW_DASHBOARD);
        cards.add(buildShell(predictionsView, VIEW_PREDICTIONS), VIEW_PREDICTIONS);
        cards.add(buildShell(myPredictionsView, VIEW_MY_PREDICTIONS), VIEW_MY_PREDICTIONS);
        cards.add(buildShell(leaderboardView, VIEW_LEADERBOARD), VIEW_LEADERBOARD);
        cards.add(buildShell(groupsView, VIEW_GROUPS),      VIEW_GROUPS);

        cardLayout.show(cards, VIEW_LOGIN);
    }

    /**
     * Envuelve una vista de contenido con el sidebar para crear el layout
     * de dos columnas del dashboard.
     */
    private JPanel buildShell(JComponent content, String viewName) {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(AppColors.BACKGROUND);

        // Sidebar compartido (mismo objeto, re-creado por vista para estado activo)
        SidebarPanel sb = new SidebarPanel(this);
        sb.setActiveView(viewName);
        sidebars.add(sb);
        shell.add(sb, BorderLayout.WEST);

        // Área de scroll para el contenido principal
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(AppColors.BACKGROUND);
        shell.add(scroll, BorderLayout.CENTER);

        return shell;
    }

    // ─── Navegación ───────────────────────────────────────────────────────────

    /**
     * Cambia la vista activa. Para vistas con sidebar, actualiza el item activo.
     */
    public void showView(String viewName) {
        cardLayout.show(cards, viewName);
        
        // Lógica específica para recargar cada vista (si aplica)
        if (VIEW_DASHBOARD.equals(viewName) && dashboardController != null) {
            dashboardController.cargarRanking();
        } else if (VIEW_PREDICTIONS.equals(viewName) && partidoController != null) {
            partidoController.recargarPartidos();
        } else if (VIEW_LEADERBOARD.equals(viewName) && leaderboardController != null) {
            leaderboardController.cargarRankingCompleto();
        } else if (VIEW_GROUPS.equals(viewName) && groupsController != null) {
            groupsController.cargarDatosIniciales();
        } else if (VIEW_MY_PREDICTIONS.equals(viewName) && myPredictionsController != null) {
            myPredictionsController.cargarPronosticos();
        }
        
        // Actualiza info de Sidebars
        for (SidebarPanel sb : sidebars) {
            sb.actualizarInfo();
        }

        revalidate();
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public RegisterView getRegisterView() { return registerView; }
    public PredictionsView getPredictionsView() { return predictionsView; }
    public MyPredictionsView getMyPredictionsView() { return myPredictionsView; }
    public DashboardView getDashboardView() { return dashboardView; }

    public LeaderboardView getLeaderboardView() {
        return leaderboardView;
    }

    public GroupsView getGroupsView() {
        return groupsView;
    }
    
    public void setDashboardController(controller.DashboardController dc) {
        this.dashboardController = dc;
    }

    public void setLeaderboardController(controller.LeaderboardController lc) {
        this.leaderboardController = lc;
    }

    public void setGroupsController(controller.GroupsController gc) {
        this.groupsController = gc;
    }

    public void setPartidoController(controller.PartidoController pc) {
        this.partidoController = pc;
    }
    
    public void setMyPredictionsController(controller.MyPredictionsController mc) {
        this.myPredictionsController = mc;
    }
}
