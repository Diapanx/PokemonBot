package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

@Singleton
public class TrainerController {
    GenericRepository<Trainer> trainerRepository;

    @Inject
    TrainerController(GenericRepository<Trainer> trainerRepository) {
        this.trainerRepository = trainerRepository;
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
        return trainerRepository.add(trainer);
    }

    public Trainer getTrainerForId(ObjectId trainerId) {
        return trainerRepository.get(trainerId);
    }

    // ----------------------------------pokemonRepository API----------------------------------
    public Trainer addPokemonToTrainer(String discordMemberId, String pokemonIdString) {
        ObjectId pokemonId = new ObjectId(pokemonIdString);
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        trainer.getPokemonInventory().add(pokemonId);
        return trainerRepository.update(trainer);
    }

    public Trainer addPokemonToTrainer(ObjectId id, String pokemonIdString) {
        ObjectId pokemonId = new ObjectId(pokemonIdString);
        Trainer trainer = getTrainerForId(id);
        trainer.getPokemonInventory().add(pokemonId);
        return trainerRepository.update(trainer);
    }

    public void removePokemonFromTrainer(
            @Nonnull String discordMemberId, @Nonnull String pokemonIdString) {
        ObjectId pokemonId = new ObjectId(pokemonIdString);
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        trainer.getPokemonInventory().remove(pokemonId);
        trainerRepository.update(trainer);
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

    // -----------------------------------------team API-----------------------------------------
    public Trainer formTeam(String discordMemberId, String pokemonIdString, int position)
            throws InvalidTeamPositionException {
        ObjectId pokemonId = new ObjectId(pokemonIdString);
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        ObjectId[] team = trainer.getTeam();
        if (position < 0 || position > 5) {
            throw new InvalidTeamPositionException(
                    "Position out of range. Please specify a position between 0 and 5, inclusive.");
        }
        // Ensure there is no empty slot before the given position.
        if (this.countTeamMember(team) < position) {
            throw new InvalidTeamPositionException(
                    "Cannot add Pokemon to a position that has an empty postion before it.");
        }
        team[position] = pokemonId;
        return trainerRepository.update(trainer);
    }

    private int countTeamMember(ObjectId[] team) {
        int count = 0;
        for (ObjectId member : team) {
            if (member != null) {
                count++;
            }
        }
        return count;
    }
}
