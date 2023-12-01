package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

@Singleton
public class TrainerController {
    GenericRepository<Trainer> trainerRepository;
    PokemonController pokemonController;

    @Inject
    TrainerController(
            GenericRepository<Trainer> trainerRepository, PokemonController pokemonController) {
        this.trainerRepository = trainerRepository;
        this.pokemonController = pokemonController;
    }

    @Nonnull
    public Trainer getTrainerForMemberId(String discordMemberId) {
        Collection<Trainer> trainers = trainerRepository.getAll();
        for (Trainer currentTrainer : trainers) {
            if (currentTrainer.getDiscordUserId().equals(discordMemberId)) {
                return currentTrainer;
            }
        }

        Trainer trainer = new Trainer();
        trainer.setDiscordUserId(discordMemberId);
        return Objects.requireNonNull(trainerRepository.add(trainer));
    }

    public Trainer getTrainerForId(ObjectId trainerId) {
        return trainerRepository.get(Objects.requireNonNull(trainerId));
    }

    // ----------------------------------pokemonRepository----------------------------------
    public Trainer addPokemonToTrainer(String discordMemberId, String pokemonIdString) {
        ObjectId pokemonId = new ObjectId(pokemonIdString);
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        trainer.getPokemonInventory().add(pokemonId);
        return trainerRepository.update(trainer);
    }

    public Trainer addPokemonToTrainer(String discordMemberId, ObjectId pokemonId) {
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        trainer.getPokemonInventory().add(pokemonId);
        return trainerRepository.update(trainer);
    }

    public void removePokemonFromTrainer(@Nonnull Trainer trainer, @Nonnull Pokemon pokemon)
            throws PokemonNotExistException {
        if (!trainer.getPokemonInventory().contains(pokemon.getId())) {
            throw new PokemonNotExistException(
                    "Cannot remove a Pokemon that is not in the user's inventory");
        }
        trainer.getPokemonInventory().remove(pokemon.getId());
        trainerRepository.update(trainer);
    }
    // -----------------------------------------team-----------------------------------------
    public Trainer formTeam(String discordMemberId, String pokemonName, int position)
            throws InvalidTeamPositionException, PokemonNotExistException {
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        ObjectId pokemonId = getPokemonByName(trainer.getPokemonInventory(), pokemonName).getId();
        position--;
        // Ensure there is no empty slot before the given position.
        if (trainer.getTeam().size() < position) {
            throw new InvalidTeamPositionException(
                    "Cannot add Pokemon to a position that has an empty postion before it.");
            // Add the given Pokemon directly to the team.
        } else if (trainer.getTeam().size() == position) {
            trainer.getTeam().add(pokemonId);
            // Replace the current Pokemon in the specific position with the given Pokemon.
        } else {
            trainer.getTeam().remove(position);
            trainer.getTeam().add(position, pokemonId);
        }
        return trainerRepository.update(trainer);
    }

    public Pokemon getPokemonByName(List<ObjectId> pokemonInventory, String pokemonName)
            throws PokemonNotExistException {
        int pokedexNumber = pokemonController.getPokedexByName(pokemonName);
        for (ObjectId pokemonId : pokemonInventory) {
            Pokemon pokemon = pokemonController.getPokemonById(pokemonId);
            if (pokemon.getPokedexNumber() == pokedexNumber) {
                return pokemon;
            }
        }
        throw new PokemonNotExistException("Pokemon is not in inventory.");
    }

    public Boolean pokemonIsInInventory(
            @Nonnull String discordMemberId, @Nonnull ObjectId pokemonId) {
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        return trainer.getPokemonInventory().contains(pokemonId);
    }
}
