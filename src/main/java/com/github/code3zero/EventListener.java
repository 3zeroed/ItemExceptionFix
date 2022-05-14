package com.github.code3zero;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.PlayerOffhandInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Code3zero
 * @date 2022/5/14
 */
public class EventListener implements Listener {

    Map<Player, Map<Integer, Item>> tempPlayerInventoryContents = new HashMap<>();
    Map<Player, Map<Integer, Item>> tempPlayerOffhandInventoryContents = new HashMap<>();

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        player.getLevel().getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        player.getLevel().getGameRules().setGameRule(GameRule.KEEP_INVENTORY, true);

        PlayerInventory playerInventory = player.getInventory();
        PlayerOffhandInventory playerOffhandInventory = player.getOffhandInventory();

        Map<Integer, Item> tempInventoryContents = new HashMap<>();
        Map<Integer, Item> tempOffhandInventoryContents = new HashMap<>();
        for (Map.Entry<Integer, Item> entry : playerInventory.getContents().entrySet()) {
            Item item = entry.getValue();
            item.setCompoundTag(item.getOrCreateNamedTag().putString("test", "test"));
            tempInventoryContents.put(entry.getKey(), item);
        }
        for (Map.Entry<Integer, Item> entry : playerOffhandInventory.getContents().entrySet()) {
            Item item = entry.getValue();
            item.setCompoundTag(item.getOrCreateNamedTag().putString("test", "test"));
            tempOffhandInventoryContents.put(entry.getKey(), item);
        }
        tempPlayerInventoryContents.put(player, tempInventoryContents);
        tempPlayerOffhandInventoryContents.put(player, tempOffhandInventoryContents);
        playerInventory.clearAll();
        playerOffhandInventory.clearAll();
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (tempPlayerInventoryContents.containsKey(player) && tempPlayerOffhandInventoryContents.containsKey(player)) {
            PlayerInventory playerInventory = player.getInventory();
            PlayerOffhandInventory playerOffhandInventory = player.getOffhandInventory();
            playerInventory.setContents(tempPlayerInventoryContents.get(player));
            playerOffhandInventory.setContents(tempPlayerOffhandInventoryContents.get(player));
            tempPlayerInventoryContents.remove(player);
            tempPlayerOffhandInventoryContents.remove(player);
        }
    }
}
