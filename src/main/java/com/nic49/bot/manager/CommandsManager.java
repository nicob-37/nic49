package com.nic49.bot.manager;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommandsManager extends ListenerAdapter {
    public List<SlashCommandEx> commands = new ArrayList<>();

    public static class SlashCommandEx {

    }
}
