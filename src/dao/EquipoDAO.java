package dao;

import model.EstadisticaEquipo;
import java.util.List;

public interface EquipoDAO {
    List<String> obtenerGruposDisponibles();
    List<EstadisticaEquipo> obtenerEstadisticasGrupo(String grupo);
}
