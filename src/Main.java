import controller.DashboardController;
import controller.GroupsController;
import controller.LeaderboardController;
import controller.MyPredictionsController;
import controller.PartidoController;
import controller.UsuarioController;
import dao.EquipoDAO;
import dao.PartidoDAO;
import dao.PronosticoDAO;
import dao.UsuarioDAO;
import dao.impl.EquipoDAOImpl;
import dao.impl.PartidoDAOImpl;
import dao.impl.PronosticoDAOImpl;
import dao.impl.UsuarioDAOImpl;
import ui.MainFrame;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada principal de la aplicación.
 * Conecta la Vista (MainFrame) con el DAO a través del Controlador.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Instanciar la ventana principal (que crea internamente las vistas)
            MainFrame mainFrame = new MainFrame();
            
            // 2. Instanciar los DAOs
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
            PartidoDAO partidoDAO = new PartidoDAOImpl();
            PronosticoDAO pronosticoDAO = new PronosticoDAOImpl();
            EquipoDAO equipoDAO = new EquipoDAOImpl();
            
            // 3. Crear los Controladores inyectando los DAOs y vistas específicas
            @SuppressWarnings("unused")
            UsuarioController usrCtrl = new UsuarioController(
                mainFrame.getLoginView(), 
                mainFrame.getRegisterView(), 
                usuarioDAO, 
                mainFrame
            );
            
            PartidoController partCtrl = new PartidoController(
                mainFrame.getPredictionsView(),
                partidoDAO,
                pronosticoDAO
            );
            mainFrame.setPartidoController(partCtrl);
            
            DashboardController dashCtrl = new DashboardController(
                mainFrame.getDashboardView(),
                usuarioDAO,
                pronosticoDAO
            );
            mainFrame.setDashboardController(dashCtrl);
            
            LeaderboardController leadCtrl = new LeaderboardController(
                mainFrame.getLeaderboardView(),
                usuarioDAO
            );
            mainFrame.setLeaderboardController(leadCtrl);
            
            MyPredictionsController myPredCtrl = new MyPredictionsController(
                mainFrame.getMyPredictionsView(),
                pronosticoDAO
            );
            mainFrame.setMyPredictionsController(myPredCtrl);
            
            GroupsController groupsCtrl = new GroupsController(
                mainFrame.getGroupsView(),
                partidoDAO,
                equipoDAO
            );
            mainFrame.setGroupsController(groupsCtrl);
            
            // 4. Mostrar la ventana al usuario (inicia en el Login por defecto)
            mainFrame.setVisible(true);
        });
    }
}
