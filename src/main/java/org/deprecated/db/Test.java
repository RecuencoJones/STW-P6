package org.deprecated.db;

import org.deprecated.db.datos.Periodo;
import org.deprecated.db.dao.IntervaloDAO;

import java.sql.SQLException;

/**
 * Created by david on 06/04/2015.
 */
public class Test {
    public static void main(String[] args) {
        try {
            IntervaloDAO idao = new IntervaloDAO();
            idao.setConnection(ConnectionAdmin.getConnection());
            for(Periodo.Interval intervalo : Periodo.Interval.values()){
                idao.guardarIntervalo(intervalo);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
