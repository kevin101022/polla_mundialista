package ui.views;

/**
 * ─── CAPA VIEWS ──────────────────────────────────────────────────────────────
 *
 * Cada vista va en su propia subcarpeta dentro de ui/views/.
 * La vista es un JPanel que se registra en MainFrame con CardLayout.
 *
 * Convenciones:
 *  - Carpeta:  ui/views/<nombre>/
 *  - Archivo:  NombreView.java
 *  - Extiende: JPanel
 *  - Recibe:   MainFrame como parámetro para poder navegar entre vistas
 *
 * Ejemplo de estructura:
 * ┌────────────────────────────────────────────────────────────┐
 * │  package ui.views.ejemplo;                                 │
 * │  import ui.MainFrame;                                      │
 * │  import ui.theme.*;                                        │
 * │  import ui.components.*;                                   │
 * │  import javax.swing.*;                                     │
 * │  import java.awt.*;                                        │
 * │                                                            │
 * │  public class EjemploView extends JPanel {                 │
 * │                                                            │
 * │      private final MainFrame mainFrame;                    │
 * │                                                            │
 * │      public EjemploView(MainFrame mainFrame) {             │
 * │          this.mainFrame = mainFrame;                       │
 * │          initUI();                                         │
 * │      }                                                     │
 * │                                                            │
 * │      private void initUI() {                               │
 * │          setBackground(AppColors.APP_BG);                  │
 * │          // ... construir la interfaz                      │
 * │      }                                                     │
 * │  }                                                         │
 * └────────────────────────────────────────────────────────────┘
 *
 * Pasos para registrar una nueva vista en MainFrame:
 *   1. Crear la carpeta: ui/views/<nombre>/
 *   2. Crear NombreView.java en esa carpeta
 *   3. En MainFrame.java:
 *      a. Declarar: public static final String VIEW_NOMBRE = "NOMBRE";
 *      b. Registrar: cards.add(new NombreView(this), VIEW_NOMBRE);
 *      c. Navegar:   mainFrame.showView(MainFrame.VIEW_NOMBRE);
 *
 * Este archivo es solo documentación.
 */
class _ViewsReadme { /* Solo sirve para que la carpeta tenga un archivo Java */ }
