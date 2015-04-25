package org.deprecated.db.dao;

import org.deprecated.db.datos.Periodo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by david on 06/04/2015.
 */
public class IntervaloDAO {

    private Connection con;

    public void setConnection(Connection con){
        this.con=con;
    }
    
    public int getIntervalo(Periodo.Interval interval){
        try{
            String query = "select id from Intervalo where name=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,interval.toString());
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }else {
                return -1;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    
    public Periodo.Interval getIntervalo(int id){
        try{
            String query = "select name from Intervalo where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return Periodo.Interval.valueOf(resultSet.getString(1));
            }else {
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean guardarIntervalo(Periodo.Interval interval){
        try{
            String query = "insert into Intervalo (name) values (?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, interval.toString());
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
    
}
