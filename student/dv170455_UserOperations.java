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
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author dv170455d
 */
public class dv170455_UserOperations implements UserOperations {

    Connection conn;
    
    public dv170455_UserOperations() {
        conn = DB.getInstance().getConnection();
    }
    
    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password) {
        String query;
        query = "select * from Korisnik where korime = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, userName);
            try (ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
                    System.out.println("Korisnik vec postoji");
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (userName == null || firstName == null || lastName == null || password == null) return false;
        if (userName.length() == 0 || firstName.length() == 0 || lastName.length() == 0 || password.length() < 8) return false;
        
        if (!Character.isUpperCase(firstName.charAt(0))) return false;
        if (!Character.isUpperCase(lastName.charAt(0))) return false;
        
        if (!password.matches("^[a-zA-Z0-9]+$")) return false;
        
        query = "insert into Korisnik (korime, ime, prezime, sifra, brPoslatihPaketa) values(?, ?, ?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, userName);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, password);
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
    public int declareAdmin(String userName) {
        String query;
        
        query = "select * from Korisnik where korime = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, userName);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    System.out.println("Nema korisnik");
                    return 2;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select * from Admin where korime = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, userName);
            try (ResultSet rs=stmt.executeQuery()){
                if(rs.next()){
                    System.out.println("Vec admin");
                    return 1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "insert into Admin (korime) values(?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, userName);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 3;
    }

    @Override
    public Integer getSentPackages(String... strings) {
        if (strings == null) return null;
        if (strings.length == 0) return null;
        
        boolean foundSomeUser = false;
        String query;
        for (String string: strings) {
            query = "select * from Korisnik where korime = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
               ps.setString(1, string);
               ResultSet rs = ps.executeQuery();
               if (rs.next()) {
                   foundSomeUser = true;
               }
            } catch (SQLException e) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
            } 
        }
        
        if (!foundSomeUser) return null;
        
        int num = 0;
        for (String string : strings) {
            try (final PreparedStatement ps = conn.prepareStatement("select count(*) as num from Paket p "
                                + "where p.korimeSalje = ?")) {
                ps.setString(1, string);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    num += rs.getInt("num");
                }
            }catch (SQLException sqle) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, sqle);
            }
        }
        return num;
    }

    @Override
    public int deleteUsers(String... strings) {
        if (strings == null) return 0;
        if (strings.length == 0) return 0;
        int num = 0;
        for (String string : strings) {
            try (final PreparedStatement ps = conn.prepareStatement("delete from Korisnik where korime = ?")) {
                ps.setString(1, string);
                num += ps.executeUpdate();
            }catch (SQLException sqle) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, sqle);
            }
        }
        return num;
    }

    @Override
    public List<String> getAllUsers() {
        String query;
        
        query = "select * from Korisnik";
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
    
}
