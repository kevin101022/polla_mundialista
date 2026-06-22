package util;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Provee Emojis de texto vacío para limpiar los strings y obtiene Banderas (ImageIcon)
 * reales usando FlagCDN para solucionar problemas de renderizado de fuentes en Windows.
 */
public class FlagUtil {

    // Caché para no descargar la misma imagen más de una vez
    private static final Map<String, ImageIcon> FLAG_CACHE = new HashMap<>();

    /**
     * Limpiamos el texto, devolviendo vacío en lugar de un emoji que se dibuja mal
     */
    public static String obtenerEmoji(String codigoIso) {
        return ""; // Se elimina el soporte de Emojis por texto para usar iconos reales
    }

    /**
     * Convierte el código ISO de 3 letras (FIFA) a 2 letras para FlagCDN
     */
    private static String getIso2(String codigoIso) {
        if (codigoIso == null) return "xx";
        switch (codigoIso.toUpperCase()) {
            case "MEX": return "mx"; case "RSA": return "za"; case "KOR": return "kr"; case "CZE": return "cz";
            case "CAN": return "ca"; case "BIH": return "ba"; case "SUI": return "ch"; case "QAT": return "qa";
            case "BRA": return "br"; case "MAR": return "ma"; case "HAI": return "ht"; case "SCO": return "gb-sct";
            case "USA": return "us"; case "PAR": return "py"; case "AUS": return "au"; case "TUR": return "tr";
            case "GER": return "de"; case "CUW": return "cw"; case "CIV": return "ci"; case "ECU": return "ec";
            case "NED": return "nl"; case "JPN": return "jp"; case "SWE": return "se"; case "TUN": return "tn";
            case "BEL": return "be"; case "EGY": return "eg"; case "IRN": return "ir"; case "NZL": return "nz";
            case "ESP": return "es"; case "CPV": return "cv"; case "KSA": return "sa"; case "URU": return "uy";
            case "FRA": return "fr"; case "SEN": return "sn"; case "IRQ": return "iq"; case "NOR": return "no";
            case "ARG": return "ar"; case "ALG": return "dz"; case "AUT": return "at"; case "JOR": return "jo";
            case "POR": return "pt"; case "COD": return "cd"; case "UZB": return "uz"; case "COL": return "co";
            case "ENG": return "gb-eng"; case "CRO": return "hr"; case "GHA": return "gh"; case "PAN": return "pa";
            default: return "xx";
        }
    }

    /**
     * Aplica la bandera asincrónicamente a un JLabel para evitar bloqueos del EDT.
     */
    public static void aplicarBandera(javax.swing.JLabel label, String codigoIso, int width, int height) {
        String iso2 = getIso2(codigoIso);
        String cacheKey = iso2 + "_" + width + "x" + height;

        if (FLAG_CACHE.containsKey(cacheKey)) {
            label.setIcon(FLAG_CACHE.get(cacheKey));
            return;
        }

        // Placeholder
        label.setIcon(new ImageIcon());

        new javax.swing.SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = URI.create("https://flagcdn.com/w40/" + iso2 + ".png").toURL();
                Image image = ImageIO.read(url);
                if (image != null) {
                    Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
                return new ImageIcon();
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    FLAG_CACHE.put(cacheKey, icon);
                    label.setIcon(icon);
                    label.repaint();
                } catch (Exception e) {
                    System.err.println("No se pudo cargar la bandera para: " + codigoIso);
                }
            }
        }.execute();
    }
}
