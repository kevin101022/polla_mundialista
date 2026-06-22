package controller;

import dao.PartidoDAO;
import dao.PronosticoDAO;
import model.Partido;
import model.Pronostico;
import ui.views.predictions.PredictionsView;
import util.Session;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PartidoController {

    private final PredictionsView view;
    private final PartidoDAO partidoDAO;
    private final PronosticoDAO pronosticoDAO;

    public PartidoController(PredictionsView view, PartidoDAO partidoDAO, PronosticoDAO pronosticoDAO) {
        this.view = view;
        this.partidoDAO = partidoDAO;
        this.pronosticoDAO = pronosticoDAO;

        init();
    }

    private void init() {
        // 1. Obtener las fases desde la base de datos y pasarlas al ComboBox
        List<String> fases = partidoDAO.obtenerFasesDisponibles();
        for (String fase : fases) {
            view.getCmbFases().addItem(fase);
        }

        // 2. Escuchar cambios en el JComboBox
        view.getCmbFases().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String faseSeleccionada = (String) e.getItem();
                cargarPartidosEnVista(faseSeleccionada);
            }
        });

        // 3. Escuchar el guardado de pronósticos desde la Vista
        view.setOnGuardarPronosticoListener((partido, txtLocal, txtVis, btnGuardar) -> {
            guardarPronostico(partido, txtLocal, txtVis, btnGuardar);
        });

        // 4. Cargar la primera fase disponible por defecto
        if (!fases.isEmpty()) {
            cargarPartidosEnVista(fases.get(0));
        }
    }

    private void cargarPartidosEnVista(String fase) {
        List<Partido> partidos = partidoDAO.obtenerPartidosPorFase(fase);
        Map<Partido, Boolean> estadosEdicion = new LinkedHashMap<>();
        Map<Partido, Pronostico> pronosticosUsuario = new LinkedHashMap<>();
        LocalDateTime ahora = LocalDateTime.now();

        List<Pronostico> pronosticos = new java.util.ArrayList<>();
        if (Session.getCurrentUser() != null) {
            pronosticos = pronosticoDAO.obtenerPorUsuario(Session.getCurrentUser().getId());
        }

        // Regla Anti-Trampas Visual: Hasta 10 minutos antes del partido
        for (Partido p : partidos) {
            boolean isPendiente = "PENDIENTE".equalsIgnoreCase(p.getEstado());
            boolean isAntesDelPitazo = p.getFechaHora() != null && p.getFechaHora().minusMinutes(10).isAfter(ahora);
            
            boolean editable = isPendiente && isAntesDelPitazo;
            estadosEdicion.put(p, editable);

            // Buscar si ya tiene pronostico
            for (Pronostico pron : pronosticos) {
                if (pron.getPartidoId() == p.getId()) {
                    pronosticosUsuario.put(p, pron);
                    break;
                }
            }
        }

        view.renderizarPartidos(estadosEdicion, pronosticosUsuario);
    }

    public void recargarPartidos() {
        if (view.getCmbFases().getItemCount() == 0) {
            List<String> fases = partidoDAO.obtenerFasesDisponibles();
            for (String fase : fases) {
                view.getCmbFases().addItem(fase);
            }
        }
        
        String faseSeleccionada = (String) view.getCmbFases().getSelectedItem();
        if (faseSeleccionada != null) {
            cargarPartidosEnVista(faseSeleccionada);
        }
    }

    private void guardarPronostico(Partido partido, JTextField txtLocal, JTextField txtVis, JButton btnGuardar) {
        // 1. Validaciones
        String strLocal = txtLocal.getText().trim();
        String strVis = txtVis.getText().trim();

        if (strLocal.isEmpty() || strVis.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debes ingresar ambos goles para guardar el pronóstico.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int golesLocal;
        int golesVis;
        try {
            golesLocal = Integer.parseInt(strLocal);
            golesVis = Integer.parseInt(strVis);
            if (golesLocal < 0 || golesVis < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Los goles deben ser números enteros positivos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Session.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(view, "Error: No hay una sesión activa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Construir Pronostico
        Pronostico p = new Pronostico();
        p.setUsuarioId(Session.getCurrentUser().getId());
        p.setPartidoId(partido.getId());
        p.setGolesLocalPred(golesLocal);
        p.setGolesVisitantePred(golesVis);

        // 3. Guardar en Base de Datos
        int resultado = pronosticoDAO.guardarPronostico(p);

        if (resultado == 1) {
            // Éxito
            JOptionPane.showMessageDialog(view, "¡Pronóstico guardado exitosamente!", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Error genérico
            JOptionPane.showMessageDialog(view, "Ocurrió un error al intentar guardar tu pronóstico. Intenta más tarde.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
