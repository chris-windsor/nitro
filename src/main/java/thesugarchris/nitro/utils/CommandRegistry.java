package thesugarchris.nitro.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import thesugarchris.nitro.Nitro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    private final Pattern optParamMatch = Pattern.compile("<\\w+\\?");

    public void registerCommands(Object object) {
        for (Method method : object.getClass().getMethods()) {
            RegisterAsCommand annotation = method.getAnnotation(RegisterAsCommand.class);

            if (annotation != null) {
                Nitro.log(Text.createMsg("&3Registering command: &1%s", annotation.command()));
                Matcher cmdBaseMatcher = cmdMatch.matcher(annotation.command());
                if (!cmdBaseMatcher.find()) return;
                String cmdBase = cmdBaseMatcher.group().trim();

                String[] commandPieces = annotation.command().split(" ");

                PluginCommand basePluginCommand = plugin.getServer().getPluginCommand(commandPieces[0]);

                Integer params = commandPieces.length - cmdBase.split(" ").length;

                int optParams = 0;
                Matcher optParamMatcher = optParamMatch.matcher(annotation.command());

                if (optParamMatcher.find()) {
                    optParams = (int) (optParamMatcher.results().count() + 1);
                }

                if (basePluginCommand == null)
                    throw new RuntimeException(String.format("Unable to register command base '%s'. Did you put it in plugin.yml?", cmdBase));
                else {
                    basePluginCommand.setExecutor(this);
                    registeredCommandTable.put(cmdBase, new RegisteredCommand(method, object, annotation, params, optParams));
                }
            }
        }
    }

    public String[] parseArgs(String[] args) {
        ArrayList<String> properArgs = new ArrayList<>();
        int argIdx = 0;
        boolean isBuildingArg = false;
        while (argIdx < args.length) {
            String currentArg = args[argIdx];
            if (isBuildingArg) {
                String previousArg = properArgs.get(properArgs.size() - 1);
                properArgs.set(properArgs.size() - 1, previousArg + currentArg);
            } else properArgs.add(currentArg);
            if (currentArg.startsWith("\"")) isBuildingArg = true;
            if (currentArg.endsWith("\"")) isBuildingArg = false;
            argIdx++;
        }
        return properArgs.toArray(String[]::new);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] rawArgs) {
        String[] args = parseArgs(rawArgs);

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

                    String[] actualParams = Arrays.copyOfRange(args, usage.split(" ").length - 1, args.length);

                    if (!(sender instanceof Player) && annotation.disallowNonPlayer()) {
                        sender.sendMessage(Text.createMsg("&cThis command can only be run by players"));
                        return true;
                    }

                    if (!annotation.permission().equals("") && !sender.hasPermission(annotation.permission())) {
                        sender.sendMessage(Text.createMsg("&cInsufficient permissions"));
                        return true;
                    }

                    // debug
                    Nitro.log(Text.createMsg("&3Provided args: [&r%s&3]", ArrayUtils.joinValues(Arrays.stream(actualParams).map(s -> s + ChatColor.RESET).toArray(String[]::new), ", ")));
                    Nitro.log(Text.createMsg("&3Min/Max -> %d:%d, received %d", wrapper.minParams, wrapper.maxParams, actualParams.length));

                    if (actualParams.length > wrapper.maxParams) {
                        sender.sendMessage(Text.createMsg("&cToo many parameters provided"));
                        return true;
                    }

                    if (actualParams.length < wrapper.minParams) {
                        sender.sendMessage(Text.createMsg("&cToo few parameters provided"));
                        return true;
                    }

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
        private final Object instance;

        private final Method method;
        private final RegisterAsCommand annotation;

        private final Integer maxParams;
        private final Integer minParams;

        RegisteredCommand(Method method, Object instance, RegisterAsCommand annotation, Integer params, Integer optParams) {
            this.method = method;
            this.instance = instance;
            this.annotation = annotation;
            this.maxParams = params;
            this.minParams = params - optParams;
        }
    }

    public void getRegisteredCommands() {
        Nitro.log(Text.createMsg("&5%s", ArrayUtils.joinValues(registeredCommandTable.keySet(), ", ")));
    }
}