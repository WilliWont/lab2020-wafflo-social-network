/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.util;

import java.io.Serializable;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;


public class AccountHelper implements Serializable{
    public static final int COOKIE_AGE = 10;
    
    public static final String PASSWORD_REGEX = "^[A-Za-z0-9]*$";
    public static final int PASSWORD_MIN_LEN = 8;
    public static final int PASSWORD_MAX_LEN = 16;
    
    public static final int NAME_MIN_LEN = 0;
    public static final int NAME_MAX_LEN = 64;
    
    public static final int ADMIN_ROLE = 0;
    public static final int MODERATOR_ROLE = 1;
    public static final int MEMBER_ROLE = 2;
    
    public static final int DEFAULT_ROLE = 2;
    
    // Uses Apache Common Codec 1.15
    public static String hashPassword(String password){
        return DigestUtils.sha256Hex(password);
    }
    
    public static boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }
}
