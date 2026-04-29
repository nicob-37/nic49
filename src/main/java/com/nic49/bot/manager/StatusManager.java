package com.nic49.bot.manager;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class StatusManager extends ListenerAdapter {
    Random r = new Random();
    private String[] statuses = {
            "Default Status Here",
            "New and improved",
            "Better than nic7"
    };

    public void randomizeStatus(Event event) {
        event.getJDA().getPresence().setActivity(Activity.customStatus(statuses[r.nextInt(0, statuses.length)]));
    }

    @Override
    public void onReady(ReadyEvent event) {
        randomizeStatus(event);
    }
}
