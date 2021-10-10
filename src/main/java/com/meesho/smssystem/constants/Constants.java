package com.meesho.smssystem.constants;

import org.springframework.beans.factory.annotation.Value;

public class Constants {
    // This will prevent class instantiation
    private Constants () { }

    //Entity based enums
    public enum FailureCode{
        API_ERROR,
        NUMBER_BLACKLISTED
    }
    public enum MsgStatus{
        IN_QUEUE,
        SENT,
        FAIL
    }

    public static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String regexpForPhoneNumber = "[^A-Za-z]+";
    public static final String authenticationErrorMessage = "Token Invalid or Not Present in the Header";


}
