package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.events.RealmDeleteEvent;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmAdminCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmAdminCommand(final JewelsRealms plugin) {
        super(plugin, false, 0, Sets.immutable.of("admin"));
    }

    @Override
    public void execute(CommandContext<?> context) {
        String[] args = context.getArguments();

        if (!context.getSender().hasPermission("realms.admin")) {
            this.plugin.getMessageCache().sendMessage(context.getSender(), "messages.no-permission-realm");
            return;
        }

        // Check if the command is "/realm admin disband"
        if (args.length == 3 && args[1].equalsIgnoreCase("disband")) {

            final Player target = Bukkit.getPlayer(args[2]);

            if (!this.plugin.getRealmUtils().isInRealm(target.getUniqueId())) {
                this.plugin.getMessageCache().sendMessage(context.getSender(), "messages.no-realm-exists");
                return;
            }

            final Realm realm = this.plugin.getRealmUtils().getRealm(target.getUniqueId());

            final RealmDeleteEvent deleteEvent = new RealmDeleteEvent(target, realm);

            Events.call(deleteEvent);

            if (deleteEvent.isCancelled()) {
                return;
            }

            // Run gen code
            this.plugin.getRealmLoader().deleteRealm(realm);
            this.plugin.getRealmStorage().remove(target.getUniqueId());

            this.plugin.getMessageCache().sendMessage(target, "messages.realm-deleted");
        }
    }
}
