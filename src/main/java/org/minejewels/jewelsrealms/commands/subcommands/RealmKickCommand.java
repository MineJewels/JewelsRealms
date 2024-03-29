package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmKickCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmKickCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("kick"));
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

        // Cannot-kick-members, user-kicked

        if (!this.plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.KICK_MEMBERS)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            return;
        }

        if (!realm.getMembers().containsKey(target.getUniqueId()) || realm.getOwner().toString().equalsIgnoreCase(target.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.cannot-kick-members");
            return;
        }

        realm.sendTeamMessage(this.plugin.getMessageCache().getMessage("messages.user-kicked"), new PlaceholderReplacer().addPlaceholder("%member%", target.getName()));
        realm.kickMember(target.getUniqueId());

    }
}
