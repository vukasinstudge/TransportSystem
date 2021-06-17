/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author dv170455d
 */
public class dv170455_VehicleOperations implements VehicleOperations {

    Connection conn;

    public dv170455_VehicleOperations() {
        conn = DB.getInstance().getConnection();
    }
    
    @Override
    public boolean insertVehicle(String string, int i, BigDecimal bd) {
        String query;
        query = "select * from Vozilo where regBr = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, string);
            try (ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
                    System.out.println("Vozilo vec postoji");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (i < 0 || i > 2) return false;
        
        query = "insert into Vozilo (regBr, gorivo, potrosnja) values(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, string);
            ps.setInt(2, i);
            ps.setBigDecimal(3, bd);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... strings) {
        if (strings == null) return 0;
        if (strings.length == 0) return 0;
        int num = 0;
        for (String string : strings) {
            try (final PreparedStatement ps = conn.prepareStatement("delete from Vozilo where regBr = ?")) {
                ps.setString(1, string);
                num += ps.executeUpdate();
            }catch (SQLException sqle) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, sqle);
            }
        }
        return num;
    }

    @Override
    public List<String> getAllVehichles() {
        String query;
        
        query = "select * from Vozilo";
        List<String> ids = new LinkedList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getString("regBr"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }

    @Override
    public boolean changeFuelType(String string, int i) {
        if (i < 0 || i > 2) return false;
        
        String query = "update Vozilo set gorivo = ? where regBr = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, i);
            ps.setString(2, string);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return false;
    }

    @Override
    public boolean changeConsumption(String string, BigDecimal bd) {
        String query = "update Vozilo set potrosnja = ? where regBr = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setBigDecimal(1, bd);
            ps.setString(2, string);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return false;
    }
    
    public int getFuelType(String regBr) {
        String query;
        
        query = "select * from Vozilo where regBr = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setString(1, regBr);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt("gorivo");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }
    
    public BigDecimal getSpend(String regBr) {
        String query;
        
        query = "select * from Vozilo where regBr = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setString(1, regBr);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getBigDecimal("potrosnja");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
}
