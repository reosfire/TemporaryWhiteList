package ru.reosfire.twl.data;

import ru.reosfire.twl.data.exporters.IDataExporter;

import java.util.concurrent.CompletableFuture;

public interface IDataProvider extends IDataExporter, IUpdatable
{
    CompletableFuture<Void> remove(String playerName);
    PlayerData get(String playerName);
}