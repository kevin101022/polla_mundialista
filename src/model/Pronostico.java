package model;

import java.time.LocalDateTime;

public class Pronostico {
    private long id;
    private long usuarioId;
    private long partidoId;
    private int golesLocalPred;
    private int golesVisitantePred;
    private Integer puntosObtenidos;
    private LocalDateTime fechaRegistro;

    public Pronostico() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(long usuarioId) { this.usuarioId = usuarioId; }

    public long getPartidoId() { return partidoId; }
    public void setPartidoId(long partidoId) { this.partidoId = partidoId; }

    public int getGolesLocalPred() { return golesLocalPred; }
    public void setGolesLocalPred(int golesLocalPred) { this.golesLocalPred = golesLocalPred; }

    public int getGolesVisitantePred() { return golesVisitantePred; }
    public void setGolesVisitantePred(int golesVisitantePred) { this.golesVisitantePred = golesVisitantePred; }

    public Integer getPuntosObtenidos() { return puntosObtenidos; }
    public void setPuntosObtenidos(Integer puntosObtenidos) { this.puntosObtenidos = puntosObtenidos; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
