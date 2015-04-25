package org.deprecated.db.datos;

/**
 * Created by david on 23/03/2015.
 */
public class Periodo {

    public static enum Interval {I0024, I0012, I1224, I0006, I0612, I1218, I1824 }
    public static enum Propiedad { P_PRECIP, C_NIEVE, E_CIELO}
    
    private Interval interval;
    private Viento viento;
    private int p_precip;
    private int c_nieve;
    private String e_cielo;

    public Periodo(Interval interval) {
        this.interval = interval;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public Viento getViento() {
        return viento;
    }

    public void setViento(Viento viento) {
        this.viento = viento;
    }

    public int getP_precip() {
        return p_precip;
    }

    public void setP_precip(int p_precip) {
        this.p_precip = p_precip;
    }

    public int getC_nieve() {
        return c_nieve;
    }

    public void setC_nieve(int c_nieve) {
        this.c_nieve = c_nieve;
    }

    public String getE_cielo() {
        return e_cielo;
    }

    public void setE_cielo(String e_cielo) {
        this.e_cielo = e_cielo;
    }
    
    public void set(String value, Propiedad prop){
        switch (prop){
            case P_PRECIP:
                this.p_precip = Integer.parseInt(value);
                break;
            case C_NIEVE:
                this.c_nieve = Integer.parseInt(value);
                break;
            case E_CIELO:
                this.e_cielo = value;
                break;
        }
    }

    @Override
    public String toString() {
        return "\n\t\tPeriodo{" +
                "interval=" + interval +
                ", p_precip=" + p_precip +
                ", c_nieve=" + c_nieve +
                ", e_cielo='" + e_cielo + '\'' +
                ", viento=" + viento +
                '}';
    }
}
