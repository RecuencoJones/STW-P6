package org.deprecated.db.dao;

import org.deprecated.db.datos.Periodo;
import org.deprecated.db.datos.Viento;
import org.deprecated.db.ConnectionAdmin;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by david on 06/04/2015.
 */
public class PeriodoDAO {
    
    private Connection con;

    public void setConnection(Connection con){
        this.con=con;
    }
    
    public int guardarPeriodo(Periodo periodo){
        try{
            int intervaloId = getIntervalo(periodo.getInterval());
            int vientoId = guardarViento(periodo.getViento());
            String query = "insert into Periodo (intervalo,viento,p_precip,c_nieve,e_cielo) " +
                    "values (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,intervaloId);
            ps.setInt(2,vientoId);
            ps.setInt(3,periodo.getP_precip());
            ps.setInt(4,periodo.getC_nieve());
            ps.setString(5, periodo.getE_cielo());
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
    
    public Periodo getPeriodo(int id){
        try{
            String query = "select * from periodo where id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                Periodo.Interval interval = getIntervalo(result.getInt("intervalo"));
                Viento viento = getViento(result.getInt("viento"));
                Periodo p = new Periodo(interval);
                p.setViento(viento);
                p.setP_precip(result.getInt("p_precip"));
                p.setC_nieve(result.getInt("c_nieve"));
                p.setE_cielo(result.getString("e_cielo"));
                return p;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean guardarDiaHasPeriodo(int diaId, int perId){
        try{
            String query = "insert into dia_has_periodo (dia,periodo) values (?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, diaId);
            ps.setInt(2, perId);
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

    public ArrayList<Integer> getDiaHasPeriodo(int id){
        ArrayList<Integer> ids = new ArrayList<>();
        try{
            String query = "select periodo from dia_has_periodo where dia = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while(result.next()){
                ids.add(result.getInt(1));
            }
            return ids;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    private int getIntervalo(Periodo.Interval interval) throws SQLException {
        IntervaloDAO idao = new IntervaloDAO();
        idao.setConnection(ConnectionAdmin.getConnection());
        return idao.getIntervalo(interval);
    }

    private Periodo.Interval getIntervalo(int id) throws SQLException {
        IntervaloDAO idao = new IntervaloDAO();
        idao.setConnection(ConnectionAdmin.getConnection());
        return idao.getIntervalo(id);
    }
    
    private int guardarViento(Viento viento) throws SQLException {
        VientoDAO vdao = new VientoDAO();
        vdao.setConnection(ConnectionAdmin.getConnection());
        return vdao.guardarViento(viento);
    }

    private Viento getViento(int id) throws SQLException {
        VientoDAO vdao = new VientoDAO();
        vdao.setConnection(ConnectionAdmin.getConnection());
        return vdao.getViento(id);
    }
}
