package sma.tech.ma5doom.model;

public class Reservation {
    
    private String name;
    private String clientName;
    private String date;
    private String time;
    private String duration;
    private boolean state;


    public Reservation(String name, String clientName, String date, String time, String duration, boolean state) {
        this.name = name;
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.state = state;
    }

    public Reservation() {

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
