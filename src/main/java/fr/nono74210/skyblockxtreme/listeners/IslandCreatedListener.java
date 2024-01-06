package fr.nono74210.skyblockxtreme.listeners;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import fr.nono74210.skyblockxtreme.database.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class IslandCreatedListener implements Listener {

    @EventHandler
    public void IslandCreatedEvent(IslandCreateEvent event) {
        DatabaseManager.addIsland(event.getIsland().getUniqueId());
    }
}
