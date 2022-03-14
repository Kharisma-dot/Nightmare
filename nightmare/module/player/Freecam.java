package nightmare.module.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventMove;
import nightmare.event.impl.EventSendPacket;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.PlayerUtils;

public class Freecam extends Module{

    private double oldX, oldY, oldZ;
    private float oldYaw, oldPitch;
    private EntityOtherPlayerMP player;
    
	public Freecam() {
		super("Freecam", 0, Category.PLAYER);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Speed", this, 1, 0.1, 3.0, false));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.playerController.isHittingBlock()) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
		}
	}
	
    @EventTarget
    public void onUpdate(EventMove event) {
        mc.thePlayer.noClip = true;
        event.x = 0;
        event.y = 0;
        event.z = 0;

        if (mc.gameSettings.keyBindSneak.pressed) {
            event.y = -1;
        } else if (mc.gameSettings.keyBindJump.pressed) {
            event.y = 1;
        }
        
        PlayerUtils.setMoveSpeed(event, Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble());
    }
    
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C0APacketAnimation || event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C09PacketHeldItemChange || event.getPacket() instanceof C07PacketPlayerDigging) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onEnable() {
    	
    	if(Nightmare.instance.moduleManager.getModuleByName("Blink").isToggled()) {
    		this.setToggled(false);
    		return;
    	}
    	
        if (mc.theWorld == null) {
            this.setToggled(false);
            return;
        }

        mc.thePlayer.noClip = true;
        this.oldX = mc.thePlayer.posX;
        this.oldY = mc.thePlayer.posY;
        this.oldZ = mc.thePlayer.posZ;
        this.oldYaw = mc.thePlayer.rotationYaw;
        this.oldPitch = mc.thePlayer.rotationPitch;
        (player = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile()))
                .clonePlayer(mc.thePlayer, true);
        player.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        player.rotationYawHead = mc.thePlayer.rotationYaw;
        player.rotationPitch = mc.thePlayer.rotationPitch;
        player.setSneaking(mc.thePlayer.isSneaking());
        mc.theWorld.addEntityToWorld(-1337, player);
        super.onEnable();
    }

    @Override
    public void onDisable() {
    	
    	if(Nightmare.instance.moduleManager.getModuleByName("Blink").isToggled()) {
    		return;
    	}
    	
    	if(mc.theWorld == null || mc.thePlayer == null) {
    		return;
    	}
    	
        mc.thePlayer.noClip = false;
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, oldYaw, oldPitch);
        mc.theWorld.removeEntity(player);
        super.onDisable();
    }
}
