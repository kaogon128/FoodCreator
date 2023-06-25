package com.github.kaogon128.foodcreator;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SimpleListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {
        // 基本if文は求めてる結果じゃなかったらreturnにするべき
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        // InteractEventで右クリック使用時に二回動作する対策
        if (e.getHand() != EquipmentSlot.HAND) return;
        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
        Food food = FoodCreator.getInstance().getFood(hand);
        // onEnableで作ったリストの中からメインハンドのアイテムがmatchKeyを持っているか確認
        // なかったらreturn (ない = 作られたアイテムじゃない)
        if (food == null) return;
        // 右クリックをキャンセルしてヘッドを設置するのを防止
        e.setCancelled(true);
        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EAT, 2, 1);
        e.getPlayer().setFoodLevel(Math.min(e.getPlayer().getFoodLevel() + food.getFoodLevel(), 20));
        // 自分はif分岐なしでもこれでいけたのでそのまま書いてます
        hand.setAmount(hand.getAmount() - 1);
    }
}
