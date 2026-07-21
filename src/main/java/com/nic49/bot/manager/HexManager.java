package com.nic49.bot.manager;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HexManager extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }


    /*public double calculateDeltaE(String hex1, String hex2) {
        double rLinear1, gLinear1, bLinear1, rLinear2, gLinear2, bLinear2;

        if (hex1.length() != 6 || hex2.length() != 6) {}
        else {
            rLinear1 = (double) Color.decode(hex1).getRed() / 255;
            gLinear1 = (double) Color.decode(hex1).getGreen() / 255;
            bLinear1 = (double) Color.decode(hex1).getBlue() / 255;
            rLinear2 = (double) Color.decode(hex2).getRed() / 255;
            gLinear2 = (double) Color.decode(hex2).getGreen() / 255;
            bLinear2 = (double) Color.decode(hex2).getBlue() / 255;



        }
    }*/
}
