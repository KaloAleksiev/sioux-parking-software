package sample.classes;

import javafx.scene.control.Alert;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataControl {

    // init database constants
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://studmysql01.fhict.local/dbi427262";
    private static final String USERNAME = "dbi427262";
    private static final String PASSWORD = "password1234";
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
        int id = 5;
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "SELECT MAX(appointment_id) AS 'max' FROM `appointment`";
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

    public void AddAppointmentToDB(int day, int month, int year, LocalTime time, List<Driver> driverList) throws SQLException {
        String sql = "INSERT INTO `appointment` (`date`, `time`) VALUES ('" + year + "-" + month + "-" + day + "', '" + time + "');";
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
        for (Driver driver : appointment.getDriverList()) {
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

    public List<Driver> GetDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
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
                Driver driver = new Driver(plate, phone, name, id);
                drivers.add(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
            return drivers;
        }
    }

    public List<Integer> GetDriversForAppointment(Appointment appointment) throws SQLException {
        List<Integer> list = new ArrayList<>();
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "SELECT driver_id FROM `driver_appointment` WHERE appointment_id IS '" + appointment.getId() + "';";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id  = rs.getInt("driver_id");
                list.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
            return list;
        }
    }

    public List<Appointment> GetAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        String sql = "SELECT 'appointment_id', 'date', 'time' FROM `appointment`";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id  = rs.getInt("appointment_id");
                String date = rs.getString("date");
                String dateYear = date.substring(0,4);
                String dateMonth = date.substring(5,7);
                String dateDay = date.substring(8);
                String time = rs.getString("time");
                Appointment appointment = new Appointment(Integer.parseInt(dateDay), Integer.parseInt(dateMonth), Integer.parseInt(dateYear), LocalTime.parse(time), id);



                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
            return appointments;
        }
    }
}
