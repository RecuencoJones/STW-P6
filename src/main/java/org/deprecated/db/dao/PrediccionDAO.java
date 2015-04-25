package org.deprecated.db.dao;

import org.deprecated.db.datos.PrediccionSemana;
import org.deprecated.db.ConnectionAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by david on 06/04/2015.
 */
public class PrediccionDAO {

    private Connection con;

    public void setConnection(Connection con){
        this.con=con;
    }
    
    public PrediccionSemana getPrediccion(){
        try{
            String query = "select * from predicciones where id = (select max(id) from predicciones)";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                return getSemana(result.getInt("id"));
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean guardarPrediccion(String filename, PrediccionSemana prediccionSemana){
        try{
            int semanaId = guardarSemana(prediccionSemana);
            String query = "insert into predicciones (fichero,prediccionSemana) values (?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, filename);
            ps.setInt(2, semanaId);
            int insertedRows = ps.executeUpdate();
            if(insertedRows == 1){
                return true;
            }else {
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private int guardarSemana(PrediccionSemana semana) throws SQLException {
        SemanaDAO sdao = new SemanaDAO();
        sdao.setConnection(ConnectionAdmin.getConnection());
        return sdao.guardarSemana(semana);
    }
    
    private PrediccionSemana getSemana(int id) throws SQLException {
        SemanaDAO sdao = new SemanaDAO();
        sdao.setConnection(ConnectionAdmin.getConnection());
        return sdao.getSemana(id);
    }
    
}
