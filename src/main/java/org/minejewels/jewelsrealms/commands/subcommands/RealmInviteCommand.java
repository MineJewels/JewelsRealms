package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmInviteCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmInviteCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("invite"));
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

        if (this.plugin.getRealmUtils().isInRealm(target.getUniqueId())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.already-in-realm-others");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(player.getUniqueId());

        if (!this.plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.LOCK_REALM)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            return;
        }

        // invited-realm, invited-personal

        if (realm.getMembers().containsKey(target.getUniqueId()) || realm.getOwner().toString().equalsIgnoreCase(target.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.cannot-reinvite-members");
            return;
        }

        if (realm.getInvites().contains(target.getUniqueId())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.already-invited");
            return;
        }

        this.plugin.getMessageCache().sendMessage(player, "messages.realm-visited", new PlaceholderReplacer().addPlaceholder("%owner%", realm.getOwnerName()));
        realm.inviteMember(target.getUniqueId());

        this.plugin.getMessageCache().sendMessage(target, "messages.invited-personal", new PlaceholderReplacer().addPlaceholder("%realm%", realm.getOwnerName()));
        realm.sendTeamMessage(this.plugin.getMessageCache().getMessage("messages.invited-realm"), new PlaceholderReplacer().addPlaceholder("%member%", target.getName()));

    }
}
