package com.github.games647.fastlogin.bukkit.hooks;

import com.github.games647.fastlogin.bukkit.BukkitLoginSession;
import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.hooks.AuthPlugin;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.RestoreSessionEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * GitHub: https://github.com/Xephi/AuthMeReloaded/
 * <p>
 * Project page:
 * <p>
 * Bukkit: https://dev.bukkit.org/bukkit-plugins/authme-reloaded/
 * <p>
 * Spigot: https://www.spigotmc.org/resources/authme-reloaded.6269/
 */
public class AuthMeHook implements AuthPlugin<Player>, Listener {

    private final FastLoginBukkit plugin;

    public AuthMeHook(FastLoginBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSessionRestore(RestoreSessionEvent restoreSessionEvent) {
        Player player = restoreSessionEvent.getPlayer();

        String id = '/' + player.getAddress().getAddress().getHostAddress() + ':' + player.getAddress().getPort();
        BukkitLoginSession session = plugin.getLoginSessions().get(id);
        if (session != null && session.isVerified()) {
            restoreSessionEvent.setCancelled(true);
        }
    }

    @Override
    public boolean forceLogin(Player player) {
        //skips registration and login
        if (AuthMeApi.getInstance().isAuthenticated(player)) {
            return false;
        }

        AuthMeApi.getInstance().forceLogin(player);
        return true;
    }

    @Override
    public boolean isRegistered(String playerName) {
        return AuthMeApi.getInstance().isRegistered(playerName);
    }

    @Override
    public boolean forceRegister(Player player, String password) {
        //this automatically registers the player too
        AuthMeApi.getInstance().forceRegister(player, password);
        return true;
    }
}
