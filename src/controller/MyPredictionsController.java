package controller;

import dao.PronosticoDAO;
import model.Usuario;
import model.dto.PronosticoDetalle;
import ui.views.mypredictions.MyPredictionsView;
import util.Session;

import java.util.List;

public class MyPredictionsController {

    private final MyPredictionsView view;
    private final PronosticoDAO pronosticoDAO;

    public MyPredictionsController(MyPredictionsView view, PronosticoDAO pronosticoDAO) {
        this.view = view;
        this.pronosticoDAO = pronosticoDAO;
        init();
    }

    private void init() {
        // Al mostrar la vista, actualizamos
        view.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                cargarPronosticos();
            }
        });
    }

    public void cargarPronosticos() {
        Usuario currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            List<PronosticoDetalle> detalles = pronosticoDAO.obtenerDetallesPorUsuario(currentUser.getId());
            view.renderizarPronosticos(detalles);
        }
    }
}
