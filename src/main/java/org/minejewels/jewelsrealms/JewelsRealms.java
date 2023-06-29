package org.minejewels.jewelsrealms;

import lombok.Getter;
import net.abyssdev.abysslib.config.AbyssConfig;
import net.abyssdev.abysslib.patterns.registry.Registry;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import net.abyssdev.abysslib.storage.Storage;
import net.abyssdev.abysslib.text.MessageCache;
import org.minejewels.jewelsrealms.commands.RealmCommand;
import org.minejewels.jewelsrealms.listeners.RealmListeners;
import org.minejewels.jewelsrealms.realm.Realm;
import org.minejewels.jewelsrealms.realm.loader.RealmLoader;
import org.minejewels.jewelsrealms.realm.storage.RealmStorage;
import org.minejewels.jewelsrealms.roles.RealmRole;
import org.minejewels.jewelsrealms.roles.registry.RealmRoleRegistry;
import org.minejewels.jewelsrealms.utils.RealmUtils;

import java.util.UUID;

@Getter
public final class JewelsRealms extends AbyssPlugin {

    private static JewelsRealms api;

    private RealmLoader realmLoader;

    private final AbyssConfig settingsConfig = this.getAbyssConfig("settings");
    private final AbyssConfig langConfig = this.getAbyssConfig("lang");
    private final AbyssConfig rolesConfig = this.getAbyssConfig("roles");

    private final Storage<UUID, Realm> realmStorage = new RealmStorage(this);
    private final Registry<String, RealmRole> roleRegistry = new RealmRoleRegistry();

    private final RealmUtils realmUtils = new RealmUtils(this);

    private final MessageCache messageCache = new MessageCache(this.langConfig);

    public int latestRealm = 0;

    @Override
    public void onEnable() {
        JewelsRealms.api = this;

        this.realmLoader = new RealmLoader(this);

        this.loadRealmRoles();
        this.loadRealms();

        this.loadMessages(this.messageCache, this.langConfig);

        new RealmCommand(this);
        new RealmListeners(this);
    }

    @Override
    public void onDisable() {
        this.realmStorage.write();
    }

    public void setLatestRealmID(final int id) {
        this.latestRealm = id;
    }

    private void loadRealmRoles() {

        for (final String key : this.rolesConfig.getSectionKeys("roles")) {
            this.roleRegistry.register(key.toUpperCase(), new RealmRole(
                    this.rolesConfig,
                    "roles." + key,
                    key.toLowerCase()
            ));
        }
    }

    private void loadRealms() {
        for (Realm realm : this.realmStorage.allValues()) {
            this.realmLoader.loadRealm(realm);
        }

        this.setLatestRealmID(this.realmStorage.allKeys().size());
    }

    public static JewelsRealms get() {
        return JewelsRealms.api;
    }
}
