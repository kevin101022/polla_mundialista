package dao.impl;

import config.DatabaseConfig;
import dao.UsuarioDAO;
import model.Usuario;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, puntos_totales) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, PasswordUtil.hashPassword(usuario.getPassword()));
            stmt.setInt(3, 0); // Puntos iniciales siempre 0
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // Falla si el username ya existe (Unique Key)
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Usuario iniciarSesion(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, username);
            stmt.setString(2, PasswordUtil.hashPassword(password));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setPuntosTotales(rs.getInt("puntos_totales"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Usuario> obtenerRanking() {
        List<Usuario> ranking = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY puntos_totales DESC, id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPuntosTotales(rs.getInt("puntos_totales"));
                ranking.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ranking;
    }

    @Override
    public boolean sumarPuntos(long usuarioId, int puntos) {
        String sql = "UPDATE usuarios SET puntos_totales = puntos_totales + ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, puntos);
             stmt.setLong(2, usuarioId);
             return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
