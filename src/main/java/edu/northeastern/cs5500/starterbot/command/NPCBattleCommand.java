package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.NPCBattleController;
import edu.northeastern.cs5500.starterbot.controller.PokedexController;
import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.NPCBattle;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
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

    @Inject NPCBattleController npcBattleController;
    @Inject PokemonController pokemonController;
    @Inject TrainerController trainerController;
    @Inject PokedexController pokedexController;

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
            Pokemon npcPokemon = getNpcBattle().getNpcPokemon();
            Pokemon trainerPokemon = getNpcBattle().getTrainerPokemon();
            PokemonSpecies npcPokemonSpecies =
                    pokedexController.getPokemonSpeciesByNumber(npcPokemon.getPokedexNumber());

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(
                    String.format("You have encountered a wild %s!", npcPokemon.getName()));

            StringBuilder npcPokemonStatus = new StringBuilder();
            npcPokemonStatus.append(
                    "HP: "
                            + npcPokemon.getHp()
                            + "\nAttack: "
                            + npcPokemon.getAttack()
                            + "\nSpecial Attack: "
                            + npcPokemon.getSpecialAttack()
                            + "\nDefense: "
                            + npcPokemon.getDefense()
                            + "\nSpecial Defense: "
                            + npcPokemon.getSpecialDefense());

            embedBuilder.addField("Enemy Pokemon", npcPokemonStatus.toString(), false);

            StringBuilder yourPokemonStatus = new StringBuilder();
            yourPokemonStatus.append(
                    "HP: "
                            + trainerPokemon.getHp()
                            + "\nAttack: "
                            + trainerPokemon.getAttack()
                            + "\nSpecial Attack: "
                            + trainerPokemon.getSpecialAttack()
                            + "\nDefense: "
                            + trainerPokemon.getDefense()
                            + "\nSpecial Defense: "
                            + trainerPokemon.getSpecialDefense());

            embedBuilder.addField("Your Pokemon", yourPokemonStatus.toString(), true);

            embedBuilder.setThumbnail(npcPokemonSpecies.getImageUrl());

            MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
            messageCreateBuilder =
                    messageCreateBuilder.addActionRow(
                            Button.primary(getName() + ":start", "Start Battle"),
                            Button.danger(getName() + ":forfeit", "Forfeit"));
            messageCreateBuilder = messageCreateBuilder.addEmbeds(embedBuilder.build());
            event.reply(messageCreateBuilder.build()).setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        String message = "";
        String buttonId = event.getComponentId();
        Pokemon npcPokemon = getNpcBattle().getNpcPokemon();
        Pokemon trainerPokemon = getNpcBattle().getTrainerPokemon();
        event.deferEdit().queue();

        if (buttonId.endsWith(":start")) {
            StringBuilder title = new StringBuilder();
            if (npcPokemon.getSpeed() > trainerPokemon.getSpeed()) {
                npcBattleController.performNPCAttack(getNpcBattle());
                title.append("You have been attacked!");
            }
            if (!npcBattleController.checkIfBattleEnds(getNpcBattle())) {
                title.append("\nIts your turn now, Please select your action:");
            }

            Button attackButton = Button.primary(getName() + ":attack", "Attack");
            Button specialAttackButton =
                    Button.primary(getName() + ":specialAttack", "SpecialAttack");
            Button forfeitButton = Button.danger(getName() + ":forfeit", "Forfeit");
            event.getHook()
                    .editOriginalEmbeds(buildEmbed(title.toString()).build())
                    .setActionRow(attackButton, specialAttackButton, forfeitButton)
                    .queue();

        } else if (buttonId.endsWith(":forfeit")) {
            npcBattleController.endBattle(getNpcBattle());
            event.getHook()
                    .setEphemeral(true)
                    .editOriginal("Battle ended, you forfeited:(")
                    .setEmbeds(new ArrayList<>())
                    .setComponents(Collections.emptyList())
                    .queue();
        } else if (buttonId.endsWith(":attack")) {
            npcBattleController.basicAttack(
                    getNpcBattle().getTrainerPokemon(), getNpcBattle().getNpcPokemon());
            npcBattleController.performNPCAttack(getNpcBattle());
            message = "Your pokemon basic attacked enemy pokemon." + "\nYou have been attacked!";
            event.getHook().editOriginalEmbeds(buildEmbed(message).build()).queue();

        } else if (buttonId.endsWith(":specialAttack")) {
            npcBattleController.specialAttack(
                    getNpcBattle().getTrainerPokemon(), getNpcBattle().getNpcPokemon());
            npcBattleController.performNPCAttack(getNpcBattle());
            message = "Your pokemon special attacked enemy pokemon." + "\nYou have been attacked!";
            event.getHook().editOriginalEmbeds(buildEmbed(message).build()).queue();
        }
        // checks battle end conditions after interaction
        if (npcBattleController.checkIfBattleEnds(getNpcBattle())) {
            String endMessage = "";
            if (npcPokemon.getCurrentHp() == 0) {
                endMessage = "Battle ended, you won! :)";
            } else {
                endMessage = "Battle ended, you lost! :(";
            }
            event.getHook()
                    .editOriginalEmbeds(buildEmbed(endMessage).build())
                    .setComponents(Collections.emptyList())
                    .queue();
            npcBattleController.endBattle(getNpcBattle());
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

    private EmbedBuilder buildEmbed(String title) {
        Pokemon npcPokemon = getNpcBattle().getNpcPokemon();
        Pokemon trainerPokemon = getNpcBattle().getTrainerPokemon();
        PokemonSpecies npcPokemonSpecies =
                pokedexController.getPokemonSpeciesByNumber(npcPokemon.getPokedexNumber());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.addField("Enemy Pokemon", "HP: " + npcPokemon.getCurrentHp(), false);
        embedBuilder.addField("Your Pokemon", "HP: " + trainerPokemon.getCurrentHp(), false);
        embedBuilder.setThumbnail(npcPokemonSpecies.getImageUrl());
        return embedBuilder;
    }
}
