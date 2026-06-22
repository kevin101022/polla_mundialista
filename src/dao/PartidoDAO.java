package dao;

import model.Partido;
import java.util.List;

public interface PartidoDAO {
    List<Partido> obtenerPartidosPorFase(String fase);
    List<String> obtenerFasesDisponibles();
    List<Partido> obtenerPartidosPorEstado(String... estados);
    List<Partido> obtenerPartidosPorGrupo(String grupo);
    boolean actualizarResultado(long partidoId, int golesLocal, int golesVisitante, String cerradoPor);
}
