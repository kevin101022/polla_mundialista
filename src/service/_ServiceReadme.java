package service;

/**
 * ─── CAPA SERVICE — LÓGICA DE NEGOCIO ───────────────────────────────────────
 *
 * Aquí van las clases de servicio que contienen la lógica de negocio.
 * Actúan como intermediarias entre las vistas (UI) y los DAOs.
 *
 * Convenciones:
 *  - Sufijo "Service" → UsuarioService, ProductoService, etc.
 *  - Instancian los DAOs internamente o los reciben por constructor.
 *  - Aplican validaciones, reglas de negocio y transformaciones
 *    ANTES de delegar al DAO.
 *
 * Ejemplo de estructura:
 * ┌──────────────────────────────────────────────────────────┐
 * │  package service;                                        │
 * │  import dao.IEntidadDAO;                                 │
 * │  import dao.impl.EntidadDAOImpl;                         │
 * │  import model.Entidad;                                   │
 * │  import java.util.List;                                  │
 * │                                                          │
 * │  public class EntidadService {                           │
 * │                                                          │
 * │      private final IEntidadDAO dao = new EntidadDAOImpl();│
 * │                                                          │
 * │      public List<Entidad> obtenerTodos() {               │
 * │          return dao.findAll();                           │
 * │      }                                                   │
 * │                                                          │
 * │      public boolean guardar(Entidad e) {                 │
 * │          // Validaciones de negocio aquí                 │
 * │          if (e.getNombre() == null) return false;        │
 * │          return dao.save(e);                             │
 * │      }                                                   │
 * │  }                                                       │
 * └──────────────────────────────────────────────────────────┘
 *
 * Este archivo es solo documentación.
 */
class _ServiceReadme { /* Solo sirve para que la carpeta tenga un archivo Java */ }
