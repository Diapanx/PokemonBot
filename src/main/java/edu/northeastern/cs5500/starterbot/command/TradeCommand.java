package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.TradeOfferController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.controller.PokedexController;
import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.TradeOffer;
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
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;


@Slf4j
public class TradeCommand implements SlashCommandHandler {

    static final String NAME = "trade";

    @Inject TradeOfferController tradeOfferController;
    @Inject PokemonController pokemonController;
    @Inject TrainerController trainerController;
    @Inject PokedexController pokedexController;

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
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) throws PokemonNotExistException{
        log.info("event: /trade");
        String trainerDiscordId = event.getMember().getId();
        switch (event.getSubcommandName()) {
            case "new":
                Pokemon pokemon = pokemonController.spawnRandomPokemon();
                TradeOffer tradeOffer = tradeOfferController.createNewOffering(trainerController.getTrainerForMemberId(trainerDiscordId), pokemon);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                String trainerName = event.getMember().getUser().getName();
                Pokemon tradePokemon = pokemonController.getPokemonById(tradeOffer.getPokemonId());
                String pokemonName = pokedexController.getPokemonSpeciesByNumber(tradePokemon.getPokedexNumber()).getName();
                embedBuilder.setTitle(String.format("%s has offered %s for trade!", trainerName, pokemonName));
                embedBuilder.addField("Level", Integer.toString(tradePokemon.getLevel()), false);
                MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
                embedBuilder.setThumbnail(pokedexController.getPokemonSpeciesByNumber(tradePokemon.getPokedexNumber()).getImageUrl());
                messageCreateBuilder =
                    messageCreateBuilder.addActionRow(
                        Button.primary(
                                getName() + ":trade:", "Trade"));
                messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
                event.reply(messageCreateBuilder.build()).queue();
                break;
            case "offer":
                //
                break;
            default:
                event.reply("Unknown error occured.").setEphemeral(true).queue();
        }
    }

    // @Override
    // public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
    //     // TradeOfferController.respondToOffering()
    //     // if (event.getMember().equals()) {
    //     //     event.reply("You can't make offer to your own trade!").setEphemeral(true).queue();
    //     // }
    //     throw new RuntimeException("Fail to make the offer"); 
    // }

}