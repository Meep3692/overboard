package ca.awoo.overboard;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class Overboard extends JavaPlugin {
    
    CommandDispatcher<CommandSender> dispatcher = new CommandDispatcher<>();
    private Logger logger;
    
    @Override
    public void onEnable() {
        logger = getServer().getLogger();
        dispatcher.register(LiteralArgumentBuilder.<CommandSender>literal("test").executes(c -> {
            c.getSource().sendMessage("Test command executed");
            return 1;
        }));
        dispatcher.register(
        LiteralArgumentBuilder.<CommandSender>literal("foo")
            .then(
                RequiredArgumentBuilder.<CommandSender, Integer>argument("bar", integer())
                    .executes(c -> {
                        System.out.println("Bar is " + getInteger(c, "bar"));
                        return 1;
                    })
            )
            .executes(c -> {
                System.out.println("Called foo with no arguments");
                return 1;
            })
        );
        getServer().getPluginManager().registerEvents(new OverboardListener(this), this);
        getCommand("nop").setExecutor(new NopCommandExecutor());
    }
    
    @Override
    public void onDisable() {
        
    }
    
    public boolean commandExists(String label){
        return dispatcher.getRoot().getChild(label) != null;
    }
    
    public void registerCommand(LiteralArgumentBuilder<CommandSender> command) {
        dispatcher.register(command);
    }
    
    public void executeCommand(String command, CommandSender sender) {
        try {
            dispatcher.execute(command, sender);
        } catch (CommandSyntaxException e) {
            CommandNode<CommandSender> node = dispatcher.getRoot().getChild(command.split(" ")[0]);
            sender.sendMessage("Failed to execute command: " + command);
            sender.sendMessage("Usage: ");
            dispatcher.getSmartUsage(node, sender).forEach((subNode, action) -> sender.sendMessage(node.getUsageText() + " " + action));
        }
    }
    
    public Logger getLogger(){
        return logger;
    }
    
}
