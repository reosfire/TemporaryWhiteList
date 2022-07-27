package ru.reosfire.temporarywhitelist.Data.Exporters;

import org.bukkit.command.CommandSender;
import ru.reosfire.temporarywhitelist.Configuration.Localization.CommandResults.ImportCommandResultConfig;
import ru.reosfire.temporarywhitelist.Data.ExportResult;
import ru.reosfire.temporarywhitelist.Data.IUpdatable;
import ru.reosfire.temporarywhitelist.Data.PlayerData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDataExporter
{
    List<PlayerData> GetAll();

    default ExportResult ExportTo(IUpdatable updatable)
    {
        List<PlayerData> players = GetAll();
        ExportResult exportResult = new ExportResult(players);

        CompletableFuture<?>[] updates = new CompletableFuture<?>[players.size()];

        for (int i = 0; i < players.size(); i++)
        {
            PlayerData playerData = players.get(i);
            updates[i] = updatable.Update(playerData);

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
    default CompletableFuture<ExportResult> ExportToAsync(IUpdatable provider)
    {
        return CompletableFuture.supplyAsync(() -> ExportTo(provider));
    }

    default void ExportAndHandle(IUpdatable updatable, ImportCommandResultConfig commandResults, CommandSender sender)
    {
        try
        {
            ExportResult exportResult = ExportTo(updatable);
            commandResults.Success.Send(sender, exportResult.getReplacements());
        }
        catch (Exception e)
        {
            commandResults.Error.Send(sender);
            e.printStackTrace();
        }
    }

    default void ExportAsyncAndHandle(IUpdatable updatable, ImportCommandResultConfig commandResults, CommandSender sender)
    {
        ExportToAsync(updatable).handle((res, ex) ->
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