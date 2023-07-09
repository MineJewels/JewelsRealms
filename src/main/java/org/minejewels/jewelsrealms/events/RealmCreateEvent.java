package org.minejewels.jewelsrealms.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.minejewels.jewelsrealms.realm.Realm;

@Getter
@Setter
public class RealmCreateEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Realm realm;

    public RealmCreateEvent(final Player player, final Realm realm) {
        this.player = player;
        this.realm = realm;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}