package sma.tech.ma5doom.networkController;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mohanad on 01/08/17.
 */

public class Validate {


    public static boolean checkRegisterUserDate(String name , String phone , String email , String password , Context context){

        // /check if email is isCorrect
        if (!Validate.name(name)){
            //show a info message
            Toast.makeText(context, "Please Insert Correct Email",Toast.LENGTH_LONG).show();
            return false;
        }

        //check if phone number is isCorrect
        if (!Validate.phone(phone)){
            //show a info message
            Toast.makeText(context, "Please Insert Correct Phone Number",Toast.LENGTH_LONG).show();
            return false;
        }

        //check if email is isCorrect
        if (!Validate.email(email)){
            //show a info message
            Toast.makeText(context, "Please Insert Correct Email",Toast.LENGTH_LONG).show();
            return false;
        }


        //check if email is isCorrect
        if (!Validate.password(password)){
            //show a info message
            Toast.makeText(context, "Please Insert a Password More Than 4 characters",Toast.LENGTH_LONG).show();
            return false;
        }

        return  true;

    }
    
    /**
     * email validation
     */
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean email(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }


    /**
     * validate phone number
     * @param phone
     * @return
     */
    public static boolean phone (String phone){
        return phone!=null&&phone.matches("01[012][0-9]+") && phone.length()==11;
    }

    /**
     * validate user name
     * @param name
     * @return
     */
    public static boolean name (String name){
        return name.matches("[a-zA-Z]+") && name.length()>3;
    }

    /**
     * validate user password
     * @param password
     * @return
     */
    public static boolean password(String password){
        return password.length()>=4;
    }

}
