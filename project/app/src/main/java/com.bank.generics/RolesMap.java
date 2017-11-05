package com.bank.generics;

import com.bank.databasehelper.DatabaseDriverAndroidHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RolesMap {
  private static RolesMap singleton = new RolesMap();
  private Map<Roles, Integer> map = new HashMap<Roles, Integer>();
  private Map<Integer, Roles> inverseMap = new HashMap<Integer, Roles>();
  
  private RolesMap() {
    update();
  }

  public static RolesMap getInstance() {
    return singleton;
  }
  
  /**
   * updates the enumMap with values on the database.
   * @return returns true if all values are mapped correctly, returns false otherwise.
   */
  public boolean update() {
    // Instantiate new maps to hold the new key/values
    Map<Roles, Integer> newMap = new HashMap<Roles, Integer>();
    Map<Integer, Roles> newInverseMap = new HashMap<Integer, Roles>();
    // instantiate String to hold current role name
    String roleName;
    
    // get id of all roles from the database
    //List<Integer> roleIds = DatabaseSelectHelper.getRoles();
    List<Integer> roleIds = DatabaseDriverAndroidHelper.getInstance().getAllRoles();
    // for each role id, find its enum counterpart and map that enum to the id.
    // if any roleName from the database is not ADMIN, CUSTOMER, or TELLER, return false.
    for (Integer id: roleIds) {
      //roleName = DatabaseSelectHelper.getRole(id);
      roleName = DatabaseDriverAndroidHelper.getInstance().getRole(id);
      if (roleName.equalsIgnoreCase("ADMIN")) {
        newMap.put(Roles.ADMIN, id);
        newInverseMap.put(id, Roles.ADMIN);
      } else if (roleName.equalsIgnoreCase("CUSTOMER")) {
        newMap.put(Roles.CUSTOMER, id);
        newInverseMap.put(id, Roles.CUSTOMER);
      } else if (roleName.equalsIgnoreCase("TELLER")) {
        newMap.put(Roles.TELLER, id);
        newInverseMap.put(id, Roles.TELLER);
      } else {
        return false;
      }
    }
    // if everything is mapped successfully, set map to the newMap and return true;
    map = newMap;
    inverseMap = newInverseMap;
    return true;
  }

  public int getId(Roles role) {
    return map.get(role);
  }
  
  public Roles getRole(int id) {
    return inverseMap.get(id);
  }

}
