package com.swiftpenguin;

import com.wasteofplastic.askyblock.events.IslandEnterEvent;
import com.wasteofplastic.askyblock.events.IslandExitEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class aSkyBlock implements Listener {

    private FlyTime plugin;

    public aSkyBlock(FlyTime plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void Enter(IslandEnterEvent e) {
        if (!plugin.getConfig().getBoolean("aSkyBlock.Enabled")) {

        } else {

            final UUID player = e.getPlayer();
            final Player p = Bukkit.getServer().getPlayer(player);

            if (p.hasPermission("flytime.skyblockfly")) {

                String msgenable = plugin.getConfig().getString("Messages.Enable");
                p.sendMessage(ChatColor.GREEN + msgenable);
                p.setAllowFlight(true);
                p.setFlying(true);
            }
        }
    }

    @EventHandler
    public void Exit(IslandExitEvent e) {
        if (!plugin.getConfig().getBoolean("aSkyBlock.Enabled")) {
        } else {

            final UUID player = e.getPlayer();
            final Player p = Bukkit.getServer().getPlayer(player);

            if (p.getAllowFlight() == true) {

                String msgdisable = plugin.getConfig().getString("Messages.Disable");
                p.sendMessage(ChatColor.RED + msgdisable);
                p.setFlying(false);
                p.setAllowFlight(false);
            }

        }
    }
}