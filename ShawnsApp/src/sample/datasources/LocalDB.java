package sample.datasources;

import sample.models.Appointment;
import sample.models.Driver;
import sample.interfaces.DataSource;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LocalDB implements DataSource {

    // init database constants
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String MAX_POOL = "250";

    private Connection connection;
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
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int GetMaxDriverID() throws SQLException {
        int id = 0;
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "SELECT MAX(driver_id) AS 'max' FROM `driver`";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                id = rs.getInt("max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
            return id;
        }
    }

    public int GetMaxAppointmentID() throws SQLException {
        int id = 1;
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "SELECT MAX(appointment_id) AS 'max' FROM `appointment`";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                id = rs.getInt("max");
            }
            if(id == 0){
                id = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
            return id;
        }
    }

    public void AddDriverToDB(String plate, String phone, String name) {
        String sql = "INSERT INTO `driver` (`license_plate`, `phone_number`, `name`) VALUES ('" + plate + "', '" + phone + "', '" + name + "');";
        try {
            PreparedStatement statement = this.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
    }

    public void AddAppointmentToDB(int day, int month, int year, LocalTime time, List<sample.models.Driver> driverList) throws SQLException {

        String month1 = Integer.toString(month);
        String day1 = Integer.toString(day);
        if(day < 10){
            day1 = "0"+ day;
        }
        if(month < 10){
            month1 = "0" + month;
        }

        String sql = "INSERT INTO `appointment` (`date`, `time`) VALUES ('" + year + "-" + month1 + "-" + day1 + "', '" + time + ":00');";
        try {
            PreparedStatement statement = this.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
    }

    public void UpdateDB(Appointment appointment) {
        for (sample.models.Driver driver : appointment.getDriverList()) {
            String sql = "INSERT INTO `driver_appointment` (`driver_id`, `appointment_id`) VALUES ('" + driver.getId() + "', '" + appointment.getId() + "');";
            try {
                PreparedStatement statement = this.connect().prepareStatement(sql);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.disconnect();
            }
        }
    }

    public List<sample.models.Driver> GetDrivers() throws SQLException {
        List<sample.models.Driver> drivers = new ArrayList<>();
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "SELECT driver_id, license_plate, phone_number, name FROM `driver`";
        try {
            ResultSet rs = stmt.executeQuery(sql);
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
            this.disconnect();
            return drivers;
        }
    }

    public List<Driver> GetDriversForAppointment(Appointment appointment) throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "" +
                "SELECT d.driver_id,d.license_plate,d.phone_number, d.name " +
                "FROM `driver` AS d INNER JOIN `driver_appointment` AS da " +
                "ON d.driver_id = da.driver_id " +
                "WHERE da.appointment_id =" + appointment.getId()+";";
        try {
            ResultSet rs = stmt.executeQuery(sql);
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
            this.disconnect();
            return drivers;
        }
    }

    public List<Appointment> GetAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "SELECT `appointment_id`, `date`, `time` FROM `appointment`";
        try{
            ResultSet rs = stmt.executeQuery(sql);
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
            this.disconnect();
            return appointments;
        }
    }

    public void ChangeDate(int day, int month, int year, int appId){
        String sql = "UPDATE appointment SET date = '" + year + "-" + month + "-" + day + "' WHERE appointment_id =" + appId;
        try {
            PreparedStatement statement = this.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
    }

    public void ChangeTime(LocalTime time, int appId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(time.format(formatter));
        String sql = "UPDATE appointment SET time = '"+time+"' WHERE  appointment_id= '" + appId + "';";
        try {
            PreparedStatement statement = this.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
    }

    public void ChangeDrivers(List<Driver> newDrivers, Appointment ap){
        List<Driver> nd = new ArrayList<>();
        for (Driver d:newDrivers) {
            nd.add(d);
        }
        for (Driver d:ap.getDriverList()){
            if(!nd.stream().anyMatch(o -> o.getId() == (d.getId()))){
                String sql = "DELETE FROM driver_appointment WHERE driver_id="+ d.getId() +" AND appointment_id="+ap.getId();
                try {
                    PreparedStatement statement = this.connect().prepareStatement(sql);
                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    this.disconnect();
                }
            }
            else if(nd.stream().anyMatch(o -> o.getId() == (d.getId()))){
                nd.removeIf(dr -> (dr.getId() == d.getId()));
            }
        }
        for (Driver dr:nd) {
            String sql = "INSERT INTO `driver_appointment` (`driver_id`, `appointment_id`) VALUES (" + dr.getId() + ", " + ap.getId() + ");";
            try {
                PreparedStatement statement = this.connect().prepareStatement(sql);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.disconnect();
            }
        }
    }

    @Override
    public void DeleteAppointment(int id) {

    }
}