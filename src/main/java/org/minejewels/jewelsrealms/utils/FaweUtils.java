package org.minejewels.jewelsrealms.utils;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public final class FaweUtils {

    private final static Map<String, Material> MATERIALS = new HashMap<>();

    public FaweUtils() {
        for (final Material material : Material.values()) {
            MATERIALS.put(material.toString().toLowerCase(), material);
        }
    }

    public static void pasteSchematic(final Location location, final File schematic, final boolean fast) {
        final BlockVector3 to = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final ClipboardFormat format = ClipboardFormats.findByFile(schematic);

        ClipboardReader reader;

        try {
            reader = format.getReader(new FileInputStream(schematic));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final Clipboard clipboard;

        try {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (final EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(FaweAPI.getWorld(location.getWorld().getName()), -1)) {

            if (fast) {
                editSession.setFastMode(true);
            }

            final Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(to)
                    .ignoreAirBlocks(false)
                    .build();

            Operations.complete(operation);
        }
    }

    public static Map<Material, List<Location>> getSchemBlocks(final Location location, final File schematic, final boolean ignoreAir) {

        final Map<Material, List<Location>> blocks = new EnumMap<>(Material.class);
        final ClipboardFormat format = ClipboardFormats.findByFile(schematic);

        ClipboardReader reader;

        try {
            reader = format.getReader(new FileInputStream(schematic));
        } catch (IOException e) {
            e.printStackTrace();
            return blocks;
        }

        final Clipboard clipboard;

        try {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return blocks;
        }

        final int originX = clipboard.getOrigin().getX();
        final int originY = clipboard.getOrigin().getY();
        final int originZ = clipboard.getOrigin().getZ();

        for (int x = clipboard.getMinimumPoint().getX(); x <= clipboard.getMaximumPoint().getX(); x++) {
            for (int y = clipboard.getMinimumPoint().getY(); y <= clipboard.getMaximumPoint().getY(); y++) {
                for (int z = clipboard.getMinimumPoint().getZ(); z <= clipboard.getMaximumPoint().getZ(); z++) {
                    final BlockState state = clipboard.getBlock(x, y, z);
                    final String name = state.getBlockType()
                            .toString()
                            .replace("minecraft:", "");

                    if (!MATERIALS.containsKey(name)) {
                        continue;
                    }

                    final Material material = MATERIALS.get(name);

                    if (material == Material.AIR && ignoreAir) {
                        continue;
                    }

                    if (blocks.containsKey(material)) {
                        blocks.get(material).add(location.clone().add(
                                x - originX,
                                y - originY,
                                z - originZ));
                        continue;
                    }

                    blocks.put(material, new ArrayList<>(Collections.singletonList(location.clone().add(
                            x - originX,
                            y - originY,
                            z - originZ))));
                }
            }
        }

        return blocks;
    }

    public static Map<Material, List<Location>> getDefaultBlocks(final World world, final File schematic, final boolean ignoreAir) {
        final Map<Material, List<Location>> blocks = new EnumMap<>(Material.class);
        final ClipboardFormat format = ClipboardFormats.findByFile(schematic);

        ClipboardReader reader;

        try {
            reader = format.getReader(new FileInputStream(schematic));
        } catch (IOException e) {
            e.printStackTrace();
            return blocks;
        }

        final Clipboard clipboard;

        try {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return blocks;
        }

        for (int x = clipboard.getMinimumPoint().getX(); x <= clipboard.getMaximumPoint().getX(); x++) {
            for (int y = clipboard.getMinimumPoint().getY(); y <= clipboard.getMaximumPoint().getY(); y++) {
                for (int z = clipboard.getMinimumPoint().getZ(); z <= clipboard.getMaximumPoint().getZ(); z++) {
                    final BlockState state = clipboard.getBlock(x, y, z);
                    final String name = state.getBlockType()
                            .toString()
                            .replace("minecraft:", "");

                    if (!MATERIALS.containsKey(name)) {
                        continue;
                    }

                    final Material material = MATERIALS.get(name);

                    if (material == Material.AIR && ignoreAir) {
                        continue;
                    }

                    if (blocks.containsKey(material)) {
                        blocks.get(material).add(new Location(world, x, y, z));
                        continue;
                    }

                    blocks.put(material, new ArrayList<>());
                }
            }
        }

        return blocks;
    }

}
