package com.nic49.bot.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TrackingManager extends ListenerAdapter {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void getFromJson(String name) {
        gson.fromJson(name, String.class);
    }
}
