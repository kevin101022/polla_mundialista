package dao.impl;

import config.DatabaseConfig;
import dao.EquipoDAO;
import model.Equipo;
import model.EstadisticaEquipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAOImpl implements EquipoDAO {

    @Override
    public List<String> obtenerGruposDisponibles() {
        List<String> grupos = new ArrayList<>();
        String sql = "SELECT grupo FROM equipos WHERE grupo IS NOT NULL GROUP BY grupo ORDER BY grupo ASC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                grupos.add(rs.getString("grupo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grupos;
    }

    @Override
    public List<EstadisticaEquipo> obtenerEstadisticasGrupo(String grupo) {
        List<EstadisticaEquipo> stats = new ArrayList<>();
        
        // Esta super consulta calcula la tabla de posiciones dinámicamente sumando victorias, empates y derrotas.
        String sql = "SELECT " +
                     "    e.id, e.nombre, e.codigo_iso, e.grupo, " +
                     "    COUNT(p.id) AS pj, " +
                     "    SUM(CASE " +
                     "        WHEN p.equipo_local_id = e.id AND p.goles_local_real > p.goles_visitante_real THEN 1 " +
                     "        WHEN p.equipo_visitante_id = e.id AND p.goles_visitante_real > p.goles_local_real THEN 1 " +
                     "        ELSE 0 " +
                     "    END) AS pg, " +
                     "    SUM(CASE " +
                     "        WHEN p.goles_local_real = p.goles_visitante_real AND p.goles_local_real IS NOT NULL THEN 1 " +
                     "        ELSE 0 " +
                     "    END) AS pe, " +
                     "    SUM(CASE " +
                     "        WHEN p.equipo_local_id = e.id AND p.goles_local_real < p.goles_visitante_real THEN 1 " +
                     "        WHEN p.equipo_visitante_id = e.id AND p.goles_visitante_real < p.goles_local_real THEN 1 " +
                     "        ELSE 0 " +
                     "    END) AS pp, " +
                     "    SUM(CASE WHEN p.equipo_local_id = e.id THEN p.goles_local_real WHEN p.equipo_visitante_id = e.id THEN p.goles_visitante_real ELSE 0 END) AS gf, " +
                     "    SUM(CASE WHEN p.equipo_local_id = e.id THEN p.goles_visitante_real WHEN p.equipo_visitante_id = e.id THEN p.goles_local_real ELSE 0 END) AS gc " +
                     "FROM equipos e " +
                     "LEFT JOIN partidos p ON (e.id = p.equipo_local_id OR e.id = p.equipo_visitante_id) AND p.estado = 'FINALIZADO' " +
                     "WHERE e.grupo = ? " +
                     "GROUP BY e.id, e.nombre, e.codigo_iso, e.grupo";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, grupo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Equipo e = new Equipo(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("codigo_iso"),
                    rs.getString("grupo")
                );
                
                EstadisticaEquipo est = new EstadisticaEquipo();
                est.setEquipo(e);
                est.setPartidosJugados(rs.getInt("pj"));
                
                int pg = rs.getInt("pg");
                int pe = rs.getInt("pe");
                int pp = rs.getInt("pp");
                int gf = rs.getInt("gf");
                int gc = rs.getInt("gc");
                
                est.setPartidosGanados(pg);
                est.setPartidosEmpatados(pe);
                est.setPartidosPerdidos(pp);
                est.setGolesFavor(gf);
                est.setGolesContra(gc);
                est.setDiferenciaGoles(gf - gc);
                est.setPuntos((pg * 3) + pe);
                
                stats.add(est);
            }
            
            // Ordenar por Puntos, luego Diferencia de Goles, luego Goles a Favor
            stats.sort((s1, s2) -> {
                if (s1.getPuntos() != s2.getPuntos()) {
                    return Integer.compare(s2.getPuntos(), s1.getPuntos());
                }
                if (s1.getDiferenciaGoles() != s2.getDiferenciaGoles()) {
                    return Integer.compare(s2.getDiferenciaGoles(), s1.getDiferenciaGoles());
                }
                return Integer.compare(s2.getGolesFavor(), s1.getGolesFavor());
            });
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }
}
