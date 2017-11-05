package com.bank.validation;

public class MessageValidatorPc implements MessageValidator {
  /**
   * The boolean which stands for whether all checks have passed, default true.
   */
  private boolean result;
  
  /**
   * Constructor.
   */
  public MessageValidatorPc() {
    //Set this.result to default value (true)
    this.result = true;
  }
  
  @Override
  public MessageValidator checkContent(String content) {
    //if the given content is NULL, empty string or its length greater than 512, this check fails
    if (content == null || content.equals("") || content.length() > 512) {
      this.result = false;
    }
    return this;
  }

  @Override
  public boolean getResult() {
    return this.result;
  }

}
