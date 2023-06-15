package ru.reosfire.twl.common;

import ru.reosfire.twl.common.configuration.Config;
import ru.reosfire.twl.common.configuration.localization.MessagesConfig;
import ru.reosfire.twl.common.data.PlayerDatabase;
import ru.reosfire.twl.common.versioning.VersionChecker;

public interface CommonTwlApi {

    Config getConfiguration();
    PlayerDatabase getDatabase();
    MessagesConfig getMessages();
    TimeConverter getTimeConverter();
    VersionChecker getVersionChecker();

    boolean disable();
    boolean enable();
    void reload();
}