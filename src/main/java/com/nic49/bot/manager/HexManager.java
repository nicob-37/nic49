package com.nic49.bot.manager;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HexManager extends ListenerAdapter {

    public class ColorComparison {
        int abs;
        double cie76, cie2000;

        public ColorComparison(String hex1, String hex2) {
            this.cie76 = HexManager.calculateDeltaE76(hex1, hex2);
            this.abs = HexManager.calculateABS(hex1, hex2);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }

    public static int calculateABS(String hex1, String hex2) {
        Color hex1Color = Color.decode(hex1);
        Color hex2Color = Color.decode(hex2);

        return Math.abs(hex1Color.getRed() - hex2Color.getRed()) + Math.abs(hex1Color.getGreen() - hex2Color.getGreen()) + Math.abs(hex1Color.getBlue() - hex2Color.getBlue());

    }

    public static double calculateDeltaE76(String hex1, String hex2) {

        float[] hex1Normalized = {
                Color.decode(hex1).getRed() / 255.0f,
                Color.decode(hex1).getGreen() / 255.0f,
                Color.decode(hex1).getBlue() / 255.0f
        };

        float[] hex2Normalized = {
                Color.decode(hex2).getRed() / 255.0f,
                Color.decode(hex2).getGreen() / 255.0f,
                Color.decode(hex2).getBlue() / 255.0f
        };

        //TODO: here
        return 0f;
    }
}
