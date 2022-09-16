package ru.reosfire.temporarywhitelist.data;

import java.util.concurrent.CompletableFuture;

public interface IUpdatable
{
    CompletableFuture<?> update(PlayerData playerData);
}