package nightmare.module.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventSendPacket;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class FastBreak extends Module{

    private boolean bzs = false;
    private float bzx = 0.0F;
    public BlockPos blockPos;
    public EnumFacing facing;
    
	public FastBreak() {
		super("FastBreak", 0, Category.WORLD);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Speed", this, 1.4, 1, 2.0, false));
	}

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.playerController.getCurBlockDamageMP() >= Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble()) {
            mc.playerController.setCurBlockDamageMP(1);
            boolean item = mc.thePlayer.getCurrentEquippedItem() == null;
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 20, item ? 1 : 0));
        }
        if (mc.playerController.extendedReach()) {
            mc.playerController.blockHitDelay = 0;
        } else {
            if (bzs) {
                Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                bzx = (float) bzx + (float) (block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) * Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble());
                if (bzx >= 1.0F) {
                    mc.theWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 11);
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, facing));
                    bzx = 0.0F;
                    bzs = false;
                }
            }
        }
    }
    
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C07PacketPlayerDigging && !mc.playerController.extendedReach() && mc.playerController != null) {
            C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging) event.getPacket();
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                bzs = true;
                blockPos = c07PacketPlayerDigging.getPosition();
                facing = c07PacketPlayerDigging.getFacing();
                bzx = 0;
            } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                bzs = false;
                blockPos = null;
                facing = null;
            }
        }
    }   
}
