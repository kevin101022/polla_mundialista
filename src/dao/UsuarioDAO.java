package dao;

import model.Usuario;
import java.util.List;

/**
 * Interfaz para definir las operaciones de base de datos relacionadas con los Usuarios.
 */
public interface UsuarioDAO {
    
    /**
     * Registra un nuevo usuario en la base de datos.
     * @param usuario El objeto usuario con username y password.
     * @return true si se registró exitosamente, false si el usuario ya existe o hubo un error.
     */
    boolean registrarUsuario(Usuario usuario);

    /**
     * Verifica las credenciales de un usuario.
     * @param username El nombre de usuario.
     * @param password La contraseña.
     * @return El objeto Usuario si las credenciales son correctas, o null si son incorrectas.
     */
    Usuario iniciarSesion(String username, String password);

    /**
     * Obtiene el ranking de todos los usuarios ordenados por puntos de mayor a menor.
     * @return Lista de usuarios.
     */
    List<Usuario> obtenerRanking();
    boolean sumarPuntos(long usuarioId, int puntos);
}
