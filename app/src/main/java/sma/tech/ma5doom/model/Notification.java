package sma.tech.ma5doom.model;

public class Notification {
    private String name;
    private String clientName;
    private String date;

    public Notification(String name, String clientName, String date) {
        this.name = name;
        this.clientName = clientName;
        this.date = date;
    }

    public Notification(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
