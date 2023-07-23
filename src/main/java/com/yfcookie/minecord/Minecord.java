package com.yfcookie.minecord;

import org.bukkit.plugin.java.JavaPlugin;

public final class Minecord extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        ChatSync.sendMessage("serverStart", "", "");

    }

    @Override
    public void onDisable() {
        ChatSync.sendMessage("serverStop", "", "");
    }
}
