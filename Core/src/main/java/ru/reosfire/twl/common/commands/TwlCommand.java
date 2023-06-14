package ru.reosfire.twl.common.commands;

import ru.reosfire.twl.common.commands.subcommands.*;
import ru.reosfire.twl.common.configuration.localization.commandResults.TwlCommandResultsConfig;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

@CommandName("twl")
public class TwlCommand extends CommandNode
{
    private final TwlCommandResultsConfig commandResults;
    public TwlCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);

        commandResults = pluginInstance.getMessages().CommandResults.Twl;

        addChildren(new AddCommand(pluginInstance));
        addChildren(new SetCommand(pluginInstance));
        addChildren(new RemoveCommand(pluginInstance));
        addChildren(new CheckCommand(pluginInstance));
        addChildren(new ListCommand(pluginInstance));
        addChildren(new ImportCommand(pluginInstance));
        addChildren(new ClearCommand(pluginInstance));
        addChildren(new EnableCommand(pluginInstance));
        addChildren(new DisableCommand(pluginInstance));
        addChildren(new ReloadCommand(pluginInstance));
    }

    @Override
    public boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return true;
    }
}