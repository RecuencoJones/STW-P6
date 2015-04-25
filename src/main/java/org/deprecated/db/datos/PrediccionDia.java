package org.deprecated.db.datos;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 23/03/2015.
 */
public class PrediccionDia {
    
    private Date fechaDia;
    private List<Periodo> periodos;
    private int uv_max;
    private int temp_max;
    private int temp_min;
    
    public PrediccionDia(String fechaDia) throws ParseException {
        this.fechaDia = Util.parseFromString(fechaDia);
    }

    public PrediccionDia(Date fechaDia, int uv_max, int temp_max, int temp_min) {
        this.fechaDia = fechaDia;
        this.uv_max = uv_max;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }

    public List<Periodo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    public Date getFechaDia() {
        return fechaDia;
    }

    public int getUv_max() {
        return uv_max;
    }

    public void setUv_max(int uv_max) {
        this.uv_max = uv_max;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(int temp_max) {
        this.temp_max = temp_max;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(int temp_min) {
        this.temp_min = temp_min;
    }

    @Override
    public String toString() {
        return "\n\tPrediccionDia{" +
                "fechaDia=" + fechaDia +
                ", uv_max=" + uv_max +
                ", temp_max=" + temp_max +
                ", temp_min=" + temp_min +
                ", periodos=" + periodos +
                '}';
    }
}
