package nightmare.module.player;

import static net.minecraft.enchantment.Enchantment.blastProtection;
import static net.minecraft.enchantment.Enchantment.fireProtection;
import static net.minecraft.enchantment.Enchantment.projectileProtection;
import static net.minecraft.enchantment.Enchantment.thorns;
import static net.minecraft.enchantment.Enchantment.unbreaking;
import static net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.TimerUtils;

public class AutoArmor extends Module{

	TimerUtils timer = new TimerUtils();
	
	public AutoArmor() {
		super("AutoArmor", 0, Category.PLAYER);
		
		 Nightmare.instance.settingsManager.rSetting(new Setting("Delay", this, 3.0D, 1.0D, 10.0D, false));
		 Nightmare.instance.settingsManager.rSetting(new Setting("InvOnly", this, false));
	}

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if ((Nightmare.instance.settingsManager.getSettingByName(this, "InvOnly").getValBoolean() && mc.currentScreen instanceof GuiInventory) || !Nightmare.instance.settingsManager.getSettingByName(this, "InvOnly").getValBoolean()) {
            for (int type = 1; type < 5; type++) {
                if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                    ItemStack slotStack = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();

                    if (isBestArmor(slotStack, type)) {
                        continue;
                    } else {
                        mc.thePlayer.drop(4 + type);
                    }
                }

                for (int i = 9; i < 45; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack slotStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                        if (isBestArmor(slotStack, type) && getProtection(slotStack) > 0) {
                            if (timer.check((int)Nightmare.instance.settingsManager.getSettingByName(this, "Delay").getValDouble() * 50)) {
                                mc.thePlayer.shiftClick(i);
                                timer.reset();
                            }
                        }
                    }
                }
            }
        }
    }

    private float getProtection(ItemStack stack) {
        float protection = 0;

        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();

            protection += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * getEnchantmentLevel(
                    Enchantment.protection.effectId, stack) * 0.0075D;
            protection += getEnchantmentLevel(blastProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(fireProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(thorns.effectId, stack) / 100d;
            protection += getEnchantmentLevel(unbreaking.effectId, stack) / 50d;
            protection += getEnchantmentLevel(projectileProtection.effectId, stack) / 100d;
        }

        return protection;
    }
    
    public boolean isWorking() {
        return !timer.check((int)Nightmare.instance.settingsManager.getSettingByName(this, "Delay").getValDouble() * 100);
    }

    boolean isBestArmor(ItemStack stack, int type) {
        String strType = "";

        switch (type) {
            case 1:
                strType = "helmet";
                break;
            case 2:
                strType = "chestplate";
                break;
            case 3:
                strType = "leggings";
                break;
            case 4:
                strType = "boots";
                break;
        }

        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }

        float protection = getProtection(stack);

        for (int i = 5; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > protection && is.getUnlocalizedName().contains(strType)) return false;
            }
        }

        return true;
    }
}