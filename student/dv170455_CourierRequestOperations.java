/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierRequestOperation;
import java.sql.Types;

/**
 *
 * @author dv170455d
 */
public class dv170455_CourierRequestOperations implements CourierRequestOperation {

    Connection conn;

    public dv170455_CourierRequestOperations() {
        conn = DB.getInstance().getConnection();
    }
    
    @Override
    public boolean insertCourierRequest(String userName, String licencePlateNumber) {
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
        
        query = "insert into ZahtevPostatiKurir (korime, regBr) values(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, userName);
            ps.setString(2, licencePlateNumber);
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
    public boolean deleteCourierRequest(String string) {
        try (PreparedStatement ps = conn.prepareStatement("delete from ZahtevPostatiKurir where korime = ?")) {
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
    public boolean changeVehicleInCourierRequest(String userName, String licencePlateNumber) {
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
        
        query = "update ZahtevPostatiKurir set regBr = ? where korime = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, licencePlateNumber);
            ps.setString(2, userName);
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
    public List<String> getAllCourierRequests() {
        String query;
        
        query = "select * from ZahtevPostatiKurir";
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
    public boolean grantRequest(String string) {
        String regBr = null;
        
        try (PreparedStatement ps = conn.prepareStatement("select * from ZahtevPostatiKurir where korime = ?")) {
           ps.setString(1, string);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               regBr = rs.getString("regBr");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        
        if (regBr == null) return false;
        
        boolean ret = false;
        try (CallableStatement stmt = conn.prepareCall("{? = call spPrihvatiKurira (?, ?)}");) {
            stmt.setString(2, string);
            stmt.setString(3, regBr);
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.execute();
            ret = stmt.getInt(1) == 1;
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CourierRequestOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return deleteCourierRequest(string) && ret;   
    }
    
}
