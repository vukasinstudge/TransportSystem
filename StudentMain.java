package student;

import java.math.BigDecimal;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

public class StudentMain {

    public static void main(String[] args) {
        dv170455_CityOperations cityOperations = new dv170455_CityOperations(); // Change this to your implementation.
        dv170455_DistrictOperations districtOperations = new dv170455_DistrictOperations(); // Do it for all classes.
        dv170455_CourierOperations courierOperations = new dv170455_CourierOperations(); // e.g. = new MyDistrictOperations();
        dv170455_CourierRequestOperations courierRequestOperation = new dv170455_CourierRequestOperations();
        dv170455_GeneralOperations generalOperations = new dv170455_GeneralOperations();
        dv170455_UserOperations userOperations = new dv170455_UserOperations();
        dv170455_VehicleOperations vehicleOperations = new dv170455_VehicleOperations();
        dv170455_PackageOperations packageOperations = new dv170455_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
        
//        TRIGGER TEST
//        userOperations.insertUser("vuki", "Vuki", "Vuki", "vukiiiiiiiiiiiiiii8");
//        userOperations.insertUser("muki", "Muki", "Muki", "mukiiiiiiiiiiiiiii8");
//        vehicleOperations.insertVehicle("UE098KD", 0, new BigDecimal(5));
//        vehicleOperations.insertVehicle("BG098KD", 0, new BigDecimal(6));
//        courierOperations.insertCourier("vuki", "UE098KD");
//        courierOperations.insertCourier("muki", "BG098KD");
//        
//        int idGrad1 = cityOperations.insertCity("Uzice", "31000");
//        int idGrad2 = cityOperations.insertCity("Beograd", "11000");
//        
//        int idOpstina1 = districtOperations.insertDistrict("Opstina1", idGrad1, 2, 10);
//        int idOpstina2 = districtOperations.insertDistrict("Opstina2", idGrad2, 10, 2);
//        
//        int idPaket = packageOperations.insertPackage(idOpstina1, idOpstina2, "vuki", 1, new BigDecimal(10));
//        
//        int idPonuda1 = packageOperations.insertTransportOffer("vuki", idPaket, new BigDecimal(10));
//        int idPonuda2 = packageOperations.insertTransportOffer("muki", idPaket, new BigDecimal(5));
//        
//        packageOperations.acceptAnOffer(idPonuda1);
        
    }
}
