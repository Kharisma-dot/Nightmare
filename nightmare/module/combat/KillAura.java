package nightmare.module.combat;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventPostMotionUpdate;
import nightmare.event.impl.EventPreMotionUpdate;
import nightmare.event.impl.EventRespawn;
import nightmare.gui.notification.NotificationManager;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.TimerUtils;

public class KillAura extends Module {
	
    private EntityLivingBase target;
    private float yaw, pitch;
    private boolean others;

    private TimerUtils timer = new TimerUtils();
    
    public KillAura() {
        super("KillAura", 0, Category.COMBAT);
        
        Nightmare.instance.settingsManager.rSetting(new Setting("AutoDisable", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("MinCPS", this, 12, 1, 20, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("MaxCPS", this, 15, 1, 20, false));
        Nightmare.instance.settingsManager.rSetting(new Setting("FOV", this, 180, 0, 180, true));
        Nightmare.instance.settingsManager.rSetting(new Setting("Invisibles", this, false));
        Nightmare.instance.settingsManager.rSetting(new Setting("Players", this, true));
        Nightmare.instance.settingsManager.rSetting(new Setting("Animals", this, false));
        Nightmare.instance.settingsManager.rSetting(new Setting("Monsters", this, false));
        Nightmare.instance.settingsManager.rSetting(new Setting("Villagers", this, false));
        Nightmare.instance.settingsManager.rSetting(new Setting("Teams", this, false));
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
    	
        target = getClosest(mc.playerController.getBlockReachDistance());
        
    	if(Nightmare.instance.moduleManager.getModuleByName("Freecam").isToggled() || Nightmare.instance.moduleManager.getModuleByName("Blink").isToggled() || target == null) {
    		return;
    	}
    	
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;
        
        if (timer.delay(1000 / ThreadLocalRandom.current().nextInt((int) Nightmare.instance.settingsManager.getSettingByName(this, "MinCPS").getValDouble(), (int) Nightmare.instance.settingsManager.getSettingByName(this, "MaxCPS").getValDouble() + 1))) {
            attack(target);
            timer.reset();
        }
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        if(target == null)
            return;
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }

    @EventTarget
    public void onRespawn(EventRespawn event) {
    	if(Nightmare.instance.settingsManager.getSettingByName(this, "AutoDisable").getValBoolean()) {
        	NotificationManager.show("Module", "Disable KillAura (AutoDisable)", getKey());
        	this.setToggled(false);
    	}
    }
    	
    private void attack(Entity entity) {
        mc.thePlayer.swingItem();
        mc.playerController.attackEntity(mc.thePlayer, entity);
    }

    private EntityLivingBase getClosest(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (canAttack(player)) {
                    double currentDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                    }
                }
            }
        }
        return target;
    }

    private boolean canAttack(EntityLivingBase player) {
        if(player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !Nightmare.instance.settingsManager.getSettingByName(this, "Players").getValBoolean())
                return false;
            if (player instanceof EntityAnimal && !Nightmare.instance.settingsManager.getSettingByName(this, "Animals").getValBoolean())
                return false;
            if (player instanceof EntityMob && !Nightmare.instance.settingsManager.getSettingByName(this, "Monsters").getValBoolean())
                return false;
            if (player instanceof EntityVillager && !Nightmare.instance.settingsManager.getSettingByName(this, "Villagers").getValBoolean())
                return false;
        }
        if(player.isOnSameTeam(mc.thePlayer) && Nightmare.instance.settingsManager.getSettingByName(this, "Teams").getValBoolean())
            return false;
        if(player.isInvisible() && !Nightmare.instance.settingsManager.getSettingByName(this, "Invisibles").getValBoolean())
            return false;
        if(!isInFOV(player, Nightmare.instance.settingsManager.getSettingByName(this, "FOV").getValDouble()))
            return false;
        return player != mc.thePlayer && player.isEntityAlive() && mc.thePlayer.getDistanceToEntity(player) <= mc.playerController.getBlockReachDistance() && player.ticksExisted > 0;
    }

    private boolean isInFOV(EntityLivingBase entity, double angle) {
        angle *= .5D;
        double angleDiff = getAngleDifference(mc.thePlayer.rotationYaw, getRotations(entity.posX, entity.posY, entity.posZ)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }

    private float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        float dist = f > 180F ? 360F - f : f;
        return dist;
    }

    private float[] getRotations(double x, double y, double z) {
        double diffX = x + .5D - mc.thePlayer.posX;
        double diffY = (y + .5D) / 2D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double diffZ = z + .5D - mc.thePlayer.posZ;

        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180D / Math.PI) - 90F;
        float pitch = (float)-(Math.atan2(diffY, dist) * 180D / Math.PI);

        return new float[] { yaw, pitch };
    }
}