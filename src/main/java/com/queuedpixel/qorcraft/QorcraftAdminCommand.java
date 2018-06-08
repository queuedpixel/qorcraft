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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class QorcraftAdminCommand extends BukkitRunnable implements CommandExecutor, Listener
{
    private final QorcraftMap qorcraftMap;

    // map of players to the current block they are looking at
    private final Map< UUID, Block > playerBlockMap = new HashMap<>();

    // map of players to the time they started a timed command
    private final Map< UUID, Long > playerTimeMap = new HashMap<>();

    // step descriptions:
    //    - 0 : unchanged
    //    - 1 : purple wool
    //    - 2 : unchanged
    //    - 3 : bedrock
    private int stepCount = 0;

    public QorcraftAdminCommand( QorcraftMap qorcraftMap )
    {
        this.qorcraftMap = qorcraftMap;
    }

    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        if ( !( sender instanceof Player ))
        {
            sender.sendMessage( ChatColor.RED + "You are not a player." );
            return true;
        }

        Player player = (Player) sender;

        if ( !sender.hasPermission( "qorcraft.admin" ))
        {
            sender.sendMessage( ChatColor.RED + "You do not have permission to do this." );
            return true;
        }

        if ( args.length < 1 )
        {
            sender.sendMessage( ChatColor.RED + "You must specify a sub-command." );
            this.sendUsage( sender );
            return true;
        }

        String subCommand = args[ 0 ].toLowerCase();
        if ( subCommand.equals( "create" ))
        {
            sender.sendMessage( ChatColor.AQUA + "Creating new Qorway." );
            sender.sendMessage( ChatColor.LIGHT_PURPLE + "Click on a block within 15 seconds to create a Qorway." );
            this.addPlayer( player.getUniqueId() );
            return true;
        }
        else
        {
            sender.sendMessage( ChatColor.RED + "Unrecognized sub-command." );
            this.sendUsage( sender );
            return true;
        }
    }

    public void run()
    {
        this.stepCount++;
        if ( this.stepCount >= 4 ) this.stepCount = 0;

        long now = new Date().getTime();
        Set< UUID > expiredPlayers = new HashSet<>();

        // update the block animation for each player
        for ( UUID playerId : this.playerBlockMap.keySet() )
        {
            // check if player has logged out
            Player player = Bukkit.getPlayer( playerId );
            if ( player == null )
            {
                expiredPlayers.add( playerId );
            }
            else
            {
                // check if 15 seconds has elapsed since player called command
                if ( this.playerTimeMap.get( playerId ) + 15000 <= now )
                {
                    player.sendMessage( ChatColor.RED + "Qorway creation timed out." );
                    expiredPlayers.add( playerId );
                }
                else
                {
                    this.updateBlock( playerId );
                }
            }
        }

        // remove players who have had their timer expire or have logged out
        for ( UUID playerId : expiredPlayers )
        {
            this.removePlayer( playerId );
        }
    }

    @EventHandler
    public void onPlayerMoveEvent( PlayerMoveEvent event )
    {
        // update the block animation for the player
        UUID playerId = event.getPlayer().getUniqueId();
        if ( this.playerBlockMap.containsKey( playerId )) this.updateBlock( playerId );
    }

    @EventHandler
    public void onPlayerInteractEvent( PlayerInteractEvent event )
    {
        UUID playerId = event.getPlayer().getUniqueId();
        if ( this.playerBlockMap.containsKey( playerId ))
        {
            this.removePlayer( event.getPlayer().getUniqueId() );
            Block block = event.getPlayer().getTargetBlock( IgnoredMaterials.getMaterials(), 100 );
            this.qorcraftMap.addQorway( block.getWorld().getName(), block.getX(), block.getY(), block.getZ() );
        }
    }

    private void sendUsage( CommandSender sender )
    {
        sender.sendMessage( ChatColor.GREEN + "Available sub-commands: " + ChatColor.LIGHT_PURPLE + "create" );
    }

    private void addPlayer( UUID playerId )
    {
        this.playerBlockMap.put( playerId, null );
        this.updateBlock( playerId );
        this.playerTimeMap.put( playerId, new Date().getTime() );
    }

    private void removePlayer( UUID playerId )
    {
        if ( this.playerBlockMap.containsKey( playerId ))
        {
            this.restoreBlock( playerId );
            this.playerBlockMap.remove( playerId );
        }

        if ( this.playerTimeMap.containsKey( playerId )) this.playerTimeMap.remove( playerId );
    }

    @SuppressWarnings( "deprecation" )
    private void updateBlock( UUID playerId )
    {
        Player player = Bukkit.getPlayer( playerId );
        if ( player == null ) return;

        this.restoreBlock( playerId );
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

            player.sendBlockChange( block.getLocation(), material, data );
        }
        else
        {
            this.playerBlockMap.put( playerId, null );
        }
    }

    @SuppressWarnings( "deprecation" )
    private void restoreBlock( UUID playerId )
    {
        Player player = Bukkit.getPlayer( playerId );
        if ( player == null ) return;

        Block oldBlock = this.playerBlockMap.get( player.getUniqueId() );
        if ( oldBlock != null )
        {
            // return the old block to its original status
            player.sendBlockChange( oldBlock.getLocation(), oldBlock.getType(), oldBlock.getData() );
        }
    }
}
