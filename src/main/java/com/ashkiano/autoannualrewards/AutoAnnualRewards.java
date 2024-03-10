package com.ashkiano.autoannualrewards;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;

public class AutoAnnualRewards extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        Metrics metrics = new Metrics(this, 21295);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                String playerName = event.getPlayer().getName();
                Calendar now = Calendar.getInstance();
                int currentYear = now.get(Calendar.YEAR);
                int lastClaimedYear = getConfig().getInt("rewards." + playerName + ".year", -1);

                if (currentYear != lastClaimedYear) {
                    String rewardCommand = getConfig().getString("reward-command").replace("%player%", playerName);
                    getServer().dispatchCommand(getServer().getConsoleSender(), rewardCommand);
                    event.getPlayer().sendMessage("You have claimed your annual reward.");

                    getConfig().set("rewards." + playerName + ".year", currentYear);
                    saveConfig();
                }
            }
        }.runTaskLater(this, 1200L);
    }
}
