package dao.impl;

import config.DatabaseConfig;
import dao.PartidoDAO;
import model.Equipo;
import model.Partido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAOImpl implements PartidoDAO {

    @Override
    public List<Partido> obtenerPartidosPorFase(String fase) {
        List<Partido> partidos = new ArrayList<>();
        
        String sql = "SELECT p.id AS p_id, p.fase, p.fecha_hora, p.goles_local_real, p.goles_visitante_real, p.estado, p.cerrado_por, " +
                     "el.id AS el_id, el.nombre AS el_nombre, el.codigo_iso AS el_iso, el.grupo AS el_grupo, " +
                     "ev.id AS ev_id, ev.nombre AS ev_nombre, ev.codigo_iso AS ev_iso, ev.grupo AS ev_grupo " +
                     "FROM partidos p " +
                     "JOIN equipos el ON p.equipo_local_id = el.id " +
                     "JOIN equipos ev ON p.equipo_visitante_id = ev.id " +
                     "WHERE p.fase = ? " +
                     "ORDER BY p.fecha_hora ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, fase);
            ResultSet rs = stmt.executeQuery();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                Partido p = new Partido();
                p.setId(rs.getLong("p_id"));
                p.setFase(rs.getString("fase"));
                
                String fechaStr = rs.getString("fecha_hora");
                if (fechaStr != null) {
                    if (fechaStr.endsWith(".0")) {
                        fechaStr = fechaStr.substring(0, fechaStr.length() - 2);
                    }
                    p.setFechaHora(LocalDateTime.parse(fechaStr, formatter));
                }
                
                // Goles pueden ser null
                p.setGolesLocalReal(rs.getObject("goles_local_real") != null ? rs.getInt("goles_local_real") : null);
                p.setGolesVisitanteReal(rs.getObject("goles_visitante_real") != null ? rs.getInt("goles_visitante_real") : null);
                
                p.setEstado(rs.getString("estado"));
                p.setCerradoPor(rs.getString("cerrado_por"));

                // Equipo Local
                Equipo local = new Equipo();
                local.setId(rs.getInt("el_id"));
                local.setNombre(rs.getString("el_nombre"));
                local.setCodigoIso(rs.getString("el_iso"));
                local.setGrupo(rs.getString("el_grupo"));
                p.setEquipoLocal(local);

                // Equipo Visitante
                Equipo visitante = new Equipo();
                visitante.setId(rs.getInt("ev_id"));
                visitante.setNombre(rs.getString("ev_nombre"));
                visitante.setCodigoIso(rs.getString("ev_iso"));
                visitante.setGrupo(rs.getString("ev_grupo"));
                p.setEquipoVisitante(visitante);

                partidos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return partidos;
    }

    @Override
    public List<String> obtenerFasesDisponibles() {
        List<String> fases = new ArrayList<>();
        // El DISTINCT respeta el orden natural de la base de datos si usamos un order by fecha_hora mínimo
        String sql = "SELECT fase FROM partidos GROUP BY fase ORDER BY MIN(fecha_hora) ASC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                fases.add(rs.getString("fase"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fases;
    }

    @Override
    public List<Partido> obtenerPartidosPorEstado(String... estados) {
        List<Partido> partidos = new ArrayList<>();
        if (estados == null || estados.length == 0) return partidos;

        StringBuilder inClause = new StringBuilder();
        for(int i=0; i<estados.length; i++) {
            inClause.append("?");
            if (i < estados.length - 1) inClause.append(",");
        }

        String sql = "SELECT p.id AS p_id, p.fase, p.fecha_hora, p.goles_local_real, p.goles_visitante_real, p.estado, p.cerrado_por, " +
                     "el.id AS el_id, el.nombre AS el_nombre, el.codigo_iso AS el_iso, el.grupo AS el_grupo, " +
                     "ev.id AS ev_id, ev.nombre AS ev_nombre, ev.codigo_iso AS ev_iso, ev.grupo AS ev_grupo " +
                     "FROM partidos p " +
                     "JOIN equipos el ON p.equipo_local_id = el.id " +
                     "JOIN equipos ev ON p.equipo_visitante_id = ev.id " +
                     "WHERE p.estado IN (" + inClause.toString() + ") " +
                     "ORDER BY p.fecha_hora ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            for(int i=0; i<estados.length; i++) {
                stmt.setString(i + 1, estados[i]);
            }
            ResultSet rs = stmt.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                Partido p = new Partido();
                p.setId(rs.getLong("p_id"));
                p.setFase(rs.getString("fase"));
                
                String fechaStr = rs.getString("fecha_hora");
                if (fechaStr != null) {
                    if (fechaStr.endsWith(".0")) fechaStr = fechaStr.substring(0, fechaStr.length() - 2);
                    p.setFechaHora(LocalDateTime.parse(fechaStr, formatter));
                }
                
                p.setGolesLocalReal(rs.getObject("goles_local_real") != null ? rs.getInt("goles_local_real") : null);
                p.setGolesVisitanteReal(rs.getObject("goles_visitante_real") != null ? rs.getInt("goles_visitante_real") : null);
                p.setEstado(rs.getString("estado"));
                p.setCerradoPor(rs.getString("cerrado_por"));

                Equipo local = new Equipo();
                local.setId(rs.getInt("el_id"));
                local.setNombre(rs.getString("el_nombre"));
                local.setCodigoIso(rs.getString("el_iso"));
                local.setGrupo(rs.getString("el_grupo"));
                p.setEquipoLocal(local);

                Equipo visitante = new Equipo();
                visitante.setId(rs.getInt("ev_id"));
                visitante.setNombre(rs.getString("ev_nombre"));
                visitante.setCodigoIso(rs.getString("ev_iso"));
                visitante.setGrupo(rs.getString("ev_grupo"));
                p.setEquipoVisitante(visitante);

                partidos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidos;
    }

    @Override
    public List<Partido> obtenerPartidosPorGrupo(String grupo) {
        List<Partido> partidos = new ArrayList<>();
        
        String sql = "SELECT p.id AS p_id, p.fase, p.fecha_hora, p.goles_local_real, p.goles_visitante_real, p.estado, p.cerrado_por, " +
                     "el.id AS el_id, el.nombre AS el_nombre, el.codigo_iso AS el_iso, el.grupo AS el_grupo, " +
                     "ev.id AS ev_id, ev.nombre AS ev_nombre, ev.codigo_iso AS ev_iso, ev.grupo AS ev_grupo " +
                     "FROM partidos p " +
                     "JOIN equipos el ON p.equipo_local_id = el.id " +
                     "JOIN equipos ev ON p.equipo_visitante_id = ev.id " +
                     "WHERE el.grupo = ? OR ev.grupo = ? " +
                     "ORDER BY p.fecha_hora ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, grupo);
            stmt.setString(2, grupo);
            ResultSet rs = stmt.executeQuery();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                Partido p = new Partido();
                p.setId(rs.getLong("p_id"));
                p.setFase(rs.getString("fase"));
                
                String fechaStr = rs.getString("fecha_hora");
                if (fechaStr != null) {
                    if (fechaStr.endsWith(".0")) {
                        fechaStr = fechaStr.substring(0, fechaStr.length() - 2);
                    }
                    p.setFechaHora(LocalDateTime.parse(fechaStr, formatter));
                }
                
                p.setGolesLocalReal(rs.getObject("goles_local_real") != null ? rs.getInt("goles_local_real") : null);
                p.setGolesVisitanteReal(rs.getObject("goles_visitante_real") != null ? rs.getInt("goles_visitante_real") : null);
                
                p.setEstado(rs.getString("estado"));
                p.setCerradoPor(rs.getString("cerrado_por"));

                Equipo local = new Equipo();
                local.setId(rs.getInt("el_id"));
                local.setNombre(rs.getString("el_nombre"));
                local.setCodigoIso(rs.getString("el_iso"));
                local.setGrupo(rs.getString("el_grupo"));
                p.setEquipoLocal(local);

                Equipo visitante = new Equipo();
                visitante.setId(rs.getInt("ev_id"));
                visitante.setNombre(rs.getString("ev_nombre"));
                visitante.setCodigoIso(rs.getString("ev_iso"));
                visitante.setGrupo(rs.getString("ev_grupo"));
                p.setEquipoVisitante(visitante);

                partidos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return partidos;
    }

    @Override
    public boolean actualizarResultado(long partidoId, int golesLocal, int golesVisitante, String cerradoPor) {
        String sql = "UPDATE partidos SET goles_local_real = ?, goles_visitante_real = ?, estado = 'FINALIZADO', cerrado_por = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
             stmt.setInt(1, golesLocal);
             stmt.setInt(2, golesVisitante);
             stmt.setString(3, cerradoPor);
             stmt.setLong(4, partidoId);
             
             return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
