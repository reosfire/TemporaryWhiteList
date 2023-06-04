package ru.reosfire.twl;

public class TWLUser {

    @SuppressWarnings("unused")
    //This thing need to make shade plugin not delete everything on optimize jar
    public void use() {
        ru.reosfire.twl.spigot.TemporaryWhiteList spigot = new ru.reosfire.twl.spigot.TemporaryWhiteList();
        ru.reosfire.twl.velocity.TemporaryWhiteList velocity = new ru.reosfire.twl.velocity.TemporaryWhiteList();
    }
}