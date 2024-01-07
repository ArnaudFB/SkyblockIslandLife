package fr.nono74210.skyblockislandlife.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import fr.nono74210.skyblockislandlife.utils.ResultT;

import java.util.UUID;

public class SuperiorsSkyBlockHook {

    public ResultT<UUID> getIslandByPlayerUUID(UUID uniqueId) {
        UUID islandUUID = SuperiorSkyblockAPI.getPlayer(uniqueId).getIsland().getUniqueId();
        if (islandUUID == null) {
            return ResultT.error("No island was found linked to player with uuid : " + uniqueId);
        }

        return ResultT.success(islandUUID);
    }
}
