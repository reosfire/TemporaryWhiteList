package ru.reosfire.temporarywhitelist.Data.Exporters;

import ru.reosfire.temporarywhitelist.Data.PlayerData;

import java.util.List;

public interface IDataExporter
{
    List<PlayerData> GetAll();
}