package org.deprecated.db.datos;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 23/03/2015.
 */
public class PrediccionSemana {
    
    private Date fechaActual;
    private int hora;
    private List<PrediccionDia> prediccionSemana;

    public PrediccionSemana(String fecha) throws ParseException {
        this.fechaActual = Util.parseFromString(fecha.substring(0,11));
        this.hora = Integer.parseInt(fecha.substring(11,13));
    }

    public PrediccionSemana(Date fechaActual, int hora) {
        this.fechaActual = fechaActual;
        this.hora = hora;
    }

    public List<PrediccionDia> getPrediccionSemana() {
        return prediccionSemana;
    }

    public void setPrediccionSemana(List<PrediccionDia> prediccionSemana) {
        this.prediccionSemana = prediccionSemana;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public int getHora() {
        return hora;
    }

    @Override
    public String toString() {
        return "PrediccionSemana{" +
                "fechaActual=" + fechaActual +
                ", hora=" + hora +
                ", prediccionSemana=" + prediccionSemana +
                '}';
    }
}
