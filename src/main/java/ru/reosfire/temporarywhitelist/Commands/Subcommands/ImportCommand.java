package ru.reosfire.temporarywhitelist.Commands.Subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes.EasyWhitelistImportCommand;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes.MinecraftDefaultImportCommand;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes.SelfSqlImportCommand;
import ru.reosfire.temporarywhitelist.Commands.Subcommands.ImportTypes.SelfYamlImportCommand;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandName;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandNode;
import ru.reosfire.temporarywhitelist.Lib.Commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

@CommandName("import")
@CommandPermission("TemporaryWhitelist.Administrate.Import")
public class ImportCommand extends CommandNode
{
    private final ImportCommandResultConfig _commandResults;

    public ImportCommand(TemporaryWhiteList pluginInstance)
    {
        super(pluginInstance.getMessages().NoPermission);
        _commandResults = pluginInstance.getMessages().CommandResults.Import;

        AddChildren(new MinecraftDefaultImportCommand(pluginInstance));
        AddChildren(new EasyWhitelistImportCommand(pluginInstance));
        AddChildren(new SelfYamlImportCommand(pluginInstance));
        AddChildren(new SelfSqlImportCommand(pluginInstance));
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args)
    {
        _commandResults.Usage.Send(sender);
        return true;
    }
}