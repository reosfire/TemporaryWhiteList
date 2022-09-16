package ru.reosfire.temporarywhitelist.commands.subcommands;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.commands.subcommands.importTypes.EasyWhitelistImportCommand;
import ru.reosfire.temporarywhitelist.commands.subcommands.importTypes.MinecraftDefaultImportCommand;
import ru.reosfire.temporarywhitelist.commands.subcommands.importTypes.SelfSqlImportCommand;
import ru.reosfire.temporarywhitelist.commands.subcommands.importTypes.SelfYamlImportCommand;
import ru.reosfire.temporarywhitelist.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.lib.commands.CommandName;
import ru.reosfire.temporarywhitelist.lib.commands.CommandNode;
import ru.reosfire.temporarywhitelist.lib.commands.CommandPermission;
import ru.reosfire.temporarywhitelist.TemporaryWhiteList;

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
    protected boolean execute(CommandSender sender, String[] args)
    {
        commandResults.Usage.Send(sender);
        return true;
    }
}