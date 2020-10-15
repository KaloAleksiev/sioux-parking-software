package sample.classes;

public class Driver {
    int id;
    String licencePlate;
    int phoneNumber;
    String name;

    public int getId() { return id; }
    public String getLicencePlate() { return licencePlate; }
    public int getPhoneNumber() { return phoneNumber; }
    public String getName() { return name; }

    public Driver(String licencePlate, int phoneNumber, String name) {
        this.licencePlate = licencePlate;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public Driver(String licencePlate, int phoneNumber, String name, int id) {
        this.id = id;
        this.licencePlate = licencePlate;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String GetInfo() {
        return "Name: " + this.name +
                ", licence plate number: " + this.licencePlate +
                ", phone number: " + this.phoneNumber;
    }
}
