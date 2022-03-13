package nightmare.module.player;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;

public class AutoTool extends Module{
	
	public AutoTool() {
		super("AutoTool", 0, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.currentScreen == null && Mouse.isButtonDown(0) && mc.objectMouseOver != null) {
            BlockPos blockPos = mc.objectMouseOver.getBlockPos();
            if (blockPos != null) {
               Block block = mc.theWorld.getBlockState(blockPos).getBlock();
               float strength = 1.0F;
               int bestToolSlot = -1;

               for(int i = 0; i < 9; ++i) {
                  ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                  if (itemStack != null && itemStack.getStrVsBlock(block) > strength) {
                     strength = itemStack.getStrVsBlock(block);
                     bestToolSlot = i;
                  }
               }

               if (bestToolSlot != -1) {
                  mc.thePlayer.inventory.currentItem = bestToolSlot;
               }
            }
		}
	}
}