package ru.reosfire.twl.common.commands.subcommands;

import ru.reosfire.twl.common.commands.subcommands.importTypes.EasyWhitelistImportCommand;
import ru.reosfire.twl.common.commands.subcommands.importTypes.MinecraftDefaultImportCommand;
import ru.reosfire.twl.common.commands.subcommands.importTypes.SelfSqlImportCommand;
import ru.reosfire.twl.common.commands.subcommands.importTypes.SelfYamlImportCommand;
import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.lib.commands.CommandName;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.CommandPermission;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;

@CommandName("import")
@CommandPermission("TemporaryWhitelist.Administrate.Import")
public class ImportCommand extends CommandNode
{
    private final ImportCommandResultConfig commandResults;

    public ImportCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        commandResults = pluginInstance.getMessages().CommandResults.Import;

        addChildren(new MinecraftDefaultImportCommand(pluginInstance));
        addChildren(new EasyWhitelistImportCommand(pluginInstance));
        addChildren(new SelfYamlImportCommand(pluginInstance));
        addChildren(new SelfSqlImportCommand(pluginInstance));
    }

    @Override
    protected boolean execute(TwlCommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return true;
    }
}