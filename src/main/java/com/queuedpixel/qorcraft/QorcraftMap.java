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

public class QorcraftMap
{
    private final MarkerSet markerSet;
    private final MarkerIcon qorwayIcon;
    private int count = 0;

    public QorcraftMap( DynmapCommonAPI dynmapApi )
    {
        MarkerAPI markerApi = dynmapApi.getMarkerAPI();
        this.qorwayIcon = markerApi.getMarkerIcon( "star" );
        this.markerSet = markerApi.createMarkerSet( "qorcraft", "Qorcraft", null, false );
        this.markerSet.setLayerPriority( 10 );
    }

    public void addQorway( String world, double x, double y, double z )
    {
        String id = "qorway-" + count++;
        this.markerSet.createMarker( id, null, world, x, y, z, this.qorwayIcon, false );
    }
}
