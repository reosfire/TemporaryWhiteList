package ru.reosfire.temporarywhitelist.Data.Exporters;

import ru.reosfire.temporarywhitelist.Data.PlayerData;

import java.util.List;

public interface IDataExporter
{
    List<PlayerData> GetAll(long defaultTimeAmount, boolean defaultPermanent);

    default List<PlayerData> GetAll()
    {
        return GetAll(0,true);
    }
}