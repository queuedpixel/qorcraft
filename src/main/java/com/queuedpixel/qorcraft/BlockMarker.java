/*

qorcraft : Players fight for control of special locations within a Minecraft world.

Copyright (c) 2018 Queued Pixel <git@queuedpixel.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

package com.queuedpixel.qorcraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BlockMarker extends BukkitRunnable implements Listener
{
    // map of player to the current block they are looking at
    private Map< UUID, Block > playerBlockMap = new HashMap<>();

    // step descriptions:
    //    - 0 : unchanged
    //    - 1 : purple wool
    //    - 2 : unchanged
    //    - 3 : bedrock
    int stepCount = 0;

    public void run()
    {
        this.stepCount++;
        if ( this.stepCount >= 4 ) this.stepCount = 0;

        // update the block animation for each player
        for ( UUID playerId : this.playerBlockMap.keySet() )
        {
            this.updateBlock( playerId );
        }
    }

    @EventHandler
    public void onPlayerMoveEvent( PlayerMoveEvent event )
    {
        // update the block animation for the player
        UUID playerId = event.getPlayer().getUniqueId();
        if ( this.playerBlockMap.containsKey( playerId )) this.updateBlock( playerId );
    }

    public void addPlayer( Player player )
    {
        UUID playerId = player.getUniqueId();
        this.playerBlockMap.put( playerId, null );
        this.updateBlock( playerId );
    }

    public void removePlayer( Player player )
    {
        UUID playerId = player.getUniqueId();

        if ( this.playerBlockMap.containsKey( playerId ))
        {
            this.restoreBlock( player );
            this.playerBlockMap.remove( playerId );
        }
    }

    @SuppressWarnings( "deprecation" )
    private void updateBlock( UUID playerId )
    {
        Player player = Bukkit.getPlayer( playerId );

        // check to see if the player has logged out
        if ( player == null )
        {
            this.playerBlockMap.remove( playerId );
            return;
        }

        this.restoreBlock( player );
        Block block = player.getTargetBlock( IgnoredMaterials.getMaterials(), 100 );
        if ( block.getType() != Material.AIR )
        {
            this.playerBlockMap.put( playerId, block );

            Material material;
            byte data;
            switch ( this.stepCount )
            {
                case 1 : material = Material.WOOL; data = 2; break;     // purple wool
                case 3 : material = Material.BEDROCK; data = 0; break;  // bedrock
                default :
                    material = this.playerBlockMap.get( playerId ).getType();
                    data = this.playerBlockMap.get( playerId ).getData();
                    break;
            }

            Bukkit.getServer().getPlayer( playerId ).sendBlockChange( block.getLocation(), material, data );
        }
        else
        {
            this.playerBlockMap.put( playerId, null );
        }
    }

    @SuppressWarnings( "deprecation" )
    private void restoreBlock( Player player )
    {
        Block oldBlock = this.playerBlockMap.get( player.getUniqueId() );
        if ( oldBlock != null )
        {
            // return the old block to its original status
            player.sendBlockChange( oldBlock.getLocation(), oldBlock.getType(), oldBlock.getData() );
        }
    }
}
