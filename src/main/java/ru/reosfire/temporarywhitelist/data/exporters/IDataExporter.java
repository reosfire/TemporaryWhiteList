package ru.reosfire.temporarywhitelist.data.exporters;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.configuration.localization.commandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.data.ExportResult;
import ru.reosfire.temporarywhitelist.data.IUpdatable;
import ru.reosfire.temporarywhitelist.data.PlayerData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDataExporter
{
    List<PlayerData> getAll();

    default ExportResult exportTo(IUpdatable updatable)
    {
        List<PlayerData> players = getAll();
        ExportResult exportResult = new ExportResult(players);

        CompletableFuture<?>[] updates = new CompletableFuture<?>[players.size()];

        for (int i = 0; i < players.size(); i++)
        {
            PlayerData playerData = players.get(i);
            updates[i] = updatable.update(playerData);

            updates[i].handle((res, ex) ->
            {
                if (ex == null) exportResult.WithoutError.add(playerData);
                else ex.printStackTrace();
                return null;
            });
        }

        CompletableFuture.allOf(updates).join();
        return exportResult;
    }
    default CompletableFuture<ExportResult> exportToAsync(IUpdatable provider)
    {
        return CompletableFuture.supplyAsync(() -> exportTo(provider));
    }

    default void ExportAndHandle(IUpdatable updatable, ImportCommandResultConfig commandResults, CommandSender sender)
    {
        try
        {
            ExportResult exportResult = exportTo(updatable);
            commandResults.Success.Send(sender, exportResult.getReplacements());
        }
        catch (Exception e)
        {
            commandResults.Error.Send(sender);
            e.printStackTrace();
        }
    }

    default void exportAsyncAndHandle(IUpdatable updatable, ImportCommandResultConfig commandResults, CommandSender sender)
    {
        exportToAsync(updatable).handle((res, ex) ->
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