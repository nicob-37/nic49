package com.nic49.bot.manager;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HexManager extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }


    public double calculateDeltaE(String hex1, String hex2) {

        Color hex1Color = Color.decode(hex1);
        Color hex2Color = Color.decode(hex2);

        float[] hex1Normalized = {
                hex1Color.getRed() / 255.0f,
                hex1Color.getGreen() / 255.0f,
                hex1Color.getBlue() / 255.0f
        };

        float[] hex2Normalized = {
                hex2Color.getRed() / 255.0f,
                hex2Color.getGreen() / 255.0f,
                hex2Color.getBlue() / 255.0f
        };

        //TODO: here
        return 0f;
    }
}
