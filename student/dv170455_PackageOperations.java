/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author dv170455d
 */
public class dv170455_PackageOperations implements PackageOperations {

    public static int CENA_GORIVA[] = {15, 32, 36};
    
    Connection conn;
    
    public dv170455_PackageOperations() {
        conn = DB.getInstance().getConnection();
    }
    
    static double euclidean(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    static BigDecimal getPackagePrice(int type, BigDecimal weight, double distance, BigDecimal percentage) {
        percentage = percentage.divide(new BigDecimal(100));
        switch (type) {
            case 0: {
                return new BigDecimal(10.0 * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 1: {
                return new BigDecimal((25.0 + weight.doubleValue() * 100.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
            case 2: {
                return new BigDecimal((75.0 + weight.doubleValue() * 300.0) * distance).multiply(percentage.add(new BigDecimal(1)));
            }
        }
        return null;
    }
    
    @Override
    public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
        String query;
        query = "select * from Opstina where idOpstina = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, districtFrom);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select * from Opstina where idOpstina = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, districtTo);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select * from Korisnik where korime = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, userName);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (packageType < 0 || packageType > 2) return -1;
        
        query = "insert into Paket (od, do, korimeSalje, korimeKurir, tip, tezina, status, vreme, cena) "
                + "values(?, ?, ?, null, ?, ?, 0, null, null)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, districtFrom);
            ps.setInt(2, districtTo);
            ps.setString(3, userName);
            ps.setInt(4, packageType);
            ps.setBigDecimal(5, weight);
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
    public int insertTransportOffer(String couriersUserName, int packageId, BigDecimal pricePercentage) {
        String query;
        
        query = "select * from Kurir where korime = ? and status = 0";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, couriersUserName);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select * from Paket where idPaket = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, packageId);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (pricePercentage == null) {
            Random r = new Random();
            double random = -10 + (10 - (-10)) * r.nextDouble();
            pricePercentage = new BigDecimal(random);
        }
        
        query = "insert into Ponuda (procenat, idPaket, korime) "
                + "values(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setBigDecimal(1, pricePercentage);
            ps.setInt(2, packageId);
            ps.setString(3, couriersUserName);
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
    public boolean acceptAnOffer(int i) {
        String query;
        
        int idPaket = -1;
        String korime = null;
        BigDecimal procenat = null;
        
        query = "select * from Ponuda where idPonuda = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, i);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return false;
                } else {
                    idPaket = rs.getInt("idPaket");
                    korime = rs.getString("korime");
                    procenat = rs.getBigDecimal("procenat");
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int tip = -1;
        BigDecimal tezina = null;
        int opstinaOd = -1;
        int opstinaDo = -1;
        
        query = "select * from Paket where idPaket = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, idPaket);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return false;
                } else {
                    tip = rs.getInt("tip");
                    tezina = rs.getBigDecimal("tezina");
                    opstinaOd = rs.getInt("od");
                    opstinaDo = rs.getInt("do");
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int xOd = -1;
        int yOd = -1;
        int xDo = -1;
        int yDo = -1;
        
        query = "select * from Opstina where idOpstina = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, opstinaOd);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return false;
                } else {
                    xOd = rs.getInt("x");
                    yOd = rs.getInt("y");
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        query = "select * from Opstina where idOpstina = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, opstinaDo);
            try (ResultSet rs=stmt.executeQuery()){
                if(!rs.next()){
                    return false;
                } else {
                    xDo = rs.getInt("x");
                    yDo = rs.getInt("y");
                }
            } catch (SQLException ex) {
                Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BigDecimal cena = getPackagePrice(tip, tezina, euclidean(xOd, yOd, xDo, yDo), procenat);
        Date vreme = new Date(System.currentTimeMillis());
        
        query = "update Paket set cena = ?, vreme = ?, korimeKurir = ?, status = 1 where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setBigDecimal(1, cena);
            ps.setDate(2, vreme);
            ps.setString(3, korime);
            ps.setInt(4, idPaket);
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
    public List<Integer> getAllOffers() {
        String query;
        
        query = "select * from Ponuda";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idPonuda"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    public class dv170455_Pair<A, B> implements PackageOperations.Pair<A, B> {
        
        private A a;
        private B b;

        public dv170455_Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public A getFirstParam() {
            return a;
        }

        @Override
        public B getSecondParam() {
            return b;
        }
        
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int i) {
        String query;
        
        List<Pair<Integer, BigDecimal>> retList = new ArrayList<>();
        query = "select * from Ponuda where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               dv170455_Pair<Integer, BigDecimal> pair = new dv170455_Pair(rs.getInt("idPonuda"), rs.getBigDecimal("procenat"));
               retList.add(pair);
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return retList;
    }

    @Override
    public boolean deletePackage(int i) {
        try (PreparedStatement ps = conn.prepareStatement("delete from Paket where idPaket = ?")) {
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
    public boolean changeWeight(int i, BigDecimal bd) {
        String query;
        
        query = "update Paket set tezina = ? where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setBigDecimal(1, bd);
            ps.setInt(2, i);
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
    public boolean changeType(int packageId, int newType) {
        String query;
        
        if (newType < 0 || newType > 2) return false;
        
        query = "update Paket set tip = ? where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, newType);
            ps.setInt(2, packageId);
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
    
    public boolean changeStatus(int packageId, int newStatus) {
        String query;
        
        if (newStatus < 0 || newStatus > 3) return false;
        
        query = "update Paket set status = ? where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, newStatus);
            ps.setInt(2, packageId);
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
    public Integer getDeliveryStatus(int i) {
        String query;
        
        query = "select * from Paket where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               return rs.getInt("status");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int i) {
        String query;
        
        query = "select * from Paket where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               return rs.getBigDecimal("cena");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int i) {
        String query;
        
        query = "select * from Paket where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               return rs.getDate("vreme");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int i) {
        String query;
        
        query = "select * from Paket where tip = ?";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idPaket"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    public List<Integer> getPackagesWithSpecificStatusAndCourier(int i, String korime) {
        String query;
        
        query = "select * from Paket where status = ? and korimeKurir = ?";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ps.setString(2, korime);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idPaket"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    public List<Integer> getPackagesWithSpecificStatusAndCourierOrderByTimeAsc(int i, String korime) {
        String query;
        
        query = "select * from Paket where status = ? and korimeKurir = ? order by vreme asc";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ps.setString(2, korime);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idPaket"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    public List<Integer> getPackagesWithSpecificStatusAndCourierOrderByTimeDesc(int i, String korime) {
        String query;
        
        query = "select * from Paket where status = ? and korimeKurir = ? order by vreme desc, idPaket desc";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, i);
           ps.setString(2, korime);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idPaket"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }

    @Override
    public List<Integer> getAllPackages() {
        String query;
        
        query = "select * from Paket";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idPaket"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }

    @Override
    public List<Integer> getDrive(String courierUsername) {
        String query;
        
        query = "select * from Paket where korimeKurir = ? and status = 2";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, courierUsername);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               ids.add(rs.getInt("idPaket"));
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return ids;
    }
    
    public int getDistrictFrom(int idPaket) {
        String query;
        
        query = "select * from Paket where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, idPaket);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt("od");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }
    
    public int getDistrictTo(int idPaket) {
        String query;
        
        query = "select * from Paket where idPaket = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, idPaket);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt("do");
           }
        } catch (SQLException e) {
            Logger.getLogger(dv170455_CityOperations.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }

    @Override
    public int driveNextPackage(String courierUserName) {
        String query;
        dv170455_CourierOperations courierOperations = new dv170455_CourierOperations();
        dv170455_DistrictOperations districtOperations = new dv170455_DistrictOperations();
        dv170455_VehicleOperations vehicleOperations = new dv170455_VehicleOperations();
        
        if (courierOperations.getCourierByUsername(courierUserName) == null) return -2;
        
        boolean prvi = false;
        List<Integer> prihvaceni = getPackagesWithSpecificStatusAndCourier(1, courierUserName);
        if (!prihvaceni.isEmpty()) {
            prvi = true;
            if (!courierOperations.updateStatusByUsername(courierUserName, 1)) return -2;
            for (int i = 0; i < prihvaceni.size(); i++) {
                if (!changeStatus(prihvaceni.get(i), 2)) return -2;
            }
        }
        
        List<Integer> pokupljeni = getPackagesWithSpecificStatusAndCourierOrderByTimeAsc(2, courierUserName);
        if (pokupljeni.isEmpty()) return -1;
        
        int opstinaOd = getDistrictFrom(pokupljeni.get(0));
        if (opstinaOd == -1) return -2;
        int opstinaOdX = districtOperations.getX(opstinaOd);
        int opstinaOdY = districtOperations.getY(opstinaOd);
        
        int opstinaDo = getDistrictTo(pokupljeni.get(0));
        if (opstinaDo == -1) return -2;
        int opstinaDoX = districtOperations.getX(opstinaDo);
        int opstinaDoY = districtOperations.getY(opstinaDo);
        
        BigDecimal ukupnaRazdaljina = new BigDecimal(0);
        
        if (!prvi) {
            List<Integer> isporuceni = getPackagesWithSpecificStatusAndCourierOrderByTimeDesc(3, courierUserName);
            int opstinaPrethodniDo = getDistrictTo(isporuceni.get(0));
            if (opstinaPrethodniDo == -1) return -2;
            int opstinaPrethodniDoX = districtOperations.getX(opstinaPrethodniDo);
            int opstinaPrethodniDoY = districtOperations.getY(opstinaPrethodniDo);
            
            ukupnaRazdaljina = ukupnaRazdaljina.add(new BigDecimal(euclidean(opstinaPrethodniDoX, opstinaPrethodniDoY, opstinaOdX, opstinaOdY)));
        }
        ukupnaRazdaljina = ukupnaRazdaljina.add(new BigDecimal(euclidean(opstinaOdX, opstinaOdY, opstinaDoX, opstinaDoY)));
        BigDecimal cena = getPriceOfDelivery(pokupljeni.get(0));
        String regBr = courierOperations.getVehicle(courierUserName);
        if (regBr == null) return -2;
        int tipGoriva = vehicleOperations.getFuelType(regBr);
        if (tipGoriva == -1) return -2;
        BigDecimal potrosnja = vehicleOperations.getSpend(regBr);
        if (potrosnja == null) return -2;
        
        BigDecimal trenutniProfit = cena.subtract(ukupnaRazdaljina.multiply(potrosnja).multiply(new BigDecimal(CENA_GORIVA[tipGoriva])));
        
        BigDecimal dosadasnjiProfit = courierOperations.getProfit(courierUserName);
        if (dosadasnjiProfit == null) return -2;
        if (!courierOperations.updateProfitByUsername(courierUserName, dosadasnjiProfit.add(trenutniProfit))) return -2; 
        
        if (!changeStatus(pokupljeni.get(0), 3)) return -2;
        
        List<Integer> ostali = getPackagesWithSpecificStatusAndCourier(2, courierUserName);
        if (ostali.isEmpty()) courierOperations.updateStatusByUsername(courierUserName, 0);
        
        return pokupljeni.get(0);  
    }
    
}
