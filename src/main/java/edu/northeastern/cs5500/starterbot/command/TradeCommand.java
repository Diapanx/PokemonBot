package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PokedexController;
import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.controller.TradeOfferController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.TradeOffer;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.types.ObjectId;

@Slf4j
public class TradeCommand implements SlashCommandHandler {

    static final String NAME = "trade";

    @Inject
    TradeOfferController tradeOfferController;
    @Inject
    PokemonController pokemonController;
    @Inject
    TrainerController trainerController;
    @Inject
    PokedexController pokedexController;

    @Inject
    public TradeCommand() {
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
        return Commands.slash(getName(), "perform trading actions between users")
                .addSubcommands(
                        new SubcommandData("new", "Create a new trade")
                                .addOption(
                                        OptionType.STRING,
                                        "pokemon-name",
                                        "The pokemon user wants to trade.",
                                        true),
                        (new SubcommandData("list", "View all the open trades.")
                                .addOption(
                                        OptionType.USER,
                                        "user",
                                        "View user's open trades.(Enter their @mentions)",
                                        true)));
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
            throws PokemonNotExistException {
        log.info("event: /trade");
        String trainerDiscordId = event.getMember().getId();
        Trainer trainer = trainerController.getTrainerForMemberId(trainerDiscordId);

        switch (event.getSubcommandName()) {
            case "new":
                // Objects.requireNonNull(trainer.getPokemonInventory());
                String pokemonName = event.getOption("pokemon-name").getAsString();
                ObjectId pokemonId = getPokemonFromInventory(trainer, pokemonName);
                if (pokemonId == null) {
                    event.reply(
                            "You do not own the pokemon.\nPlease enter a valid pokemon for trade.")
                            .queue();
                    break;
                }
                tradeOfferController.createNewOffering(
                        trainer, pokemonController.getPokemonById(pokemonId));

                event.reply(
                        String.format(
                                "Player <@%s> has listed %s for trade.",
                                trainerDiscordId, pokemonName))
                        .queue();
                break;
            case "list":
                String mentionedUserId = event.getOption("user").getAsMember().getId();
                Trainer mentionedTrainer = trainerController.getTrainerForMemberId(trainerDiscordId);

                StringBuilder avaliableTrades = new StringBuilder();
                avaliableTrades.append(String.format("Player <@%s>'s Open Trades:\n", trainerDiscordId));
                for (TradeOffer openTrade : tradeOfferController.getOpenTradesByTrainer(mentionedTrainer)) {
                    Pokemon pokemonForTrade = pokemonController.getPokemonById(openTrade.getPokemonId());
                    avaliableTrades
                            .append(
                                    "     " + pokedexController.getPokemonSpeciesByNumber(
                                            pokemonForTrade.getPokedexNumber()).getName())
                            .append("\n");
                }
                event.reply(avaliableTrades.toString()).setEphemeral(true).queue();

                break;
            default:
                event.reply("Invalid subcommand").setEphemeral(true).queue();
        }
    }

    // @Override
    // public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
    // }

    private ObjectId getPokemonFromInventory(
            @Nonnull Trainer trainer, @Nonnull String pokemonName) {
        List<ObjectId> trainerInventory = trainer.getPokemonInventory();
        PokemonSpecies species = pokedexController.getSpeciesByName(pokemonName);
        ObjectId resultPokemonId = null;
        for (ObjectId pokemonId : trainerInventory) {
            if (pokemonController
                    .getPokemonById(pokemonId)
                    .getPokedexNumber()
                    .equals(species.getPokedexNumber())) {
                resultPokemonId = pokemonId;
                break;
            }
        }
        return resultPokemonId;
    }
}
