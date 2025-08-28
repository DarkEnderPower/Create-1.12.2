package darkenderhilda.create.foundation.block;

import com.mojang.realmsclient.util.Pair;
import darkenderhilda.create.AllCreativeModeTabs;
import darkenderhilda.create.Create;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

public class BlockProperties {

    private String name;
    private Material material;
    private final List<Pair<String, Integer>> harvestLevels = new ArrayList<>();
    private CreativeTabs creativeTab = AllCreativeModeTabs.CREATE_TAB;

    private float hardness = 0.0F;
    private float resistance = 0.0F;
    private SoundType soundType = SoundType.STONE;
    private MapColor mapColor;
    private float lightLevel = 0.0F;

    //---Setters----
    public BlockProperties name(String name) {
        this.name = name;
        return this;
    }

    public BlockProperties tool(String toolClass, int level) {
        harvestLevels.add(Pair.of(toolClass, level));
        return this;
    }

    public BlockProperties material(Material material) {
        this.material = material;
        return this;
    }

    public BlockProperties creativeTab(CreativeTabs creativeTab) {
        this.creativeTab = creativeTab;
        return this;
    }

    public BlockProperties hardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    public BlockProperties resistance(float resistance) {
        this.resistance = resistance;
        return this;
    }

    public BlockProperties soundType(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public BlockProperties mapColor(MapColor mapColor) {
        this.mapColor = mapColor;
        return this;
    }

    public BlockProperties lightLevel(float lightLevel) {
        this.lightLevel = lightLevel;
        return this;
    }

    //---Getters----
    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public List<Pair<String, Integer>> getHarvestLevels() {
        return harvestLevels;
    }

    public CreativeTabs getCreativeTab() {
        return creativeTab;
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public MapColor getMapColor() {
        if(mapColor == null)
            return material.getMaterialMapColor();
        else
            return mapColor;
    }

    public float getLightLevel() {
        return lightLevel;
    }
}
