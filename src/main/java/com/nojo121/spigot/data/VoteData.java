package com.nojo121.spigot.data;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public final class VoteData
{

    private static final String CONFIG_YML      = "config.yml";
    private static final String VOTES_YML       = "votes.yml";
    private static final String JURORS          = "jurors";
    private static final String BUILDERS        = "builders";
    private static final String EXAMPLE_JUROR   = "Kostronor";
    private static final String EXAMPLE_BUILDER = "nm2588";

    /**
     * The main config file for the plugin.
     */
    private FileConfiguration config;

    /**
     * Here are all votes stored.
     */
    private FileConfiguration votes;

    private File configFile;
    private File votesFile;

    private VoteData()
    {
        // empty
    }

    private static VoteData instance;

    public static synchronized VoteData getVoteData()
    {
        if (instance == null)
        {
            instance = new VoteData();
        }
        return instance;
    }

    public synchronized void loadAndInit(final JavaPlugin plugin) throws IOException, InvalidConfigurationException
    {
        configFile = new File(plugin.getDataFolder(), CONFIG_YML);
        votesFile = new File(plugin.getDataFolder(), VOTES_YML);

        // create empty files if not existing
        if (!configFile.exists())
        {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(CONFIG_YML, false);
        }
        if (!votesFile.exists())
        {
            votesFile.getParentFile().mkdirs();
            plugin.saveResource(VOTES_YML, false);
        }

        config = new YamlConfiguration();
        votes = new YamlConfiguration();

        initConfigs();
    }

    private synchronized void initConfigs() throws IOException, InvalidConfigurationException
    {
        config.load(configFile);
        initConfig();

        votes.load(votesFile);
        initVotes();
    }

    private synchronized void initConfig() throws IOException, InvalidConfigurationException
    {
        if (!config.contains(JURORS, false))
        {
            final LinkedList<String> list = new LinkedList<>();
            list.add(EXAMPLE_JUROR);
            config.set(JURORS, list);
        }
        if (!config.contains(BUILDERS, false))
        {
            final LinkedList<String> list = new LinkedList<>();
            list.add("wood");
            list.add("stone");
            list.add("sandstone");
            config.set(BUILDERS + '.' + EXAMPLE_BUILDER, list);
        }
    }

    private synchronized void initVotes() throws IOException, InvalidConfigurationException
    {
        if (!votes.contains(JURORS, false))
        {
            final LinkedList<String> list = new LinkedList<>();
            list.add(EXAMPLE_JUROR);
            votes.set(JURORS, list);
        }
    }
}
