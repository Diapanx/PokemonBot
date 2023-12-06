package edu.northeastern.cs5500.starterbot.command;

import static org.junit.Assert.assertEquals;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TeamCommandTest {
    @Test
    void testNameMatchesData() {
        TeamCommand teamCommand = new TeamCommand();
        String name = teamCommand.getName();
        CommandData commandData = teamCommand.getCommandData();

        assertEquals(name, commandData.getName());
    }

    @Test
    void testSubcommand() {
        CommandDataImpl command =
                new CommandDataImpl("team", "player's battle team")
                        .addSubcommands(
                                new SubcommandData("display", "Display your team"),
                                new SubcommandData("add", "Add a Pokemon to your team")
                                        .addOption(
                                                OptionType.STRING,
                                                "pokemon",
                                                "Name of the Pokemon",
                                                true)
                                        .addOptions(
                                                new OptionData(
                                                                OptionType.INTEGER,
                                                                "position",
                                                                "Position on the team (from 1 to 6)",
                                                                true)
                                                        .setRequiredRange(1, 6)));

        DataObject data = command.toData();
        Assertions.assertEquals("team", data.getString("name"));
        Assertions.assertEquals("player's battle team", data.getString("description"));

        DataObject display = data.getArray("options").getObject(0);
        Assertions.assertEquals("display", display.getString("name"));
        Assertions.assertEquals("Display your team", display.getString("description"));

        DataObject add = data.getArray("options").getObject(1);
        Assertions.assertEquals("add", add.getString("name"));
        Assertions.assertEquals("Add a Pokemon to your team", add.getString("description"));

        DataArray options = add.getArray("options");

        DataObject pokemonName = options.getObject(0);
        Assertions.assertTrue(pokemonName.getBoolean("required"));
        Assertions.assertEquals("pokemon", pokemonName.getString("name"));
        Assertions.assertEquals("Name of the Pokemon", pokemonName.getString("description"));

        DataObject position = options.getObject(1);
        Assertions.assertTrue(position.getBoolean("required"));
        Assertions.assertEquals("position", position.getString("name"));
        Assertions.assertEquals(
                "Position on the team (from 1 to 6)", position.getString("description"));
    }
}
