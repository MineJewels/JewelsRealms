package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.text.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RealmVisitCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmVisitCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("visit", "warp", "goto"));
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (context.getArguments().length == 0) {
            return;
        }

        if (!context.asOfflinePlayer(0).hasPlayedBefore()) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm-exists");
            return;
        }

        final OfflinePlayer target = context.asOfflinePlayer(0);

        if (!this.plugin.getRealmUtils().isInRealm(target.getUniqueId())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm-exists");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(target.getUniqueId());

        if (realm.getMembers().containsKey(player.getUniqueId()) || realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.cannot-visit-yourself");
            return;
        }

        if (!realm.isOpen() && !player.hasPermission("realm.admin")) {
            this.plugin.getMessageCache().sendMessage(player, "messages.realm-not-vistable");
            return;
        }

        this.plugin.getMessageCache().sendMessage(player, "messages.realm-visited", new PlaceholderReplacer().addPlaceholder("%owner%", realm.getOwnerName()));
        realm.warp(player);
    }
}
