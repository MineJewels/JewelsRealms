package org.minejewels.jewelsrealms.realm;

import com.infernalsuite.aswm.api.world.SlimeWorld;
import lombok.Getter;
import lombok.Setter;
import net.abyssdev.abysslib.collections.entry.EntryTimeLimitSet;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.storage.id.Id;
import net.abyssdev.abysslib.text.message.Message;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Maps;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.roles.RealmRole;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @BsonIgnore
    private final Set<UUID> invites;

    public Realm(final JewelsRealms plugin, final UUID owner) {
        this.owner = owner;

        this.id = plugin.getLatestRealm() + 1;

        final Player player = Bukkit.getPlayer(owner);

        if (player != null) {
            this.ownerName = player.getName();
        } else {
            this.ownerName = "null";
        }

        this.invites = new EntryTimeLimitSet<>(TimeUnit.SECONDS, 60);

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
        this.invites.add(member);
    }

    public void addMember(final UUID member, final JewelsRealms plugin) {
        this.members.put(member, plugin.getRoleRegistry().get("MEMBER").get());
    }

    public void kickMember(final UUID member) {
        this.members.remove(member);
    }

    public void promoteMember(final Player promoter, final UUID member, final JewelsRealms plugin) {

        if (this.getOwner().toString().equalsIgnoreCase(member.toString())) {
            plugin.getMessageCache().sendMessage(promoter, "messages.already-top-rank");
            return;
        }

        final RealmRole nextRole = plugin.getRealmUtils().getNextRole(this.members.get(member));

        if (this.members.get(member) == nextRole) {
            plugin.getMessageCache().sendMessage(promoter, "messages.already-top-rank");
            return;
        }

        if (promoter.getUniqueId().toString().equalsIgnoreCase(member.toString())) {
            plugin.getMessageCache().sendMessage(promoter, "messages.cannot-promote-self");
            return;
        }

        this.members.put(member, nextRole);

        this.sendTeamMessage(plugin.getMessageCache().getMessage("messages.user-promoted"), new PlaceholderReplacer()
            .addPlaceholder("%player%", Bukkit.getOfflinePlayer(member).getName())
            .addPlaceholder("%role%", nextRole.getName()));
    }

    public void demoteMember(final Player promoter, final UUID member, final JewelsRealms plugin) {
        if (this.getOwner().toString().equalsIgnoreCase(member.toString())) {
            plugin.getMessageCache().sendMessage(promoter, "messages.already-top-rank");
            return;
        }

        final RealmRole nextRole = plugin.getRealmUtils().getNextRole(this.members.get(member));

        if (this.members.get(member) == nextRole) {
            plugin.getMessageCache().sendMessage(promoter, "messages.already-bottom-rank");
            return;
        }

        if (promoter.getUniqueId().toString().equalsIgnoreCase(member.toString())) {
            plugin.getMessageCache().sendMessage(promoter, "messages.cannot-demote-self");
            return;
        }

        this.members.put(member, nextRole);

        this.sendTeamMessage(plugin.getMessageCache().getMessage("messages.user-demoted"), new PlaceholderReplacer()
                .addPlaceholder("%player%", Bukkit.getOfflinePlayer(member).getName())
                .addPlaceholder("%role%", nextRole.getName()));
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
