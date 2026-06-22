package controller;

import dao.EquipoDAO;
import dao.PartidoDAO;
import model.EstadisticaEquipo;
import model.Partido;
import ui.views.groups.GroupsView;

import java.util.List;

public class GroupsController {
    private final GroupsView view;
    private final PartidoDAO partidoDAO;
    private final EquipoDAO equipoDAO;

    private List<String> gruposDisponibles;
    private int activeGroupIndex = 0;

    public GroupsController(GroupsView view, PartidoDAO partidoDAO, EquipoDAO equipoDAO) {
        this.view = view;
        this.partidoDAO = partidoDAO;
        this.equipoDAO = equipoDAO;

        // Inyectamos un callback en la vista para escuchar los clics en las pestañas
        this.view.setOnGroupSelectedListener(this::cambiarGrupo);
    }

    public void cargarDatosIniciales() {
        gruposDisponibles = equipoDAO.obtenerGruposDisponibles();
        
        if (gruposDisponibles.isEmpty()) return;

        view.actualizarTabs(gruposDisponibles, activeGroupIndex);
        cargarDatosGrupoActual();
    }

    private void cambiarGrupo(int index) {
        if (index >= 0 && index < gruposDisponibles.size()) {
            activeGroupIndex = index;
            view.actualizarTabs(gruposDisponibles, activeGroupIndex);
            cargarDatosGrupoActual();
        }
    }

    private void cargarDatosGrupoActual() {
        if (gruposDisponibles.isEmpty()) return;
        
        String grupoStr = gruposDisponibles.get(activeGroupIndex);
        
        List<EstadisticaEquipo> estadisticas = equipoDAO.obtenerEstadisticasGrupo(grupoStr);
        List<Partido> partidos = partidoDAO.obtenerPartidosPorGrupo(grupoStr);
        
        view.renderizarClasificacion(estadisticas);
        view.renderizarPartidosRecientes(partidos);
        view.renderizarEstadisticas(estadisticas);
    }
}
