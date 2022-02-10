package ru.reosfire.temporarywhitelist.Data;

import java.util.List;

public interface IDataProvider
{
    boolean CanJoin(String nick);
    void Add(String nick, long addedTime) throws Exception;
    void Add(String nick) throws Exception;
    void Remove(String nick) throws Exception;
    void SetPermanent(String nick, boolean permanent) throws Exception;
    String Check(String nick) throws Exception;;
    List<String> ActiveList() throws Exception;;
    List<String> AllList() throws Exception;;
}