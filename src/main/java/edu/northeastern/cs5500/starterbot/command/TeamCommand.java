package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

@Slf4j
public class TeamCommand implements SlashCommandHandler {

    static final String NAME = "team";
    @Inject TrainerController trainerController;

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
                                                        "Position on the team (from 0 to 5)",
                                                        true)
                                                .setRequiredRange(0, 5)));
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
            throws InvalidTeamPositionException {
        log.info("event: /team");
        String trainerDiscordId = event.getMember().getId();
        switch (event.getSubcommandName()) {
            case "display":
                display(event, trainerDiscordId);
                break;
            case "add":
                add(event, trainerDiscordId);
                break;
            default:
                event.reply("I can't handle that command right now:(").setEphemeral(true).queue();
        }
    }

    public void display(@Nonnull SlashCommandInteractionEvent event, String trainerDiscordId) {
        EmbedBuilder eb = new EmbedBuilder();
        String username = event.getMember().getUser().getName();
        eb.setTitle(String.format("%s's Battle Team", username));

        // Trainer trainer = trainerController.getTrainerForMemberId(trainerDiscordId);
        // ObjectId[] team = trainer.getTeam();
        // int index = 0;
        // while (index <= 5 || team[index] != null){
        //     eb.addField(String.format("[%d]", index), "objectId to name", false);
        // }

        eb.addField("[1]", "Bulbassaur", false);
        eb.addField("[2]", "Quaxly", false);
        eb.addField("[3]", "Pikachu", false);
        eb.addField("[4]", "Pika", false);
        MessageCreateBuilder mcb = new MessageCreateBuilder();
        mcb = mcb.addEmbeds(eb.build());
        event.reply(mcb.build()).queue();
    }

    public void add(@Nonnull SlashCommandInteractionEvent event, String trainerDiscordId)
            throws InvalidTeamPositionException {
        String pokemon = event.getOption("pokemon").getAsString();
        Integer position = event.getOption("position").getAsInt();
        // trainerController.formTeam(trainerDiscordId, pokemon, position);
        event.reply(
                        String.format(
                                "Player <@%s> adds %s at position %s",
                                trainerDiscordId, pokemon, String.valueOf(position)))
                .queue();
    }
}
