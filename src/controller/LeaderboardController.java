package controller;

import dao.UsuarioDAO;
import model.Usuario;
import ui.views.leaderboard.LeaderboardView;

import java.util.List;

public class LeaderboardController {
    private final LeaderboardView view;
    private final UsuarioDAO usuarioDAO;

    public LeaderboardController(LeaderboardView view, UsuarioDAO usuarioDAO) {
        this.view = view;
        this.usuarioDAO = usuarioDAO;
    }

    public void cargarRankingCompleto() {
        List<Usuario> ranking = usuarioDAO.obtenerRanking();
        view.actualizarRanking(ranking);
    }
}
