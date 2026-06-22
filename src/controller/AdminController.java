package controller;

import dao.PartidoDAO;
import dao.PronosticoDAO;
import dao.UsuarioDAO;
import model.Partido;
import model.Pronostico;
import ui.views.admin.AdminPanelView;
import ui.views.admin.AdminPanelView.PartidoItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdminController {

    private final AdminPanelView view;
    private final PartidoDAO partidoDAO;
    private final PronosticoDAO pronosticoDAO;
    private final UsuarioDAO usuarioDAO;

    public AdminController(AdminPanelView view, PartidoDAO partidoDAO, PronosticoDAO pronosticoDAO, UsuarioDAO usuarioDAO) {
        this.view = view;
        this.partidoDAO = partidoDAO;
        this.pronosticoDAO = pronosticoDAO;
        this.usuarioDAO = usuarioDAO;

        init();
    }

    private void init() {
        // Cargar Fases
        List<String> fases = partidoDAO.obtenerFasesDisponibles();
        for (String fase : fases) {
            view.getCmbFases().addItem(fase);
        }

        // Listener para cambio de Fase
        view.getCmbFases().addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String faseSel = (String) view.getCmbFases().getSelectedItem();
                if (faseSel != null) cargarPartidosPorFase(faseSel);
            }
        });

        // Carga inicial
        String inicial = (String) view.getCmbFases().getSelectedItem();
        if (inicial != null) cargarPartidosPorFase(inicial);
        
        // Inyectamos la lógica al botón de finalizar
        view.setOnFinalizarListener(this::finalizarPartido);
    }

    private void cargarPartidosPorFase(String fase) {
        view.getMatchSelect().removeAllItems();
        
        List<Partido> partidosFase = partidoDAO.obtenerPartidosPorFase(fase);
        view.getMatchSelect().addItem(new PartidoItem(null, "Seleccione un encuentro activo..."));
        
        for (Partido p : partidosFase) {
            if ("PENDIENTE".equals(p.getEstado()) || "EN_JUEGO".equals(p.getEstado())) {
                String label = "ID: " + p.getId() + " | " + p.getEquipoLocal().getNombre() + " vs " + p.getEquipoVisitante().getNombre();
                view.getMatchSelect().addItem(new PartidoItem(p, label));
            }
        }
    }

    private void finalizarPartido(ActionEvent e) {
        PartidoItem selected = (PartidoItem) view.getMatchSelect().getSelectedItem();
        
        if (selected == null || selected.getPartido() == null) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un partido de la lista antes de continuar.", "Validación de Partido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String strHome = view.getTxtHome().getText().trim();
        String strAway = view.getTxtAway().getText().trim();

        if (strHome.isEmpty() || strAway.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe ingresar los goles oficiales locales y visitantes.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int golesL, golesV;
        try {
            golesL = Integer.parseInt(strHome);
            golesV = Integer.parseInt(strAway);
            if (golesL < 0 || golesV < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Por favor, ingrese un número entero válido (mayor o igual a cero) para los goles.", "Error de Formato en Goles", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Está seguro de que desea cerrar este partido?\nEsta acción es irreversible y calculará los puntos de todos los usuarios inmediatamente.",
                "Confirmar Cierre de Partido",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // --- TRANSACCIÓN PRINCIPAL ---
        Partido partido = selected.getPartido();
        view.getBtnFinalize().setText("⏳ Procesando Resultados...");
        view.getBtnFinalize().setEnabled(false);

        // 1. Actualizar Partido
        // Podríamos usar la sesión para "cerradoPor" pero para evitar dependencia, usamos ADMIN_MANUAL
        boolean exitoPartido = partidoDAO.actualizarResultado(partido.getId(), golesL, golesV, "ADMIN_MANUAL");
        
        if (!exitoPartido) {
            JOptionPane.showMessageDialog(view, "Hubo un error al actualizar el partido.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
            resetBtn();
            return;
        }

        // 2. El Cálculo Mágico
        List<Pronostico> pronosticos = pronosticoDAO.obtenerPorPartido(partido.getId());

        for (Pronostico pronostico : pronosticos) {
            int predL = pronostico.getGolesLocalPred();
            int predV = pronostico.getGolesVisitantePred();
            int puntos = 0;

            if (predL == golesL && predV == golesV) {
                // Acierto exacto: 3 Puntos
                puntos = 3;
            } else if (Integer.compare(predL, predV) == Integer.compare(golesL, golesV)) {
                // Acierto tendencia: 1 Punto
                puntos = 1;
            } else {
                // Falla: 0 Puntos
                puntos = 0;
            }

            // 3. Actualizar la base de datos para ese usuario
            pronosticoDAO.actualizarPuntosObtenidos(pronostico.getId(), puntos);
            usuarioDAO.sumarPuntos(pronostico.getUsuarioId(), puntos);
        }

        // 4. Interfaz de Usuario Exitosa
        JOptionPane.showMessageDialog(view, "✅ Partido finalizado exitosamente. Puntos calculados y repartidos (" + pronosticos.size() + " predicciones evaluadas).", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        view.getTxtHome().setText("");
        view.getTxtAway().setText("");
        String faseActual = (String) view.getCmbFases().getSelectedItem();
        if (faseActual != null) cargarPartidosPorFase(faseActual);
        resetBtn();
    }
    
    private void resetBtn() {
        view.getBtnFinalize().setText("⚠️ Finalizar Partido y Calcular Puntos ⚠️");
        view.getBtnFinalize().setEnabled(true);
    }
}
