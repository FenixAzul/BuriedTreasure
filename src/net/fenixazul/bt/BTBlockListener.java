
package net.fenixazul.bt;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * Buried Treasure Alpha Code
 * 
 * @author FenixAzul, with the Help[HUGE] of Humsas & StrikeForceZero <3.
 */
public class BTBlockListener extends BlockListener {
    private final BuriedTreasure plugin;

    public BTBlockListener(final BuriedTreasure plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();

        if ((block.getType() == Material.SAND) || (block.getType() == Material.GRAVEL)) {
            Block above = block.getFace(BlockFace.UP);
            if (above.getType() == Material.IRON_BLOCK) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        Material mat = event.getMaterial();

        if (mat.equals(Material.CACTUS)) {
            event.setBuildable(true);
        }
    }
    
	//Cancels Block Breaking when using wand
    @Override
	public void onBlockBreak(BlockBreakEvent event){
		
		if(plugin.isBearingWand(event.getPlayer())){
			event.setCancelled(true);
			
		}
		
	}
}
