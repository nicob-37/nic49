package com.nic49.bot.manager;

import com.nic49.bot.ID;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuildManager extends ListenerAdapter {
    private static GuildManager instance;
    private static List<RichCustomEmoji> sigmaNationEmojis = new ArrayList<>();

    public GuildManager() {
        instance = this;
    }

    public static GuildManager getInstance() {
        return instance;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        var guild = event.getJDA().getGuildById(ID.SIG_NATION);

        if (guild != null) {
            guild.retrieveEmojis().queue(emojis -> {
                sigmaNationEmojis = emojis;
                System.out.println("Successfully retrieved " + emojis.size() + " emojis for Sigma Nation.");
            });
        }
    }

    public List<RichCustomEmoji> getEmojiList() {
        return sigmaNationEmojis;
    }
}
