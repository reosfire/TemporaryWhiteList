package ru.reosfire.temporarywhitelist.Data.Exporters;

import ru.reosfire.temporarywhitelist.Data.IDataProvider;
import ru.reosfire.temporarywhitelist.Data.PlayerData;
import ru.reosfire.temporarywhitelist.Data.PlayerDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDataExporter
{
    List<PlayerData> GetAll();

    default void ExportTo(IDataProvider provider)
    {
        for (PlayerData playerData : GetAll())
        {
            provider.Update(playerData);
        }
    }
    default CompletableFuture<Void> ExportToAsync(IDataProvider provider)
    {
        return CompletableFuture.runAsync(() -> ExportTo(provider));
    }

    default void ExportTo(PlayerDatabase database)
    {
        for (PlayerData playerData : GetAll())
        {
            database.Update(playerData);
        }
    }
    default CompletableFuture<Void> ExportToAsync(PlayerDatabase database)
    {
        return CompletableFuture.runAsync(() -> ExportTo(database));
    }
}