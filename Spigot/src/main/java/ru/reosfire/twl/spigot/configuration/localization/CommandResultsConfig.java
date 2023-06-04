package ru.reosfire.twl.spigot.configuration.localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.*;
import ru.reosfire.twl.spigot.lib.yaml.YamlConfig;

public class CommandResultsConfig extends YamlConfig
{
    public final TwlCommandResultsConfig Twl;
    public final AddCommandResultsConfig Add;
    public final SetCommandResultsConfig Set;
    public final RemoveCommandResultsConfig Remove;
    public final CheckCommandResultsConfig Check;
    public final ListCommandResultsConfig List;
    public final EnableCommandResultsConfig Enable;
    public final DisableCommandResultsConfig Disable;
    public final ReloadCommandResultsConfig Reload;
    public final ImportCommandResultConfig Import;

    public CommandResultsConfig(ConfigurationSection configurationSection)
    {
        super(configurationSection);

        Twl = new TwlCommandResultsConfig(getSection("Twl"));
        Add = new AddCommandResultsConfig(getSection("Add"));
        Set = new SetCommandResultsConfig(getSection("Set"));
        Remove = new RemoveCommandResultsConfig(getSection("Remove"));
        Check = new CheckCommandResultsConfig(getSection("Check"));
        List = new ListCommandResultsConfig(getSection("List"));
        Enable = new EnableCommandResultsConfig(getSection("Enable"));
        Disable = new DisableCommandResultsConfig(getSection("Disable"));
        Reload = new ReloadCommandResultsConfig(getSection("Reload"));
        Import = new ImportCommandResultConfig(getSection("Import"));
    }
}