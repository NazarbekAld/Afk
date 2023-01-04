package com.nazarxexe.afk.countdown;

import com.nazarxexe.afk.Afk;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    Player player;
    int count;

    Afk plugin;


    public Countdown(Player player, Afk plugin){
        this.player = player;
        this.count = 0;
        this.plugin = plugin;
    }

    public Countdown runtask ()
    {
        this.runTaskTimerAsynchronously(plugin, 0L, 20L);
        return this;
    }

    @Override
    public void run() {
        this.count++;
        if (this.count >= this.plugin.getConfig().getInt("Countdown")){
            this.count = 0;
            // reward
            new BukkitRunnable() {

                @Override
                public void run() {
                    for (String i : plugin.getConfig().getStringList("Rewards")){
                        plugin.getServer()
                                .dispatchCommand(plugin.getServer().getConsoleSender(),
                                        PlaceholderAPI.setPlaceholders(player, i));
                    }
                }
            }.runTask(plugin);
            // reward
            return;
        }

    }
}
