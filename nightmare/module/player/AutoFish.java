package nightmare.module.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventReceivePacket;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.utils.TimerUtils;

public class AutoFish extends Module{
	
	private TimerUtils timer = new TimerUtils();
	private boolean fishing = false;
	private boolean nextFishing = false;
	
	public AutoFish() {
		super("AutoFish", 0, Category.PLAYER);
	}

	@EventTarget
	public void onPacket(EventReceivePacket event) {
		 if (event.getPacket() instanceof S29PacketSoundEffect) {
             S29PacketSoundEffect packet = (S29PacketSoundEffect) event.getPacket();

             if (packet.getSoundName().equals("random.splash")) {
            	 this.fishing = true;
             }
         }
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
    	if(this.fishing == true) {
    		Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.getHeldItem()));
     		this.fishing = false;
     		nextFishing = true;
    	}
    	
    	if(this.nextFishing == true) {
    		if(timer.delay(500)) {
    			Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.getHeldItem()));
    			this.nextFishing = false;
    			timer.reset();
    		}
    	}else {
    		timer.reset();
    	}
	}
}