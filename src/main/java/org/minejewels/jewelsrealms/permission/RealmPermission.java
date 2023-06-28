package org.minejewels.jewelsrealms.permission;

public enum RealmPermission {

    PLACE_BLOCKS,
    BREAK_BLOCKS,
    DROP_ITEMS,
    PICKUP_ITEMS,
    INVITE_MEMBERS,
    PROMOTE_MEMBERS,
    DEMOTE_MEMBERS,
    KICK_MEMBERS,
    LOCK_REALM,
    UNLOCK_REALM,
    DELETE_REALM,
    CHANGE_DESCRIPTION,
    ALL;

    public static boolean exists(final String input) {
        for (final RealmPermission perm : RealmPermission.values()) {
            if (perm.name().equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }
}
