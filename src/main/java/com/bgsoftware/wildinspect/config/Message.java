package com.bgsoftware.wildinspect.config;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public enum Message {

    PREFIX("Command.prefix", "&6&lInspect &f➤ "),
    COOLDOWN("Command.in-cooldown", "&4✕ &f&lWAIT {0} SEC &4✕"),

    RELOAD_SUCCESS("Command.reload-success", "&7[&a✔&7] &aConfig Reloaded!"),
    COMMAND_USAGE("Command.usage", "&7[&4✕&7] &cCorrect Usage: &f/{0} {1}"),
    INSPECTOR_ON("Command.inspect.enabled", "&7[&a✔&7] &eInspector &aEnabled&e!"),
    INSPECTOR_OFF("Command.inspect.disabled", "&7[&a✔&7] &eInspector &cDisabled&e!"),
    INSPECT_DATA_HEADER("Command.inspect.data-header", new String[]{
            "&f----- &3Inspect &f----- &7(x{0}/y{1}/z{2})"
    }),
    INSPECT_DATA_ROW("Command.inspect.data-row", "&7{0} &f- &3{1} &f{2} &3{3}&f."),
    INSPECT_DATA_FOOTER("Command.inspect.data-footer", new String[]{
            "&f-----",
            "&fPage {0}/{1} - View older data by typing &3/f inspect <page>"
    }),
    NO_PERMISSION("Error.no-permission", "&7[&4✕&7] &cYou lack the required permissions!"),
    NOT_INSIDE_CLAIM("Error.not-inside-claim", "&7[&4✕&7] &cYou must be inside your claims to read Logs!"),
    LIMIT_REACH("Error.no-more-pages", "&7[&4✕&7] &cThere are no more Log pages!"),
    NO_DATA("Error.no-page-data", "&7[&4✕&7] &cNo Data was found for that Page!"),
    NO_BLOCK_DATA("Error.no-block-data", "&7[&4✕&7] &cNo Logs were found for Block '{0}'!"),
    NO_BLOCK_INTERACTIONS("Error.no-block-interactions", "&7[&4✕&7] &cNo Interaction Logs were found for Block '{0}'!"),
    NO_CONTAINER_TRANSACTIONS("Error.no-container-transactions", "&7[&4✕&7] &cNo Container Logs were found for Block '{0}'!"),
    NO_BLOCK_SELECTED("Error.no-block-selected", "&7[&4✕&7] &cYou must first select a Block with '{0}'!"),
    SPECIFY_PAGE("Error.no-page", "&7[&4✕&7] &cYou must specify a page number!"),
    REQUIRED_ROLE("Error.no-required-role", "&7[&4✕&7] &cYou don't have the required Role! ({0})");


    String config, message;
    String[] messages;

    Message(String config, String message) {
        this.config = config;
        this.message = message;
    }

    Message(String config, String[] messages) {
        this.config = config;
        this.messages = messages;
    }

    public String getConfig() {
        return config;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Object... objects) {
        if (message != null && !message.isEmpty()) {
            String msg = message;
            for (int i = 0; i < objects.length; i++)
                msg = msg.replace("{" + i + "}", objects[i].toString());
            return msg;
        }
        return null;
    }

    public String[] getMessages() {
        return this.messages;
    }

    public String[] getMessages(Object... objects) {
        String[] parsed = new String[messages.length];
        int index = 0;
        for (String s : messages) {
            String msg = s;
            for (int i = 0; i < objects.length; i++)
                msg = msg.replace("{" + i + "}", objects[i].toString());
            parsed[index] = msg;
            index++;
        }
        return parsed;
    }

    public void setMessages(List<String> list) {
        this.messages = color(list.toArray(new String[0]));
    }

    public void setMessage(String message) {
        this.message = color(message);
    }

    private String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private String[] color(String[] str) {
        if (str.length <= 0) return str;
        List<String> colored = new ArrayList<>();
        for (String string : str) {
            colored.add(color(string));
        }
        String[] itemsArray = new String[colored.size()];
        return colored.toArray(itemsArray);
    }

}
