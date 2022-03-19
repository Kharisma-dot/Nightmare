package nightmare.module.movement;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventPostMotionUpdate;
import nightmare.event.impl.EventPreMotionUpdate;
import nightmare.event.impl.EventSlowDown;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.ChatUtils;
import nightmare.utils.PlayerUtils;

public class NoSlow extends Module{

	public NoSlow() {
		super("NoSlow", 0, Category.MOVEMENT);
		
		ArrayList<String> options = new ArrayList<>();
		
		options.add("Vanilla");
		options.add("NCP");
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Mode", this, "Vanilla", options));
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		this.setDisplayName("NoSlow " + ChatUtils.GRAY + mode);
	}
	
	@EventTarget
	public void onSlowDown(EventSlowDown event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onPreUpdate(EventPreMotionUpdate event) {
		String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		
		if(mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && PlayerUtils.isMoving() && mode.equals("NCP")) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}
	}
	
    @EventTarget
    public void onPost(EventPostMotionUpdate e) {
    	String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
    	if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && PlayerUtils.isMoving() && mode.equalsIgnoreCase("NCP")) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.getHeldItem()));
        }
    }

}
