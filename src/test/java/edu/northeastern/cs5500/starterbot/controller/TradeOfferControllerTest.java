package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.TradeOffer;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TradeOfferControllerTest {
    final String DISCORD_USER_ID_1 = "discordUserId1";
    final String DISCORD_USER_ID_2 = "discordUserId2";

    PokedexController getPokedexController() {
        return new PokedexController();
    }

    PokemonController getPokemonController(PokedexController pokedexController) {
        return new PokemonController(new InMemoryRepository<>(), pokedexController);
    }

    TrainerController getTrainerController(PokemonController pokemonController) {
        TrainerController trainerController =
                new TrainerController(new InMemoryRepository<>(), pokemonController);
        trainerController.addPokemonToTrainer(
                DISCORD_USER_ID_1, pokemonController.spawnPokemon(1).getId().toString());
        trainerController.addPokemonToTrainer(
                DISCORD_USER_ID_2, pokemonController.spawnPokemon(4).getId().toString());
        return trainerController;
    }

    TradeOfferController getTradeOfferController(
            TrainerController trainerController, PokemonController pokemonController) {
        TradeOfferController tradeOfferController =
                new TradeOfferController(
                        new InMemoryRepository<>(), trainerController, pokemonController);
        return tradeOfferController;
    }

    @Test
    void testThatTrainersCanOfferPokemonTheyHave() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        TradeOfferController tradeOfferController =
                getTradeOfferController(trainerController, pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Trainer otherTrainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_2);
        Pokemon pokemon = pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        Pokemon otherPokemon =
                pokemonController.getPokemonById(otherTrainer.getPokemonInventory().get(0));
        TradeOffer tradeOffer = tradeOfferController.createNewOffering(trainer, pokemon);
        TradeOffer newTradeOffer =
                tradeOfferController.respondToOffering(tradeOffer, otherTrainer, otherPokemon);

        TradeOffer parentTrade = tradeOfferController.getTradeById(newTradeOffer.getParent());
        assertNull(tradeOffer.getParent());

        assertNull(tradeOfferController.createNewOffering(trainer, otherPokemon));

        assertEquals(tradeOffer, parentTrade);
        assertEquals(tradeOffer, tradeOfferController.getTradeById(tradeOffer.getId()));

        List<TradeOffer> openTrades = new ArrayList<>();
        openTrades.add(tradeOffer);
        assertEquals(tradeOfferController.getOpenTradesByTrainer(trainer), openTrades);
        assertEquals(
                tradeOfferController.getTradeByTrainerAndPokemon(
                        trainer, pokemonController.getNameById(pokemon.getId())),
                tradeOffer);

        assertNull(
                tradeOfferController.getTradeByTrainerAndPokemon(
                        otherTrainer, pokemonController.getNameById(pokemon.getId())));

        tradeOfferController.acceptOffer(newTradeOffer);
        List<TradeOffer> tradeOffers = new ArrayList<>();
        assertEquals(tradeOfferController.getAllOpenTrades(), tradeOffers);

        TradeOffer tradeOffer1 = tradeOfferController.createNewOffering(trainer, otherPokemon);
        TradeOffer newTradeOffer1 =
                tradeOfferController.respondToOffering(tradeOffer, otherTrainer, pokemon);
        tradeOfferController.declineOffer(newTradeOffer1);

        List<TradeOffer> openTrades1 = new ArrayList<>();
        openTrades1.add(tradeOffer1);
        assertEquals(tradeOfferController.getOpenTradesByTrainer(trainer), openTrades1);
    }
}
