package nightmare.module.movement;

import java.util.ArrayList;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventReceivePacket;
import nightmare.event.impl.EventSendPacket;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Blink extends Module{

    private EntityOtherPlayerMP blinkEntity;
    private final ArrayList<Packet> packetList = new ArrayList();
    
	public Blink() {
		super("Blink", 0, Category.MOVEMENT);
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
		if(mc.thePlayer == null || mc.theWorld == null) {
			this.setToggled(false);
			return;
		}
		
		if(Nightmare.instance.moduleManager.getModuleByName("Freecam").isToggled()) {
			this.setToggled(false);
			return;
		}
		
        blinkEntity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        blinkEntity.copyLocationAndAnglesFrom(mc.thePlayer);
        blinkEntity.setRotationYawHead(mc.thePlayer.rotationYawHead);
        mc.theWorld.addEntityToWorld(blinkEntity.getEntityId(), blinkEntity);
	}
	
	@Override
	public void onDisable() {
		
        super.onDisable();
        
		if(mc.thePlayer == null || mc.theWorld == null) {
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
