package ui.views.mypredictions;

import model.dto.PronosticoDetalle;
import ui.MainFrame;
import ui.theme.AppColors;
import ui.theme.AppFonts;
import util.FlagUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class MyPredictionsView extends JPanel {

    @SuppressWarnings("unused")
    private final MainFrame mainFrame;
    private JPanel centerPanel;

    public MyPredictionsView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppColors.BACKGROUND);
        topPanel.setBorder(new EmptyBorder(32, 32, 16, 32));
        topPanel.add(buildHeader(), BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(AppColors.BACKGROUND);
        centerPanel.setBorder(new EmptyBorder(0, 32, 32, 32));

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(AppColors.BACKGROUND);
        scroll.getViewport().setBackground(AppColors.BACKGROUND);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Mis Pronósticos");
        title.setFont(AppFonts.headlineLg());
        title.setForeground(AppColors.ON_SURFACE);
        
        JLabel subtitle = new JLabel("Revisa tu historial de predicciones y los puntos obtenidos.");
        subtitle.setFont(AppFonts.bodyLg());
        subtitle.setForeground(AppColors.ON_SURFACE_VARIANT);
        subtitle.setBorder(new EmptyBorder(4, 0, 0, 0));

        panel.add(title);
        panel.add(subtitle);
        return panel;
    }

    public void renderizarPronosticos(List<PronosticoDetalle> lista) {
        centerPanel.removeAll();
        
        if (lista == null || lista.isEmpty()) {
            JLabel empty = new JLabel("No has guardado ningún pronóstico aún.");
            empty.setFont(AppFonts.bodyLg());
            empty.setForeground(AppColors.ON_SURFACE_VARIANT);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerPanel.add(empty);
        } else {
            for (PronosticoDetalle dto : lista) {
                centerPanel.add(buildPredictionCard(dto));
                centerPanel.add(Box.createVerticalStrut(16));
            }
        }
        
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPanel buildPredictionCard(PronosticoDetalle dto) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                
                g2.setColor(AppColors.OUTLINE_VARIANT);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 24, 16, 24));
        card.setMaximumSize(new Dimension(800, 120));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Izquierda: Fase y equipos
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        infoPanel.setOpaque(false);
        
        JLabel lblFase = new JLabel(dto.getFase() + " • " + (dto.getFechaHoraPartido() != null ? dto.getFechaHoraPartido().substring(0, 10) : ""));
        lblFase.setFont(AppFonts.labelMd());
        lblFase.setForeground(AppColors.ON_SURFACE_VARIANT);
        
        JPanel pnlMatch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlMatch.setOpaque(false);
        
        JLabel lblLocalIcon = new JLabel();
        FlagUtil.aplicarBandera(lblLocalIcon, dto.getEquipoLocalIso(), 24, 18);
        JLabel lblLocalName = new JLabel(dto.getEquipoLocalNombre());
        lblLocalName.setFont(AppFonts.headlineMd());
        lblLocalName.setForeground(AppColors.ON_SURFACE);
        
        JLabel lblVs = new JLabel(" vs ");
        lblVs.setFont(AppFonts.bodyMd());
        lblVs.setForeground(AppColors.OUTLINE_VARIANT);
        
        JLabel lblVisIcon = new JLabel();
        FlagUtil.aplicarBandera(lblVisIcon, dto.getEquipoVisitanteIso(), 24, 18);
        JLabel lblVisName = new JLabel(dto.getEquipoVisitanteNombre());
        lblVisName.setFont(AppFonts.headlineMd());
        lblVisName.setForeground(AppColors.ON_SURFACE);
        
        pnlMatch.add(lblLocalIcon);
        pnlMatch.add(lblLocalName);
        pnlMatch.add(lblVs);
        pnlMatch.add(lblVisIcon);
        pnlMatch.add(lblVisName);
        
        infoPanel.add(lblFase);
        infoPanel.add(pnlMatch);
        
        // Centro: Pronóstico vs Real
        JPanel scorePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        scorePanel.setOpaque(false);
        
        JLabel lblPred = new JLabel("Tu pronóstico: " + dto.getGolesLocalPred() + " - " + dto.getGolesVisitantePred(), SwingConstants.CENTER);
        lblPred.setFont(AppFonts.bodyMd());
        lblPred.setForeground(AppColors.PRIMARY);
        
        String realRes = "FINALIZADO".equals(dto.getEstadoPartido()) ? 
            "Resultado Real: " + dto.getGolesLocalReal() + " - " + dto.getGolesVisitanteReal() : 
            "Partido no finalizado";
        JLabel lblReal = new JLabel(realRes, SwingConstants.CENTER);
        lblReal.setFont(AppFonts.bodyMd());
        lblReal.setForeground(AppColors.ON_SURFACE_VARIANT);
        
        scorePanel.add(lblPred);
        scorePanel.add(lblReal);

        // Derecha: Puntos
        JPanel ptsPanel = new JPanel(new BorderLayout());
        ptsPanel.setOpaque(false);
        
        String ptsTxt = dto.getPuntosObtenidos() != null ? "+" + dto.getPuntosObtenidos() + " Pts" : "- Pts";
        JLabel lblPts = new JLabel(ptsTxt, SwingConstants.RIGHT);
        lblPts.setFont(AppFonts.headlineMd());
        
        if (dto.getPuntosObtenidos() != null) {
            if (dto.getPuntosObtenidos() == 3) lblPts.setForeground(AppColors.PRIMARY);
            else if (dto.getPuntosObtenidos() == 1) lblPts.setForeground(AppColors.SECONDARY);
            else lblPts.setForeground(AppColors.ERROR);
        } else {
            lblPts.setForeground(AppColors.ON_SURFACE_VARIANT);
        }
        
        ptsPanel.add(lblPts, BorderLayout.CENTER);

        // Ensamblar
        card.add(infoPanel, BorderLayout.WEST);
        card.add(scorePanel, BorderLayout.CENTER);
        card.add(ptsPanel, BorderLayout.EAST);

        return card;
    }
}
