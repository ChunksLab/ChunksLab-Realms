package com.chunkslab.realms.api.upgrade;

public interface IUpgradeManager {

    void enable();

    Upgrade getUpgrade(Upgrade.Type type, int level);

}