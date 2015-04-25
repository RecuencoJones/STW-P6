package org.deprecated.db;

import org.deprecated.db.datos.PrediccionSemana;
import org.deprecated.db.dao.PrediccionDAO;

import java.sql.SQLException;

/**
 * Created by david on 06/04/2015.
 */
public class Retriever {
    
    public static PrediccionSemana ultimaPrediccion() throws SQLException {

        PrediccionDAO pdao = new PrediccionDAO();
        pdao.setConnection(ConnectionAdmin.getConnection());
        return pdao.getPrediccion();
    }
}
