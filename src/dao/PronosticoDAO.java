package dao;

import model.Pronostico;

public interface PronosticoDAO {
    /**
     * Guarda el pronóstico en la base de datos.
     * @return 1 si es exitoso, -1 si hay violación de clave única (ya votó), 0 si hay otro error.
     */
    int guardarPronostico(Pronostico pronostico);
    
    java.util.List<Pronostico> obtenerPorPartido(long partidoId);
    java.util.List<Pronostico> obtenerPorUsuario(long usuarioId);
    java.util.List<model.dto.PronosticoDetalle> obtenerDetallesPorUsuario(long usuarioId);
    boolean actualizarPuntosObtenidos(long pronosticoId, int puntos);
}
