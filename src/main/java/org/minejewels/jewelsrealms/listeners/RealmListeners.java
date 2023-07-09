package org.minejewels.jewelsrealms.listeners;

import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.events.RealmBreakEvent;
import org.minejewels.jewelsrealms.events.RealmInteractEvent;
import org.minejewels.jewelsrealms.events.RealmPlaceEvent;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmListeners extends AbyssListener<JewelsRealms> {

    public RealmListeners(JewelsRealms plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {

        final Player player = event.getPlayer();
        final Location location = event.getBlock().getLocation();

        if (!this.plugin.getRealmUtils().isRealmWorld(location.getWorld())) {
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(location.getWorld());

        if (!realm.getMembers().containsKey(player.getUniqueId()) && !realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        if (!plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.BREAK_BLOCKS)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        final RealmBreakEvent breakEvent = new RealmBreakEvent(event, player, realm);

        Events.call(breakEvent);

        if (breakEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }


        event.setCancelled(false);
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {

        final Player player = event.getPlayer();
        final Location location = event.getBlock().getLocation();

        if (!this.plugin.getRealmUtils().isRealmWorld(location.getWorld())) {
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(location.getWorld());

        if (!realm.getMembers().containsKey(player.getUniqueId()) && !realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        if (!plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.PLACE_BLOCKS)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        final RealmPlaceEvent placeEvent = new RealmPlaceEvent(event, player, realm);

        Events.call(placeEvent);

        if (placeEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }


        event.setCancelled(false);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final Location location = player.getLocation();

        if (!this.plugin.getRealmUtils().isRealmWorld(location.getWorld())) {
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(location.getWorld());

        if (!realm.getMembers().containsKey(player.getUniqueId()) && !realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        if (!plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.INTERACT)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        final RealmInteractEvent interactEvent = new RealmInteractEvent(event, player, realm);

        Events.call(interactEvent);

        if (interactEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }


        event.setCancelled(false);
    }

    @EventHandler
    public void onPickup(final EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        final Player player = (Player) event.getEntity();
        final Location location = player.getLocation();

        if (!this.plugin.getRealmUtils().isRealmWorld(location.getWorld())) {
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(location.getWorld());

        if (!realm.getMembers().containsKey(player.getUniqueId()) && !realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        if (!plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.PICKUP_ITEMS)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }


        event.setCancelled(false);
    }

    @EventHandler
    public void onDrop(final EntityDropItemEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        final Player player = (Player) event.getEntity();
        final Location location = player.getLocation();

        if (!this.plugin.getRealmUtils().isRealmWorld(location.getWorld())) {
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(location.getWorld());

        if (!realm.getMembers().containsKey(player.getUniqueId()) && !realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }

        if (!plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.DROP_ITEMS)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            event.setCancelled(true);
            return;
        }


        event.setCancelled(false);
    }
}
