package controller;

import dao.UsuarioDAO;
import model.Usuario;
import ui.MainFrame;
import ui.views.login.LoginView;
import ui.views.register.RegisterView;
import util.Session;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioController {

    private final LoginView loginView;
    private final RegisterView registerView;
    private final UsuarioDAO usuarioDAO;
    private final MainFrame mainFrame; // Opcional, para cambiar de vista tras login exitoso

    public UsuarioController(LoginView loginView, RegisterView registerView, UsuarioDAO usuarioDAO, MainFrame mainFrame) {
        this.loginView = loginView;
        this.registerView = registerView;
        this.usuarioDAO = usuarioDAO;
        this.mainFrame = mainFrame;

        asignarEventos();
    }

    private void asignarEventos() {
        // Asignar listener al botón Ingresar del LoginView
        loginView.getBtnIngresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarLogin();
            }
        });

        // Asignar listener al botón Registrar del RegisterView
        registerView.getBtnRegistrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarRegistro();
            }
        });
    }

    private void manejarLogin() {
        String username = loginView.getUserField().getText().trim();
        String password = new String(loginView.getPassField().getPassword()).trim();

        // Limpiar errores previos
        loginView.getUserError().setText(" ");
        loginView.getPassError().setText(" ");

        if (!validarCamposLogin(username, password)) {
            return;
        }

        Usuario usuarioAutenticado = usuarioDAO.iniciarSesion(username, password);

        if (usuarioAutenticado != null) {
            // Guardar en la sesión global
            Session.setCurrentUser(usuarioAutenticado);

            JOptionPane.showMessageDialog(loginView, 
                "¡Bienvenido de nuevo, " + usuarioAutenticado.getUsername() + "!", 
                "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
                
            // Limpiar cajas de texto tras entrar
            loginView.getUserField().setText("Ingrese su nombre de usuario");
            loginView.getPassField().setText("");
            
            // Navegar al dashboard
            mainFrame.showView(MainFrame.VIEW_DASHBOARD);
        } else {
            JOptionPane.showMessageDialog(loginView, 
                "Credenciales incorrectas. Verifica tu usuario y contraseña.", 
                "Error de Autenticación", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manejarRegistro() {
        String username = registerView.getUserField().getText().trim();
        String password = new String(registerView.getPassField().getPassword()).trim();

        // Limpiar errores previos
        registerView.getUserError().setText(" ");
        registerView.getPassError().setText(" ");

        if (!validarCamposRegistro(username, password)) {
            return;
        }

        Usuario nuevoUsuario = new Usuario(username, password);
        boolean registrado = usuarioDAO.registrarUsuario(nuevoUsuario);

        if (registrado) {
            JOptionPane.showMessageDialog(registerView, 
                "Usuario registrado correctamente. ¡Ahora puedes iniciar sesión!", 
                "Registro Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
                
            // Limpiar campos
            registerView.getUserField().setText("Ingrese su nombre de usuario");
            registerView.getPassField().setText("");
            
            // Redirigir al login
            mainFrame.showView(MainFrame.VIEW_LOGIN);
        } else {
            JOptionPane.showMessageDialog(registerView, 
                "El nombre de usuario '" + username + "' ya existe o hubo un error en la base de datos.", 
                "Error al Registrar", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Validaciones limpias ---

    private boolean validarCamposLogin(String username, String password) {
        boolean ok = true;
        if (username.isEmpty() || username.equals("Ingrese su nombre de usuario")) {
            loginView.getUserError().setText("El nombre de usuario es requerido.");
            ok = false;
        }
        if (password.isEmpty() || password.equals("Ingrese su contraseña")) {
            loginView.getPassError().setText("La contraseña es requerida.");
            ok = false;
        }
        return ok;
    }

    private boolean validarCamposRegistro(String username, String password) {
        boolean ok = true;
        if (username.isEmpty() || username.equals("Ingrese su nombre de usuario")) {
            registerView.getUserError().setText("El nombre de usuario es requerido.");
            ok = false;
        }
        if (password.isEmpty() || password.equals("Ingrese su contraseña")) {
            registerView.getPassError().setText("La contraseña es requerida.");
            ok = false;
        }
        return ok;
    }
}
