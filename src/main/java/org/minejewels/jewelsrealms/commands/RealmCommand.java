package org.minejewels.jewelsrealms.commands;

import net.abyssdev.abysslib.command.AbyssCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Lists;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.commands.subcommands.*;
import org.minejewels.jewelsrealms.events.RealmCreateEvent;
import org.minejewels.jewelsrealms.events.RealmDeleteEvent;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmCommand extends AbyssCommand<JewelsRealms, Player> {

    public RealmCommand(JewelsRealms plugin) {
        super(plugin, "realm", Player.class);

        this.setAliases(Lists.mutable.of("realm", "realms"));
        this.register(
                new RealmHomeCommand(plugin),
                new RealmDeleteCommand(plugin),
                new RealmWhoisCommand(plugin),
                new RealmDescriptionCommand(plugin),
                new RealmLockCommand(plugin),
                new RealmUnlockCommand(plugin),
                new RealmVisitCommand(plugin),
                new RealmSetWarpCommand(plugin),
                new RealmSetSpawnCommand(plugin),
                new RealmJoinCommand(plugin),
                new RealmInviteCommand(plugin),
                new RealmLeaveCommand(plugin),
                new RealmKickCommand(plugin),
                new RealmPromoteCommand(plugin),
                new RealmHelpCommand(plugin),
                new RealmDemoteCommand(plugin));
        this.register();
    }

    @Override
    public void execute(CommandContext<Player> context) {

        final Player player = context.getSender();

        if (this.plugin.getRealmUtils().isInRealm(player.getUniqueId())) {
            this.plugin.getRealmUtils().getRealm(player.getUniqueId()).spawn(player);

            this.plugin.getMessageCache().sendMessage(player, "messages.teleported-home");
            return;
        }

        long startTime = System.nanoTime();

        final Realm realm = new Realm(plugin, player.getUniqueId());

        this.plugin.getRealmStorage().save(realm);

        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000;

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%time%", Utils.format(executionTime));

        this.plugin.getMessageCache().sendMessage(player, "messages.realm-created", replacer);

        final RealmCreateEvent createEvent = new RealmCreateEvent(player, realm);

        Events.call(createEvent);
    }
}
