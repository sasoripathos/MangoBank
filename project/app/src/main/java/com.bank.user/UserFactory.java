package com.bank.user;

import com.bank.generics.Roles;
import com.bank.validation.UserValidator;
import com.bank.validation.UserValidatorPc;

public class UserFactory {

  /**
   * return a new user using the given parameters if the parameters are valid,
   * return null otherwise.
   * @param role the role you want your new user to be.
   * @param id the id you want your new user to have.
   * @param name the name you want your new user to have.
   * @param age the age you want your new user to have.
   * @param address the address you want your new user to have.
   * @return a new user with the given specifications if they are valid, returns null otherwise.
   */
  public User getUser(Roles role, int id, String name, int age, String address) {
    // make a new instance of userValidator
    UserValidator usrValidator = new UserValidatorPc();
    // check the id, name, age, and address for validity
    usrValidator.checkName(name).checkAge(age).checkAddress(address);
    // if the parameters are valid, return the user based on their chosen role
    if (usrValidator.getResult()) {
      switch (role) {
        case ADMIN:
          return new Admin(id, name, age, address);
          
        case CUSTOMER:
          return new Customer(id, name, age, address);
          
        case TELLER:
          return new Teller(id, name, age, address);
          
        default:
          return null;
      }
    }
    // returns null if parameters are not valid
    return null;
  }
}
