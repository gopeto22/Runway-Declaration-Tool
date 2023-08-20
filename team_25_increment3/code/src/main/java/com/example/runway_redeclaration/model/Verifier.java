package com.example.runway_redeclaration.model;

import java.util.List;
import javafx.util.Pair; // This isn't a UI element, it's just a data structure provided by javafx

/**
 * Class to verify inputs.
 */
public class Verifier {

  /**
   * Verify all fields, return the first one that fails
   * @param fields
   * @return
   */
  public Pair<String,Boolean> verifyAllIntFormat(List<Pair<String,String>> fields) {
    for (Pair<String,String> field:fields) {
      var errorValuePair = verifyIntFormat(field);
      if (!errorValuePair.getValue()) {
        return new Pair<>(errorValuePair.getKey(),false);
      }
    }
    return new Pair<>("",true);
  }

  /**
   * Check that the input is in a valid format, otherwise show an error message.
   *
   * @param field fieldName,input pair
   * @return a corresponding error message + true if input has valid format, false otherwise
   */
  public Pair<String,Boolean> verifyIntFormat(Pair<String,String>field) {
    String errorMessage = "Invalid input: ";

    if (field.getValue().trim().isEmpty()) {
      errorMessage = errorMessage + field.getKey() + " value is empty.";
    } else if (!isInteger(field.getValue())) {
      errorMessage = errorMessage + field.getKey() + " value is not a valid integer.";
    } else {
      return new Pair<>("",true);
    }

    return new Pair<>(errorMessage,false);
  }

  /**
   * Check if the input is a valid integer.
   *
   * @param s string to check
   * @return true if valid integer, false otherwise
   */
  private Boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
