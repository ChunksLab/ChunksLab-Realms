package com.chunkslab.realms.api.role;

public interface IRoleManager {

    void enable();

    RealmRole getRealmRole(String id);

    RealmRole getDefaultRole();

    RealmRole getLastRole();
}