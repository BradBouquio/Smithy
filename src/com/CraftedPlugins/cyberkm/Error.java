package com.CraftedPlugins.cyberkm;

import java.util.logging.Level;

import net.minespire.smithy.Smithy;

public class Error {
    public static void execute(Smithy plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(Smithy plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}