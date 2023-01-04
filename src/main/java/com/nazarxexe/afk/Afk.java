package com.nazarxexe.afk;

import com.nazarxexe.afk.countdown.Countdown;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;

public final class Afk extends JavaPlugin {

    public static HashMap<Player, Countdown> playersonafk;
    Afk plugin;

    String region;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        playersonafk = new HashMap<>();
        saveDefaultConfig();
        region = getConfig().getString("Region");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            for (int i=0; i<= 5; i++){
                System.out.println("NO PLACEHOLDER API!!!");
            }
            Bukkit.getPluginManager().disablePlugin(this);
        }

        BukkitRunnable afkchecker = new BukkitRunnable() {

            @Override
            public void run() {
                for (Player p : getServer().getOnlinePlayers()){
                    if (PlaceholderAPI.setPlaceholders(p, "%worldguard_region_name%").equals(region)){
                        if (playersonafk.get(p) != null) return;
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou in the afk zone!"));
                        playersonafk.put(p, new Countdown(p, plugin)
                                .runtask());

                    }
                    if (!(PlaceholderAPI.setPlaceholders(p, "%worldguard_region_name%").equals(region))){
                        if (playersonafk.get(p) == null) return;
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou left afk zone!"));
                        playersonafk.get(p).cancel();
                        playersonafk.remove(p);
                    }
                }
            }
        };
        afkchecker.runTaskTimerAsynchronously(this, 0L, 20L);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
