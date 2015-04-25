package org.deprecated.db.datos;

/**
 * Created by david on 23/03/2015.
 */
public class Viento {
    
    private String direccion;
    private int velocidad;

    public Viento(String direccion, int velocidad) {
        this.direccion = direccion;
        this.velocidad = velocidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public String toString() {
        return "Viento{" +
                "direccion='" + direccion + '\'' +
                ", velocidad=" + velocidad +
                '}';
    }
}
