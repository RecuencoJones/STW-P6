package org.deprecated.db.dao;

import org.deprecated.db.datos.PrediccionDia;
import org.deprecated.db.datos.PrediccionSemana;
import org.deprecated.db.ConnectionAdmin;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by david on 06/04/2015.
 */
public class SemanaDAO {

    private Connection con;

    public void setConnection(Connection con){
        this.con=con;
    }

    public int guardarSemana(PrediccionSemana semana){
        try{
            String query = "insert into prediccionsemana (hora,fecha) values (?,?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, semana.getHora());
            ps.setLong(2, semana.getFechaActual().getTime());
            int insertedRows = ps.executeUpdate();
            if(insertedRows == 1){
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                int semanaId = keys.getInt(1);
                guardarDias(semanaId, semana.getPrediccionSemana());
                return semanaId;
            }else {
                return -1;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    
    public PrediccionSemana getSemana(int id){
        try{
            String query = "select * from prediccionsemana where id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet result = ps.executeQuery();
            if(result.next()){
                PrediccionSemana p = new PrediccionSemana(new Date(result.getLong("fecha")),result.getInt("hora"));
                ArrayList<PrediccionDia> prediccionDias = getDias(result.getInt("id"));
                p.setPrediccionSemana(prediccionDias);
                return p;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private int guardarDia(PrediccionDia dia) throws SQLException {
        DiaDAO ddao = new DiaDAO();
        ddao.setConnection(ConnectionAdmin.getConnection());
        return ddao.guardarDia(dia);
    }

    private void guardarDias(int semanaId, List<PrediccionDia> dias) throws SQLException {
        for(PrediccionDia p : dias){
            int diaId = guardarDia(p);
            DiaDAO ddao = new DiaDAO();
            ddao.setConnection(ConnectionAdmin.getConnection());
            ddao.guardarSemanaHasDia(semanaId, diaId);
        }
    }
    
    private PrediccionDia getDia(int id) throws SQLException {
        DiaDAO ddao = new DiaDAO();
        ddao.setConnection(ConnectionAdmin.getConnection());
        return ddao.getDia(id);
    }
    
    private ArrayList<PrediccionDia> getDias(int id) throws SQLException {
        ArrayList<PrediccionDia> dias = new ArrayList<>();
        ArrayList<Integer> diasIds = new ArrayList<>();
        DiaDAO ddao = new DiaDAO();
        ddao.setConnection(ConnectionAdmin.getConnection());
        diasIds = ddao.getSemanaHasDia(id);
        for(Integer diaId : diasIds){
            PrediccionDia dia = getDia(diaId);
            dias.add(dia);
        }
        return dias;
    }
}
