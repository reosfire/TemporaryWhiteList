package ru.reosfire.twl.common.data;

import ru.reosfire.twl.common.data.exporters.IDataExporter;

import java.util.concurrent.CompletableFuture;

public interface IDataProvider extends IDataExporter, IUpdatable
{
    CompletableFuture<Void> remove(String playerName);
    CompletableFuture<Void> clear();
    PlayerData get(String playerName);
}