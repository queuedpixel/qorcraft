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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class QorcraftAdminCommand implements CommandExecutor, Listener
{
    private final BlockMarker blockMarker;

    public QorcraftAdminCommand( BlockMarker blockMarker )
    {
        this.blockMarker = blockMarker;
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
            sender.sendMessage( ChatColor.LIGHT_PURPLE + "Click on a block to create a Qorway." );
            this.blockMarker.addPlayer( player );
            return true;
        }
        else
        {
            sender.sendMessage( ChatColor.RED + "Unrecognized sub-command." );
            this.sendUsage( sender );
            return true;
        }
    }

    @EventHandler
    public void onPlayerInteractEvent( PlayerInteractEvent event )
    {
        this.blockMarker.removePlayer( event.getPlayer() );
    }

    private void sendUsage( CommandSender sender )
    {
        sender.sendMessage( ChatColor.GREEN + "Available sub-commands: " + ChatColor.LIGHT_PURPLE + "create" );
    }
}
