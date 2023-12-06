package fr.nono74210.pluginvie.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;

import java.util.UUID;

public class SuperiorsSkyBlockHook {

    public UUID getIslandByPlayerUUID(UUID uniqueId) {
        Island playerIsland = SuperiorSkyblockAPI.getPlayer(uniqueId).getIsland();
        if (playerIsland == null) {
            //todo:
            return;
        }

        return playerIsland.getUniqueId();
    }
}
