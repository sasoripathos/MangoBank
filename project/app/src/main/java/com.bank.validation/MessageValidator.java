package com.bank.validation;

public interface MessageValidator {
  /**
   * Given a message content, check whether the content is valid (not NULL, not empty and maximum
   * length 512).
   * @param content a String stands for a message content
   * @return the current MessageValidator instance
   */
  public MessageValidator checkContent(String content);
  
  /**
   * Return the final result after a series of check.
   * 
   * @return a boolean stands for whether all checks have passed
   */
  public boolean getResult();
}
