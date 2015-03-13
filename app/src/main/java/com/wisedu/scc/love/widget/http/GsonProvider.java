package com.wisedu.scc.love.widget.http;


import com.google.gson.Gson;

public final class GsonProvider {
  private static final Gson gson = new Gson();

  public static Gson getInstance() {
    return gson;
  }

  private GsonProvider() {
    // No instances.
  }
}