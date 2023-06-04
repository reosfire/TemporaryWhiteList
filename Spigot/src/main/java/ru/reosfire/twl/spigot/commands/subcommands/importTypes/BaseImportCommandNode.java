package ru.reosfire.twl.spigot.commands.subcommands.importTypes;

import org.bukkit.command.CommandSender;
import ru.reosfire.twl.common.data.IUpdatable;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.spigot.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.spigot.lib.commands.CommandNode;

public abstract class BaseImportCommandNode extends CommandNode {

    public BaseImportCommandNode(String noPermission) {
        super(noPermission);
    }


    void exportAsyncAndHandle(IDataExporter exporter, IUpdatable updatable, ImportCommandResultConfig commandResults, CommandSender sender)
    {
        exporter.exportToAsync(updatable).handle((res, ex) ->
        {
            if (ex == null) commandResults.Success.Send(sender, res.getReplacements());
            else
            {
                commandResults.Error.Send(sender);
                ex.printStackTrace();
            }
            return null;
        });
    }
}