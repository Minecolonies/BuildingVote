package com.nojo121.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Vote extends JavaPlugin
{
    private File configf, userf;
    private FileConfiguration config, user;

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
    {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("bvote"))
        {
            if (sender instanceof Player)
            {
                if (args.length == 3)
                {
                    String juryMember = ((Player) sender).getDisplayName().toLowerCase();
                    String builder = args[0].toLowerCase();
                    String style = args[1].toLowerCase();
                    String pointsStr = args[2].toLowerCase();

                    if (user.contains("Jury-Member." + juryMember))
                    {
                        if (user.contains("Jury-Member." + juryMember + ".builder." + builder))
                        {
                            if (user.contains("Jury-Member." + juryMember + ".builder." + builder + ".style." + style))
                            {
                                if (isInteger(args[2]))
                                {
                                    int points = Integer.parseInt(args[2]);
                                    if (points <= 10 && points >= 0)
                                    {
                                        sender.sendMessage("You voted for " + builder + " " + style + ": " + pointsStr + " Points");
                                        getUserConfig().set("Jury-Member." + juryMember + ".builder." + builder + ".style." + style + ".points", pointsStr);
                                        try
                                        {
                                            getUserConfig().save(new File(getDataFolder(), "user.yml"));
                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        return true;
                                    }
                                    else
                                    {
                                        sender.sendMessage("Points have to be 0 or 1 or 2 or 3 or 4 or 5 or 6 or 7 or 8 or 9 or 10");
                                    }
                                }
                                else
                                {
                                    sender.sendMessage("Points have to be an Integer (0-10)");
                                }
                            }
                            else
                            {
                                sender.sendMessage("Style not valid");
                                sender.sendMessage("Usage: /bvote <builder> <style(wood,stone,sandstone)> <points(0-10)>");
                            }
                        }
                        else
                        {
                            sender.sendMessage("This Builder is not valid");
                        }
                    }
                    else
                    {
                        sender.sendMessage("You are not a valid Jury Member!");
                    }
                }
                else
                {
                    sender.sendMessage("Usage: /bvote <builder> <style(wood,stone,sandstone)> <points(0-10)>");
                }
            }
        }
        return false;
    }

    // args: 0 = builder      1 = style     2 = points

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        File dir = getDataFolder();
        if (!dir.exists())
        {
            if (!dir.mkdir())
            {
                getLogger().info("Could not create data folder");
            }
        }
        createFiles();

        getLogger().info("BuildingVote plugin started - Copyright nojo121 - nojo121.de");
    }

    public void createFiles()
    {
        configf = new File(getDataFolder(), "config.yml");
        userf = new File(getDataFolder(), "user.yml");

        if (!configf.exists())
        {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        if (!userf.exists())
        {
            userf.getParentFile().mkdirs();
            saveResource("user.yml", false);
        }

        config = new YamlConfiguration();
        user = new YamlConfiguration();

        try
        {
            config.load(configf);
            user.load(userf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
        if (userf.length() == 0)
        {
            getUserConfig().set("Jury-Member.nojo121.builder.theswedishpilot.style.wood.points", "500");
            getUserConfig().set("Jury-Member.nojo121.builder.theswedishpilot.style.stone.points", "500");
            getUserConfig().set("Jury-Member.nojo121.builder.theswedishpilot.style.sandstone.points", "500");
            try
            {
                getUserConfig().save(new File(getDataFolder(), "user.yml"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getUserConfig()
    {
        return this.user;
    }

    public static boolean isInteger(String s)
    {
        try
        {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        catch (NullPointerException e)
        {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
