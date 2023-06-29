package org.minejewels.jewelsrealms.realm;

import com.infernalsuite.aswm.api.world.SlimeWorld;
import lombok.Getter;
import lombok.Setter;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.storage.id.Id;
import net.abyssdev.abysslib.text.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Maps;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.roles.RealmRole;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Realm {

    @Id
    private final UUID owner;
    private final String ownerName;
    private final int id;
    private final Map<UUID, RealmRole> members = Maps.mutable.empty();

    private boolean open = true;
    private String description = "N/A";
    private double warpX, warpY, warpZ;
    private double spawnX, spawnY, spawnZ;

    public Realm(final JewelsRealms plugin, final UUID owner) {
        this.owner = owner;

        this.id = plugin.getLatestRealm() + 1;

        final Player player = Bukkit.getPlayer(owner);

        if (player != null) {
            this.ownerName = player.getName();
        } else {
            this.ownerName = "null";
        }

        this.loadRealm(plugin);
    }

    public void loadRealm(final JewelsRealms plugin) {
        plugin.setLatestRealmID(this.id);

        final Player player = Bukkit.getPlayer(owner);

        assert player != null;

        plugin.getRealmLoader().createRealmProperties(plugin.getRealmLoader().loadRealm(this), player, this);
    }

    public void deleteRealm(final JewelsRealms plugin) {
        plugin.getRealmLoader().deleteRealm(this);
    }

    public void inviteMember(final UUID member) {

    }

    public void kickMember(final UUID member) {
        this.members.remove(member);
    }

    public void promoteMember(final UUID member) {

    }

    public void demoteMember(final UUID member) {

    }

    public World adapt() {
        return Bukkit.getWorld(this.getOwnerName() + "-" + this.getId());
    }

    public void spawn(final Player player) {
        final World world = this.adapt();

        final Location playerSpawnLocation = new Location(
                world,
                this.spawnX + 0.5,
                this.spawnY + 1,
                this.spawnZ + 0.5
        );

        player.teleport(playerSpawnLocation);
    }

    public void warp(final Player player) {
        final World world = this.adapt();

        final Location warpLocation = new Location(
                world,
                this.warpX + 0.5,
                this.warpY + 1,
                this.warpZ + 1
        );

        player.teleport(warpLocation);
    }

    public void sendTeamMessage(final Message message, final PlaceholderReplacer replacer) {

        final Player owner = Bukkit.getPlayer(this.getOwner());

        message.send(owner, replacer);

        this.members.keySet().forEach(member -> {

            final Player player = Bukkit.getPlayer(member);

            assert player != null;

            if (!player.isOnline()) return;

            message.send(player, replacer);
        });
    }
}
