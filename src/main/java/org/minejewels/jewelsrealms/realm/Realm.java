package org.minejewels.jewelsrealms.realm;

import com.infernalsuite.aswm.api.world.SlimeWorld;
import lombok.Getter;
import lombok.Setter;
import net.abyssdev.abysslib.storage.id.Id;
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

    private boolean open;
    private String description = "N/A";

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

        plugin.getRealmLoader().createRealmProperties(plugin.getRealmLoader().loadRealm(this), player);
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

        final Location spawnLocation = world.getSpawnLocation().add(0.5, 1, 0.5);

        player.teleport(spawnLocation);
    }
}
