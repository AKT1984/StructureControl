package net.alexandr.structurecontrol;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.world.gen.feature.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraft.util.MathHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mod(modid = "structurecontrol", name = "Structure Control", version = "1.0")
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class StructureControl {

    // Config options (default to true, meaning structures are enabled by default)
    private static boolean generateMineshafts;
    private static boolean generateStrongholds;
    private static boolean generateVillages;
    private static boolean generateNetherFortresses;
    // Individual scattered feature control
    static boolean generateJungleTemple;
    static boolean generateDesertPyramid;
    static boolean generateWitchHut;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load configuration
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "structurecontrol.cfg"));
        config.load();

        // Default values are true, meaning the structures are enabled by default
        generateMineshafts = config.get("Structures", "GenerateMineshafts", true, "Set to false to disable Mineshaft generation").getBoolean(true);
        generateStrongholds = config.get("Structures", "GenerateStrongholds", true, "Set to false to disable Stronghold generation").getBoolean(true);
        generateVillages = config.get("Structures", "GenerateVillages", true, "Set to false to disable Village generation").getBoolean(true);
        generateNetherFortresses = config.get("Structures", "GenerateNetherFotresses", true, "Set to false to disable Nether Fortress generation").getBoolean(true);

        // Individual scattered feature controls
        generateJungleTemple = config.get("ScatteredFeatures", "GenerateJungleTemple", true, "Set to false to disable Jungle Temple generation").getBoolean(true);
        generateDesertPyramid = config.get("ScatteredFeatures", "GenerateDesertPyramid", true, "Set to false to disable Desert Pyramid generation").getBoolean(true);
        generateWitchHut = config.get("ScatteredFeatures", "GenerateWitchHut", true, "Set to false to disable Witch Hut generation").getBoolean(true);

        config.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the event handler
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
    }

    @ForgeSubscribe
    public void onInitMapGen(InitMapGenEvent event) {
        switch (event.type) {
            case MINESHAFT:
                if (!generateMineshafts) {
                    event.newGen = new NoOpMapGenMineshaft();
                }
                break;
            case STRONGHOLD:
                if (!generateStrongholds) {
                    event.newGen = new NoOpMapGenStronghold();
                }
                break;
            case VILLAGE:
                if (!generateVillages) {
                    event.newGen = new NoOpMapGenVillage();
                }
                break;
            case SCATTERED_FEATURE:
            	if (!generateJungleTemple && !generateDesertPyramid && !generateWitchHut) {
                    event.newGen = new NoOpMapGenScatteredFeature();
                    break;
                }
            	
            	if (!generateJungleTemple || !generateDesertPyramid || !generateWitchHut) {
                    event.newGen = new CustomMapGenScatteredFeature();
                }
                break;
            case NETHER_BRIDGE:
                if (!generateNetherFortresses) {
                    event.newGen = new NoOpMapGenNetherBridge();
                }
                break;
            default:
                break;
        }
    }

    // Custom no-op generator for Mineshafts
    public static class NoOpMapGenMineshaft extends MapGenMineshaft {
        @Override
        protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
            return false; // Prevent any Mineshaft from generating
        }
    }

    // Custom no-op generator for Strongholds
    public static class NoOpMapGenStronghold extends MapGenStronghold {
        @Override
        protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
            return false; // Prevent any Stronghold from generating
        }
    }

    // Custom no-op generator for Villages
    public static class NoOpMapGenVillage extends MapGenVillage {
        @Override
        protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
            return false; // Prevent any Village from generating
        }
    }
    
 // Custom no-op generator for Nether Bridges
    public static class NoOpMapGenNetherBridge extends MapGenNetherBridge {
        @Override
        protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
            return false; // Prevent any Nether Fotress from generating
        }
    }
    public static class NoOpMapGenScatteredFeature extends MapGenScatteredFeature {
        @Override
        protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
            return false; // Prevent any Scattered Feature from generating
        }
    }



}
