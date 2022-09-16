package ru.reosfire.temporarywhitelist.data;

import ru.reosfire.temporarywhitelist.data.exporters.IDataExporter;

import java.util.concurrent.CompletableFuture;

public interface IDataProvider extends IDataExporter, IUpdatable
{
    CompletableFuture<Void> remove(String playerName);
    PlayerData get(String playerName);
}