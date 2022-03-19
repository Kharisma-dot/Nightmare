package nightmare.module.combat;

import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventPreMotionUpdate;
import nightmare.event.impl.EventSendPacket;
import nightmare.event.impl.EventTick;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.PlayerUtils;
import nightmare.utils.TimerUtils;

public class AutoClicker extends Module{

	private TimerUtils timer = new TimerUtils();
	
    private boolean blocking;
    
	public AutoClicker() {
		super("AutoClicker", 0, Category.COMBAT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoBlock", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("MinCPS", this, 12, 1, 20, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("MaxCPS", this, 15, 1, 20, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Weapons Only", this, false));
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		int key = mc.gameSettings.keyBindAttack.getKeyCode();
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").getValDouble() > Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() - 1) {
			Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").setValDouble(Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() - 0.1);
		}
		
		if(!Nightmare.instance.settingsManager.getSettingByName(this, "AutoBlock").getValBoolean()) {
			if (mc.gameSettings.keyBindAttack.isKeyDown() && (!Nightmare.instance.settingsManager.getSettingByName(this, "Weapons Only").getValBoolean() || mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemTool || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))) {
	            if (timer.delay(1000 / ThreadLocalRandom.current().nextInt((int) Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() + 1))) {
	            	KeyBinding.onTick(key);
	                timer.reset();
	            }
	        }
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "AutoBlock").getValBoolean() && Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode()) || Nightmare.instance.moduleManager.getModuleByName("Sprint").isToggled() && mc.thePlayer.moveForward != 0.0f) {
			mc.thePlayer.setSprinting(true);
		}
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "AutoBlock").getValBoolean()) {
			if (mc.gameSettings.keyBindAttack.isKeyDown() && (!Nightmare.instance.settingsManager.getSettingByName(this, "Weapons Only").getValBoolean() || mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemTool || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))) {
	            if (timer.delay(1000 / ThreadLocalRandom.current().nextInt((int) Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() + 1))) {
	            	if(mc.objectMouseOver.entityHit != null) {
		            	this.attackEntity(mc.objectMouseOver.entityHit);
	            	}else {
	            		mc.thePlayer.swingItem();
	            	}
	            	timer.reset();
	            }
			}
		}
		
        if (shouldBlock()) {
            interactAutoBlock();
            mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
            
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            blocking = true;
        }
	}
	
	@EventTarget
	public void onPreUpdate(EventPreMotionUpdate event) {
        if (shouldBlock() || blocking) {
        	mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blocking = false;
        }
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
        if (shouldBlock() || blocking) {
            if (event.getPacket() instanceof C07PacketPlayerDigging) {
                C07PacketPlayerDigging packet = (C07PacketPlayerDigging) event.getPacket();

                if (packet.getStatus().equals(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
                    event.setCancelled(true);
                }
            }

            if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.getPacket();

                if (packet.getPlacedBlockDirection() == 255) {
                    event.setCancelled(true);
                }
            }
        }
	}
	
    private void interactAutoBlock() {
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (mc.objectMouseOver.entityHit != null) {
            	mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.INTERACT));

            } else if (interactable(mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock())) {
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
                        mc.objectMouseOver.getBlockPos(), Block.getFacingDirection(mc.objectMouseOver.getBlockPos()), mc.objectMouseOver.hitVec);
            }
        }
    }
    
    private boolean interactable(Block block) {
        return block == Blocks.chest || block == Blocks.trapped_chest || block == Blocks.crafting_table
                || block == Blocks.furnace || block == Blocks.ender_chest || block == Blocks.enchanting_table;
    }
    
    private void attackEntity(Entity target) {
        if (shouldBlock()) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blocking = false;
        }
        mc.thePlayer.swingItem();
        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }
    
    public boolean shouldBlock() {
    	if(mc.thePlayer != null && mc.theWorld != null && mc.objectMouseOver.entityHit != null && mc.thePlayer.getHeldItem() != null) {
            return Nightmare.instance.settingsManager.getSettingByName(this, "AutoBlock").getValBoolean() && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.objectMouseOver.entityHit.isEntityAlive() && this.isToggled() && !mc.playerController.isHittingBlock();
    	}else {
    		return false;
    	}
    }
}
