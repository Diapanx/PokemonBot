package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.NPCBattleController;
import edu.northeastern.cs5500.starterbot.controller.PokedexController;
import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.NPCBattle;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
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
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

@Slf4j
public class NPCBattleCommand implements SlashCommandHandler, ButtonHandler {

    static final String NAME = "battle";
    // defining a class field to use in button interaction
    private static String trainerDiscordId;
    private static NPCBattle npcBattle;

    @Inject
    NPCBattleController npcBattleController;
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
        return Commands.slash(getName(), "battle with a random NPC pokemon.")
                .addOption(
                        OptionType.STRING,
                        "pokemon-name",
                        "The pokemon you want to use for battle.",
                        true);
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
            throws InvalidTeamPositionException, PokemonNotExistException {
        log.info("event: /battle");
        setTrainerDiscordId(event.getMember().getId());
        Trainer trainer = trainerController.getTrainerForMemberId(trainerDiscordId);
        String pokemonName = event.getOption("pokemon-name").getAsString();
        if (npcBattleController.checkIfPokemonInventoryIsNull(trainer)) {
            event.reply("You do not have any pokemon, use /spawn to catch pokemon.")
                    .setEphemeral(true)
                    .queue();
        } else {
            setNpcBattle(npcBattleController.startBattle(trainer, pokemonName));
            Pokemon npcPokemon = npcBattle.getNpcPokemon();
            Pokemon trainerPokemon = npcBattle.getTrainerPokemon();
            while (!npcBattleController.checkIfBattleEnds(npcBattle)) {
                if (npcBattleController.takeTurn(npcBattle).equals(npcPokemon)) {
                    npcBattleController.performNPCAttack(npcBattle, npcPokemon);
                } else if (npcBattleController.takeTurn(npcBattle).equals(trainerPokemon)) {
                    MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
                    messageCreateBuilder = messageCreateBuilder.addActionRow(
                            Button.primary(getName() + ":attack", "Attack"),
                            Button.danger(getName() + ":specialAttack", "SpecialAttack"));
                    messageCreateBuilder = messageCreateBuilder.setContent(
                            String.format(
                                    "Its <@%s>'s turn now, Please select your action:",
                                    trainerDiscordId));
                    event.reply(messageCreateBuilder.build()).setEphemeral(true).queue();
                } else {
                    event.reply("Unknow error occured.").setEphemeral(true).queue();
                }
            }
            if (npcPokemon.getCurrentHp() == 0) {
                event.getHook()
                        .setEphemeral(true)
                        .editOriginal("Battle ended, you won:)")
                        .setEmbeds(new ArrayList<>())
                        .setComponents(Collections.emptyList())
                        .queue();
                npcBattleController.endBattle(npcBattle);
            } else {
                event.getHook()
                        .setEphemeral(true)
                        .editOriginal("Battle ended, you lost:(")
                        .setEmbeds(new ArrayList<>())
                        .setComponents(Collections.emptyList())
                        .queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        if (event.getMember().getId().equals(getTrainerDiscordId())) {
            String message = "";
            String buttonId = event.getComponentId();
            if (buttonId.endsWith(":attack")) {
                npcBattleController.basicAttack(
                        npcBattle.getTrainerPokemon(), npcBattle.getNpcPokemon());
                message = "Your pokemon basic attacked enemy pokemon.";
            } else if (buttonId.endsWith(":specialAttack")) {
                npcBattleController.specialAttack(
                        npcBattle.getTrainerPokemon(), npcBattle.getNpcPokemon());
                message = "Your pokemon special attacked enemy pokemon.";
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

    public static String getTrainerDiscordId() {
        return trainerDiscordId;
    }

    public static void setTrainerDiscordId(String trainerDiscordId) {
        NPCBattleCommand.trainerDiscordId = trainerDiscordId;
    }

    public static NPCBattle getNpcBattle() {
        return npcBattle;
    }

    public static void setNpcBattle(NPCBattle npcBattle) {
        NPCBattleCommand.npcBattle = npcBattle;
    }
}
