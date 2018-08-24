package sma.tech.ma5doom.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ValidationUtils {

    private static final boolean isValid(String input) {
        boolean valid = true;
        if (input.trim().isEmpty()) {
            valid = false;
        }
        return valid;
    }

    public static final boolean checkError(EditText editText, String error_message) {
        if (!isValid(editText.getText().toString())) {
            editText.setError(error_message);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public static final boolean checkPassSize(EditText editText) {
        if (editText.getText().toString().length() <= 5) {
            editText.setError("password should be more 6 characters");
            return false;
        } else {
            return true;
        }

    }

    public static final boolean checkNamePref(EditText editText, String error_message) {
        if (editText.getText().toString().matches("^[a-zA-Z].*")) {
            return true;
        } else {
            editText.setError(error_message);
            return false;
        }
    }

    public static final boolean checkMatch(EditText pass, EditText confirm_pass) {
        boolean isMatch = false;
        if (pass.getText().toString().matches(confirm_pass.getText().toString())) {
            isMatch = true;
        } else {
            confirm_pass.setError("invalid confirmation");
        }
        return isMatch;
    }

    public static boolean isEmailValid(EditText email) {
        String emailString = email.getText().toString();
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailString);
        if (matcher.matches() == true) {
            return true;
        } else {
            email.setError("email formate isn't correct");
            return false;
        }
    }
}
