package ru.reosfire.temporarywhitelist.Data;

import java.util.concurrent.CompletableFuture;

public interface IUpdatable
{
    CompletableFuture<?> Update(PlayerData playerData);
}