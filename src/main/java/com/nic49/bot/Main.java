package com.nic49.bot;

import com.nic49.bot.manager.CommandsManager;
import com.nic49.bot.manager.GuildManager;
import com.nic49.bot.manager.StatusManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.EnumSet;

public class Main {
    public static void main(String args[]) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        if (token == null || token.isEmpty()) {
            System.err.println("!!! Check .env file Discord Token is missing !!!");
            return;
        }

        JDABuilder builder = JDABuilder.createDefault(token);

        builder.enableIntents(EnumSet.allOf(GatewayIntent.class));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE);

        GuildManager guildManager = new GuildManager();
        CommandsManager commandsManager = new CommandsManager();
        StatusManager statusManager = new StatusManager();



        builder.addEventListeners(
                commandsManager,
                statusManager,
                guildManager
        );

        JDA bot = builder.build();
    }

}
