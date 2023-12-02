package fr.nono74210.pluginvie.listeners;

import fr.nono74210.pluginvie.database.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;

import java.sql.SQLException;

public class IslandCreatedListener implements Listener {

    private final Database database;

    public IslandCreatedListener(Database database) {
        this.database = database;
    }

    @EventHandler
    public void IslandCreatedEvent(IslandCreateEvent event) throws SQLException {

        database.addIslandToDatabase(event.getIsland().getUniqueId());

    }
}
