package ru.reosfire.temporarywhitelist.Data;

import java.util.concurrent.CompletableFuture;

public interface IUpdatable
{
    CompletableFuture<?> update(PlayerData playerData);
}