package net.fenixazul.bt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BTArena {

	private String ArenaName;
	private String world;
	private Location Pos1, Pos2, Lobbyspawn;
	private List<Location> Gamespawns;
	private ItemStack winPrize;
	private List<Integer> Powerups;
	private boolean pvp;
	private ArrayList<Player> playersAttached; 

public BTArena(String name, Location loc1, Location loc2, String world){
	this.ArenaName = name;
	this.world = world;
	this.Pos1 = loc1;
	this.Pos2 = loc2;
	
}

//Overloaded constructor
public BTArena(String name, Location loc1, Location loc2, String world,Location lobbyspawn,List<Location> gamespawns,
		ItemStack winprize, List<Integer> powerups,Boolean pvp, ArrayList<Player> playersAttached){
	this.ArenaName = name;
	this.world = world;
	this.Pos1 = loc1;
	this.Pos2 = loc2;
	this.Lobbyspawn = lobbyspawn;
	this.Gamespawns = gamespawns;
	this.winPrize = winprize;
	this.Powerups = powerups;
	this.pvp = pvp;
	this.playersAttached = playersAttached;
	
}

public String getWorldName(){
	return world;
}
/**
 * Setter for Arena name, it overrides if there its a name already.
 * @param name Name of Arena
 */
public void setArenaName(String name){
	this.ArenaName = name;
}
/**
 * Getter for Arena name 
 * @return ArenaName
 */
public String getArenaName(){
	return this.ArenaName;
}
/**
 * Setter for world name, it overrides if there its a name already.
 * @param worldName Name of world
 */
public void setWorld(String worldName){
	this.world = worldName;
}
/**
 * Setter for Arena cuboid position 1.
 * @param pos1 Position Location
 */
public void setPos1(Location pos1){
	this.Pos1 = pos1;
}
/**
 * Setter for Arena cuboid position 2.
 * @param pos2 Position Location
 */
public void setPos2(Location pos2){
	this.Pos2 = pos2;
}
/**
 * Setter for Arena cuboid position 1.
 * @param pos1 Position Location
 */
public Location getPos1(){
	return this.Pos1;
}
/**
 * Setter for Arena cuboid position 2.
 * @param pos2 Position Location
 * @return 
 */
public Location getPos2(){
	return this.Pos2;
}

public void setLobbyspawn(Location pos){
	this.Lobbyspawn = pos;
}

//TODO
public void addGamespawn(Location loc){
	//search for current spawns, staring from S1 then S2 then S3
	this.Gamespawns.add(loc);
	
}
public void removeGamespawn(Location loc){
	
}

public void attachPlayer(Player player){
	playersAttached.add(player);
	
}
public void deatachPlayer(Player player){
	playersAttached.remove(player);
}
public boolean isPlayerAttached(Player player){
	
	if(playersAttached.contains(player))
	{
		return true;
	}
	return false;
}

public void setwinPrize(ItemStack item){
	this.winPrize = item;
}
public void setPowerUps(List<Integer> powerups2){
	this.Powerups = powerups2;
}
public void setpvp(Boolean flag){
	this.pvp = flag;
}

public Location getLobbyspawn() {
	return this.Lobbyspawn;
}
public void setGameSpawns(List<Location> gameSpawns) {
	this.Gamespawns = gameSpawns;
}
public List<Location> getGameSpawns() {
	return this.Gamespawns;
}
public ItemStack getwinPrize(){
	return this.winPrize;
}
public List<Integer> getPowerUps(){
	return this.Powerups;
}
public Boolean getpvp(){
	return this.pvp;
}
}
