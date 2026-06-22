package dao.impl;

/**
 * ─── CAPA DAO — IMPLEMENTACIONES ────────────────────────────────────────────
 *
 * Aquí van las clases que IMPLEMENTAN las interfaces de dao/.
 * Contienen el código JDBC real para interactuar con la base de datos.
 *
 * Convenciones de nombres:
 *  - Sufijo "Impl" → UsuarioDAOImpl, ProductoDAOImpl, etc.
 *  - Implementan la interfaz correspondiente (implements IEntidadDAO)
 *  - Usan config.DatabaseConfig.getConnection() para obtener la conexión
 *
 * Ejemplo de estructura:
 * ┌────────────────────────────────────────────────────────────┐
 * │  package dao.impl;                                         │
 * │  import config.DatabaseConfig;                             │
 * │  import dao.IEntidadDAO;                                   │
 * │  import model.Entidad;                                     │
 * │  import java.sql.*;                                        │
 * │  import java.util.*;                                       │
 * │                                                            │
 * │  public class EntidadDAOImpl implements IEntidadDAO {      │
 * │                                                            │
 * │      @Override                                             │
 * │      public List<Entidad> findAll() {                      │
 * │          List<Entidad> lista = new ArrayList<>();          │
 * │          String sql = "SELECT * FROM entidad";             │
 * │          try (Connection c = DatabaseConfig.getConnection();│
 * │               PreparedStatement ps = c.prepareStatement(sql);│
 * │               ResultSet rs = ps.executeQuery()) {          │
 * │              while (rs.next()) {                           │
 * │                  lista.add(mapRow(rs));                    │
 * │              }                                             │
 * │          } catch (SQLException e) { e.printStackTrace(); } │
 * │          return lista;                                     │
 * │      }                                                     │
 * │      // ... otros métodos                                  │
 * │                                                            │
 * │      private Entidad mapRow(ResultSet rs) throws SQLException {│
 * │          return new Entidad(rs.getInt("id"), rs.getString("nombre"));│
 * │      }                                                     │
 * │  }                                                         │
 * └────────────────────────────────────────────────────────────┘
 *
 * Este archivo es solo documentación.
 */
class _DaoImplReadme { /* Solo sirve para que la carpeta tenga un archivo Java */ }
