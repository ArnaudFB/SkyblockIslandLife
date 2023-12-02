package fr.nono74210.pluginvie.listeners;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import fr.nono74210.pluginvie.database.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;

public class IslandDeletedListener implements Listener {

    private final Database database;

    public IslandDeletedListener(Database database) {
        this.database = database;
    }

    @EventHandler
    public void IslandDeletedEvent(IslandDisbandEvent event) throws SQLException {

        database.deleteIslandFromDatabase(event.getIsland().getUniqueId());

    }
}
