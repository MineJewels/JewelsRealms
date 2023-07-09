package org.minejewels.jewelsrealms.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Lists;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;
import org.minejewels.jewelsrealms.roles.RealmRole;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RealmUtils {

    private JewelsRealms plugin;

    public RealmUtils(final JewelsRealms plugin) {
        this.plugin = plugin;
    }

    public boolean isInRealm(final Player player) {

        if (this.plugin.getRealmStorage().contains(player.getUniqueId())) return true;

        for (Realm realm : this.plugin.getRealmStorage().allValues()) {
            if (!realm.getMembers().containsKey(player.getUniqueId())) continue;
            return true;
        }

        return false;
    }

    public Realm getRealm(final Player player) {
        if (!isInRealm(player)) return null;

        if (this.plugin.getRealmStorage().contains(player.getUniqueId())) return this.plugin.getRealmStorage().get(player.getUniqueId());

        for (Realm realm : this.plugin.getRealmStorage().allValues()) {
            if (!realm.getMembers().containsKey(player.getUniqueId())) continue;
            return realm;
        }

        return null;
    }

    public boolean isInRealm(final UUID player) {

        if (this.plugin.getRealmStorage().contains(player)) return true;

        for (Realm realm : this.plugin.getRealmStorage().allValues()) {
            if (!realm.getMembers().containsKey(player)) continue;
            return true;
        }

        return false;
    }

    public Realm getRealm(final UUID player) {
        if (!isInRealm(player)) return null;

        if (this.plugin.getRealmStorage().contains(player)) return this.plugin.getRealmStorage().get(player);

        for (Realm realm : this.plugin.getRealmStorage().allValues()) {
            if (!realm.getMembers().containsKey(player)) continue;
            return realm;
        }

        return null;
    }

    public boolean isRealmWorld(final World world) {

        for (final Realm realm : this.plugin.getRealmStorage().allValues()) {
            if (!world.getName().endsWith("-" + realm.getId())) continue;
            return true;
        }

        return false;
    }

    public Realm getRealm(final World world) {
        if (!isRealmWorld(world)) return null;

        for (final Realm realm : this.plugin.getRealmStorage().allValues()) {
            if (!world.getName().equalsIgnoreCase(realm.getOwnerName() + "-" + realm.getId())) continue;
            return realm;
        }

        return null;
    }

    public boolean hasPermission(final Realm realm, final Player player, final RealmPermission permission) {
        if (realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) return true;
        if (realm.getMembers().get(player.getUniqueId()).getPermissions().contains(RealmPermission.ALL)) return true;
        return realm.getMembers().get(player.getUniqueId()).getPermissions().contains(permission);
    }

    public RealmRole getNextRole(final RealmRole previousRole) {

        final int oldWeight = previousRole.getWeight();

        for (final RealmRole role : this.plugin.getRoleRegistry().values()) {
            if (oldWeight+1 != role.getWeight()) continue;
            return role;
        }

        return previousRole;
    }

    public RealmRole getPreviousRole(final RealmRole currentRole) {

        final int oldWeight = currentRole.getWeight();

        for (final RealmRole role : this.plugin.getRoleRegistry().values()) {
            if (oldWeight-1 != role.getWeight()) continue;
            return role;
        }

        return currentRole;
    }

    public List<Player> getPlayersOnRealm(final Realm realm) {
        final World world = this.plugin.getRealmLoader().adapt(realm);

        return world.getPlayers();
    }

    public List<Player> getMembersOnRealm(final Realm realm) {

        final List<Player> members = Lists.mutable.empty();

        members.add(Bukkit.getPlayer(realm.getOwner()));

        for (final Player player : this.getPlayersOnRealm(realm)) {
            if (!realm.getMembers().containsKey(player.getUniqueId())) continue;
            members.add(player);
        }

        return members;
    }
}
