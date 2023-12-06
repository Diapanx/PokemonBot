package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.TradeOffer;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

@Singleton
public class TradeOfferController {
    GenericRepository<TradeOffer> tradeOfferRepository;
    TrainerController trainerController;
    PokemonController pokemonController;

    @Inject
    public TradeOfferController(
            GenericRepository<TradeOffer> tradeOfferRepository,
            TrainerController trainerController,
            PokemonController pokemonController) {
        this.tradeOfferRepository = tradeOfferRepository;
        this.trainerController = trainerController;
        this.pokemonController = pokemonController;
    }

    public TradeOffer createNewOffering(Trainer trainer, Pokemon pokemon)
            throws PokemonNotExistException {
        try {
            trainerController.removePokemonFromTrainer(trainer, pokemon);
            TradeOffer tradeOffer = new TradeOffer(trainer.getId(), pokemon.getId());
            return tradeOfferRepository.add(Objects.requireNonNull(tradeOffer));
        } catch (PokemonNotExistException e) {
            return null;
        }
    }

    public TradeOffer respondToOffering(TradeOffer tradeOffer, Trainer trainer, Pokemon pokemon)
            throws PokemonNotExistException {
        try {
            trainerController.removePokemonFromTrainer(trainer, pokemon);
            TradeOffer responseOffering = new TradeOffer(trainer.getId(), pokemon.getId());
            responseOffering.setParent(tradeOffer.getId());
            return tradeOfferRepository.add(Objects.requireNonNull(responseOffering));
        } catch (PokemonNotExistException e) {
            return null;
        }
    }

    public void acceptOffer(TradeOffer tradeOffer) {
        TradeOffer parentOffer = tradeOfferRepository.get(tradeOffer.getParent());

        Trainer parentTrainer = trainerController.getTrainerForId(parentOffer.getTrainerId());
        Trainer otherTrainer = trainerController.getTrainerForId(tradeOffer.getTrainerId());

        trainerController.addPokemonToTrainer(
                parentTrainer.getDiscordUserId(), tradeOffer.getPokemonId().toString());
        trainerController.addPokemonToTrainer(
                otherTrainer.getDiscordUserId(), parentOffer.getPokemonId().toString());

        tradeOfferRepository.delete(tradeOffer.getId());
        tradeOfferRepository.delete(parentOffer.getId());
    }

    public void declineOffer(TradeOffer tradeOffer) {
        tradeOfferRepository.delete(tradeOffer.getId());
    }

    public int getResources() {
        InputStream stream = this.getClass().getResourceAsStream("/pokemon.json");
        try {
            return stream.readAllBytes().length;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public TradeOffer getTradeById(ObjectId tradeOfferId) {
        return tradeOfferRepository.get(tradeOfferId);
    }

    public List<TradeOffer> getAllOpenTrades() {
        List<TradeOffer> openTrades = new ArrayList<>();
        for (TradeOffer trade : tradeOfferRepository.getAll()) {
            if (trade.getParent() == null) {
                openTrades.add(trade);
            }
        }
        return openTrades;
    }

    public List<TradeOffer> getOpenTradesByTrainer(Trainer trainer) {
        List<TradeOffer> trainerOpenTrades = new ArrayList<>();
        for (TradeOffer trade : getAllOpenTrades()) {
            if (trade.getTrainerId().equals(trainer.getId())) {
                trainerOpenTrades.add(trade);
            }
        }
        return trainerOpenTrades;
    }

    public TradeOffer getTradeByTrainerAndPokemon(Trainer trainer, String pokemonName) {
        for (TradeOffer trade : getOpenTradesByTrainer(trainer)) {
            if (pokemonController.getNameById(trade.getPokemonId()).equals(pokemonName)) {
                return trade;
            }
        }
        return null;
    }
}
