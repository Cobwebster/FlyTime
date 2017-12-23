package com.swiftpenguin;

import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public class JobsReborn implements Listener {

    private FlyTime plugin;

    public JobsReborn(FlyTime plugin) {
        this.plugin = plugin;
    }

    ArrayList<String> flymodeusing = new ArrayList<String>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (flymodeusing.contains(e.getPlayer().getName())) {
            Player player = e.getPlayer();
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    @EventHandler
    public void LeveLUP(JobsLevelUpEvent e) {
        if (!plugin.getConfig().getBoolean("JobsReborn.Enabled")) {
        } else {

            UUID uuid = e.getPlayer().getPlayerUUID();
            Player p = Bukkit.getServer().getPlayer(uuid);

            if (!p.hasPermission("flytime.jobsreborn")) {

            } else {

                String enablemsg = plugin.getConfig().getString("Messages.Enable");

                p.getPlayer().setAllowFlight(true);
                p.getPlayer().setFlying(true);
                flymodeusing.add(p.getPlayer().getName());
                p.getPlayer().sendMessage(ChatColor.GREEN + enablemsg);

                int time = plugin.getConfig().getInt("JobsReborn.FlyTime");
                int warn = plugin.getConfig().getInt("JobsReborn.FlyTime") - 1;
                String warnmsg = plugin.getConfig().getString("Messages.Warn");
                String disablemsg = plugin.getConfig().getString("Messages.Disable");

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    p.getPlayer().setAllowFlight(false);
                    p.getPlayer().setFlying(false);
                    flymodeusing.remove(p.getPlayer().getName());
                    p.getPlayer().sendMessage(ChatColor.RED + disablemsg);

                }, 1200 * time);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    p.getPlayer().sendMessage(ChatColor.RED + warnmsg);

                }, 1200 * warn);
            }
        }
    }
}