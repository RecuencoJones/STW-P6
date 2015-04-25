package org.deprecated.db.dao;

import org.deprecated.db.datos.Periodo;
import org.deprecated.db.datos.PrediccionDia;
import org.deprecated.db.ConnectionAdmin;

import java.sql.*;
import java.util.*;

/**
 * Created by david on 06/04/2015.
 */
public class DiaDAO {

    private Connection con;

    public void setConnection(Connection con){
        this.con=con;
    }
    
    public int guardarDia(PrediccionDia dia){
        try{
            String query = "insert into Dia (fecha,uv_max,temp_max,temp_min) " +
                    "values (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, dia.getFechaDia().getTime());
            ps.setInt(2,dia.getUv_max());
            ps.setInt(3,dia.getTemp_max());
            ps.setInt(4, dia.getTemp_min());
            int insertedRows = ps.executeUpdate();
            if(insertedRows == 1){
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                int diaId = keys.getInt(1);
                guardarPeriodos(diaId,dia.getPeriodos());
                return diaId;
            }else {
                return -1;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }        
    }
    
    public PrediccionDia getDia(int id){
        try{
            String query = "select * from dia where id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                PrediccionDia p = new PrediccionDia(new java.util.Date(result.getLong("fecha")),result.getInt("uv_max"),
                        result.getInt("temp_max"),result.getInt("temp_min"));
                ArrayList<Periodo> periodos = getPeriodos(result.getInt("id"));
                p.setPeriodos(periodos);
                return p;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private int guardarPeriodo(Periodo periodo) throws SQLException {
        PeriodoDAO pdao = new PeriodoDAO();
        pdao.setConnection(ConnectionAdmin.getConnection());
        return pdao.guardarPeriodo(periodo);
    }
    
    private void guardarPeriodos(int diaId, List<Periodo> periodos) throws SQLException {
        for(Periodo p : periodos){
            int perId = guardarPeriodo(p);
            PeriodoDAO pdao = new PeriodoDAO();
            pdao.setConnection(ConnectionAdmin.getConnection());
            pdao.guardarDiaHasPeriodo(diaId, perId);
        }
    }

    private Periodo getPeriodo(int id) throws SQLException {
        PeriodoDAO pdao = new PeriodoDAO();
        pdao.setConnection(ConnectionAdmin.getConnection());
        return pdao.getPeriodo(id);
    }
    
    private ArrayList<Periodo> getPeriodos(int id) throws SQLException {
        ArrayList<Periodo> periodos = new ArrayList<>();
        ArrayList<Integer> periodosIds = new ArrayList<>();
        PeriodoDAO pdao = new PeriodoDAO();
        pdao.setConnection(ConnectionAdmin.getConnection());
        periodosIds = pdao.getDiaHasPeriodo(id);
        for(Integer periodoId : periodosIds){
            Periodo periodo = getPeriodo(periodoId);
            periodos.add(periodo);
        }
        return periodos;
    }

    public boolean guardarSemanaHasDia(int semanaId, int diaId){
        try{
            String query = "insert into prediccionsemana_has_dia (prediccionSemana,dia) values (?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, semanaId);
            ps.setInt(2, diaId);
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
    
    public ArrayList<Integer> getSemanaHasDia(int id){
        ArrayList<Integer> ids = new ArrayList<>();
        try{
            String query = "select dia from prediccionsemana_has_dia where prediccionSemana = ?";
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
    
}
