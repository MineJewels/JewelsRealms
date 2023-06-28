package org.minejewels.jewelsrealms.utils;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

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
}
