package fr.edminecoreteam.core.teams;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamsTagsManager
{
    private String prefix;
    private String suffix;
    private Team team;
    public static Scoreboard scoreboard;
    
    public TeamsTagsManager(String name, String prefix, String suffix, Scoreboard current) throws Exception {
        this.prefix = prefix;
        this.suffix = suffix;
        this.team = current.getTeam(name);
        if (this.team == null) {
            this.team = current.registerNewTeam(name);
        }
        TeamsTagsManager.scoreboard = current;
        this.team.setCanSeeFriendlyInvisibles(false);
        this.team.setAllowFriendlyFire(true);
        int prefixLength = 0;
        int suffixLength = 0;
        if (prefix != null) {
            prefixLength = prefix.length();
        }
        if (suffix != null) {
            suffixLength = suffix.length();
        }
        if (prefixLength + suffixLength >= 32) {
            throw new Exception("prefix and suffix lenghts are greater than 16");
        }
        if (suffix != null) {
            this.team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));
        }
        if (prefix != null) {
            this.team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
        }
    }
    
    public TeamsTagsManager(String name, String prefix, String suffix) throws Exception {
        this(name, prefix, suffix, Bukkit.getScoreboardManager().getMainScoreboard());
    }
    
    @SuppressWarnings("deprecation")
	public void set(Player player) {
        team.addPlayer((OfflinePlayer)player);
        player.setScoreboard(TeamsTagsManager.scoreboard);
    }
    
    @SuppressWarnings("deprecation")
	public void remove(Player player) {
        team.removePlayer((OfflinePlayer)player);
    }
    
    public void resetTagUtils(UUID uuid) {
        remove(Bukkit.getPlayer(uuid));
    }
    
    public void setAll(Collection<Player> players) {
        for (Player player : players) {
            set(player);
        }
    }
    
    public void setAll(Player[] players) {
        Player[] arrayOfPlayer = players;
        for (int j = players.length, i = 0; i < j; ++i) {
            Player player = arrayOfPlayer[i];
            set(player);
        }
    }
    
    public void setPrefix(String prefix) {
        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        team.setPrefix(prefix);
    }
    
    public void setSuffix(String suffix) {
        this.suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        team.setSuffix(this.suffix);
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void removeTeam() {
        team.unregister();
    }
    
    public Scoreboard getScoreboard() {
        return TeamsTagsManager.scoreboard;
    }
    
    public static void setNameTag(Player player, String name, String prefix, String suffix) {
        try {
            TeamsTagsManager tagplayer = new TeamsTagsManager(name, prefix, suffix);
            tagplayer.set(player);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
