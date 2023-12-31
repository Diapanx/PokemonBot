package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public class CommandModule {

    @Provides
    @IntoMap
    @StringKey(TeamCommand.NAME)
    public SlashCommandHandler provideTeamCommand(TeamCommand teamCommand) {
        return teamCommand;
    }

    @Provides
    @IntoMap
    @StringKey(DropdownCommand.NAME)
    public SlashCommandHandler provideDropdownCommand(DropdownCommand dropdownCommand) {
        return dropdownCommand;
    }

    @Provides
    @IntoMap
    @StringKey(DropdownCommand.NAME)
    public StringSelectHandler provideDropdownCommandMenuHandler(DropdownCommand dropdownCommand) {
        return dropdownCommand;
    }

    @Provides
    @IntoMap
    @StringKey(SpawnCommand.NAME)
    public SlashCommandHandler provideSpawnCommand(SpawnCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(SpawnCommand.NAME)
    public ButtonHandler provideSpawnCommandClickHandler(SpawnCommand command) {
        return command;
    }

    @Provides
    @IntoMap
    @StringKey(TradeCommand.NAME)
    public SlashCommandHandler provideTradeCommand(TradeCommand tradeCommand) {
        return tradeCommand;
    }

    @Provides
    @IntoMap
    @StringKey(TradeCommand.NAME)
    public ButtonHandler provideTradeCommandClickHandler(TradeCommand tradeCommand) {
        return tradeCommand;
    }

    @Provides
    @IntoMap
    @StringKey(InventoryCommand.NAME)
    public SlashCommandHandler provideInventoryCommand(InventoryCommand inventoryCommand) {
        return inventoryCommand;
    }

    @Provides
    @IntoMap
    @StringKey(NPCBattleCommand.NAME)
    public SlashCommandHandler provideNPCBattleCommand(NPCBattleCommand npcBattleCommand) {
        return npcBattleCommand;
    }

    @Provides
    @IntoMap
    @StringKey(NPCBattleCommand.NAME)
    public ButtonHandler provideNPCBattleCommandClickHandler(NPCBattleCommand npcBattleCommand) {
        return npcBattleCommand;
    }

    @Provides
    @IntoMap
    @StringKey(HelpCommand.NAME)
    public SlashCommandHandler provideHelpCommand(HelpCommand helpCommand) {
        return helpCommand;
    }
}
