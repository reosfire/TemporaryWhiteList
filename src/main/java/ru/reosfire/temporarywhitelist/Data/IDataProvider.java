package ru.reosfire.temporarywhitelist.Data;

import ru.reosfire.temporarywhitelist.Data.Exporters.IDataExporter;

import java.util.concurrent.CompletableFuture;

public interface IDataProvider extends IDataExporter
{
    CompletableFuture<Void> Update(PlayerData playerData);
    CompletableFuture<Void> Remove(String playerName);
    PlayerData Get(String playerName);
}