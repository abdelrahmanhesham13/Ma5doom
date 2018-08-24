package sma.tech.ma5doom.utils;

import java.util.ArrayList;

import sma.tech.ma5doom.model.BankAccount;
import sma.tech.ma5doom.model.Notification;
import sma.tech.ma5doom.model.Reservation;
import sma.tech.ma5doom.model.Rest;

public class FakeData {


    public static ArrayList<Reservation> restFakeData(){
        ArrayList<Reservation> result= new ArrayList<>();
        Reservation r = new Reservation();
        r.setName("اسم الاستراحه");
        r.setClientName("مصطفي السيد علي");
        r.setDate("20 مارس 2018");
        r.setTime("12:05 PM");
        r.setDuration("20 يوم");
        r.setState(true);

        result.add(r);


        Reservation r2= new Reservation();
        r2.setName("اسم الاستراحه");
        r2.setClientName("مصطفي السيد علي");
        r2.setDate("20 مارس 2018");
        r2.setTime("12:05 PM");
        r2.setDuration("20 يوم");
        r2.setState(false);

        result.add(r2);

        return result;


    }



    public static ArrayList<Notification> notificationFakeData(){
        ArrayList<Notification> result= new ArrayList<>();
        Notification r = new Notification();
        r.setName("اسم الاستراحه");
        r.setClientName("مصطفي السيد علي");
        r.setDate("20 مارس 2018");

        result.add(r);


        Notification r2= new Notification();
        r2.setName("اسم الاستراحه");
        r2.setClientName("مصطفي السيد علي");
        r2.setDate("20 مارس 2018");

        result.add(r2);




        Notification r3= new Notification();
        r3.setName("اسم الاستراحه");
        r3.setClientName("مصطفي السيد علي");
        r3.setDate("20 مارس 2018");

        result.add(r3);


        return result;


    }


    public static ArrayList<Rest> restfakeData(){
        ArrayList<Rest> ans = new ArrayList<>();
        ans.add(new Rest("اسم الاستراحه هنا"));
        ans.add(new Rest("اسم الاستراحه هنا"));
        ans.add(new Rest("اسم الاستراحه هنا"));
        ans.add(new Rest("اسم الاستراحه هنا"));
        ans.add(new Rest("اسم الاستراحه هنا"));
        ans.add(new Rest("اسم الاستراحه هنا"));

        return ans;


    }



    public static ArrayList<BankAccount> bankFake(){
        ArrayList<BankAccount> ans = new ArrayList<>();
        ans.add(new BankAccount("بنك عوده", "مهند المغربي ","7864597864578964"));
        ans.add(new BankAccount("بنك عوده", "مهند المغربي ","7864597864578964"));
        ans.add(new BankAccount("بنك عوده", "مهند المغربي ","7864597864578964"));
        ans.add(new BankAccount("بنك عوده", "مهند المغربي ","7864597864578964"));
        ans.add(new BankAccount("بنك عوده", "مهند المغربي ","7864597864578964"));

        return ans;


    }


}
