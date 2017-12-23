<<<<<<< HEAD
package com.swiftpenguin;

import me.clip.chatreaction.events.ReactionWinEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

import static org.bukkit.event.EventPriority.HIGHEST;

public class ChatReaction implements Listener {

    private FlyTime plugin;

    public ChatReaction(FlyTime plugin) {
        this.plugin = plugin;
    }

    ArrayList<String> flymodeuse = new ArrayList<String>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (flymodeuse.contains(e.getPlayer().getName())) {
            Player player = e.getPlayer();
                player.setAllowFlight(true);
                player.setFlying(true);
            } else {

            }
        }

    @EventHandler(priority = HIGHEST, ignoreCancelled = true)
    public void onReactionWin(ReactionWinEvent event) {

        if (!plugin.getConfig().getBoolean("ChatReaction.Enabled")) {
        } else {
            Player player = event.getWinner();

            if (!player.hasPermission("flytime.chatreactionfly")) {

            } else {

                int time = plugin.getConfig().getInt("ChatReaction.FlyTime");
                int warn = plugin.getConfig().getInt("ChatReaction.FlyTime") - 1;
                String warnmsg = plugin.getConfig().getString("Messages.Warn");
                String enablemsg = plugin.getConfig().getString("Messages.Enable");
                String disablemsg = plugin.getConfig().getString("Messages.Disable");

                Bukkit.getScheduler().runTask(plugin, () -> {
                    flymodeuse.add(player.getName());
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(ChatColor.GREEN + enablemsg);

                });

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    flymodeuse.remove(player.getName());
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(ChatColor.RED + disablemsg);

                }, 1200 * time);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.sendMessage(ChatColor.YELLOW + warnmsg);

                }, 1200 * warn);
            }
        }
    }

=======
package com.swiftpenguin;

import me.clip.chatreaction.events.ReactionWinEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

import static org.bukkit.event.EventPriority.HIGHEST;

public class ChatReaction implements Listener {

    private FlyTime plugin;

    public ChatReaction(FlyTime plugin) {
        this.plugin = plugin;
    }

    ArrayList<String> flymodeuse = new ArrayList<String>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (flymodeuse.contains(e.getPlayer().getName())) {
            Player player = e.getPlayer();
                player.setAllowFlight(true);
                player.setFlying(true);
            } else {

            }
        }

    @EventHandler(priority = HIGHEST, ignoreCancelled = true)
    public void onReactionWin(ReactionWinEvent event) {

        if (!plugin.getConfig().getBoolean("ChatReaction.Enabled")) {
        } else {
            Player player = event.getWinner();

            if (!player.hasPermission("flytime.chatreactionfly")) {

            } else {

                int time = plugin.getConfig().getInt("ChatReaction.FlyTime");
                int warn = plugin.getConfig().getInt("ChatReaction.FlyTime") - 1;
                String warnmsg = plugin.getConfig().getString("Messages.Warn");
                String enablemsg = plugin.getConfig().getString("Messages.Enable");
                String disablemsg = plugin.getConfig().getString("Messages.Disable");

                Bukkit.getScheduler().runTask(plugin, () -> {
                    flymodeuse.add(player.getName());
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(ChatColor.GREEN + enablemsg);

                });

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    flymodeuse.remove(player.getName());
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(ChatColor.RED + disablemsg);

                }, 1200 * time);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.sendMessage(ChatColor.YELLOW + warnmsg);

                }, 1200 * warn);
            }
        }
    }

>>>>>>> 2fe1d9cb541fc6de1cf021ce2c0893e2a1bad4c7
}