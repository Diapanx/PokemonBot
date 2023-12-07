package edu.northeastern.cs5500.starterbot.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.northeastern.cs5500.starterbot.model.PokemonConfig;
import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.PokemonSpecies.PokemonSpeciesBuilder;
import edu.northeastern.cs5500.starterbot.model.PokemonType;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PokedexController {

    private final List<PokemonConfig> pokemonConfigs;

    @Inject
    PokedexController() {
        // System.out.println("Current Directory: " + System.getProperty("user.dir"));

        pokemonConfigs = loadPokemonConfigs();
        // System.out.println("pokemon config is " + pokemonConfigs);
    }

    // Get the parsed Pokemon Config
    // Get the name, types and imageUrl from the PokemonConfig
    // Pass the values in above step to PokemonSpeciesBuilder to build the Pokemon Species.

    private List<PokemonConfig> loadPokemonConfigs() {
        try {
            Gson gson = new Gson();
            // Define the Type for the list of PokemonConfig objects
            Type listType = new TypeToken<List<PokemonConfig>>() {}.getType();
            // Create a FileReader to read the JSON file
            FileReader reader =
                    new FileReader(
                            "/workspaces/bot-bear/src/main/java/edu/northeastern/cs5500/starterbot/resources/pokedex.json");

            return gson.fromJson(reader, listType);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nonnull
    public PokemonSpecies getPokemonSpeciesByNumber(int pokedexNumber) {
        PokemonConfig foundConfig = null;
        for (PokemonConfig config : pokemonConfigs) {
            if (config.getId() == pokedexNumber) {
                foundConfig = config;
                break;
            }
        }

        if (foundConfig == null) {
            throw new IllegalStateException("Pokemon not found");
        }

        return buildPokemonSpecies(foundConfig);
    }

    private PokemonSpecies buildPokemonSpecies(PokemonConfig config) {
        List<String> typeStrings = config.getType();
        List<PokemonType> types = new ArrayList<PokemonType>();
        Map<String, Integer> base = config.getBase();

        for (int i = 0; i < typeStrings.size(); i++) {
            types.add(PokemonType.fromString(typeStrings.get(i)));
        }

        PokemonSpeciesBuilder builder = PokemonSpecies.builder();
        builder.pokedexNumber(config.getId());
        builder.name(config.getName().getEnglish());
        builder.types(types);
        builder.imageUrl(config.getImage().getThumbnail());
        builder.base(base);

        return builder.build();
    }

    // @Nonnull
    // public PokemonSpecies getPokemonSpeciesByNumber(int pokedexNumber) {
    //     PokemonSpeciesBuilder builder = PokemonSpecies.builder();
    //     builder.pokedexNumber(pokedexNumber);

    //     switch (pokedexNumber) {
    //         case 1: // Bulbasaur
    //             builder.name("Bulbasaur");
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.GRASS));
    //             builder.imageUrl("https://placehold.co/256x256/green/white.png?text=Bulbasaur");
    //             break;
    //         case 4: // Charmander
    //             builder.name("Charmander");
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.FIRE));
    //             builder.imageUrl("https://placehold.co/256x256/red/white.png?text=Charmander");
    //             break;
    //         case 7: // Squirtle
    //             builder.name("Squirtle");
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.WATER));
    //             builder.imageUrl("https://placehold.co/256x256/blue/white.png?text=Squirtle");
    //             break;
    //         case 19: // Rattata
    //             builder.name("Rattata");
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.NORMAL));
    //             builder.imageUrl("https://placehold.co/256x256.png?text=Rattata");
    //             break;
    //         default:
    //             throw new IllegalStateException();
    //     }

    //     return Objects.requireNonNull(builder.build());
    // }

    public PokemonSpecies getSpeciesByName(String name) {
        PokemonConfig foundConfig = null;
        for (PokemonConfig config : pokemonConfigs) {
            if (config.getName().getEnglish().equalsIgnoreCase(name)) {
                foundConfig = config;
                break;
            }
        }

        if (foundConfig == null) {
            throw new IllegalStateException("Pokemon with name '" + name + "' not found");
        }

        return buildPokemonSpecies(foundConfig);
    }

    // public PokemonSpecies getSpeciesByName(String name) {
    //     PokemonSpeciesBuilder builder = PokemonSpecies.builder();
    //     builder.name(name);
    //     switch (name) {
    //         case "Bulbasaur": // Bulbasaur
    //             builder.pokedexNumber(1);
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.GRASS));
    //             builder.imageUrl("https://placehold.co/256x256/green/white.png?text=Bulbasaur");
    //             break;
    //         case "Charmander": // Charmander
    //             builder.pokedexNumber(4);
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.FIRE));
    //             builder.imageUrl("https://placehold.co/256x256/red/white.png?text=Charmander");
    //             break;
    //         case "Squirtle": // Squirtle
    //             builder.pokedexNumber(7);
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.WATER));
    //             builder.imageUrl("https://placehold.co/256x256/blue/white.png?text=Squirtle");
    //             break;
    //         case "Rattata": // Rattata
    //             builder.pokedexNumber(19);
    //             builder.types(PokemonType.getSingleTypeArray(PokemonType.NORMAL));
    //             builder.imageUrl("https://placehold.co/256x256.png?text=Rattata");
    //             break;
    //         default:
    //             throw new IllegalStateException();
    //     }

    //     return Objects.requireNonNull(builder.build());
    // }
}
