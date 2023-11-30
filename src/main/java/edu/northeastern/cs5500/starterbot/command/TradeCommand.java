package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PokedexController;
import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.controller.TradeOfferController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.TradeOffer;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.bson.types.ObjectId;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
// import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import java.util.ArrayList;
import java.util.List;

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
                        new SubcommandData("new", "Create a new trade"),
                        new SubcommandData("offer", "View all offers to user's trade"));
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
            throws PokemonNotExistException {
        log.info("event: /trade");
        String trainerDiscordId = event.getMember().getId();
        Trainer trainer = trainerController.getTrainerForMemberId(trainerDiscordId);
        switch (event.getSubcommandName()) {
            case "new":
                if (trainer.getPokemonInventory().isEmpty()) {
                    event.reply("You don't have any pokemons avaliable for trade.").queue();
                    break;
                }
                selectPokemonForTrade(event, trainer);
                Pokemon pokemon = pokemonController.spawnRandomPokemon();
                TradeOffer tradeOffer = tradeOfferController.createNewOffering(
                        trainerController.getTrainerForMemberId(trainerDiscordId), pokemon);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                String trainerName = event.getMember().getUser().getName();
                Pokemon tradePokemon = pokemonController.getPokemonById(tradeOffer.getPokemonId());
                String pokemonName = pokedexController
                        .getPokemonSpeciesByNumber(tradePokemon.getPokedexNumber())
                        .getName();
                embedBuilder.setTitle(
                        String.format("%s has offered %s for trade!", trainerName, pokemonName));
                embedBuilder.addField("Level", Integer.toString(tradePokemon.getLevel()), false);
                MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
                embedBuilder.setThumbnail(
                        pokedexController
                                .getPokemonSpeciesByNumber(tradePokemon.getPokedexNumber())
                                .getImageUrl());
                messageCreateBuilder = messageCreateBuilder.addActionRow(
                        Button.primary(getName() + ":trade:", "Trade"));
                messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
                event.reply(messageCreateBuilder.build()).queue();
                break;
            case "offer":
                event.reply("Not yet implemented.").queue();
                break;
            default:
                event.reply("Invalid subcommand").setEphemeral(true).queue();
        }
    }

    private void selectPokemonForTrade(@Nonnull SlashCommandInteractionEvent event, Trainer trainer) {
        List<ObjectId> trainerPokemonIds = trainer.getPokemonInventory();
        List<Pokemon> trainerPokemons = new ArrayList<>();
        for (ObjectId pokemonId : trainerPokemonIds) {
            trainerPokemons.add(pokemonController.getPokemonById(pokemonId));
        }
        List<SelectOption> selectOptions = new ArrayList<>();
        for (Pokemon pokemon : trainerPokemons) {
            selectOptions.add(
                    SelectOption.of(pokedexController.getPokemonSpeciesByNumber(pokemon.getPokedexNumber()).getName(),
                            Integer.toString(pokemon.getLevel())));
        }
        StringSelectMenu menu = StringSelectMenu.create("dropdown")
                .setPlaceholder(
                        "Choose your pokemon: ")
                .addOptions(selectOptions)
                .build();

        event.reply("Please pick the pokemon you want to trade:")
                .setEphemeral(true)
                .addActionRow(menu)
                .addActionRow(
                        Button.primary("dropdown:ok", "OK"),
                        Button.danger("dropdown:cancel", "Cancel"))
                .queue();
    }

    // @Override
    // public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
    // // TradeOfferController.respondToOffering()
    // // if (event.getMember().equals()) {
    // // event.reply("You can't make offer to your own
    // trade!").setEphemeral(true).queue();
    // // }
    // throw new RuntimeException("Fail to make the offer");
    // }

}
