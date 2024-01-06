package fr.nono74210.skyblockxtreme.listeners;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import fr.nono74210.skyblockxtreme.database.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class IslandDeletedListener implements Listener {

    @EventHandler
    public void IslandDeletedEvent(IslandDisbandEvent event) {
        DatabaseManager.deleteIsland(event.getIsland().getUniqueId());
    }
}
