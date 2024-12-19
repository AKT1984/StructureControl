package net.alexandr.structurecontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.gen.feature.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.ComponentScatteredFeatureSwampHut;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureScatteredFeatureStart;
import net.minecraft.world.gen.structure.StructureStart;

public class CustomMapGenScatteredFeature extends MapGenScatteredFeature {
    private static List biomelist = Arrays.asList(new BiomeGenBase[] {
        BiomeGenBase.desert, BiomeGenBase.desertHills,
        BiomeGenBase.jungle, BiomeGenBase.jungleHills,
        BiomeGenBase.swampland
    });

    private List scatteredFeatureSpawnList;

    private int maxDistanceBetweenScatteredFeatures;
    private int minDistanceBetweenScatteredFeatures;

    public CustomMapGenScatteredFeature() {
        this.scatteredFeatureSpawnList = new ArrayList();
        this.maxDistanceBetweenScatteredFeatures = 32;
        this.minDistanceBetweenScatteredFeatures = 8;
        this.scatteredFeatureSpawnList.add(new SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    public CustomMapGenScatteredFeature(Map par1Map) {
        this();
        Iterator iterator = par1Map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("distance")) {
                this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax(
                    (String) entry.getValue(), this.maxDistanceBetweenScatteredFeatures, this.minDistanceBetweenScatteredFeatures + 1
                );
            }
        }
    }

    public String func_143025_a() {
        return "Temple";
    }

    protected boolean canSpawnStructureAtCoords(int par1, int par2) {
        int k = par1;
        int l = par2;

        if (par1 < 0) {
            par1 -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        if (par2 < 0) {
            par2 -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        int i1 = par1 / this.maxDistanceBetweenScatteredFeatures;
        int j1 = par2 / this.maxDistanceBetweenScatteredFeatures;
        Random random = this.worldObj.setRandomSeed(i1, j1, 14357617);
        i1 *= this.maxDistanceBetweenScatteredFeatures;
        j1 *= this.maxDistanceBetweenScatteredFeatures;
        i1 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
        j1 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);

        if (k == i1 && l == j1) {
            BiomeGenBase biomegenbase = this.worldObj.getWorldChunkManager().getBiomeGenAt(k * 16 + 8, l * 16 + 8);

            // Check biome-specific structure generation
            if ((biomegenbase == BiomeGenBase.desert || biomegenbase == BiomeGenBase.desertHills)
                && !StructureControl.generateDesertPyramid) {
                return false; // Desert Pyramid generation disabled
            }

            if ((biomegenbase == BiomeGenBase.jungle || biomegenbase == BiomeGenBase.jungleHills)
                && !StructureControl.generateJungleTemple) {
                return false; // Jungle Temple generation disabled
            }

            if (biomegenbase == BiomeGenBase.swampland
                && !StructureControl.generateWitchHut) {
                return false; // Witch Hut generation disabled
            }

            // Default behavior for other biomes
            Iterator iterator = biomelist.iterator();
            while (iterator.hasNext()) {
                BiomeGenBase biomegenbase1 = (BiomeGenBase) iterator.next();

                if (biomegenbase == biomegenbase1) {
                    return true;
                }
            }
        }

        return false;
    }

    protected StructureStart getStructureStart(int par1, int par2) {
        return new StructureScatteredFeatureStart(this.worldObj, this.rand, par1, par2);
    }

    public boolean func_143030_a(int par1, int par2, int par3) {
        StructureStart structurestart = this.func_143028_c(par1, par2, par3);

        if (structurestart != null && structurestart instanceof StructureScatteredFeatureStart && !structurestart.components.isEmpty()) {
            StructureComponent structurecomponent = (StructureComponent) structurestart.components.getFirst();
            return structurecomponent instanceof ComponentScatteredFeatureSwampHut;
        } else {
            return false;
        }
    }

    public List getScatteredFeatureSpawnList() {
        return this.scatteredFeatureSpawnList;
    }
}
