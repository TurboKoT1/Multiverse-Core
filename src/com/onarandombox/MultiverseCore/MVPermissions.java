package com.onarandombox.MultiverseCore;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.nijiko.permissions.Group;

public class MVPermissions {
    
    private MultiverseCore plugin;
    
    /**
     * Constructor FTW
     * 
     * @param plugin Pass along the Core Plugin.
     */
    public MVPermissions(MultiverseCore plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Check if the player has the following Permission node, if a Permissions plugin is not installed then we default to isOp()
     * 
     * @param p The player instance.
     * @param node The permission node we are checking against.
     * @return
     */
    public boolean has(Player p, String node) {
        boolean result = false;
        
        if (MultiverseCore.Permissions != null) {
            result = MultiverseCore.Permissions.has(p, node);
        } else if (p.isOp()) {
            result = true;
        }
        
        return result;
    }
    
    /**
     * Check if a Player can teleport to the Destination world from there current world. This checks against the Worlds Blacklist
     * 
     * @param p
     * @param w
     * @return
     */
    public Boolean canTravelFromWorld(Player p, World w) {
        List<String> blackList = this.plugin.worlds.get(w.getName()).worldBlacklist;
        
        boolean returnValue = true;
        
        if (blackList.size() == 0) {
            returnValue = true;
        }
        
        for (int i = 0; i < blackList.size(); i++) {
            if (blackList.get(i).equalsIgnoreCase(p.getWorld().getName())) {
                returnValue = false;
                break;
            }
        }
        
        return returnValue;
    }
    
    /**
     * Check if the Player has the permissions to enter this world.
     * 
     * @param p
     * @param w
     * @return
     */
    public Boolean canEnterWorld(Player p, World w) {
        
        List<String> whiteList = this.plugin.worlds.get(w.getName()).joinWhitelist;
        List<String> blackList = this.plugin.worlds.get(w.getName()).joinBlacklist;
        
        boolean returnValue = true;
        
        // TODO: Not sure if I want this.
        // if (whiteList.size() > 0) {
        // returnValue = false;
        // }
        for (String bls : blackList) {
            System.out.print(w.getName() + " has " + bls + " BLACKlisted");
            if (bls.contains("g:") && inGroup(p, w.getName(), bls.split(":")[1])) {
                System.out.print(p.getName() + " Is on the BLACKlist for " + w.getName());
                returnValue = false;
                break;
            }
            if (bls.equalsIgnoreCase(p.getName())) {
                System.out.print(p.getName() + " Is on the BLACKlist for " + w.getName());
                returnValue = false;
                break;
            }
        }
        for (String wls : whiteList) {
            System.out.print(w.getName() + " has " + wls + " WHTIElisted");
            if (wls.contains("g:") && inGroup(p, w.getName(), wls.split(":")[1])) {
                System.out.print(p.getName() + " Is on the WHITElist for " + w.getName());
                returnValue = true;
                break;
            }
            if (wls.equalsIgnoreCase(p.getName())) {
                System.out.print(p.getName() + " Is on the WHITElist for " + w.getName());
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }
    
    /**
     * Returns true if a player is in a group.
     * 
     * @param player The player to check
     * @param worldName The world to check in
     * @param group The group are we checking
     * @return True if the player is in the group, false if not.
     */
    private boolean inGroup(Player player, String worldName, String group) {
        if (MultiverseCore.Permissions != null) {
            return MultiverseCore.Permissions.inGroup(worldName, player.getName(), group);
        } else {
            return player.isOp();
        }
    }
}