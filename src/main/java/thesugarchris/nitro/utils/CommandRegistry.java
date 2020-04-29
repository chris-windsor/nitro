package thesugarchris.nitro.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import thesugarchris.nitro.Nitro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandRegistry implements CommandExecutor {
    private final Plugin plugin;
    private final Map<String, RegisteredCommand> registeredCommandTable = new HashMap<>();

    public CommandRegistry(Plugin plugin) {
        this.plugin = plugin;
    }

    private final Pattern cmdMatch = Pattern.compile("(\\w|\\s)+(?=<)?");

    public void registerCommands(Object object) {
        for (Method method : object.getClass().getMethods()) {
            RegisterAsCommand annotation = method.getAnnotation(RegisterAsCommand.class);

            if (annotation != null) {
                Nitro.log(Text.createMsg("&3Attempting to register command: &1%s", annotation.command()));
                Matcher matcher = cmdMatch.matcher(annotation.command());
                if (!matcher.find()) return;
                String base = matcher.group().trim();

                String[] commandPieces = annotation.command().split(" ");

                PluginCommand basePluginCommand = plugin.getServer().getPluginCommand(commandPieces[0]);

                Integer params = commandPieces.length - base.split(" ").length;

                if (basePluginCommand == null)
                    throw new RuntimeException(String.format("Unable to register command base '%s'. Did you put it in plugin.yml?", base));
                else {
                    basePluginCommand.setExecutor(this);
                    registeredCommandTable.put(base, new RegisteredCommand(method, object, annotation, params));
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*  The code below basically does 3 things:
              1) Try to find a registered sub command matching the command issued by players.
                 If player issued a command "/skyblock create mySkyBlock", there are 3 possibilities about the sub command:
                 a. A sub command named "/skyblock" will handle this with 2 arguments ["create", "mySkyBlock"\
                 b. A sub command named "/skyblock create" will handle this with 1 argument ["mySkyBlock"]
                 c. A sub command named "/skyblock create mySkyBlock" will handle this with no arguments

                 The code below will try to find a registered sub command accepting player's input, from left to right.
                 i.e. first find "/skyblock", if not exist then find "/skyblock create", ...
                 Proceed to step 2 if it finds one.

              2) Pass arguments to the sub command module

              3) Handle exceptions e.g. check permission, number of arguments, whether the sender is the console, etc.
         */

        // Try to find a registered command matching player's input
        StringBuilder sb = new StringBuilder();
        for (int i = -1; i <= args.length - 1; i++) {
            if (i == -1)
                sb.append(command.getName().toLowerCase());
            else
                sb.append(" ").append(args[i].toLowerCase());

            for (String usage : registeredCommandTable.keySet()) {
                if (usage.equals(sb.toString())) {
                    RegisteredCommand wrapper = registeredCommandTable.get(usage);
                    RegisterAsCommand annotation = wrapper.annotation;

                    /* Find actual parameters.

                       Suppose we have a command "/skyblock create <island-name>" and a player issued "/skyblock create BigIsland"
                       The array params[] the method processing the command received should be ["BigIsland"]
                       But here we have ["create", "BigIsland"] in args

                       The idea is to get rid of parts of arguments that belongs to the usage of the command
                       And leave only the parameters that the method should receive (which I called "actual parameters")
                    */
                    String[] actualParams = Arrays.copyOfRange(args, usage.split(" ").length - 1, args.length);

                    // check and handle exceptions

                    // check whether the command only allows to be executed by a player and whether user is a player
                    if (!(sender instanceof Player) && annotation.disallowNonPlayer()) {
                        sender.sendMessage(Text.createMsg("&cThis command can only be run by players"));
                        return true;
                    }

                    // check whether user has no permission the command requires
                    if (!annotation.permission().equals("") && !sender.hasPermission(annotation.permission())) {
                        sender.sendMessage(Text.createMsg("&cInsufficient permissions"));
                        return true;
                    }

                    // check whether user has entered wrong number of parameters
                    Nitro.log(Text.createMsg("&3" + Text.stringFromArray(actualParams)));
                    Nitro.log(Text.createMsg("&3Expected params %d, received %d", wrapper.params, actualParams.length));
                    if (actualParams.length != wrapper.params) {
                        if (actualParams.length > wrapper.params)
                            sender.sendMessage(Text.createMsg("&cToo many parameters provided"));
                        else sender.sendMessage(Text.createMsg("&cToo few parameters provided"));
                        return true;
                    }

                    // user is eligible to execute the command. let's go
                    try {
                        wrapper.method.invoke(wrapper.instance, sender, actualParams);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        sender.sendMessage(Text.createMsg("&cSystem error occurred"));
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }

        sender.sendMessage(Text.createMsg("&cCommand could not be found"));
        return true;
    }

    final static class RegisteredCommand {
        // The instance containing annotated command
        private final Object instance;

        // The method and annotation of annotated command. Used to invoke the target method in the instance
        private final Method method;
        private final RegisterAsCommand annotation;

        private final Integer params;

        RegisteredCommand(Method method, Object instance, RegisterAsCommand annotation, Integer params) {
            this.method = method;
            this.instance = instance;
            this.annotation = annotation;
            this.params = params;
        }
    }

    public void getRegisteredCommands() {
        Nitro.log(Text.createMsg("&5" + Text.stringFromArray(registeredCommandTable.keySet())));
    }
}