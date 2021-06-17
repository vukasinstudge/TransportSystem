/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author dv170455d
 */
public class dv170455_DistrictOperations implements DistrictOperations {
  
    Connection conn;
    
    public dv170455_DistrictOperations() {
        conn = DB.getInstance().getConnection();
    }
    
    @Override
    public int insertDistrict(String string, int i, int i1, int i2) {
        String query;
        query = "insert into Opstina (naziv, idGrad, x, y) values(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, string);
            ps.setInt(2, i);
            ps.setInt(3, i1);
            ps.setInt(4, i2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int deleteDistricts(String... strings) {
        if (strings == null) return 0;
        if (strings.length == 0) return 0;
        int num = 0;
        for (String string : strings) {
            try (final PreparedStatement ps = conn.prepareStatement("delete from Opstina where naziv = ?")) {
                ps.setString(1, string);
                num += ps.executeUpdate();
            }catch (SQLException sqle) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, sqle);
            }
        }
        return num;
    }

    @Override
    public boolean deleteDistrict(int i) {
        try (PreparedStatement ps = conn.prepareStatement("delete from Opstina where idOpstina = ?")) {
            ps.setInt(1, i);
            int ret = ps.executeUpdate();
            if (ret == 1) {
                return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @Override
    public int deleteAllDistrictsFromCity(String string) {
        int num = 0;
        String query = "delete o from Opstina o join Grad g on o.idGrad = g.idGrad "
                + "where g.naziv = ?";
        try (final PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            num = ps.executeUpdate();
        }catch (SQLException sqle) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, sqle);
        }
        return num;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int i) {
        String query;
        
        query = "select * from Opstina where idGrad = ?";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idOpstina"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }

    @Override
    public List<Integer> getAllDistricts() {
        String query;
        
        query = "select * from Opstina";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idOpstina"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    public int getX(int idOpstina) {
        String query;
        
        query = "select * from Opstina where idOpstina = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, idOpstina);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt("x");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }
    
    public int getY(int idOpstina) {
        String query;
        
        query = "select * from Opstina where idOpstina = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, idOpstina);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt("y");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }
    
}
