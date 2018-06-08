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

import org.bukkit.Material;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class IgnoredMaterials
{
    private static final Set< Material > materials = new HashSet<>();

    static
    {
        // construct the set of ignored materials
        IgnoredMaterials.materials.add( Material.ACACIA_DOOR             );
        IgnoredMaterials.materials.add( Material.ACACIA_FENCE            );
        IgnoredMaterials.materials.add( Material.ACACIA_FENCE_GATE       );
        IgnoredMaterials.materials.add( Material.AIR                     );
        IgnoredMaterials.materials.add( Material.BEETROOT_BLOCK          );
        IgnoredMaterials.materials.add( Material.BIRCH_DOOR              );
        IgnoredMaterials.materials.add( Material.BIRCH_FENCE             );
        IgnoredMaterials.materials.add( Material.BIRCH_FENCE_GATE        );
        IgnoredMaterials.materials.add( Material.BROWN_MUSHROOM          );
        IgnoredMaterials.materials.add( Material.CARROT                  );
        IgnoredMaterials.materials.add( Material.CROPS                   );
        IgnoredMaterials.materials.add( Material.DARK_OAK_DOOR           );
        IgnoredMaterials.materials.add( Material.DARK_OAK_FENCE          );
        IgnoredMaterials.materials.add( Material.DARK_OAK_FENCE_GATE     );
        IgnoredMaterials.materials.add( Material.DEAD_BUSH               );
        IgnoredMaterials.materials.add( Material.DIODE_BLOCK_OFF         );
        IgnoredMaterials.materials.add( Material.DIODE_BLOCK_ON          );
        IgnoredMaterials.materials.add( Material.ENDER_PORTAL            );
        IgnoredMaterials.materials.add( Material.FENCE                   );
        IgnoredMaterials.materials.add( Material.FENCE_GATE              );
        IgnoredMaterials.materials.add( Material.GLASS                   );
        IgnoredMaterials.materials.add( Material.GOLD_PLATE              );
        IgnoredMaterials.materials.add( Material.IRON_DOOR_BLOCK         );
        IgnoredMaterials.materials.add( Material.IRON_FENCE              );
        IgnoredMaterials.materials.add( Material.IRON_PLATE              );
        IgnoredMaterials.materials.add( Material.IRON_TRAPDOOR           );
        IgnoredMaterials.materials.add( Material.JUNGLE_DOOR             );
        IgnoredMaterials.materials.add( Material.JUNGLE_FENCE            );
        IgnoredMaterials.materials.add( Material.JUNGLE_FENCE_GATE       );
        IgnoredMaterials.materials.add( Material.LADDER                  );
        IgnoredMaterials.materials.add( Material.LEVER                   );
        IgnoredMaterials.materials.add( Material.LONG_GRASS              );
        IgnoredMaterials.materials.add( Material.MELON_STEM              );
        IgnoredMaterials.materials.add( Material.NETHER_FENCE            );
        IgnoredMaterials.materials.add( Material.NETHER_WARTS            );
        IgnoredMaterials.materials.add( Material.PORTAL                  );
        IgnoredMaterials.materials.add( Material.POTATO                  );
        IgnoredMaterials.materials.add( Material.PUMPKIN_STEM            );
        IgnoredMaterials.materials.add( Material.RED_MUSHROOM            );
        IgnoredMaterials.materials.add( Material.RED_ROSE                );
        IgnoredMaterials.materials.add( Material.REDSTONE_COMPARATOR     );
        IgnoredMaterials.materials.add( Material.REDSTONE_COMPARATOR_OFF );
        IgnoredMaterials.materials.add( Material.REDSTONE_COMPARATOR_ON  );
        IgnoredMaterials.materials.add( Material.REDSTONE_LAMP_OFF       );
        IgnoredMaterials.materials.add( Material.REDSTONE_LAMP_ON        );
        IgnoredMaterials.materials.add( Material.REDSTONE_TORCH_OFF      );
        IgnoredMaterials.materials.add( Material.REDSTONE_TORCH_ON       );
        IgnoredMaterials.materials.add( Material.REDSTONE_WIRE           );
        IgnoredMaterials.materials.add( Material.SIGN_POST               );
        IgnoredMaterials.materials.add( Material.SKULL                   );
        IgnoredMaterials.materials.add( Material.SPRUCE_DOOR             );
        IgnoredMaterials.materials.add( Material.SPRUCE_FENCE            );
        IgnoredMaterials.materials.add( Material.SPRUCE_FENCE_GATE       );
        IgnoredMaterials.materials.add( Material.STAINED_GLASS           );
        IgnoredMaterials.materials.add( Material.STAINED_GLASS_PANE      );
        IgnoredMaterials.materials.add( Material.STANDING_BANNER         );
        IgnoredMaterials.materials.add( Material.STATIONARY_WATER        );
        IgnoredMaterials.materials.add( Material.STONE_BUTTON            );
        IgnoredMaterials.materials.add( Material.STONE_PLATE             );
        IgnoredMaterials.materials.add( Material.SUGAR_CANE_BLOCK        );
        IgnoredMaterials.materials.add( Material.THIN_GLASS              );
        IgnoredMaterials.materials.add( Material.TORCH                   );
        IgnoredMaterials.materials.add( Material.TRAP_DOOR               );
        IgnoredMaterials.materials.add( Material.VINE                    );
        IgnoredMaterials.materials.add( Material.WALL_BANNER             );
        IgnoredMaterials.materials.add( Material.WALL_SIGN               );
        IgnoredMaterials.materials.add( Material.WOOD_BUTTON             );
        IgnoredMaterials.materials.add( Material.WOOD_DOOR               );
        IgnoredMaterials.materials.add( Material.WOODEN_DOOR             );
        IgnoredMaterials.materials.add( Material.WOOD_PLATE              );
        IgnoredMaterials.materials.add( Material.YELLOW_FLOWER           );
    }

    public static Set< Material > getMaterials()
    {
        return Collections.unmodifiableSet( IgnoredMaterials.materials );
    }
}
