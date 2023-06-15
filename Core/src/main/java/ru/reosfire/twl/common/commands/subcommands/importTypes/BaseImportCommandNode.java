package ru.reosfire.twl.common.commands.subcommands.importTypes;

import ru.reosfire.twl.common.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.twl.common.data.IUpdatable;
import ru.reosfire.twl.common.data.exporters.IDataExporter;
import ru.reosfire.twl.common.lib.commands.CommandNode;
import ru.reosfire.twl.common.lib.commands.TwlCommandSender;
import ru.reosfire.twl.common.lib.yaml.common.text.MultilineMessage;

public abstract class BaseImportCommandNode extends CommandNode {
    public BaseImportCommandNode(MultilineMessage noPermission, MultilineMessage unexpectedError) {
        super(noPermission, unexpectedError);
    }

    protected void exportAsyncAndHandle(IDataExporter exporter, IUpdatable updatable, ImportCommandResultConfig commandResults, TwlCommandSender sender) {
        exporter.exportToAsync(updatable).handle((res, ex) -> {
            if (ex == null) commandResults.Success.Send(sender);
            else {
                commandResults.Error.Send(sender);
                ex.printStackTrace();
            }
            return null;
        });
    }
}