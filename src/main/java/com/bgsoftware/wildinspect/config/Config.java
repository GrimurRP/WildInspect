package com.bgsoftware.wildinspect.config;

import java.util.Arrays;
import java.util.List;

public enum Config {
    DEBUG("Debug", false),

    COMMANDS("commands", new String[]{"f inspect", "factions inspect"}),
    REQUIRED_ROLES("required-roles", new String[]{
            "MODERATOR",
            "ADMIN"
    }),
    HISTORY_LIMIT_DATE("history-limit.date", -1),
    HISTORY_LIMIT_PAGE("history-limit.page", -1),
    ENTRIES_PER_PAGE("entries-per-page", 10),
    COOLDOWN("cooldown-between-checks", 3000),
    HIDDEN_NAMES("hidden-nicknames", new String[]{"HiddenPlayer1", "HiddenPlayer2"}),
    PERM_RELOAD("Permissions.reload-permission", "wildinspect.reload"),
    PERM_INSPECT("Permissions.inspect-permission", "");

    String config, message;
    Boolean option;
    String[] messages;
    Integer number;
    Double dnumber;

    Config(String config, String message) {
        this.config = config;
        this.message = message;
    }

    Config(String config, String[] messages) {
        this.config = config;
        this.messages = messages;
    }

    Config(String config, Boolean option) {
        this.config = config;
        this.option = option;
    }

    Config(String config, Integer number) {
        this.config = config;
        this.number = number;
    }

    Config(String config, Double dnumber) {
        this.config = config;
        this.dnumber = dnumber;
    }

    public boolean getOption() {
        return option;
    }

    public String getConfig() {
        return config;
    }

    public String getString() {
        return message;
    }

    public Double getDouble() {
        return dnumber;
    }

    public Integer getInt() {
        return number;
    }

    public String[] getStrings() {
        return this.messages;
    }

    public List<String> getStringList() {
        return Arrays.asList(this.messages);
    }

    public void setInt(int number) {
        this.number = number;
    }

    public void setDouble(double dnumber) {
        this.dnumber = dnumber;
    }

    public void setStrings(List<String> list) {
        this.messages = list.stream().toArray(String[]::new);
    }

    public void setString(String message) {
        this.message = message;
    }

    public void setOption(Boolean option) {
        this.option = option;
    }
}
