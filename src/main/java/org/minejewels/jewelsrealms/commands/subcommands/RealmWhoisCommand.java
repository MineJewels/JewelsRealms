package org.minejewels.jewelsrealms.commands.subcommands;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.text.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RealmWhoisCommand extends AbyssSubCommand<JewelsRealms> {

    private final String locked, unlocked;

    public RealmWhoisCommand(JewelsRealms plugin) {
        super(plugin, 0, Sets.immutable.of("whois", "who", "info"));

        this.locked = plugin.getSettingsConfig().getColoredString("status.locked");
        this.unlocked = plugin.getSettingsConfig().getColoredString("status.unlocked");
    }

    @Override
    public void execute(CommandContext<?> context) {

        final Player player = context.getSender();

        if (context.getArguments().length == 0) {

            if (!this.plugin.getRealmUtils().isInRealm(player)) {
                this.plugin.getMessageCache().sendMessage(player, "messages.no-realm");
                return;
            }

            final Realm realm = this.plugin.getRealmUtils().getRealm(player);

            this.getInfo(player, realm);
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

        final Realm realm = this.plugin.getRealmUtils().getRealm(target.getUniqueId());

        this.getInfo(player, realm);
    }

    private void getInfo(final Player player, final Realm realm) {

        final StringBuilder builder = new StringBuilder();
        final List<UUID> list = new LinkedList<>(realm.getMembers().keySet());

        for (final UUID uuid : list) {

            if (realm.getMembers().size() == 1) {
                builder.append(Bukkit.getOfflinePlayer(uuid).getName());
                break;
            }

            if (list.indexOf(uuid) == list.size() - 1) {
                builder.append(Bukkit.getOfflinePlayer(uuid).getName());
                break;
            }

            builder.append(Bukkit.getOfflinePlayer(uuid).getName()).append("&8, ");
        }

        if (builder.toString().equalsIgnoreCase("")) {
            builder.append("N/A");
        }

        final PlaceholderReplacer replacer = new PlaceholderReplacer();

        replacer.addPlaceholder("%owner%", realm.getOwnerName());
        replacer.addPlaceholder("%description%", realm.getDescription());
        replacer.addPlaceholder("%members%", Color.parse(builder.toString()));
        replacer.addPlaceholder("%status%", this.getFormattedStatus(realm.isOpen()));

        this.plugin.getMessageCache().sendMessage(player, "messages.realm-whois", replacer);
    }

    private String getFormattedStatus(final boolean status) {
        if (status) {
            return this.unlocked;
        }

        return this.locked;
    }
}
