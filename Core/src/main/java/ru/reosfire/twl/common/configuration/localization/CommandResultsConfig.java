package ru.reosfire.twl.common.configuration.localization;

import ru.reosfire.twl.common.configuration.localization.commandResults.*;
import ru.reosfire.twl.common.lib.yaml.ConfigSection;
import ru.reosfire.twl.common.lib.yaml.YamlConfig;

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
    //public final ClearCommandResultsConfig Clear;

    public CommandResultsConfig(ConfigSection configurationSection)
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
        //Clear = new ClearCommandResultsConfig(getSection("Clear"));
    }
}