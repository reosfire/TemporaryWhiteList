package ru.reosfire.temporarywhitelist.Data.Exporters;

import ru.reosfire.temporarywhitelist.Data.PlayerData;

import javax.management.ReflectionException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EasyWhitelist implements IDataExporter
{
    private final long defaultTimeAmount;
    private final boolean defaultPermanent;
    private final Method getWhiteListsMethod;
    private final Object WLStorage;

    public EasyWhitelist(long defaultTimeAmount, boolean defaultPermanent) throws ReflectionException
    {
        this.defaultTimeAmount = defaultTimeAmount;
        this.defaultPermanent = defaultPermanent;

        try
        {
            Class<?> easyWhiteListClass = Class.forName("com.gmail.jyckosianjaya.easywhitelist.EasyWhiteList");
            Method getInstanceMethod = easyWhiteListClass.getMethod("getInstance");
            Method getStorageMethod = easyWhiteListClass.getMethod("getStorage");

            Object easyWhiteListInstance = getInstanceMethod.invoke(null);

            WLStorage = getStorageMethod.invoke(easyWhiteListInstance);

            Class<?> WLStorageClass = Class.forName("com.gmail.jyckosianjaya.easywhitelist.WLStorage");
            getWhiteListsMethod = WLStorageClass.getMethod("getWhiteLists");
        }
        catch (Exception e)
        {
            throw new ReflectionException(e, "Can't load easy-whitelist plugin");
        }
    }

    @Override
    public List<PlayerData> getAll()
    {
        long currentTime = Instant.now().getEpochSecond();
        ArrayList<PlayerData> result = new ArrayList<>();

        try
        {
            for (Object player : (ArrayList<?>) getWhiteListsMethod.invoke(WLStorage))
            {
                if (!(player instanceof String)) continue;
                result.add(new PlayerData((String) player, currentTime, defaultTimeAmount, defaultPermanent));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error while getting data from easy-whitelist", e);
        }

        return result;
    }
}