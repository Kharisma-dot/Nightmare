package nightmare.module.movement;

import java.util.ArrayList;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventSendPacket;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.TimerUtils;

public class Blink extends Module{

    private EntityOtherPlayerMP blinkEntity;
    private final ArrayList<Packet> packetList = new ArrayList();
    
    private TimerUtils timer = new TimerUtils();
    
	public Blink() {
		super("Blink", 0, Category.MOVEMENT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoDisable", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Delay", this, 1.5, 1, 5, false));
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if(Nightmare.instance.settingsManager.getSettingByName(this, "AutoDisable").getValBoolean()) {
			if(timer.delay(Nightmare.instance.settingsManager.getSettingByName(this, "Delay").getValDouble() * 1000)) {
				this.setToggled(false);
				timer.reset();
			}
		}
	}
	
    @EventTarget
    private void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();

            if (packet.isMoving()) {
                packetList.add(event.getPacket());
                event.setCancelled(true);
            }
        }

        if (event.getPacket() instanceof C0APacketAnimation || event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            event.setCancelled(true);
        }
    }
    
	@Override
	public void onEnable() {
		super.onEnable();
		if(mc.thePlayer == null || mc.theWorld == null || mc.isSingleplayer() || Nightmare.instance.moduleManager.getModuleByName("Freecam").isToggled()) {
			this.setToggled(false);
			return;
		}
		
        blinkEntity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        blinkEntity.copyLocationAndAnglesFrom(mc.thePlayer);
        blinkEntity.setRotationYawHead(mc.thePlayer.rotationYawHead);
        mc.theWorld.addEntityToWorld(blinkEntity.getEntityId(), blinkEntity);
        timer.reset();
	}
	
	@Override
	public void onDisable() {
		
        super.onDisable();
        
		if(mc.thePlayer == null || mc.theWorld == null || mc.isSingleplayer()) {
			return;
		}
	
		if(Nightmare.instance.moduleManager.getModuleByName("Freecam").isToggled()) {
			return;
		}
		
        mc.theWorld.removeEntityFromWorld(blinkEntity.getEntityId());

        if (!packetList.isEmpty()) {
            packetList.forEach(this::sendPacket);
            packetList.clear();
        }
	}
	
    public void sendPacket(Packet<?> packet) {
        if (mc.thePlayer != null) {
        	mc.getNetHandler().getNetworkManager().sendPacket(packet);
        }
    }
}
