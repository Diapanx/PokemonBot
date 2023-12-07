package edu.northeastern.cs5500.starterbot.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class PokemonConfig {
    private int id;
    private Name name;
    private List<String> type;
    private Map<String, Integer> base;
    private String species;
    private Image image;

    @Data
    public static class Name {
        private String english;
    }

    @Data
    public static class Image {
        private String thumbnail;
    }
}
