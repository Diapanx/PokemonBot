package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import java.util.Objects;
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
            throws InvalidTeamPositionException {
        // get pokedex number by name
        // get object id by pokedex number
        ObjectId pokemonId = new ObjectId(pokemonName);
        Trainer trainer = getTrainerForMemberId(discordMemberId);
        ObjectId[] team = trainer.getTeam();

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
