package com.bank.message;

public interface Message {
  /**
   * Set the content of the message.
   * 
   * @param content a String which stands for the content of a message
   */
  public void setContent(String content);
  
  /**
   * Get the content of the message.
   * @return a String which stands for the content of a message
   */
  public String getContent();
  
  /**
   * Set the message ID of the message.
   * 
   * @param id an integer which stands for the message ID of the message
   */
  public void setMessageId(int id);
  
  /**
   * Get the message ID of the message.
   * 
   * @return an integer which stands for the message ID of the message
   */
  public int getMessageId();
  
  /**
   * Set the ID of the user for whom the message is left.
   * 
   * @param userId an integer which stands for the ID of the user the message is left for
   */
  public void setUserId(int userId);
  
  /**
   * Get the ID of the user for whom the message is left.
   * 
   * @return an integer which stands for the ID of the user the message is left for
   */
  public int getUserId();
  
  /**
   * Set the viewed state of the message (1 means read, 0 means not read).
   * 
   * @param state an integer which stands for the viewed state of the message
   */
  public void setViewedState(int state);

  /**
   * Get the viewed state of the message.
   * 
   * @return an integer which stands for the viewed state of the message
   */
  public int getViewedState();

}
