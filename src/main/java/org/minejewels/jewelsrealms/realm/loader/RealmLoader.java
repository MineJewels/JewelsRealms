package org.minejewels.jewelsrealms.realm.loader;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmLoader {

    private final JewelsRealms plugin;
    private final SlimePlugin slimePlugin;
    private final SlimeLoader loader;

    public RealmLoader(final JewelsRealms plugin) {
        this.plugin = plugin;
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        assert this.slimePlugin != null;

        this.loader = this.slimePlugin.getLoader("file");
    }

    @SneakyThrows
    public SlimeWorld loadRealm(final Realm realm) {
        final SlimePropertyMap map = new SlimePropertyMap();
        final String name = realm.getOwnerName() + "-" + realm.getId();

        map.setValue(SlimeProperties.DIFFICULTY, "peaceful");
        map.setValue(SlimeProperties.SPAWN_X, 0);
        map.setValue(SlimeProperties.SPAWN_Y, 100);
        map.setValue(SlimeProperties.SPAWN_Z, 0);
        map.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        map.setValue(SlimeProperties.ALLOW_ANIMALS, false);

        final boolean exists = this.loader.worldExists(realm.getOwnerName() + "-" + realm.getId());

        if (exists) {
            this.loader.unlockWorld(name);

            final SlimeWorld world = this.slimePlugin.loadWorld(this.loader, name, false, map);

            return this.slimePlugin.loadWorld(world);
        }

        final SlimeWorld world = this.slimePlugin.createEmptyWorld(
                this.loader,
                name,
                false,
                map
        );

        return this.slimePlugin.loadWorld(world);
    }

    @SneakyThrows
    public void deleteRealm(final Realm realm) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "swm unload " + realm.getOwnerName() + "-" + realm.getId());
        this.loader.unlockWorld(realm.getOwnerName() + "-" + realm.getId());
        this.loader.deleteWorld(realm.getOwnerName() + "-" + realm.getId());
    }

    public World adapt(final SlimeWorld world) {
        return Bukkit.getWorld(world.getName());
    }

    public World adapt(final Realm realm) {
        return Bukkit.getWorld(realm.getOwnerName() + "-" + realm.getId());
    }

    public void spawn(final Realm realm, final Player player) {

        final World world = this.adapt(realm);

        final Location spawnLocation = world.getSpawnLocation().add(0.5, 1, 0.5);

        player.teleport(spawnLocation);
    }

    public void createRealmProperties(final SlimeWorld slimeWorld, final Player player) {
        final World world = this.adapt(slimeWorld);

        final Location spawnLocation = world.getSpawnLocation().add(0.5, 0, 0.5);

        WorldBorder border = world.getWorldBorder();

        border.setCenter(spawnLocation);
        border.setSize(100);

        int radius = 5;
        int layers = 5;

        for (int i = 0; i < layers; i++) {
            int layerRadius = radius - i;

            for (int x = -layerRadius + 1; x < layerRadius; x++) {
                for (int z = -layerRadius + 1; z < layerRadius; z++) {
                    Location blockLocation = spawnLocation.clone().add(x, -i, z);
                    Block block = blockLocation.getBlock();
                    if (i == layers - 1) {
                        block.setType(Material.BEDROCK);
                    } else {
                        block.setType(Material.GRASS_BLOCK);
                    }
                }
            }
        }

        player.teleport(spawnLocation);
    }
}
