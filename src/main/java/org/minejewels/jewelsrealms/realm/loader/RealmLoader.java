package org.minejewels.jewelsrealms.realm.loader;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimeProperties;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import lombok.SneakyThrows;
import net.abyssdev.abysslib.utils.file.Files;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldLoadEvent;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;
import org.minejewels.jewelsrealms.utils.FaweUtils;

import java.io.File;
import java.io.IOException;

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

        this.loader.unlockWorld(name);

        final World bukkitWorld = this.adapt(realm);

        WorldBorder border = bukkitWorld.getWorldBorder();

        border.setCenter(bukkitWorld.getSpawnLocation());
        border.setSize(100);

        return this.slimePlugin.loadWorld(world);
    }

    public void deleteRealm(final Realm realm) {

        final World world = this.adapt(realm);

        for (final Player player : world.getPlayers()) {
            player.teleport(new Location(
                    Bukkit.getWorld(this.plugin.getSettingsConfig().getString("deletion-location.world")),
                    this.plugin.getSettingsConfig().getInt("deletion-location.x") + 0.5,
                    this.plugin.getSettingsConfig().getInt("deletion-location.y"),
                    this.plugin.getSettingsConfig().getInt("deletion-location.z") + 0.5,
                    this.plugin.getSettingsConfig().getInt("deletion-location.yaw"),
                    this.plugin.getSettingsConfig().getInt("deletion-location.pitch")
            ));
        }
        if (Bukkit.unloadWorld(realm.getOwnerName() + "-" + realm.getId(), false)) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    this.loader.deleteWorld(realm.getOwnerName() + "-" + realm.getId());
                    System.out.println("true");
                } catch (UnknownWorldException | IOException exception) {
                    throw new RuntimeException(exception);
                }
            });
        }
    }

    public World adapt(final SlimeWorld world) {
        return Bukkit.getWorld(world.getName());
    }

    public World adapt(final Realm realm) {
        return Bukkit.getWorld(realm.getOwnerName() + "-" + realm.getId());
    }

    public void createRealmProperties(final SlimeWorld slimeWorld, final Player player, final Realm realm) {
        final World world = this.adapt(slimeWorld);

        final Location spawnLocation = world.getSpawnLocation().add(0.5, 0, 0.5);
        final Location playerSpawnLocation = new Location(
                world,
                this.plugin.getSettingsConfig().getInt("spawn-location.x") + 0.5,
                this.plugin.getSettingsConfig().getInt("spawn-location.y"),
                this.plugin.getSettingsConfig().getInt("spawn-location.z") + 0.5
        );

        WorldBorder border = world.getWorldBorder();

        border.setCenter(spawnLocation);
        border.setSize(100);

        final WorldLoadEvent event = new WorldLoadEvent(world);

        Events.call(event);

        FaweUtils.pasteSchematic(spawnLocation, Files.file("realm.schem", plugin), true);

        realm.setWarpX(playerSpawnLocation.getX());
        realm.setWarpY(playerSpawnLocation.getY());
        realm.setWarpZ(playerSpawnLocation.getZ());

        realm.setSpawnX(playerSpawnLocation.getX());
        realm.setSpawnY(playerSpawnLocation.getY());
        realm.setSpawnZ(playerSpawnLocation.getZ());

        player.teleport(playerSpawnLocation);
    }
}
