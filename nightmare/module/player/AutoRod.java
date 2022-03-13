package nightmare.module.player;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Multimap;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.TimerUtils;

public class AutoRod extends Module{

    private final TimerUtils timer = new TimerUtils();
    private final TimerUtils timer2 = new TimerUtils();

    private Boolean switchBack = false;
    private Boolean useRod = false;
    
    private int oldCurrentItem;
    
	public AutoRod() {
		super("AutoRod", 0, Category.PLAYER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Delay", this, 100, 50, 1000, false));
	}

	@Override
	public void onEnable() {
		
		if(mc.thePlayer == null || mc.theWorld == null) {
			return;
		}
		
		oldCurrentItem = mc.thePlayer.inventory.currentItem;
		super.onEnable();
	}
	
    @EventTarget
    private void onUpdate(EventUpdate event) {
    	
        int item = mc.thePlayer.getHeldItem() != null ? Item.getIdFromItem(mc.thePlayer.getHeldItem().getItem()) : 0;
        float rodDelay = (float) Nightmare.instance.settingsManager.getSettingByName(this, "Delay").getValDouble();
        
        if (mc.currentScreen != null) {
            return;
        }

        if (item == 346) {
            if (timer2.delay(rodDelay + 200)) {
                Rod();
                timer2.reset();
            }

            if (timer.delay(rodDelay)) {
                mc.thePlayer.inventory.currentItem = oldCurrentItem;
                timer.reset();
                toggle();
            }
        } else if (timer.delay(100)) {
            switchToRod();
            timer.reset();
        }
    }
    
    private int findRod(int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() == Items.fishing_rod) {
                return i;
            }
        }

        return -1;
    }

    private void switchToRod() {
        for (int i = 36; i < 45; i++) {
            ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemstack != null && Item.getIdFromItem(itemstack.getItem()) == 346) {
                mc.thePlayer.inventory.currentItem = i - 36;
                break;
            }
        }
    }

    private void Rod() {
        int rod = findRod(36, 45);
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventoryContainer.getSlot(rod).getStack());
        switchBack = true;
        timer.reset();
    }
}
