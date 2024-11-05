package com.sistema_energia.eventos;

public class EventoCrud {
    private Integer id;
    private TipoCrud tipo;
    private String horaFehca;
    private String mensaje;

    public EventoCrud() {
    }
    public EventoCrud(TipoCrud tipo, String horaFehca, String mensaje) {
        this.tipo = tipo;
        this.horaFehca = horaFehca;
        this.mensaje = mensaje;
    }

    public void setTipo(TipoCrud tipo) {
        this.tipo = tipo;
    }

    public String getHoraFehca() {
        return horaFehca;
    }

    public void setHoraFehca(String horaFehca) {
        this.horaFehca = horaFehca;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public TipoCrud getTipo() {
        return tipo;
    }

}