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

public class Region
{
    final String world;
    final int x;
    final int y;
    final int z;

    Region( String world, int x, int y, int z )
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals( Object obj )
    {
        if ( !( obj instanceof Region )) return false;
        Region region = (Region) obj;
        return ( this.world.equals( region.world )) &&
               ( this.x == region.x ) &&
               ( this.y == region.y ) &&
               ( this.z == region.z );
    }

    public int hashCode()
    {
        int hashX = this.x & 0b1111111111;
        int hashY = ( this.y & 0b1111111111 ) << 10;
        int hashZ = ( this.z & 0b1111111111 ) << 20;
        return this.world.hashCode() ^ hashX ^ hashY ^ hashZ;
    }
}
