package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmLeaveCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmLeaveCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("delete", "disband"));
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (!this.plugin.getRealmUtils().isInRealm(player)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(player);

        if (realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.cannot-leave");
            return;
        }

        realm.sendTeamMessage(this.plugin.getMessageCache().getMessage("messages.user-left"), new PlaceholderReplacer().addPlaceholder("%member%", player.getName()));
        realm.kickMember(player.getUniqueId());
    }
}
