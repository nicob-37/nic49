package com.nic49.bot.manager;

import com.nic49.bot.ID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommandsManager extends ListenerAdapter {
    public List<SlashCommandEx> commands = new ArrayList<>();
    boolean commandsEnabled = true;
    boolean pushingGlobal = false;
    List<RichCustomEmoji> snEmojis = new ArrayList<>();

    public void onReady(@NotNull net.dv8tion.jda.api.events.session.ReadyEvent event) {
        initCommands(event);
    }

    public static class SlashCommandEx {
        String name, description;
        List<String> authorizedUsers = new ArrayList<>();
        SlashCommandData data;

        public SlashCommandEx(String name, String description) {
            this(name, description, new String[0]);
        }

        public SlashCommandEx(String name, String description, String... allowedUsers) {
            this.name = name;
            this.description = description;

            if (allowedUsers != null) {
                this.authorizedUsers.addAll(java.util.Arrays.asList(allowedUsers));
            }

            this.data = Commands.slash(this.name, this.description)
                    .setContexts(
                            InteractionContextType.GUILD,
                            InteractionContextType.BOT_DM,
                            InteractionContextType.PRIVATE_CHANNEL)
                    .setIntegrationTypes(
                            IntegrationType.GUILD_INSTALL,
                            IntegrationType.USER_INSTALL);
        }

        public SlashCommandEx addOption(OptionType type, String name, String desc, boolean required) {
            this.data.addOption(type, name, desc, required);
            return this;
        }

        public SlashCommandEx addOptions(OptionData... options) {
            this.data.addOptions(options);
            return this;
        }
    }

    public void initCommands(@NotNull net.dv8tion.jda.api.events.session.ReadyEvent event) {
        commands.clear();

        var guild = event.getJDA().getGuildById(ID.SIG_NATION);

        // ------------------------Commands----------------------------
        commands.add(new SlashCommandEx("ping", "Pong"));

        // ADMIN COMMANDS
        commands.add(new SlashCommandEx("update", "Restarts the bot", ID.NICO));
        commands.add(new SlashCommandEx("stop", "Stops the bot", ID.NICO));
        commands.add(new SlashCommandEx("adminpanel", "View Current Bot Settings", ID.NICO));

        // ADMIN MANUAL COMMANDS
        commands.add(new SlashCommandEx("message", "Send a message as the bot", ID.NICO)
                .addOption(OptionType.STRING, "message", "Message to send", true));

        // HYPIXEL COMMANDS
        OptionData pieceOption = new OptionData(OptionType.STRING, "piece", "The type of armor", true)
                .addChoice("Helmet", "helmet")
                .addChoice("Chestplate", "chestplate")
                .addChoice("Leggings", "leggings")
                .addChoice("Boots", "boots");
        OptionData hexCode = new OptionData(OptionType.STRING, "hex", "Hex Code", true);
        commands.add(new SlashCommandEx("armor", "Generate a piece of armor with a hex code")
                .addOptions(pieceOption, hexCode)
                .addOption(OptionType.BOOLEAN, "prism", "Prism Texture by looshy", false));

        // ALL COMMANDS
        commands.add(new SlashCommandEx("makepost", "Make new post")
                .addOption(OptionType.STRING, "title", "Title of your post", true)
                .addOption(OptionType.STRING, "body", "Body of your post", true)
                .addOption(OptionType.ATTACHMENT, "attachment", "ONLY IMAGES SUPPORTED FOR NOW", false));


        // ------------------------------------------------------------
        List<SlashCommandData> jdaData = new ArrayList<>();
        for (SlashCommandEx ex : commands) { jdaData.add(ex.data); }

        if (guild != null) {
            guild.updateCommands().addCommands(jdaData).queue();
            System.out.println("Custom Commands Synced in Sigma Nation");
        }

        if (pushingGlobal) {
            event.getJDA().updateCommands().addCommands(jdaData).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        SlashCommandEx commandEx = commands.stream()
                .filter(cmd -> cmd.name.equalsIgnoreCase(event.getName()))
                .findFirst()
                .orElse(null);

        if (commandEx == null) return;

        if (!commandEx.authorizedUsers.isEmpty()) {
            if (!commandEx.authorizedUsers.contains(event.getUser().getId())) {
                event.reply("Nice try, " + event.getUser().getEffectiveName() + ". You aren't authorized to use this.")
                        .setEphemeral(true)
                        .queue();
                return;
            }
        }

        if (event.getUser().getId().equals(ID.NICO) || commandsEnabled) {

            switch (event.getName().toLowerCase()) {

                case "ping" -> {
                    event.reply("Pong! (" + event.getJDA().getGatewayPing() + "ms)").queue();
                }

                case "update" -> {
                    event.reply("Restarting and checking for update...").queue(success -> {
                        try {
                            ProcessBuilder pb = new ProcessBuilder("setsid", "sh", "/home/ubuntu/nic49/update_bot.sh");
                            pb.start();
                            Thread.sleep(1000);
                            event.getJDA().shutdown();
                            System.exit(0);
                        } catch (Exception e) {
                            event.getChannel().sendMessage("Critical error: " + e.getMessage()).queue();
                        }
                    });
                }

                case "stop" -> {
                    event.reply("Shutting off bot").queue();
                    event.getJDA().shutdown();
                    System.exit(0);
                }

                case "adminpanel" -> {
                    var embed = new EmbedBuilder();

                    embed.setTitle("Admin Panel")
                            .setDescription("Bot Operating Fine")
                            .setColor(Color.DARK_GRAY)
                            .addField("Commands Enabled?", commandsEnabled ? "True" : "False", true)
                            .setFooter("Requested by: " + event.getUser().getEffectiveName())
                            .setTimestamp(java.time.Instant.now());


                    event.replyEmbeds(embed.build())
                            .addActionRow(
                                    Button.primary("ap_togglecommands", "Toggle Commands"), // Blurple button
                                    Button.danger("ap_update", "Update Bot")        // Red button
                            )
                            .setEphemeral(true)
                            .queue();
                }

                case "makepost" -> {
                    var embed = new EmbedBuilder()
                            .setTitle(event.getOption("title").getAsString())
                            .setDescription(event.getOption("body").getAsString())
                            .setFooter("Post by " + event.getUser().getEffectiveName())
                            .setTimestamp(java.time.Instant.now())
                            .setColor(Color.decode("#FF5700"));

                    var attachmentOption = event.getOption("attachment");

                    if (attachmentOption != null) {
                        var postImage = attachmentOption.getAsAttachment();

                        if (postImage.isImage()) {
                            embed.setImage(postImage.getProxyUrl());
                        }
                    }

                    event.replyEmbeds(embed.build()).queue(hook -> hook.retrieveOriginal().queue(message -> {
                        message.addReaction(Emoji.fromCustom("updoot", 1474560551773536366L, false)).queue();
                        message.addReaction(Emoji.fromCustom("downdoot", 1474560608346312936L, false)).queue();

                        if (attachmentOption != null) {
                            if (!attachmentOption.getAsAttachment().isImage()) {
                            hook.sendMessage("You tried to attach something that is not an image (NOT ALLOWED!!!!)").setEphemeral(true).queue();
                            }
                        }

                    }));
                }

                case "message" -> {
                    try {
                        var message = event.getOption("message").getAsString();

                        event.getChannel().sendMessage(message).queue();
                    }
                    catch (Exception e) {e.printStackTrace();}
                }

                case "armor" -> {
                    var pieceOpt = event.getOption("piece");
                    var hexOpt = event.getOption("hex");
                    boolean prism = event.getOption("prism") != null && event.getOption("prism").getAsBoolean();
                    if (pieceOpt == null || hexOpt == null) return;

                    String piece = pieceOpt.getAsString();
                    String hex = hexOpt.getAsString().replace("#", "");
                    event.deferReply().queue();

                    try {
                        String urlString = prism ? "https://nico-armor-api.vercel.app/api/prism/" + piece + "/" + hex : "https://nico-armor-api.vercel.app/api/" + piece + "/" + hex;
                        URL url = new URI(urlString).toURL();
                        try (InputStream in = url.openStream()) {
                            byte[] imageBytes = in.readAllBytes();
                            FileUpload file = FileUpload.fromData(imageBytes, "armor.png");
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle("Dye Result: " + piece.substring(0, 1).toUpperCase() + piece.substring(1))
                                    .setColor(Color.decode("0x" + hex))
                                    .setImage("attachment://armor.png")
                                    .setFooter("Hex: #" + hex);

                            event.getHook().sendMessageEmbeds(embed.build()).addFiles(file).queue();
                        }
                    } catch (Exception e) {
                        event.getHook().sendMessage("Failed to generate armor: " + e.getMessage()).setEphemeral(true).queue();
                    }
                }
            }

        }
        else {
            event.reply("Commands are currently disabled.").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        switch (event.getComponentId()) {

            // Admin Panel
            case "ap_togglecommands" -> {

            }

            case "ap_update" -> {
                event.reply("Restarting and checking for update...").queue(success -> {
                    try {
                        ProcessBuilder pb = new ProcessBuilder("setsid", "sh", "/home/ubuntu/nic49/update_bot.sh");
                        pb.start();
                        Thread.sleep(1000);
                        event.getJDA().shutdown();
                        System.exit(0);
                    } catch (Exception e) {
                        event.getChannel().sendMessage("Critical error: " + e.getMessage()).queue();
                    }
                });
            }

        }
    }
}
