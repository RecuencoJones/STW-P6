package org.deprecated.db.dao;

import org.deprecated.db.datos.Viento;

import java.sql.*;

/**
 * Created by david on 06/04/2015.
 */
public class VientoDAO {
    
    private Connection con;
    
    public void setConnection(Connection con){
        this.con=con;
    }
    
    public int guardarViento(Viento viento){
        try{
            String query = "insert into Viento (direccion,velocidad) values (?,?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,viento.getDireccion());
            ps.setInt(2,viento.getVelocidad());
            int insertedRows = ps.executeUpdate();
            if(insertedRows == 1){
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            }else {
                return -1;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    public Viento getViento(int id){
        try{
            String query = "select * from Viento where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return new Viento(resultSet.getString(2),resultSet.getInt(3));
            }else {
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
