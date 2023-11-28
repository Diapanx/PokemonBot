package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Slf4j
public class TeamCommand implements SlashCommandHandler {

    static final String NAME = "team";

    @Inject
    public TeamCommand() {
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
        return Commands.slash(getName(), "player's battle team")
                .addSubcommands(
                        new SubcommandData("display", "Display your team"),
                        new SubcommandData("add", "Add a Pokemon to your team")
                                .addOption(
                                        OptionType.STRING, "pokemon", "Name of the Pokemon", true)
                                .addOptions(
                                        new OptionData(
                                                        OptionType.INTEGER,
                                                        "position",
                                                        "Position on the team (from 1 to 6)",
                                                        true)
                                                .setRequiredRange(1, 6)));
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /team");
        switch (event.getSubcommandName()) {
            case "display":
                display(event);
                break;
            case "add":
                add(event);
                break;
            default:
                event.reply("I can't handle that command right now:(").setEphemeral(true).queue();
        }
    }

    public void display(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("displaying").queue();
    }

    public void add(@Nonnull SlashCommandInteractionEvent event) {
        event.reply("adding").queue();
    }
}
