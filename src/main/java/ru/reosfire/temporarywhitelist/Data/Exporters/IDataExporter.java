package ru.reosfire.temporarywhitelist.Data.Exporters;

import ru.reosfire.temporarywhitelist.Data.ExportResult;
import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IDataExporter
{
    List<PlayerData> GetAll();

    default ExportResult ExportWith(Function<PlayerData, CompletableFuture<?>> updater)
    {
        List<PlayerData> players = GetAll();
        ExportResult exportResult = new ExportResult(players);

        CompletableFuture<?>[] updates = new CompletableFuture<?>[players.size()];

        for (int i = 0; i < players.size(); i++)
        {
            PlayerData playerData = players.get(i);
            updates[i] = updater.apply(playerData);

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

    default ExportResult ExportTo(IDataProvider provider)
    {
        return ExportWith(provider::Update);
    }
    default CompletableFuture<ExportResult> ExportToAsync(IDataProvider provider)
    {
        return CompletableFuture.supplyAsync(() -> ExportTo(provider));
    }

    default ExportResult ExportTo(PlayerDatabase database)
    {
        return ExportWith(database::Update);
    }
    default CompletableFuture<ExportResult> ExportToAsync(PlayerDatabase database)
    {
        return CompletableFuture.supplyAsync(() -> ExportTo(database));
    }
}