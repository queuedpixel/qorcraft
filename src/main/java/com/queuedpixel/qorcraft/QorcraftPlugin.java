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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.dynmap.markers.PolyLineMarker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class QorcraftPlugin extends JavaPlugin
{
    private Path dataFile;
    QorcraftData data;

    public void onEnable()
    {
        Path pluginDirectory = this.getDataFolder().toPath();
        this.dataFile = pluginDirectory.resolve( "qorcraft.json" );

        try
        {
            if ( !Files.exists( pluginDirectory )) Files.createDirectory( pluginDirectory );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        this.loadData();
        DynmapCommonAPI dynmapApi = (DynmapCommonAPI) this.getServer().getPluginManager().getPlugin( "dynmap" );
        QorcraftMap qorcraftMap = new QorcraftMap( this, dynmapApi );

        QorcraftCommand qorcraftCommand = new QorcraftCommand();
        QorcraftAdminCommand qorcraftAdminCommand = new QorcraftAdminCommand( qorcraftMap );

        this.getServer().getPluginManager().registerEvents( qorcraftAdminCommand, this );

        this.getCommand( "qorcraft" ).setExecutor( qorcraftCommand );
        this.getCommand( "qorcraftadmin" ).setExecutor( qorcraftAdminCommand );

        qorcraftAdminCommand.runTaskTimer( this, 5, 5 );

        MarkerAPI markerApi = dynmapApi.getMarkerAPI();
        MarkerIcon icon = markerApi.getMarkerIcon( MarkerIcon.DEFAULT );
        MarkerSet markerSet = markerApi.createMarkerSet( "qorcraft-test", "Qorcraft Test", null, false );
        markerSet.setLayerPriority( 5 );
        markerSet.createMarker( "qorcraft-marker-1", "Qorcraft Marker 1", "world", -56, 64, -14, icon, false );
        markerSet.createMarker( "qorcraft-marker-2", "Qorcraft Marker 2", "world",  10, 64, -67, icon, false );
        markerSet.createMarker( "qorcraft-marker-3", "Qorcraft Marker 3", "world",  47, 64,  14, icon, false );
        markerSet.createMarker( "qorcraft-marker-4", "Qorcraft Marker 4", "world", -34, 64,   6, icon, false );
        this.createLine(
                markerSet, "qorcraft-line-1", "Qorcraft Line 1", false, "world", -56, 64, -14, -34, 64, 6 );
        this.createArea(
                markerSet, "qorcraft-area-1", "Qorcraft Area 1", false, "world", -56, -14, 10, -67, 47, 14 );
    }

    public void onDisable()
    {
    }

    void saveData()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try
        {
            BufferedWriter writer = Files.newBufferedWriter(
                    this.dataFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING );
            writer.write( gson.toJson( this.data ));
            writer.newLine();
            writer.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private void loadData()
    {
        // initialize new data structure if the data file does not exist
        if ( !Files.exists( this.dataFile ))
        {
            this.data = new QorcraftData();
            return;
        }

        try
        {
            Gson gson = new Gson();
            BufferedReader reader = Files.newBufferedReader( this.dataFile );
            this.data = gson.fromJson( reader, QorcraftData.class );
            reader.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            this.data = new QorcraftData();
        }
    }

    private void createLine( MarkerSet markerSet, String id, String label, boolean markup, String world,
                             double x1, double y1, double z1, double x2, double y2, double z2 )
    {
        PolyLineMarker line = markerSet.createPolyLineMarker( id, label, markup, world,
                new double[] { x1, x2 }, new double[] { y1, y2 }, new double[] { z1, z2 }, false );
        line.setLineStyle( 3, 0.75, 0xFF00FF );
    }

    private void createArea( MarkerSet markerSet, String id, String label, boolean markup, String world,
                             double x1, double z1, double x2, double z2, double x3, double z3 )
    {
        AreaMarker area = markerSet.createAreaMarker( id, label, markup, world,
                new double[] { x1, x2, x3 }, new double[] { z1, z2, z3 }, false );
        area.setLineStyle( 3, 0.75, 0xFF00FF );
        area.setFillStyle( 0.25, 0xFF00FF );
    }
}
