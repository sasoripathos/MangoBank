package com.bank.message;

import java.io.Serializable;

public class MessageImpl implements Message, Serializable{

  private static final long serialVersionUID = 4617817361463122641L;
  /**
   * The content of the message.
   */
  private String content;
  /**
   * The message ID of the message.
   */
  private int messageId;
  /**
   * The ID of the user for whom this message is left.
   */
  private int userId;
  /**
   * The viewed state of the message.
   */
  private int viewedState;
  
  /**
   * Constructor for a message.
   * 
   * @param content the content of the message
   * @param messageId the message ID of the message
   * @param userId the ID of the user for whom this message is left
   * @param viewedState the viewed state of the message
   */
  public MessageImpl(String content, int messageId, int userId, int viewedState) {
    this.content = content;
    this.messageId = messageId;
    this.userId = userId;
    this.viewedState = viewedState;
  }
  
  @Override
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String getContent() {
    return this.content;
  }

  @Override
  public void setMessageId(int id) {
    this.messageId = id;
  }

  @Override
  public int getMessageId() {
    return this.messageId;
  }

  @Override
  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Override
  public int getUserId() {
    return this.userId;
  }

  @Override
  public void setViewedState(int state) {
    this.viewedState = state;
  }

  @Override
  public int getViewedState() {
    return this.viewedState;
  }

}
