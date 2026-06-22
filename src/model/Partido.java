package model;

import java.time.LocalDateTime;

public class Partido {
    private long id;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private String fase;
    private LocalDateTime fechaHora;
    private Integer golesLocalReal;
    private Integer golesVisitanteReal;
    private String estado; // PENDIENTE, EN_JUEGO, FINALIZADO
    private String cerradoPor;

    public Partido() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Equipo getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(Equipo equipoLocal) { this.equipoLocal = equipoLocal; }

    public Equipo getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(Equipo equipoVisitante) { this.equipoVisitante = equipoVisitante; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Integer getGolesLocalReal() { return golesLocalReal; }
    public void setGolesLocalReal(Integer golesLocalReal) { this.golesLocalReal = golesLocalReal; }

    public Integer getGolesVisitanteReal() { return golesVisitanteReal; }
    public void setGolesVisitanteReal(Integer golesVisitanteReal) { this.golesVisitanteReal = golesVisitanteReal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCerradoPor() { return cerradoPor; }
    public void setCerradoPor(String cerradoPor) { this.cerradoPor = cerradoPor; }
}
