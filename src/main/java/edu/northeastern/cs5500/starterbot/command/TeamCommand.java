package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PokedexController;
import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import java.util.Objects;
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
import org.bson.types.ObjectId;

@Slf4j
public class TeamCommand implements SlashCommandHandler {

    static final String NAME = "team";
    @Inject PokemonController pokemonController;

    @Inject PokedexController pokedexController;

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
                                                        "Position on the team (from 1 to 6)",
                                                        true)
                                                .setRequiredRange(1, 6)));
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
            throws InvalidTeamPositionException, PokemonNotExistException {
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

        Trainer trainer = trainerController.getTrainerForMemberId(trainerDiscordId);
        int index = 0;
        eb.setThumbnail(event.getMember().getAvatarUrl());
        for (ObjectId pokemonId : trainer.getTeam()) {
            Pokemon pokemon = pokemonController.getPokemonById(pokemonId);
            PokemonSpecies species =
                    pokedexController.getPokemonSpeciesByNumber(pokemon.getPokedexNumber());
            index++;
            eb.addField(
                    String.format("[%d]", index), String.format("%s", species.getName()), false);
        }
        MessageCreateBuilder mcb = new MessageCreateBuilder();
        mcb = mcb.addEmbeds(eb.build());
        event.reply(mcb.build()).queue();
    }

    public void add(@Nonnull SlashCommandInteractionEvent event, String trainerDiscordId)
            throws InvalidTeamPositionException, PokemonNotExistException {
        String pokemon = event.getOption("pokemon").getAsString();
        Integer position = event.getOption("position").getAsInt();

        try {
            trainerController.formTeam(trainerDiscordId, pokemon, position);
        } catch (InvalidTeamPositionException e) {
            event.reply("Cannot add Pokemon to a position that has an empty postion before it.")
                    .queue();
        } catch (PokemonNotExistException e) {
            event.reply("Pokemon does not exist.").queue();
        }
        event.reply(
                        Objects.requireNonNull(
                                String.format(
                                        "Player <@%s> adds %s at position %d",
                                        trainerDiscordId, pokemon, position)))
                .queue();
    }
}
