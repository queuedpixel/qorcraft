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

import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import java.util.UUID;

public class QorcraftMap
{
    private final QorcraftPlugin qorcraftPlugin;
    private final MarkerSet markerSet;
    private final MarkerIcon qorwayIcon;

    QorcraftMap( QorcraftPlugin qorcraftPlugin, DynmapCommonAPI dynmapApi )
    {
        this.qorcraftPlugin = qorcraftPlugin;

        // initialize dynmap api
        MarkerAPI markerApi = dynmapApi.getMarkerAPI();
        this.markerSet = markerApi.createMarkerSet( "qorcraft", "Qorcraft", null, false );
        this.markerSet.setLayerPriority( 10 );
        this.qorwayIcon = markerApi.getMarkerIcon( "star" );

        // create markers for existing Qorways
        for ( Qorway qorway : this.qorcraftPlugin.data.qorways ) this.addQorwayMarker( qorway );
    }

    void addQorway( Qorway qorway )
    {
        this.addQorwayMarker( qorway );
        this.qorcraftPlugin.data.qorways.add( qorway );
        this.qorcraftPlugin.saveData();
    }

    private void addQorwayMarker( Qorway qorway )
    {
        this.markerSet.createMarker( qorway.id.toString(), "Qorway", qorway.world,
                                     qorway.x, qorway.y, qorway.z, this.qorwayIcon, false );
    }
}
