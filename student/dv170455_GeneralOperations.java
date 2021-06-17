/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author dv170455d
 */
public class dv170455_GeneralOperations implements GeneralOperations {

    Connection conn;
    
    public dv170455_GeneralOperations() {
        conn = DB.getInstance().getConnection();
    }
    
    @Override
    public void eraseAll() {
        String query;     
        
        query = "delete from Ponuda";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from Paket";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from Opstina";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from Grad";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from ZahtevPostatiKurir";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from Kurir";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from Admin";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from Korisnik";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "delete from Vozilo";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
