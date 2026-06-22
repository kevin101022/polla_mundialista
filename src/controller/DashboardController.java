package controller;

import dao.PronosticoDAO;
import dao.UsuarioDAO;
import model.Pronostico;
import model.Usuario;
import ui.views.dashboard.DashboardView;
import util.Session;

import java.util.List;

public class DashboardController {

    private final DashboardView view;
    private final UsuarioDAO usuarioDAO;
    private final PronosticoDAO pronosticoDAO;

    public DashboardController(DashboardView view, UsuarioDAO usuarioDAO, PronosticoDAO pronosticoDAO) {
        this.view = view;
        this.usuarioDAO = usuarioDAO;
        this.pronosticoDAO = pronosticoDAO;
        
        init();
    }

    private void init() {
        // Cargar datos apenas inicia
        cargarRanking();

        // Escuchar el botón de Refrescar
        view.getBtnRefresh().addActionListener(e -> cargarRanking());
    }

    public void cargarRanking() {
        // 1. Traer ranking desde base de datos
        List<Usuario> ranking = usuarioDAO.obtenerRanking();
        
        // 2. Actualizar Tabla
        view.actualizarRanking(ranking);
        
        // 3. Actualizar la tarjeta hero de saludo con puntos actualizados
        if (Session.getCurrentUser() != null) {
            // Buscamos el usuario logueado en la base para tener sus puntos frescos
            for(Usuario u : ranking) {
                if(u.getId() == Session.getCurrentUser().getId()) {
                    Session.setCurrentUser(u); // Actualizamos en memoria
                    break;
                }
            }
            
            // Buscar cuantos pronosticos ha hecho
            List<Pronostico> pronosticos = pronosticoDAO.obtenerPorUsuario(Session.getCurrentUser().getId());
            
            view.actualizarSaludo(Session.getCurrentUser(), pronosticos.size());
        }
    }
}
