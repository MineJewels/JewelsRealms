package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmHomeCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmHomeCommand(final JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("home", "go"));
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (!this.plugin.getRealmUtils().isInRealm(player)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(player);

        realm.spawn(player, plugin);

        this.plugin.getMessageCache().sendMessage(player, "messages.teleported-home");
    }
}
