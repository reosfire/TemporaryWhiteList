package ru.reosfire.twl.common.data.exporters;

import ru.reosfire.twl.common.data.ExportResult;
import ru.reosfire.twl.common.data.IUpdatable;
import ru.reosfire.twl.common.data.PlayerData;

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

            updates[i] = updatable.update(playerData).handle((res, ex) ->
            {
                if (ex == null) exportResult.addWithoutError(playerData);
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
}