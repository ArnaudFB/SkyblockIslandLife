package fr.nono74210.skyblockislandlife.listeners;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import fr.nono74210.skyblockislandlife.database.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class IslandDeletedListener implements Listener {

    @EventHandler
    public void IslandDeletedEvent(IslandDisbandEvent event) {
        DatabaseManager.deleteIsland(event.getIsland().getUniqueId());
    }
}
