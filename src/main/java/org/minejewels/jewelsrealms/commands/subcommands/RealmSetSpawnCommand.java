package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmSetSpawnCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmSetSpawnCommand(final JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("setspawn"));
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (!this.plugin.getRealmUtils().isInRealm(player)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(player);

        if (!this.plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.SET_SPAWN)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            return;
        }

        final Location location = player.getLocation();

        realm.setSpawnX(location.getX() + 0.5);
        realm.setSpawnY(location.getY() + 1);
        realm.setSpawnZ(location.getZ() + 0.5);

        this.plugin.getMessageCache().sendMessage(player, "messages.realm-spawn-updated");
    }
}
