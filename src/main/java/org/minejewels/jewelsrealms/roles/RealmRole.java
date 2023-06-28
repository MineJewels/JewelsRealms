package org.minejewels.jewelsrealms.roles;

import lombok.Getter;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelsrealms.permission.RealmPermission;

import java.util.Set;

@Getter
public class RealmRole {

    private final String name, identifier;
    private final Set<RealmPermission> permissions = Sets.mutable.withInitialCapacity(RealmPermission.values().length);
    private final int weight;

    public RealmRole(final FileConfiguration config, final String path, final String identifier) {
        this.identifier = identifier;
        this.name = WordUtils.formatText(this.identifier
                .replace("-", " ")
                .replace("_", " "));

        this.weight = config.getInt(path + ".weight");

        for (final String permission : config.getStringList(path + ".permissions")) {
            if (!RealmPermission.exists(permission)) {
                continue;
            }

            this.permissions.add(RealmPermission.valueOf(permission));
        }
    }
}
