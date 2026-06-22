package dao.impl;

import config.DatabaseConfig;
import dao.PronosticoDAO;
import model.Pronostico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PronosticoDAOImpl implements PronosticoDAO {

    @Override
    public int guardarPronostico(Pronostico pronostico) {
        String sql = "INSERT INTO pronosticos (usuario_id, partido_id, goles_local_pred, goles_visitante_pred, puntos_obtenidos) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE goles_local_pred = VALUES(goles_local_pred), goles_visitante_pred = VALUES(goles_visitante_pred)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setLong(1, pronostico.getUsuarioId());
            stmt.setLong(2, pronostico.getPartidoId());
            stmt.setInt(3, pronostico.getGolesLocalPred());
            stmt.setInt(4, pronostico.getGolesVisitantePred());
            stmt.setObject(5, null); // Los puntos inician en nulo hasta que el admin cierre el partido
            
            int affected = stmt.executeUpdate();
            return affected > 0 ? 1 : 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Pronostico> obtenerPorPartido(long partidoId) {
        List<Pronostico> lista = new ArrayList<>();
        String sql = "SELECT * FROM pronosticos WHERE partido_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setLong(1, partidoId);
             ResultSet rs = stmt.executeQuery();
             while (rs.next()) {
                 Pronostico p = new Pronostico();
                 p.setId(rs.getLong("id"));
                 p.setUsuarioId(rs.getLong("usuario_id"));
                 p.setPartidoId(rs.getLong("partido_id"));
                 p.setGolesLocalPred(rs.getInt("goles_local_pred"));
                 p.setGolesVisitantePred(rs.getInt("goles_visitante_pred"));
                 p.setPuntosObtenidos(rs.getObject("puntos_obtenidos") != null ? rs.getInt("puntos_obtenidos") : null);
                 // fecha_registro no se requiere estrictamente acá
                 lista.add(p);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Pronostico> obtenerPorUsuario(long usuarioId) {
        List<Pronostico> lista = new ArrayList<>();
        String sql = "SELECT * FROM pronosticos WHERE usuario_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setLong(1, usuarioId);
             ResultSet rs = stmt.executeQuery();
             while (rs.next()) {
                 Pronostico p = new Pronostico();
                 p.setId(rs.getLong("id"));
                 p.setUsuarioId(rs.getLong("usuario_id"));
                 p.setPartidoId(rs.getLong("partido_id"));
                 p.setGolesLocalPred(rs.getInt("goles_local_pred"));
                 p.setGolesVisitantePred(rs.getInt("goles_visitante_pred"));
                 p.setPuntosObtenidos(rs.getObject("puntos_obtenidos") != null ? rs.getInt("puntos_obtenidos") : null);
                 lista.add(p);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizarPuntosObtenidos(long pronosticoId, int puntos) {
        String sql = "UPDATE pronosticos SET puntos_obtenidos = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, puntos);
             stmt.setLong(2, pronosticoId);
             return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public java.util.List<model.dto.PronosticoDetalle> obtenerDetallesPorUsuario(long usuarioId) {
        java.util.List<model.dto.PronosticoDetalle> lista = new java.util.ArrayList<>();
        String sql = "SELECT pr.id AS pronostico_id, pr.partido_id, pr.goles_local_pred, pr.goles_visitante_pred, pr.puntos_obtenidos, pr.fecha_registro, " +
                     "p.fase, p.fecha_hora, p.estado, p.goles_local_real, p.goles_visitante_real, " +
                     "el.nombre AS equipo_local_nombre, el.codigo_iso AS equipo_local_iso, " +
                     "ev.nombre AS equipo_visitante_nombre, ev.codigo_iso AS equipo_visitante_iso " +
                     "FROM pronosticos pr " +
                     "JOIN partidos p ON pr.partido_id = p.id " +
                     "JOIN equipos el ON p.equipo_local_id = el.id " +
                     "JOIN equipos ev ON p.equipo_visitante_id = ev.id " +
                     "WHERE pr.usuario_id = ? " +
                     "ORDER BY p.fecha_hora ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
             stmt.setLong(1, usuarioId);
             ResultSet rs = stmt.executeQuery();
             
             java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

             while (rs.next()) {
                 model.dto.PronosticoDetalle dto = new model.dto.PronosticoDetalle();
                 dto.setPronosticoId(rs.getLong("pronostico_id"));
                 dto.setPartidoId(rs.getLong("partido_id"));
                 dto.setGolesLocalPred(rs.getInt("goles_local_pred"));
                 dto.setGolesVisitantePred(rs.getInt("goles_visitante_pred"));
                 dto.setPuntosObtenidos(rs.getObject("puntos_obtenidos") != null ? rs.getInt("puntos_obtenidos") : null);
                 
                 String fechaRegStr = rs.getString("fecha_registro");
                 if (fechaRegStr != null) {
                     dto.setFechaRegistro(java.time.LocalDateTime.parse(fechaRegStr, formatter));
                 }

                 dto.setFase(rs.getString("fase"));
                 dto.setFechaHoraPartido(rs.getString("fecha_hora"));
                 dto.setEstadoPartido(rs.getString("estado"));
                 
                 dto.setGolesLocalReal(rs.getInt("goles_local_real"));
                 dto.setGolesVisitanteReal(rs.getInt("goles_visitante_real"));
                 
                 dto.setEquipoLocalNombre(rs.getString("equipo_local_nombre"));
                 dto.setEquipoLocalIso(rs.getString("equipo_local_iso"));
                 dto.setEquipoVisitanteNombre(rs.getString("equipo_visitante_nombre"));
                 dto.setEquipoVisitanteIso(rs.getString("equipo_visitante_iso"));
                 
                 lista.add(dto);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
