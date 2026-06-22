package util;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseSeeder {

    public static void main(String[] args) {
        String matchData = 
            "jueves, 11 de junio de 2026 2:00 p. m. 1 A MÉXICO VS SUDÁFRICA\n" +
            "jueves, 11 de junio de 2026 9:00 p. m. 1 A COREA DEL SUR VS REP. CHECA\n" +
            "viernes, 12 de junio de 2026 2:00 p. m. 1 B CANADÁ VS BOSNIA\n" +
            "viernes, 12 de junio de 2026 8:00 p. m. 1 D ESTADOS UNIDOS VS PARAGUAY\n" +
            "sábado, 13 de junio de 2026 2:00 p. m. 1 B QATAR VS SUIZA\n" +
            "sábado, 13 de junio de 2026 5:00 p. m. 1 C BRASIL VS MARRUECOS\n" +
            "sábado, 13 de junio de 2026 8:00 p. m. 1 C HAITÍ VS ESCOCIA\n" +
            "sábado, 13 de junio de 2026 11:00 p. m. 1 D AUSTRALIA VS TURQUIA\n" +
            "domingo, 14 de junio de 2026 12:00 p. m. 1 E ALEMANIA VS CURAZAO\n" +
            "domingo, 14 de junio de 2026 3:00 p. m. 1 F PAÍSES BAJOS VS JAPÓN\n" +
            "domingo, 14 de junio de 2026 6:00 p. m. 1 E COSTA DE MARFIL VS ECUADOR\n" +
            "domingo, 14 de junio de 2026 9:00 p. m. 1 F SUECIA VS TÚNEZ\n" +
            "lunes, 15 de junio de 2026 11:00 a. m. 1 H ESPAÑA VS CABO VERDE\n" +
            "lunes, 15 de junio de 2026 2:00 p. m. 1 G BÉLGICA VS EGIPTO\n" +
            "lunes, 15 de junio de 2026 5:00 p. m. 1 H ARABIA SAUDITA VS URUGUAY\n" +
            "lunes, 15 de junio de 2026 8:00 p. m. 1 G IRÁN VS NUEVA ZELANDA\n" +
            "martes, 16 de junio de 2026 2:00 p. m. 1 I FRANCIA VS SENEGAL\n" +
            "martes, 16 de junio de 2026 5:00 p. m. 1 I IRAK VS NORUEGA\n" +
            "martes, 16 de junio de 2026 8:00 p. m. 1 J ARGENTINA VS ARGELIA\n" +
            "martes, 16 de junio de 2026 11:00 p. m. 1 J AUSTRIA VS JORDANIA\n" +
            "miércoles, 17 de junio de 2026 12:00 p. m. 1 K PORTUGAL VS RD CONGO\n" +
            "miércoles, 17 de junio de 2026 3:00 p. m. 1 L INGLATERRA VS CROACIA\n" +
            "miércoles, 17 de junio de 2026 6:00 p. m. 1 L GHANA VS PANAMÁ\n" +
            "miércoles, 17 de junio de 2026 9:00 p. m. 1 K UZBEKISTÁN VS COLOMBIA\n" +
            "jueves, 18 de junio de 2026 11:00 a. m. 2 A SUDÁFRICA VS REP. CHECA\n" +
            "jueves, 18 de junio de 2026 2:00 p. m. 2 B BOSNIA VS SUIZA\n" +
            "jueves, 18 de junio de 2026 5:00 p. m. 2 B CANADÁ VS QATAR\n" +
            "jueves, 18 de junio de 2026 8:00 p. m. 2 A MÉXICO VS COREA DEL SUR\n" +
            "viernes, 19 de junio de 2026 2:00 p. m. 2 D ESTADOS UNIDOS VS AUSTRALIA\n" +
            "viernes, 19 de junio de 2026 5:00 p. m. 2 C ESCOCIA VS MARRUECOS\n" +
            "viernes, 19 de junio de 2026 7:30 p. m. 2 C BRASIL VS HAITÍ\n" +
            "viernes, 19 de junio de 2026 10:00 p. m. 2 D TURQUIA VS PARAGUAY\n" +
            "sábado, 20 de junio de 2026 12:00 p. m. 2 F PAÍSES BAJOS VS SUECIA\n" +
            "sábado, 20 de junio de 2026 3:00 p. m. 2 E ALEMANIA VS COSTA DE MARFIL\n" +
            "sábado, 20 de junio de 2026 7:00 p. m. 2 E ECUADOR VS CURAZAO\n" +
            "sábado, 20 de junio de 2026 11:00 p. m. 2 F TÚNEZ VS JAPÓN\n" +
            "domingo, 21 de junio de 2026 11:00 a. m. 2 H ESPAÑA VS ARABIA SAUDITA\n" +
            "domingo, 21 de junio de 2026 2:00 p. m. 2 G BÉLGICA VS IRÁN\n" +
            "domingo, 21 de junio de 2026 5:00 p. m. 2 H URUGUAY VS CABO VERDE\n" +
            "domingo, 21 de junio de 2026 8:00 p. m. 2 G NUEVA ZELANDA VS EGIPTO\n" +
            "lunes, 22 de junio de 2026 12:00 p. m. 2 J ARGENTINA VS AUSTRIA\n" +
            "lunes, 22 de junio de 2026 4:00 p. m. 2 I FRANCIA VS IRAK\n" +
            "lunes, 22 de junio de 2026 7:00 p. m. 2 I NORUEGA VS SENEGAL\n" +
            "lunes, 22 de junio de 2026 10:00 p. m. 2 J JORDANIA VS ARGELIA\n" +
            "martes, 23 de junio de 2026 12:00 p. m. 2 K PORTUGAL VS UZBEKISTÁN\n" +
            "martes, 23 de junio de 2026 3:00 p. m. 2 L INGLATERRA VS GHANA\n" +
            "martes, 23 de junio de 2026 6:00 p. m. 2 K PANAMÁ VS CROACIA\n" +
            "martes, 23 de junio de 2026 9:00 p. m. 2 K COLOMBIA VS RD CONGO\n" +
            "miércoles, 24 de junio de 2026 2:00 p. m. 3 B SUIZA VS CANADÁ\n" +
            "miércoles, 24 de junio de 2026 2:00 p. m. 3 B BOSNIA VS QATAR\n" +
            "miércoles, 24 de junio de 2026 5:00 p. m. 3 C ESCOCIA VS BRASIL\n" +
            "miércoles, 24 de junio de 2026 5:00 p. m. 3 C MARRUECOS VS HAITÍ\n" +
            "miércoles, 24 de junio de 2026 8:00 p. m. 3 A REP. CHECA VS MÉXICO\n" +
            "miércoles, 24 de junio de 2026 8:00 p. m. 3 A SUDÁFRICA VS COREA DEL SUR\n" +
            "jueves, 25 de junio de 2026 3:00 p. m. 3 E ECUADOR VS ALEMANIA\n" +
            "jueves, 25 de junio de 2026 3:00 p. m. 3 E CURAZAO VS COSTA DE MARFIL\n" +
            "jueves, 25 de junio de 2026 6:00 p. m. 3 F TÚNEZ VS PAÍSES BAJOS\n" +
            "jueves, 25 de junio de 2026 6:00 p. m. 3 F JAPÓN VS SUECIA\n" +
            "jueves, 25 de junio de 2026 9:00 p. m. 3 D TURQUIA VS ESTADOS UNIDOS\n" +
            "jueves, 25 de junio de 2026 9:00 p. m. 3 D PARAGUAY VS AUSTRALIA\n" +
            "viernes, 26 de junio de 2026 2:00 p. m. 3 I NORUEGA VS FRANCIA\n" +
            "viernes, 26 de junio de 2026 2:00 p. m. 3 I SENEGAL VS IRAK\n" +
            "viernes, 26 de junio de 2026 7:00 p. m. 3 H URUGUAY VS ESPAÑA\n" +
            "viernes, 26 de junio de 2026 7:00 p. m. 3 H CABO VERDE VS ARABIA SAUDITA\n" +
            "viernes, 26 de junio de 2026 10:00 p. m. 3 G NUEVA ZELANDA VS BÉLGICA\n" +
            "viernes, 26 de junio de 2026 10:00 p. m. 3 G EGIPTO VS IRÁN\n" +
            "sábado, 27 de junio de 2026 4:00 p. m. 3 L PANAMÁ VS INGLATERRA\n" +
            "sábado, 27 de junio de 2026 4:00 p. m. 3 L CROACIA VS GHANA\n" +
            "sábado, 27 de junio de 2026 6:30 p. m. 3 K COLOMBIA VS PORTUGAL\n" +
            "sábado, 27 de junio de 2026 6:30 p. m. 3 K RD CONGO VS UZBEKISTÁN\n" +
            "sábado, 27 de junio de 2026 9:00 p. m. 3 J JORDANIA VS ARGENTINA\n" +
            "sábado, 27 de junio de 2026 9:00 p. m. 3 J ARGELIA VS AUSTRIA";

        Map<String, String[]> equiposData = new HashMap<>();
        // Grupo A
        equiposData.put("MÉXICO", new String[]{"MEX", "A"});
        equiposData.put("SUDÁFRICA", new String[]{"RSA", "A"});
        equiposData.put("COREA DEL SUR", new String[]{"KOR", "A"});
        equiposData.put("REP. CHECA", new String[]{"CZE", "A"});
        // Grupo B
        equiposData.put("CANADÁ", new String[]{"CAN", "B"});
        equiposData.put("BOSNIA", new String[]{"BIH", "B"});
        equiposData.put("SUIZA", new String[]{"SUI", "B"});
        equiposData.put("QATAR", new String[]{"QAT", "B"});
        // Grupo C
        equiposData.put("BRASIL", new String[]{"BRA", "C"});
        equiposData.put("MARRUECOS", new String[]{"MAR", "C"});
        equiposData.put("HAITÍ", new String[]{"HAI", "C"});
        equiposData.put("ESCOCIA", new String[]{"SCO", "C"});
        // Grupo D
        equiposData.put("ESTADOS UNIDOS", new String[]{"USA", "D"});
        equiposData.put("PARAGUAY", new String[]{"PAR", "D"});
        equiposData.put("AUSTRALIA", new String[]{"AUS", "D"});
        equiposData.put("TURQUIA", new String[]{"TUR", "D"});
        // Grupo E
        equiposData.put("ALEMANIA", new String[]{"GER", "E"});
        equiposData.put("CURAZAO", new String[]{"CUW", "E"});
        equiposData.put("COSTA DE MARFIL", new String[]{"CIV", "E"});
        equiposData.put("ECUADOR", new String[]{"ECU", "E"});
        // Grupo F
        equiposData.put("PAÍSES BAJOS", new String[]{"NED", "F"});
        equiposData.put("JAPÓN", new String[]{"JPN", "F"});
        equiposData.put("SUECIA", new String[]{"SWE", "F"});
        equiposData.put("TÚNEZ", new String[]{"TUN", "F"});
        // Grupo G
        equiposData.put("BÉLGICA", new String[]{"BEL", "G"});
        equiposData.put("EGIPTO", new String[]{"EGY", "G"});
        equiposData.put("IRÁN", new String[]{"IRN", "G"});
        equiposData.put("NUEVA ZELANDA", new String[]{"NZL", "G"});
        // Grupo H
        equiposData.put("ESPAÑA", new String[]{"ESP", "H"});
        equiposData.put("CABO VERDE", new String[]{"CPV", "H"});
        equiposData.put("ARABIA SAUDITA", new String[]{"KSA", "H"});
        equiposData.put("URUGUAY", new String[]{"URU", "H"});
        // Grupo I
        equiposData.put("FRANCIA", new String[]{"FRA", "I"});
        equiposData.put("SENEGAL", new String[]{"SEN", "I"});
        equiposData.put("IRAK", new String[]{"IRQ", "I"});
        equiposData.put("NORUEGA", new String[]{"NOR", "I"});
        // Grupo J
        equiposData.put("ARGENTINA", new String[]{"ARG", "J"});
        equiposData.put("ARGELIA", new String[]{"ALG", "J"});
        equiposData.put("AUSTRIA", new String[]{"AUT", "J"});
        equiposData.put("JORDANIA", new String[]{"JOR", "J"});
        // Grupo K
        equiposData.put("PORTUGAL", new String[]{"POR", "K"});
        equiposData.put("RD CONGO", new String[]{"COD", "K"});
        equiposData.put("UZBEKISTÁN", new String[]{"UZB", "K"});
        equiposData.put("COLOMBIA", new String[]{"COL", "K"});
        // Grupo L
        equiposData.put("INGLATERRA", new String[]{"ENG", "L"});
        equiposData.put("CROACIA", new String[]{"CRO", "L"});
        equiposData.put("GHANA", new String[]{"GHA", "L"});
        equiposData.put("PANAMÁ", new String[]{"PAN", "L"});

        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Borrando datos antiguos...");
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM pronosticos");
                st.executeUpdate("DELETE FROM partidos");
                st.executeUpdate("DELETE FROM equipos");
            }

            System.out.println("Insertando 48 Equipos...");
            String insertTeam = "INSERT INTO equipos (nombre, codigo_iso, grupo) VALUES (?, ?, ?)";
            Map<String, Integer> teamIds = new HashMap<>();
            
            try (PreparedStatement ps = conn.prepareStatement(insertTeam, Statement.RETURN_GENERATED_KEYS)) {
                for (Map.Entry<String, String[]> entry : equiposData.entrySet()) {
                    ps.setString(1, entry.getKey());
                    ps.setString(2, entry.getValue()[0]);
                    ps.setString(3, entry.getValue()[1]);
                    ps.executeUpdate();
                    
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            teamIds.put(entry.getKey(), rs.getInt(1));
                        }
                    }
                }
            }

            System.out.println("Insertando equipo Por Definir...");
            int tbdId = 0;
            try (PreparedStatement psTbd = conn.prepareStatement(insertTeam, Statement.RETURN_GENERATED_KEYS)) {
                psTbd.setString(1, "Por definir");
                psTbd.setString(2, "TBD");
                psTbd.setString(3, "X");
                psTbd.executeUpdate();
                
                try (ResultSet rs = psTbd.getGeneratedKeys()) {
                    if (rs.next()) {
                        tbdId = rs.getInt(1);
                    }
                }
            }

            System.out.println("Insertando 72 Partidos...");
            String insertMatch = "INSERT INTO partidos (equipo_local_id, equipo_visitante_id, fase, fecha_hora, estado) VALUES (?, ?, ?, ?, 'PENDIENTE')";
            try (PreparedStatement psMatch = conn.prepareStatement(insertMatch)) {
                String[] lines = matchData.split("\n");
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(" VS ");
                    String t2 = parts[1].trim();
                    
                    String left = parts[0].trim();
                    String t1 = null;
                    for (String t : equiposData.keySet()) {
                        if (left.endsWith(t)) {
                            t1 = t;
                            break;
                        }
                    }
                    
                    if (t1 == null) {
                        System.out.println("ERROR: No se encontro team1 en la linea: " + left);
                        continue;
                    }
                    
                    String remain = left.substring(0, left.length() - t1.length()).trim();
                    String[] words = remain.split(" ");
                    String grupo = words[words.length - 1];
                    String jornada = words[words.length - 2];
                    String dateStr = remain.substring(0, remain.length() - grupo.length() - jornada.length() - 2).trim();
                    
                    dateStr = dateStr.replace("de ", "").replace(",", "");
                    String[] dateParts = dateStr.split(" ");
                    String day = dateParts[1].length() == 1 ? "0" + dateParts[1] : dateParts[1];
                    String month = "06";
                    String year = dateParts[3];
                    String time = dateParts[4];
                    boolean isPm = dateParts[5].toLowerCase().contains("p");
                    
                    String[] hm = time.split(":");
                    int h = Integer.parseInt(hm[0]);
                    int m = Integer.parseInt(hm[1]);
                    if (isPm && h != 12) h += 12;
                    if (!isPm && h == 12) h = 0;
                    
                    String hs = h < 10 ? "0" + h : "" + h;
                    String ms = m < 10 ? "0" + m : "" + m;
                    String sqlDate = year + "-" + month + "-" + day + " " + hs + ":" + ms + ":00";
                    
                    psMatch.setInt(1, teamIds.get(t1));
                    psMatch.setInt(2, teamIds.get(t2));
                    psMatch.setString(3, "Jornada " + jornada);
                    psMatch.setString(4, sqlDate);
                    psMatch.executeUpdate();
                }

                System.out.println("Insertando 32 Partidos Eliminatorios...");
                String[] fases = {
                    "Dieciseisavos de Final", "Octavos de Final", "Cuartos de Final", 
                    "Semifinal", "3er y 4to Puesto", "Final"
                };
                
                int[] numPartidos = {16, 8, 4, 2, 1, 1};
                String[] startDates = {"2026-06-28", "2026-07-04", "2026-07-09", "2026-07-14", "2026-07-18", "2026-07-19"};
                
                for(int f=0; f<fases.length; f++) {
                    String fase = fases[f];
                    int total = numPartidos[f];
                    String sDate = startDates[f];
                    
                    for(int i=0; i<total; i++) {
                        String time = (i % 2 == 0) ? " 15:00:00" : " 19:00:00";
                        String sqlDate = sDate + time;
                        
                        psMatch.setInt(1, tbdId);
                        psMatch.setInt(2, tbdId);
                        psMatch.setString(3, fase);
                        psMatch.setString(4, sqlDate);
                        psMatch.executeUpdate();
                    }
                }
            }

            System.out.println("¡Actualización exitosa! Base de datos Mundial 2026 lista con fases eliminatorias.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
