package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PokedexController;
import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.controller.TradeOfferController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.TradeOffer;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class NPCBattleCommand implements SlashCommandHandler {

    static final String NAME = "battle";
    // defining a class field to use in button interaction

    @Inject
    TradeOfferController tradeOfferController;
    @Inject
    PokemonController pokemonController;
    @Inject
    TrainerController trainerController;
    @Inject
    PokedexController pokedexController;

    @Inject
    public NPCBattleCommand() {
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
        return Commands.slash(getName(), "battle with a random NPC pokemon.").addOption(
                OptionType.STRING,
                "pokemon-name",
                "The pokemon you want to use for battle.",
                true);
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
            throws InvalidTeamPositionException, PokemonNotExistException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onSlashCommandInteraction'");
    }

}
