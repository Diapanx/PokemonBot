package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Slf4j
public class HelpCommand implements SlashCommandHandler {
    static final String NAME = "help";

    @Inject
    public HelpCommand() {
        // Defined public and empty for Dagger injection
    }

    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Displays help information about the bot's commands");
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /help");

        String helpMessage =
                "Here are the commands you can use:\n"
                        + "/say - Ask the bot to reply with the provided text.\n"
                        + "/help - Displays a list of available commands and their descriptions.\n"
                        + "/spawn - Meet a random wild Pokemon. You can try to catch the Pokemon.\n"
                        + "/inventory - Shows a list of Pokemon you currently have.\n"
                        + "/team - Provides information about your currnt Pokemon team.\n"
                        + "/trade - Initiates a trade process with an NPC, allowing you to exchange Pokemon.\n"
                        + "/preferredname - Set or change your preferred nickname or title that the bot will use for you.";

        event.reply(helpMessage).queue();
    }
}
