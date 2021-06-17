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
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author dv170455d
 */
public class dv170455_CityOperations implements CityOperations {
    private Connection conn;
    
    public dv170455_CityOperations() {
        conn = DB.getInstance().getConnection();
    }
    
    @Override
    public int insertCity(String name, String postalCode) {
        String query;
        
        query = "select * from Grad where naziv = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    System.out.println("Grad vec postoji");
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select * from Grad where postanskiBroj = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, postalCode);
            try (ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
                    System.out.println("Grad vec postoji");
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "insert into Grad (naziv, postanskiBroj) values(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, name);
            ps.setString(2, postalCode);
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
    public int deleteCity(String... strings) {
        if (strings == null) return 0;
        if (strings.length == 0) return 0;
        int num = 0;
        for (String string : strings) {
            try (final PreparedStatement ps = conn.prepareStatement("delete from Grad where naziv = ?")) {
                ps.setString(1, string);
                num += ps.executeUpdate();
            }catch (SQLException sqle) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, sqle);
            }
        }
        return num;
    }

    @Override
    public boolean deleteCity(int i) {
        try (PreparedStatement ps = conn.prepareStatement("delete from Grad where idGrad = ?")) {
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
    public List<Integer> getAllCities() {
        String query;
        
        query = "select * from Grad";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idGrad"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
}
