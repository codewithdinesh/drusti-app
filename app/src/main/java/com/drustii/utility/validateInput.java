package com.drustii.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validateInput {


    // Validate Email using Regular Expression..
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public  boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    
    // validate password
    public static final Pattern VALID_PASSWORD_REGEX=
            Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}");
    
    public boolean validatePassword(String passStr){
        Matcher matchpass=VALID_PASSWORD_REGEX.matcher(passStr);
        return matchpass.find();
    }

    //validate OTP
    String regex = "\\d{6}";
    Pattern VALID_OTP_REGEX = Pattern.compile(regex);

    public boolean validateOTP(String otp) {
        Matcher mObj = VALID_OTP_REGEX.matcher(otp);
        return mObj.find();
    }

    //validate username
    Pattern VALID_USERNAME_REGEX = Pattern.compile("^(?=.{1,27}$)(?![0-9_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    public  boolean validateUsername(String username){

        Matcher mObj=VALID_USERNAME_REGEX.matcher(username.toLowerCase().trim());
        return mObj.find();
    }

    // validate name
    public boolean validInput(String name) {
        if (name.trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
