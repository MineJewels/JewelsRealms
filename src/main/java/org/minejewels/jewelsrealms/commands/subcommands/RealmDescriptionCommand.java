package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.realm.Realm;

public class RealmDescriptionCommand extends AbyssSubCommand<JewelsRealms> {

    public RealmDescriptionCommand(final JewelsRealms plugin) {
        super(plugin, 1, Sets.immutable.of("description", "setdescription"));
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (!this.plugin.getRealmUtils().isInRealm(player)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-realm");
            return;
        }

        final Realm realm = this.plugin.getRealmUtils().getRealm(player);

        if (context.getArguments().length < 1) {
            return;
        }

        if (!this.plugin.getRealmUtils().hasPermission(realm, player, RealmPermission.CHANGE_DESCRIPTION)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission-realm");
            return;
        }

        final String description = String.join(" ", context.getArguments());

        if (description.equalsIgnoreCase("")) {
            this.plugin.getMessageCache().sendMessage(player, "messages.realm-help");
            return;
        }

        realm.setDescription(description);
        realm.sendTeamMessage(this.plugin.getMessageCache().getMessage("messages.realm-description-changed"), new PlaceholderReplacer());
    }
}
