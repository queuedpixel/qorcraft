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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QorwayManager
{
    // maximum distance of nearby Qorways
    private final int distance = 100;

    private final Map< Region, Set< Qorway >> regions = new HashMap<>();

    void addQorway( Qorway qorway )
    {
        Region region = this.getRegion( qorway.world, qorway.x, qorway.y, qorway.z );
        if ( !this.regions.containsKey( region )) this.regions.put( region, new HashSet<>() );
        Set< Qorway > qorways = this.regions.get( region );
        qorways.add( qorway );
    }

    Set< Qorway > getNearby( String world, int x, int y, int z )
    {
        // create a set of Qorways for all regions near the specified location
        Set< Qorway > qorways = new HashSet<>();
        Region baseRegion = this.getRegion( world, x, y, z );
        for ( int rx = baseRegion.x - 1; rx <= baseRegion.x + 1; rx++ )
        {
            for ( int ry = baseRegion.y - 1; ry <= baseRegion.y + 1; ry++ )
            {
                for ( int rz = baseRegion.z - 1; rz <= baseRegion.z + 1; rz++ )
                {
                    Region region = new Region( world, rx, ry, rz );
                    if ( this.regions.containsKey( region ))
                    {
                        qorways.addAll( this.regions.get( region ));
                    }
                }
            }
        }

        // check the distance for nearby Qorways
        Set< Qorway > nearbyQorways = new HashSet<>();
        for ( Qorway qorway : qorways )
        {
            if (( world.equals( qorway.world )) &&
                ( Math.abs( x - qorway.x ) < this.distance ) &&
                ( Math.abs( y - qorway.y ) < this.distance ) &&
                ( Math.abs( z - qorway.z ) < this.distance ))
            {
                nearbyQorways.add( qorway );
            }
        }

        return nearbyQorways;
    }

    private Region getRegion( String world, int x, int y, int z )
    {
        int rx = Math.floorDiv( x, this.distance );
        int ry = Math.floorDiv( y, this.distance );
        int rz = Math.floorDiv( z, this.distance );
        Region region = new Region( world, rx, ry, rz );
        return region;
    }
}
