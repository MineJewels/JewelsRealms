package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmDeleteCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmDeleteCommand(JewelsRealms plugin) {
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

        if (!realm.getOwner().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            return;
        }

        // Run gen code
        this.plugin.getRealmLoader().deleteRealm(realm);
        this.plugin.getRealmStorage().remove(player.getUniqueId());

        this.plugin.getMessageCache().sendMessage(player, "messages.realm-deleted");
    }
}
