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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author dv170455d
 */
public class dv170455_CourierOperations implements CourierOperations {

    Connection conn;

    public dv170455_CourierOperations() {
        conn = DB.getInstance().getConnection();
    }        
    
    @Override
    public boolean insertCourier(String userName, String licencePlateNumber) {
        String query;
        
        query = "select * from Korisnik where korime = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, userName);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()) return false;
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select * from Vozilo where regBr = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, licencePlateNumber);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()) return false;
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "insert into Kurir (korime, regBr, brIsporucenihPaketa, status, profit) values(?, ?, 0, 0, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, userName);
            ps.setString(2, licencePlateNumber);
            ps.setBigDecimal(3, BigDecimal.ZERO);
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
    public boolean deleteCourier(String string) {
        try (PreparedStatement ps = conn.prepareStatement("delete from Kurir where korime = ?")) {
            ps.setString(1, string);
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
    public List<String> getCouriersWithStatus(int i) {
        String query;
        
        query = "select * from Kurir where status = ?";
        List<String> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getString("korime"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    public String getCourierByUsername(String korime) {
        String query;
        
        query = "select * from Kurir where korime = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setString(1, korime);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getString("korime");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<String> getAllCouriers() {
        String query;
        
        query = "select * from Kurir order by profit desc";
        List<String> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getString("korime"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int i) {
        String query;
        
        query = "select avg(profit) as average from Kurir where brIsporucenihPaketa >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, i);
            try (ResultSet rs=stmt.executeQuery()){
                if(rs.next()) return rs.getBigDecimal("average");
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(0);
    }
    
    public boolean updateStatusByUsername(String korime, int status) {
        String query = "update Kurir set status = ? where korime = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, status);
            ps.setString(2, korime);
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
    
    public String getVehicle(String korime) {
        String query;
        
        query = "select * from Kurir where korime = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setString(1, korime);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getString("regBr");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public BigDecimal getProfit(String korime) {
        String query;
        
        query = "select * from Kurir where korime = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setString(1, korime);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getBigDecimal("profit");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public boolean updateProfitByUsername(String korime, BigDecimal profit) {
        String query = "update Kurir set profit = ? where korime = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setBigDecimal(1, profit);
            ps.setString(2, korime);
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
    
}
