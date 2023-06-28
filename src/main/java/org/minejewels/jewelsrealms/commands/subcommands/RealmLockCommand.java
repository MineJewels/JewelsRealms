package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmLockCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmLockCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("lock", "close"));
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (!this.plugin.getRealmUtils().isInRealm(player)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(player);

        if (!this.plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.LOCK_REALM)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            return;
        }

        if (!realm.isOpen()) {
            this.plugin.getMessageCache().sendMessage(player, "messages.already-locked");
            return;
        }

        realm.setOpen(false);

        realm.sendTeamMessage(this.plugin.getMessageCache().getMessage("messages.realm-locked"), new PlaceholderReplacer());
    }
}
