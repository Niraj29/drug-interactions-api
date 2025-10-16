package com.example.app.dto;

public class ReactionCount {
  private String reaction;
  private int count;

  public ReactionCount() {}

  public ReactionCount(String reaction, int count) {
    this.reaction = reaction;
    this.count = count;
  }

  public String getReaction() {
    return reaction;
  }

  public void setReaction(String reaction) {
    this.reaction = reaction;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
