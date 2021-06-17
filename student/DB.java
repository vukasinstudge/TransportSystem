/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dv170455d
 */
public class DB {
    private static final String username="scyther";
    private static final String password="123";
    private static final String database="sabprojekat";
    private static final int port=1433;
    private static final String server="localhost";
    
    private static final String connectionUrl = 
        "jdbc:sqlserver://"+server+":"+port+";databaseName="+database;

    private Connection connection;

    public Connection getConnection(){
        return connection;
    }
    

    private DB(){
        try {
            connection=DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    private static DB db=null;
    public static DB getInstance(){
        if(db==null)
            db=new DB();
        return db;
    }
}
