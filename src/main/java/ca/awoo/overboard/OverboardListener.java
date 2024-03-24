package ca.awoo.overboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class OverboardListener implements Listener {
    private final Overboard plugin;

    public OverboardListener(Overboard plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if(plugin.commandExists(event.getMessage().substring(1).split(" ")[0])){
            event.setCancelled(true);
            plugin.executeCommand(event.getMessage().substring(1), event.getPlayer());
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        if(plugin.commandExists(event.getCommand())){
            String command = event.getCommand();
            event.setCommand("nop");
            plugin.executeCommand(command, event.getSender());
        }
    }
}
