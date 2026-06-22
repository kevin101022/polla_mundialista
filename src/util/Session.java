package util;

import model.Usuario;

/**
 * Gestiona la sesión global del usuario actual.
 */
public class Session {
    private static Usuario currentUser;

    public static void setCurrentUser(Usuario user) {
        currentUser = user;
    }

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
