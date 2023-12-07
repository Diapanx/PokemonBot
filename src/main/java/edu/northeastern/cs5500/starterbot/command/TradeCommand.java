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

@Slf4j
public class TradeCommand implements SlashCommandHandler, ButtonHandler {

    static final String NAME = "trade";
    // defining a class field to use in button interaction
    private static TradeOffer respondTrade;

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
                        new SubcommandData("new", "Create a new trade")
                                .addOption(
                                        OptionType.STRING,
                                        "pokemon-name",
                                        "The pokemon user wants to trade.",
                                        true),
                        new SubcommandData("view", "View all the open trades.")
                                .addOption(
                                        OptionType.USER,
                                        "user",
                                        "View user's open trades.(Enter their @mentions)",
                                        true),
                        new SubcommandData("with", "Trade with a player")
                                .addOption(
                                        OptionType.USER,
                                        "user",
                                        "User you want to trade with.(Enter their @mentions)",
                                        true)
                                .addOption(
                                        OptionType.STRING,
                                        "your-pokemon",
                                        "Enter your pokmon you want to trade with.)",
                                        true)
                                .addOption(
                                        OptionType.STRING,
                                        "their-pokemon",
                                        "Enter the pokmon you want to trade for.)",
                                        true));
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
            throws InvalidTeamPositionException, PokemonNotExistException {
        log.info("event: /trade");
        String trainerDiscordId = event.getMember().getId();
        Trainer trainer = trainerController.getTrainerForMemberId(trainerDiscordId);

        switch (event.getSubcommandName()) {
            case "new":
                String pokemonName = event.getOption("pokemon-name").getAsString();
                Pokemon pokemon = trainerController.getInventoryPokemonByName(trainer, pokemonName);
                tradeOfferController.createNewOffering(trainer, pokemon);
                String message =
                        String.format(
                                "Player <@%s> has listed %s for trade.",
                                trainerDiscordId, pokemonName);
                event.reply(message).queue();
                break;
            case "view":
                String mentionedUserId = event.getOption("user").getAsMember().getId();
                event.reply(avaliableTradeString(mentionedUserId)).setEphemeral(true).queue();
                break;
            case "with":
                String tradeUserId = event.getOption("user").getAsMember().getId();
                if (tradeUserId.equals(trainerDiscordId)) {
                    event.reply("You can not trade with your own.").setEphemeral(true).queue();
                    break;
                }
                Trainer tradeTrainer = trainerController.getTrainerForMemberId(tradeUserId);
                String trainerPokemonName = event.getOption("your-pokemon").getAsString();
                String tradePokemonName = event.getOption("their-pokemon").getAsString();
                offerTrade(trainer, tradeTrainer, trainerPokemonName, tradePokemonName);

                MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
                messageCreateBuilder =
                        messageCreateBuilder.addActionRow(
                                Button.primary(getName() + ":accept", "Accept"),
                                Button.danger(getName() + ":decline", "Decline"));
                messageCreateBuilder =
                        messageCreateBuilder.setContent(
                                String.format(
                                        "Player <@%s> has offered %s for Player <@%s>'s %s:%n",
                                        trainerDiscordId,
                                        trainerPokemonName,
                                        tradeUserId,
                                        tradePokemonName));
                event.reply(messageCreateBuilder.build()).queue();
                break;
            default:
                event.reply("Invalid subcommand").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        TradeOffer parent = tradeOfferController.getTradeById(getRespondTrade().getParent());

        Trainer parentTrainer = trainerController.getTrainerForId(parent.getTrainerId());
        if (event.getMember().getId().equals(parentTrainer.getDiscordUserId())) {
            String buttonId = event.getComponentId();
            String message = "";
            if (buttonId.endsWith(":accept")) {
                tradeOfferController.acceptOffer(getRespondTrade());
                Trainer trainer =
                        trainerController.getTrainerForId(getRespondTrade().getTrainerId());
                message =
                        String.format(
                                "Player <@%s> has traded %s with Player <@%s>'s %s.",
                                parentTrainer.getDiscordUserId(),
                                pokemonController.getNameById(parent.getPokemonId()),
                                trainer.getDiscordUserId(),
                                pokemonController.getNameById(getRespondTrade().getPokemonId()));
            } else if (buttonId.endsWith(":decline")) {
                tradeOfferController.declineOffer(getRespondTrade());
                message = "This offer has been declined.";
            }
            event.deferEdit().queue();
            event.getHook()
                    .editOriginal(message)
                    .setEmbeds(new ArrayList<>())
                    .setComponents(Collections.emptyList())
                    .queue();
        } else {
            event.reply("You can only interact with your own trade.").setEphemeral(true).queue();
        }
    }

    private static TradeOffer getRespondTrade() {
        return respondTrade;
    }

    private static void setRespondTrade(TradeOffer tradeOffer) {
        respondTrade = tradeOffer;
    }

    private String avaliableTradeString(String discordId) {
        Trainer mentionedTrainer = trainerController.getTrainerForMemberId(discordId);
        StringBuilder avaliableTrades = new StringBuilder();
        avaliableTrades.append(String.format("Player <@%s>'s Open Trades:%n", discordId));
        for (TradeOffer openTrade : tradeOfferController.getOpenTradesByTrainer(mentionedTrainer)) {
            Pokemon pokemonForTrade = pokemonController.getPokemonById(openTrade.getPokemonId());
            avaliableTrades
                    .append(
                            "     "
                                    + pokedexController
                                            .getPokemonSpeciesByNumber(
                                                    pokemonForTrade.getPokedexNumber())
                                            .getName())
                    .append("\n");
        }
        return avaliableTrades.toString();
    }

    private void offerTrade(
            Trainer trainer,
            Trainer tradeTrainer,
            String trainerPokemonName,
            String tradePokemonName)
            throws PokemonNotExistException {
        Pokemon trainerPokemon =
                trainerController.getInventoryPokemonByName(trainer, trainerPokemonName);
        TradeOffer parentTrade =
                tradeOfferController.getTradeByTrainerAndPokemon(tradeTrainer, tradePokemonName);
        setRespondTrade(
                tradeOfferController.respondToOffering(parentTrade, trainer, trainerPokemon));
    }
}
