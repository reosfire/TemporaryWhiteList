package ru.reosfire.temporarywhitelist.Data.Exporters;

import ru.reosfire.temporarywhitelist.Data.PlayerData;

import javax.management.ReflectionException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EasyWhitelist implements IDataExporter
{
    private final long _defaultTimeAmount;
    private final boolean _defaultPermanent;
    private final Method _getWhiteListsMethod;
    private final Object _WLStorage;

    public EasyWhitelist(long defaultTimeAmount, boolean defaultPermanent) throws ReflectionException
    {
        _defaultTimeAmount = defaultTimeAmount;
        _defaultPermanent = defaultPermanent;

        try
        {
            Class<?> easyWhiteListClass = Class.forName("com.gmail.jyckosianjaya.easywhitelist.EasyWhiteList");
            Method getInstanceMethod = easyWhiteListClass.getMethod("getInstance");
            Method getStorageMethod = easyWhiteListClass.getMethod("getStorage");

            Object easyWhiteListInstance = getInstanceMethod.invoke(null);

            _WLStorage = getStorageMethod.invoke(easyWhiteListInstance);

            Class<?> WLStorageClass = Class.forName("com.gmail.jyckosianjaya.easywhitelist.WLStorage");
            _getWhiteListsMethod = WLStorageClass.getMethod("getWhiteLists");
        }
        catch (Exception e)
        {
            throw new ReflectionException(e, "Can't load easy-whitelist plugin");
        }
    }

    @Override
    public List<PlayerData> GetAll()
    {
        long currentTime = Instant.now().getEpochSecond();
        ArrayList<PlayerData> result = new ArrayList<>();

        try
        {
            for (Object player : (ArrayList<?>)_getWhiteListsMethod.invoke(_WLStorage))
            {
                if (!(player instanceof String)) continue;
                result.add(new PlayerData((String) player, currentTime, _defaultTimeAmount, _defaultPermanent));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while getting data from easy-whitelist", e);
        }

        return result;
    }
}