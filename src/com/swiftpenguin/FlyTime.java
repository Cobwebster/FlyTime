package com.swiftpenguin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class FlyTime extends JavaPlugin implements Listener {

    public static Economy econ = null;
    ArrayList<String> flymode = new ArrayList<String>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        registerConfig();

        if (getConfig().getInt("Version") == 1) {

            int ver = getConfig().getInt("Version") + 1;
            getConfig().addDefault("BuyFlyTime.Discounted-Cost", 200);
            getConfig().options().copyDefaults(true);
            getConfig().set("Version", ver);
            saveConfig();

            System.out.println("[FlyTime] Discounted-Cost config added.");
        }

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        System.out.println("[FlyTime] Version: " + getDescription().getVersion());

        if (getServer().getPluginManager().getPlugin("ChatReaction") != null) {
            getServer().getPluginManager().registerEvents(new ChatReaction(this), this);
            System.out.println("[FlyTime] ChatReaction hook enabled.");
        } else {

        }

        if (getServer().getPluginManager().getPlugin("Jobs") != null) {
            getServer().getPluginManager().registerEvents(new JobsReborn(this), this);
            System.out.println("[FlyTime] JobsReborn hook enabled.");
        } else {

        }

        if (getServer().getPluginManager().getPlugin("aSkyBlock") != null) {
            getServer().getPluginManager().registerEvents(new aSkyBlock(this), this);
            System.out.println("[FlyTime] aSkyBlock hook enabled.");
        } else {

        }
    }

    public void onDisable() {
        for (String player : flymode) {

            Bukkit.getServer().getPlayer(player).setFlying(false);
            Bukkit.getServer().getPlayer(player).setAllowFlight(false);
            Bukkit.getServer().getPlayer(player).setFlySpeed(1);
        }
        System.out.println("[Flytime] Removed all current flying players.");
    }

    private void registerConfig() {
        saveDefaultConfig();

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (flymode.contains(e.getPlayer().getName())) {
            Player player = e.getPlayer();
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    public void paymentSuccess(CommandSender sender) {

        Player p = ((Player) sender).getPlayer();

        String enablemsg = getConfig().getString("Messages.Enable").replace("%time%", Integer.toString(getConfig().getInt("BuyFlyTime.FlyTime")));
        p.getPlayer().sendMessage(ChatColor.GREEN + enablemsg);
        p.getPlayer().setAllowFlight(true);
        p.getPlayer().setFlying(true);

        flymode.add(p.getPlayer().getName());

        System.out.println(((Player) sender).getPlayer().getName() + " Brought FlyTime");

        if (!getConfig().getBoolean("FlightSound.Enabled")) {
            return;

        }

        if (getServer().getVersion().contains("1.8")) {

        } else {

            String sound = getConfig().getString("FlightSound.Sound");
            int volume = getConfig().getInt("FlightSound.Volume");
            int pitch = getConfig().getInt("FlightSound.Pitch");
            p.playSound(p.getLocation(), Sound.valueOf(sound), volume, pitch);
        }

        int time = getConfig().getInt("BuyFlyTime.FlyTime");
        int warn = getConfig().getInt("BuyFlyTime.FlyTime") - 1;
        String warnmsg = getConfig().getString("Messages.Warn");
        String disablemsg = getConfig().getString("Messages.Disable");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            p.getPlayer().setAllowFlight(false);
            p.getPlayer().setFlying(false);
            p.getPlayer().sendMessage(ChatColor.RED + disablemsg);
            flymode.remove(p.getPlayer().getName());

        }, 1200 * time);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            p.getPlayer().sendMessage(ChatColor.YELLOW + warnmsg);

        }, 1200 * warn);
    }

    public void giveFlyTime(Player sender,Player p, int time) {

        p.setAllowFlight(true);
        p.setFlying(true);

        String warnmsg = getConfig().getString("Messages.Warn");
        String disablemsg = getConfig().getString("Messages.Disable");
        int warnTime = time - 1;

        flymode.add(p.getName());
        sender.sendMessage(ChatColor.GREEN + "You Gave " + p.getName() +" "+ time + " minutes of FlyTime");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            p.getPlayer().setAllowFlight(false);
            p.getPlayer().setFlying(false);
            p.getPlayer().sendMessage(ChatColor.RED + disablemsg);
            flymode.remove(p.getPlayer().getName());

        }, 1200 * time);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            p.getPlayer().sendMessage(ChatColor.YELLOW + warnmsg);

        }, 1200 * warnTime);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("flytime") && sender instanceof Player) {
            if (args.length == 0) {
                int time = getConfig().getInt("BuyFlyTime.FlyTime");
                double cost = getConfig().getDouble("BuyFlyTime.Cost");
                double dcost = getConfig().getDouble("BuyFlyTime.Discounted-Cost");

                if (!sender.hasPermission("flytime.vip")) {

                    sender.sendMessage(ChatColor.GOLD + "§m---------------------------------------------");
                    sender.sendMessage(ChatColor.GOLD + "§lFlyTime Info");
                    sender.sendMessage(ChatColor.GOLD + "");
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + "FlyTime in Minutes " + ChatColor.YELLOW + time);
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + "FlyTime Cost " + ChatColor.YELLOW + cost);
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + "To Buy use /FlyTime Buy");
                    sender.sendMessage(ChatColor.GOLD + "§m---------------------------------------------");
                    return true;

                } else {

                    sender.sendMessage(ChatColor.GOLD + "§m---------------------------------------------");
                    sender.sendMessage(ChatColor.GOLD + "§lFlyTime Info");
                    sender.sendMessage(ChatColor.GREEN + "VIP Discount");
                    sender.sendMessage(ChatColor.GOLD + "");
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + "FlyTime in Minutes " + ChatColor.YELLOW + time);
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + "FlyTime Cost " + ChatColor.YELLOW + dcost);
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + "To Buy use /FlyTime Buy");
                    sender.sendMessage(ChatColor.GOLD + "§m---------------------------------------------");
                    return true;
                }

            } else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("flytime.reload")) { // reload

                reloadConfig();
                sender.sendMessage(ChatColor.RED + "[FlyTime] Reloaded.");
                return true;

            } else if (args.length >= 3 && args[0].equalsIgnoreCase("give") && args[1] != null && !args[1].isEmpty() && !args[2].isEmpty() && sender.hasPermission("flytime.give")) {

                            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (p.isOnline()) {

                                if (!flymode.contains(p.getName())) {

                                    int time = Integer.parseInt(args[2]);
                                    Player player = Bukkit.getServer().getPlayer(args[1]);
                                    giveFlyTime(((Player) sender).getPlayer(),player, time);
                                    return true;
                                }

                            } else {
                                sender.sendMessage(ChatColor.RED + args[1] + " was not found.");
                            }

                        } else if (args[0].equalsIgnoreCase("buy")) { // cancel because of gamemode/flying
                            if (flymode.contains(sender.getName()) || ((Player) sender).getGameMode() == GameMode.CREATIVE) {
                                sender.sendMessage(ChatColor.RED + "Please wait for your FlyTime to expire!");

                            } else {

                                if (!sender.hasPermission("flytime.buy")) {
                                    sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");


                                } else {

                                    if (sender.hasPermission("flytime.vip")) {

                                        Player p = ((Player) sender).getPlayer();
                                        double cost = getConfig().getDouble("BuyFlyTime.Discounted-Cost");
                                        EconomyResponse r = econ.withdrawPlayer(p, cost);

                                        if (r.transactionSuccess()) {
                                            paymentSuccess(p);
                                            sender.sendMessage(String.format(ChatColor.GOLD + "You paid %s and now have %s", econ.format(r.amount), econ.format(r.balance)));

                                        } else {
                                            sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
                                            return true;
                                        }


                                    } else { // normal charge/buying

                                        Player p = ((Player) sender).getPlayer();
                                        double cost = getConfig().getDouble("BuyFlyTime.Cost");
                                        EconomyResponse r = econ.withdrawPlayer(p, cost);

                                        if (r.transactionSuccess()) {
                                            paymentSuccess(p);
                                            sender.sendMessage(String.format(ChatColor.GOLD + "You paid %s and now have %s", econ.format(r.amount), econ.format(r.balance)));

                                        } else {
                                            sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
                                            return true;
                                        }
                                    }
                                }
                            }
                            return true;
                        }
        }

        return true;
    }
}