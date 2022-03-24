package ru.reosfire.temporarywhitelist.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDataProvider
{
    CompletableFuture<Void> Update(PlayerData playerData);
    CompletableFuture<Void> Remove(String playerName);
    PlayerData Get(String playerName);
    List<PlayerData> GetAll();
}