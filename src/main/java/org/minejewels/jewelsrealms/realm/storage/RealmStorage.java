package org.minejewels.jewelsrealms.realm.storage;

import net.abyssdev.abysslib.storage.json.JsonStorage;
import net.abyssdev.abysslib.utils.file.Files;
import org.minejewels.jewelsrealms.JewelsRealms;
import org.minejewels.jewelsrealms.realm.Realm;

import java.util.UUID;

public class RealmStorage extends JsonStorage<UUID, Realm> {

    public RealmStorage(final JewelsRealms plugin) {
        super(Files.file("realms.json", plugin), Realm.class);
    }
}
