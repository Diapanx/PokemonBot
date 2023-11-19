package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
@Singleton
public class TrainerController{
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

    public void removePokemonFromTrainer(@Nonnull Trainer trainer, @Nonnull Pokemon pokemon) {
        if (!trainer.getPokemonInventory().contains(pokemon.getId())) {
            throw new PokemonNotExistException(
                    "Cannot remove a Pokemon that is not in the user's inventory");
        }
        trainer.getPokemonInventory().remove(pokemon.getId());
        trainerRepository.update(trainer);
    }

    
}