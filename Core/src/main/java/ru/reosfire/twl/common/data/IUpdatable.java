package ru.reosfire.twl.common.data;

import java.util.concurrent.CompletableFuture;

public interface IUpdatable
{
    CompletableFuture<?> update(PlayerData playerData); //TODO why ?
}