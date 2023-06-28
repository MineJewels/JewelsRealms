package org.minejewels.jewelsrealms.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.minejewels.jewelsrealms.permission.RealmPermission;
import org.minejewels.jewelsrealms.roles.RealmRole;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RealmPlayer {

    private final UUID uuid;
    private RealmRole role;

    public boolean hasPermission(final RealmPermission permission) {
        if (this.role == null) {
            return false;
        }

        return this.role.getPermissions().contains(RealmPermission.ALL) || this.role.getPermissions().contains(permission);
    }
}
