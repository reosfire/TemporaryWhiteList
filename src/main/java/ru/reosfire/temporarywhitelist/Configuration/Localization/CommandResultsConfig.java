package ru.reosfire.temporarywhitelist.Configuration.Localization;

import org.bukkit.configuration.ConfigurationSection;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.*;
import ru.reosfire.temporarywhitelist.Lib.Yaml.YamlConfig;

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
    }
}