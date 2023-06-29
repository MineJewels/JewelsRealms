package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmDemoteCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmDemoteCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("demote"));
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (!this.plugin.getRealmUtils().isInRealm(player)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm");
            return;
        }

        if (context.getArguments().length == 0) {
            return;
        }

        if (!context.asOfflinePlayer(0).hasPlayedBefore()) {
            this.plugin.getMessageCache().sendMessage(player, "messages.invalid-player");
            return;
        }

        final Player target = context.asPlayer(0);

        final Realm realm = this.plugin.getRealmUtils().getRealm(player.getUniqueId());

        if (!this.plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.DEMOTE_MEMBERS)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            return;
        }

        if (!realm.getMembers().containsKey(target.getUniqueId())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.invalid-player");
            return;
        }

       realm.demoteMember(player, target.getUniqueId(), this.plugin);

    }
}
