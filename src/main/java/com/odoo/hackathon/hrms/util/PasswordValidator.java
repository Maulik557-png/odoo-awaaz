package com.odoo.hackathon.hrms.util;

public class PasswordValidator {

    public static boolean isValid(String password) {
        if (password == null) return false;
        return password.length() >= 8;
    }
}
