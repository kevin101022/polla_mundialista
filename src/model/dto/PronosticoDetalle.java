package model.dto;

import java.time.LocalDateTime;

public class PronosticoDetalle {
    private long pronosticoId;
    private long partidoId;
    private int golesLocalPred;
    private int golesVisitantePred;
    private Integer puntosObtenidos;
    private LocalDateTime fechaRegistro;

    private String fase;
    private String fechaHoraPartido;
    private String estadoPartido;

    private String equipoLocalNombre;
    private String equipoLocalIso;
    private int golesLocalReal;

    private String equipoVisitanteNombre;
    private String equipoVisitanteIso;
    private int golesVisitanteReal;

    public PronosticoDetalle() {
    }

    public long getPronosticoId() { return pronosticoId; }
    public void setPronosticoId(long pronosticoId) { this.pronosticoId = pronosticoId; }

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

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public String getFechaHoraPartido() { return fechaHoraPartido; }
    public void setFechaHoraPartido(String fechaHoraPartido) { this.fechaHoraPartido = fechaHoraPartido; }

    public String getEstadoPartido() { return estadoPartido; }
    public void setEstadoPartido(String estadoPartido) { this.estadoPartido = estadoPartido; }

    public String getEquipoLocalNombre() { return equipoLocalNombre; }
    public void setEquipoLocalNombre(String equipoLocalNombre) { this.equipoLocalNombre = equipoLocalNombre; }

    public String getEquipoLocalIso() { return equipoLocalIso; }
    public void setEquipoLocalIso(String equipoLocalIso) { this.equipoLocalIso = equipoLocalIso; }

    public int getGolesLocalReal() { return golesLocalReal; }
    public void setGolesLocalReal(int golesLocalReal) { this.golesLocalReal = golesLocalReal; }

    public String getEquipoVisitanteNombre() { return equipoVisitanteNombre; }
    public void setEquipoVisitanteNombre(String equipoVisitanteNombre) { this.equipoVisitanteNombre = equipoVisitanteNombre; }

    public String getEquipoVisitanteIso() { return equipoVisitanteIso; }
    public void setEquipoVisitanteIso(String equipoVisitanteIso) { this.equipoVisitanteIso = equipoVisitanteIso; }

    public int getGolesVisitanteReal() { return golesVisitanteReal; }
    public void setGolesVisitanteReal(int golesVisitanteReal) { this.golesVisitanteReal = golesVisitanteReal; }
}
