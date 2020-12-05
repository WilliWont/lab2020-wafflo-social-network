/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tiennh.usertable;

import java.io.Serializable;


public class UserTableRegisterError implements Serializable{
    private String registrationError;
    
    private String emailError;

    private String passwordError;
    private String passwordConfError;
    
    private String firstNameError;
    private String lastNameError;

    public String getRegistrationError() {
        return registrationError;
    }

    public void setRegistrationError(String registrationError) {
        this.registrationError = registrationError;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordConfError() {
        return passwordConfError;
    }

    public void setPasswordConfError(String passwordConfError) {
        this.passwordConfError = passwordConfError;
    }

    public String getFirstNameError() {
        return firstNameError;
    }

    public void setFirstNameError(String firstNameError) {
        this.firstNameError = firstNameError;
    }

    public String getLastNameError() {
        return lastNameError;
    }

    public void setLastNameError(String lastNameError) {
        this.lastNameError = lastNameError;
    }

    
}
