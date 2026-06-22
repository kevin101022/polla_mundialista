package model;

public class Equipo {
    private int id;
    private String nombre;
    private String codigoIso;
    private String grupo;

    public Equipo() {
    }

    public Equipo(int id, String nombre, String codigoIso, String grupo) {
        this.id = id;
        this.nombre = nombre;
        this.codigoIso = codigoIso;
        this.grupo = grupo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigoIso() { return codigoIso; }
    public void setCodigoIso(String codigoIso) { this.codigoIso = codigoIso; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    @Override
    public String toString() {
        return nombre;
    }
}
