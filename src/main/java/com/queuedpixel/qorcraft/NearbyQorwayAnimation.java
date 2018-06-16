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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NearbyQorwayAnimation extends BukkitRunnable
{
    private final QorcraftPlugin qorcraftPlugin;

    // map of players to nearby Qorways
    private final Map< UUID, Set< Block >> playerBlockMap = new HashMap<>();

    // maximum distance of nearby Qorways
    private final int distance = 100;

    // step descriptions:
    //    - 0 : unchanged
    //    - 1 : white wool
    //    - 2 : unchanged
    //    - 3 : bedrock
    private int stepCount = 0;

    public NearbyQorwayAnimation( QorcraftPlugin qorcraftPlugin )
    {
        this.qorcraftPlugin = qorcraftPlugin;
    }

    public void run()
    {
        this.stepCount++;
        if ( this.stepCount >= 4 ) this.stepCount = 0;

        // update the block animation for each player
        for ( UUID playerId : this.playerBlockMap.keySet() )
        {
            // check if player has logged out
            Player player = Bukkit.getPlayer( playerId );
            if ( player == null )
            {
                this.playerBlockMap.remove( playerId );
            }
            else
            {
                this.updateBlocks( playerId );
            }
        }
    }

    void addPlayer( UUID playerId )
    {
        this.playerBlockMap.put( playerId, new HashSet<>() );
    }

    void removePlayer( UUID playerId )
    {
        if ( this.playerBlockMap.containsKey( playerId ))
        {
            restoreBlocks( playerId );
            this.playerBlockMap.remove( playerId );
        }
    }

    @SuppressWarnings( "deprecation" )
    private void updateBlocks( UUID playerId )
    {
        Player player = Bukkit.getPlayer( playerId );
        if ( player == null ) return;

        this.restoreBlocks( playerId );
        Set< Block > blocks = this.getNearbyQorways( playerId );
        this.playerBlockMap.put( playerId, blocks );

        for ( Block block : blocks )
        {
            Material material;
            byte data;
            switch ( this.stepCount )
            {
                case 1  : material = Material.WOOL;    data = 0;               break;  // white wool
                case 3  : material = Material.BEDROCK; data = 0;               break;  // bedrock
                default : material = block.getType();  data = block.getData(); break;  // original material
            }

            player.sendBlockChange( block.getLocation(), material, data );
        }
    }

    @SuppressWarnings( "deprecation" )
    private void restoreBlocks( UUID playerId )
    {
        Player player = Bukkit.getPlayer( playerId );
        if ( player == null ) return;

        Set< Block > blocks = this.playerBlockMap.get( player.getUniqueId() );
        for ( Block block : blocks ) player.sendBlockChange( block.getLocation(), block.getType(), block.getData() );
    }

    private Set< Block > getNearbyQorways( UUID playerId )
    {
        Set< Block > blocks = new HashSet<>();
        Player player = Bukkit.getPlayer( playerId );
        if ( player == null ) return blocks;

        Location location = player.getLocation();
        for ( Qorway qorway : this.qorcraftPlugin.qorwayManager.getNearby(
                location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ() ))
        {
            if (( player.getLocation().getWorld().getName().equals( qorway.world )) &&
                ( Math.abs( player.getLocation().getBlockX() - qorway.x ) < this.distance ) &&
                ( Math.abs( player.getLocation().getBlockY() - qorway.y ) < this.distance ) &&
                ( Math.abs( player.getLocation().getBlockZ() - qorway.z ) < this.distance ))
            {
                blocks.add( Bukkit.getWorld( qorway.world ).getBlockAt( qorway.x, qorway.y, qorway.z ));
            }
        }

        return blocks;
    }
}
