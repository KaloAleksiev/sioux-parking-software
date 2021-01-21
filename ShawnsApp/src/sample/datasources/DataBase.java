package sample.datasources;

import sample.models.Appointment;
import sample.models.Driver;
import sample.interfaces.DataSource;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DataBase implements DataSource {

    // init database constants
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://database-mysql.cbp7gz9tiss2.eu-central-1.rds.amazonaws.com/sioux";
    private static final String USERNAME = "whyIBI";
    private static final String PASSWORD = "IBIproject777";
    private static final String MAX_POOL = "250";

    private Properties properties;

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    // connect database
    public Connection connect() {
        Connection conn = null;

        try {
            Class.forName(DATABASE_DRIVER);
            conn = DriverManager.getConnection(DATABASE_URL, getProperties());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public int GetMaxDriverID(){
        int id = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT MAX(driver_id) AS 'max' FROM `driver`";
        try {
            conn = this.connect();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                id = rs.getInt("max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn,stmt,rs);
            return id;
        }
    }

    public int GetMaxAppointmentID(){
        int id = 1;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT MAX(appointment_id) AS 'max' FROM `appointment`";
        try {
            conn = this.connect();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                id = rs.getInt("max");
            }
            if(id == 0){
                id = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn,stmt,rs);
        }
        return id;
    }

    public void AddDriverToDB(String plate, String phone, String name){
        String sql = "INSERT INTO `driver` (`license_plate`, `phone_number`, `name`) VALUES ('" + plate + "', '" + phone + "', '" + name + "');";
        executeStatement(sql);
    }

    public void AddAppointmentToDB(int day, int month, int year, LocalTime time, List<sample.models.Driver> driverList){
        String month1 = Integer.toString(month);
        String day1 = Integer.toString(day);
        if(day < 10){
            day1 = "0"+ day;
        }
        if(month < 10){
            month1 = "0" + month;
        }
        String sql = "INSERT INTO `appointment` (`date`, `time`) VALUES ('" + year + "-" + month1 + "-" + day1 + "', '" + time + ":00');";
        executeStatement(sql);
    }

    public void UpdateDB(Appointment appointment){
        for (Driver driver : appointment.getDriverList()) {
            String sql = "INSERT INTO `driver_appointment` (`driver_id`, `appointment_id`) VALUES ('" + driver.getId() + "', '" + appointment.getId() + "');";
            executeStatement(sql);
        }
    }

    public List<Driver> GetDrivers() throws SQLException {
        List<sample.models.Driver> drivers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT driver_id, license_plate, phone_number, name FROM `driver`";
        try {
            conn = this.connect();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id  = rs.getInt("driver_id");
                String plate = rs.getString("license_plate");
                String phone = rs.getString("phone_number");
                String name = rs.getString("name");
                sample.models.Driver driver = new Driver(plate, phone, name, id);
                drivers.add(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn,stmt,rs);
        }
        return drivers;
    }

    public List<Driver> GetDriversForAppointment(Appointment appointment){
        List<Driver> drivers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "" +
                "SELECT d.driver_id,d.license_plate,d.phone_number, d.name " +
                "FROM `driver` AS d INNER JOIN `driver_appointment` AS da " +
                "ON d.driver_id = da.driver_id " +
                "WHERE da.appointment_id =" + appointment.getId()+";";
        try {
            conn = this.connect();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id  = rs.getInt("d.driver_id");
                String lp = rs.getString("d.license_plate");
                String phone = rs.getString("d.phone_number");
                String name = rs.getString("d.name");

                Driver d = new Driver(lp, phone,name,id);
                drivers.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn,stmt,rs);
            return drivers;
        }
    }

    public List<Appointment> GetAppointments(){
        List<Appointment> appointments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT `appointment_id`, `date`, `time` FROM `appointment`";
        try{
            conn = this.connect();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String id  = rs.getString("appointment_id");
                String date = rs.getString("date");
                String dateYear = date.substring(0,4);
                String dateMonth = date.substring(5,7);
                String dateDay = date.substring(8);
                String time = rs.getString("time");

                appointments.add(new Appointment(
                        Integer .parseInt(dateDay),
                        Integer.parseInt(dateMonth),
                        Integer.parseInt(dateYear),
                        LocalTime.parse(time),
                        Integer.parseInt(id))
                );
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            this.close(conn,stmt,rs);
            return appointments;
        }
    }

    public void ChangeDate(int day, int month, int year, int appId){
        String sql = "UPDATE appointment SET date = '" + year + "-" + month + "-" + day + "' WHERE appointment_id =" + appId;
        executeStatement(sql);
    }

    public void ChangeTime(LocalTime time, int appId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(time.format(formatter));
        String sql = "UPDATE appointment SET time = '"+time+"' WHERE  appointment_id= '" + appId + "';";
        executeStatement(sql);
    }

    public void ChangeDrivers(List<Driver> newDrivers, Appointment ap){
        List<Driver> nd = new ArrayList<>();
        for (Driver d:newDrivers) {
            nd.add(d);
        }
        for (Driver d:ap.getDriverList()){
            this.SafeModeOff();
            String sql = "DELETE FROM `driver_appointment` WHERE driver_id="+ d.getId() +" AND appointment_id="+ap.getId()+";";
            executeStatement(sql);
            this.SafeModeOn();
        }
        for (Driver dr:nd) {
            String sql = "INSERT INTO `driver_appointment` (`driver_id`, `appointment_id`) VALUES (" + dr.getId() + ", " + ap.getId() + ");";
            executeStatement(sql);
        }
    }

    public void DeleteAppointment(int id){
        String sql = "DELETE FROM appointment WHERE  appointment_id= '" + id + "';";
        executeStatement(sql);
    }

    public void ChangeDriverName(String name, int driverId){
        String sql = "UPDATE driver SET name = '"+name+"' WHERE  driver_id= '" + driverId + "';";
        executeStatement(sql);
    }

    public void ChangeDriverLicensePlate(String license, int driverId){
        String sql = "UPDATE driver SET license_plate = '"+license+"' WHERE  driver_id= '" + driverId + "';";
        executeStatement(sql);
    }

    public void ChangeDriverNumber(String number, int driverId){
        String sql = "UPDATE driver SET phone_number = '"+number+"' WHERE  driver_id= '" + driverId + "';";
        executeStatement(sql);
    }

    public void DeleteDriver(int id){
        String sql = "DELETE FROM driver WHERE  driver_id= '" + id + "';";
        executeStatement(sql);
    }

    private void executeStatement(String sql){
        PreparedStatement statement = null;
        Connection conn = null;
        try {
            conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn,statement, null);
        }
    }

    private void  close(Connection c, PreparedStatement p, ResultSet r){
        try { if (r != null) r.close(); } catch (Exception e) {/*ignore*/}
        try { if (p != null) p.close(); } catch (Exception e) {/*ignore*/}
        try { if (c != null) c.close(); } catch (Exception e) {/*ignore*/}
    }

    private void SafeModeOn(){
        String sql = "SET SQL_SAFE_UPDATES=1;";
        executeStatement(sql);
    }

    private void SafeModeOff(){
        String sql = "SET SQL_SAFE_UPDATES=0;";
        executeStatement(sql);
    }
}
