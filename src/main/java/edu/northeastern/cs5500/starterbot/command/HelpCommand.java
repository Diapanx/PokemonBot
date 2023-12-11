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
                        + "/team - Provides information about your current Pokemon team. Subcommands:\n"
                        + "    /team display - Display your current team.\n"
                        + "    /team add [pokemon] [position] - Add a Pokemon to your team at a specified position.\n"
                        + "/trade - Initiates a trade process with other players, allowing you to exchange Pokemon. Subcommands:\n"
                        + "    /trade new [pokemon-name] - Create a new trade offer with a specific Pokemon.\n"
                        + "    /trade view [user] - View all the open trades of a specific user.\n"
                        + "    /trade with [user] [your-pokemon] [their-pokemon] - Initiate a trade with a player for specific Pokemon.\n"
                        + "/preferredname - Set or change your preferred nickname or title that the bot will use for you.";

        event.reply(helpMessage).queue();
    }
}
