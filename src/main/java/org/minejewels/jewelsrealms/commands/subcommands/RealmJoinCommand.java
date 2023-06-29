package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmJoinCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmJoinCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("join", "accept"));
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

        if (this.plugin.getRealmUtils().isInRealm(player)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.already-in-realm");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(target.getUniqueId());

        if (realm.getMembers().containsKey(player.getUniqueId()) || realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.cannot-join-yourself");
            return;
        }

        if (!realm.getInvites().contains(player.getUniqueId())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-invite");
            return;
        }

        realm.getInvites().remove(player.getUniqueId());
        realm.addMember(player.getUniqueId(), plugin);
        realm.sendTeamMessage(this.plugin.getMessageCache().getMessage("messages.joined"), new PlaceholderReplacer().addPlaceholder("%member%", player.getName()));
        realm.spawn(player);
    }
}
