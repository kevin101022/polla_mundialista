package dao;

/**
 * ─── CAPA DAO — INTERFACES ───────────────────────────────────────────────────
 *
 * Aquí van las INTERFACES que definen el contrato de acceso a datos.
 * Cada entidad tiene su propia interfaz.
 *
 * Convenciones de nombres:
 *  - Prefijo "I" → IUsuarioDAO, IProductoDAO, etc.
 *  - Métodos CRUD estándar:
 *      List<T>  findAll()
 *      T        findById(int id)
 *      boolean  save(T entity)
 *      boolean  update(T entity)
 *      boolean  delete(int id)
 *
 * Ejemplo de estructura:
 * ┌────────────────────────────────────────────┐
 * │  package dao;                              │
 * │  import model.Entidad;                     │
 * │  import java.util.List;                    │
 * │                                            │
 * │  public interface IEntidadDAO {            │
 * │      List<Entidad> findAll();              │
 * │      Entidad       findById(int id);       │
 * │      boolean       save(Entidad e);        │
 * │      boolean       update(Entidad e);      │
 * │      boolean       delete(int id);         │
 * │  }                                         │
 * └────────────────────────────────────────────┘
 *
 * Las implementaciones van en dao/impl/EntidadDAOImpl.java
 *
 * Este archivo es solo documentación.
 */
class _DaoReadme { /* Solo sirve para que la carpeta tenga un archivo Java */ }
