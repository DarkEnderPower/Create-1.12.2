package darkenderhilda.create.foundation.data;

import darkenderhilda.create.foundation.block.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class SharedProperties {

    public static BlockProperties stone() {
        return new BlockProperties()
                .material(Material.ROCK)
                .hardness(1.5F)
                .resistance(6.0F)
                .soundType(SoundType.STONE)
                .mapColor(MapColor.GRAY);
    }

    public static BlockProperties wooden() {
        return new BlockProperties()
                .material(Material.WOOD)
                .hardness(2.0F)
                .resistance(2.0F)
                .soundType(SoundType.WOOD)
                .mapColor(MapColor.WOOD);
    }

    public static BlockProperties softMetal() {
        return new BlockProperties()
                .material(Material.WOOD)
                .hardness(3.0F)
                .resistance(6.0F)
                .soundType(SoundType.METAL)
                .mapColor(MapColor.GOLD);
    }
}
