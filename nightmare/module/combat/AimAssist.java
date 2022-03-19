package nightmare.module.combat;

import java.util.List;
import java.util.Random;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class AimAssist extends Module{

	protected Random rand = new Random();
	
	public AimAssist() {
		super("AimAssist", 0, Category.COMBAT);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("ClickAim", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("BreakBlock", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("HeldItem", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Horizontal", this, 4.2, 0, 6, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Vertical", this, 2.4, 0, 6, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Range", this, 4.2, 1.0, 8.0, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("FOV", this, 100, 20, 360, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Strafe", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Team", this, false));
	}

	@EventTarget
	public void onTick(EventTick event) {
		
		Entity entity = null;
		double maxDistance = 360.0;
		double maxAngle = Nightmare.instance.settingsManager.getSettingByName(this, "FOV").getValDouble();
		double minAngle = 0;
		
		if (Nightmare.instance.settingsManager.getSettingByName(this, "ClickAim").getValBoolean() && !this.mc.gameSettings.keyBindAttack.isKeyDown() || Nightmare.instance.settingsManager.getSettingByName(this, "BreakBlock").getValBoolean() && mc.playerController.isHittingBlock() || Nightmare.instance.moduleManager.getModuleByName("Spin").isToggled() || Nightmare.instance.moduleManager.getModuleByName("Perspective").isToggled()) {
			return;
		}
		
		if(mc.theWorld != null) {
			for (Object e : this.mc.theWorld.getLoadedEntityList()) {
				double yawdistance;
				float yaw;
				Entity en = (Entity) e;
				if (en == this.mc.thePlayer || !this.isValid(en) || !(maxDistance > (yawdistance = getDistanceBetweenAngles(yaw = getAngles(en)[1], this.mc.thePlayer.rotationYaw)))) {
					continue;
				}
				entity = en;
				maxDistance = yawdistance;
			}
		}
		
        if (!Nightmare.instance.settingsManager.getSettingByName(this, "HeldItem").getValBoolean() || mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
    		if (entity != null) {
    			
				double horizontalSpeed = Nightmare.instance.settingsManager.getSettingByName(this, "Horizontal").getValDouble() * 3.0 + (Nightmare.instance.settingsManager.getSettingByName(this, "Horizontal").getValDouble() > 0.0 ? this.rand.nextDouble() : 0.0);
				double verticalSpeed = Nightmare.instance.settingsManager.getSettingByName(this, "Vertical").getValDouble() * 3.0 + (Nightmare.instance.settingsManager.getSettingByName(this, "Vertical").getValDouble() > 0.0 ? this.rand.nextDouble() : 0.0);
    			float yaw = getAngles(entity)[1];
    			float pitch = getAngles(entity)[0];
    			double yawdistance = getDistanceBetweenAngles(yaw, this.mc.thePlayer.rotationYaw);
    			double pitchdistance = getDistanceBetweenAngles(pitch, this.mc.thePlayer.rotationPitch);
    			if (pitchdistance <= maxAngle && yawdistance >= minAngle && yawdistance <= maxAngle) {
    				
       				if (Nightmare.instance.settingsManager.getSettingByName(this, "Strafe").getValBoolean() && this.mc.thePlayer.moveStrafing != 0.0f) {
    					horizontalSpeed *= 1.25;
    				}
       				
    				if (getEntity(24.0) != null && getEntity(24.0).equals(entity)) {
    					horizontalSpeed *= 1;
    					verticalSpeed *= 1;
    				}
    				this.faceTarget(entity, (float) horizontalSpeed, (float) verticalSpeed);
    			}
    		}
        }
	}
	
	public static double getDistanceBetweenAngles(float angle1, float angle2) {
		float distance = Math.abs(angle1 - angle2) % 360.0f;
		if (distance > 180.0f) {
			distance = 360.0f - distance;
		}
		return distance;
	}
	
	protected float getRotation(float currentRotation, float targetRotation, float maxIncrement) {
		float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
		if (deltaAngle > maxIncrement) {
			deltaAngle = maxIncrement;
		}
		if (deltaAngle < -maxIncrement) {
			deltaAngle = -maxIncrement;
		}
		return currentRotation + deltaAngle / 2.0f;
	}

	private void faceTarget(Entity target, float yawspeed, float pitchspeed) {
		EntityPlayerSP player = this.mc.thePlayer;
		float yaw = getAngles(target)[1];
		float pitch = getAngles(target)[0];
		player.rotationYaw = this.getRotation(player.rotationYaw, yaw, yawspeed);
		player.rotationPitch = this.getRotation(player.rotationPitch, pitch, pitchspeed);
	}

	public static float[] getAngles(Entity entity) {
		double x = entity.posX - mc.thePlayer.posX;
		double z = entity.posZ - mc.thePlayer.posZ;
		double y = entity instanceof EntityEnderman ? entity.posY - mc.thePlayer.posY
				: entity.posY + ((double) entity.getEyeHeight() - 1.9) - mc.thePlayer.posY
						+ ((double) mc.thePlayer.getEyeHeight() - 1.9);
		double helper = MathHelper.sqrt_double(x * x + z * z);
		float newYaw = (float) Math.toDegrees(-Math.atan(x / z));
		float newPitch = (float) (-Math.toDegrees(Math.atan(y / helper)));
		if (z < 0.0 && x < 0.0) {
			newYaw = (float) (90.0 + Math.toDegrees(Math.atan(z / x)));
		} else if (z < 0.0 && x > 0.0) {
			newYaw = (float) (-90.0 + Math.toDegrees(Math.atan(z / x)));
		}
		return new float[] { newPitch, newYaw };
	}
	
	public static Entity getEntity(double distance) {
		if (getEntity(distance, 0.0, 0.0f) == null) {
			return null;
		}
		return (Entity) getEntity(distance, 0.0, 0.0f)[0];
	}
	
	public static Object[] getEntity(double distance, double expand, float partialTicks) {
		Entity entityView = mc.getRenderViewEntity();
		Entity entity = null;
		if (entityView != null && mc.theWorld != null) {
			double var3;
			mc.mcProfiler.startSection("pick");
			double var5 = var3 = distance;
			Vec3 var7 = entityView.getPositionEyes(0.0f);
			Vec3 var8 = entityView.getLook(0.0f);
			Vec3 var9 = var7.addVector(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3);
			Vec3 var10 = null;
			float var11 = 1.0f;
			List var12 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(entityView, entityView.getEntityBoundingBox()
					.addCoord(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3).expand(var11, var11, var11));
			double var13 = var5;
			for (int var15 = 0; var15 < var12.size(); ++var15) {
				double var20;
				Entity var16 = (Entity) var12.get(var15);
				if (!var16.canBeCollidedWith())
					continue;
				float var17 = var16.getCollisionBorderSize();
				AxisAlignedBB var18 = var16.getEntityBoundingBox().expand(var17, var17, var17);
				var18 = var18.expand(expand, expand, expand);
				MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
				if (var18.isVecInside(var7)) {
					if (!(0.0 < var13) && var13 != 0.0)
						continue;
					entity = var16;
					var10 = var19 == null ? var7 : var19.hitVec;
					var13 = 0.0;
					continue;
				}
				if (var19 == null || !((var20 = var7.distanceTo(var19.hitVec)) < var13) && var13 != 0.0)
					continue;
				boolean canRiderInteract = false;
				if (var16 == entityView.ridingEntity && !canRiderInteract) {
					if (var13 != 0.0)
						continue;
					entity = var16;
					var10 = var19.hitVec;
					continue;
				}
				entity = var16;
				var10 = var19.hitVec;
				var13 = var20;
			}
			if (var13 < var5 && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
				entity = null;
			}
			mc.mcProfiler.endSection();
			if (entity == null || var10 == null) {
				return null;
			}
			return new Object[] { entity, var10 };
		}
		return null;
	}
	
	public boolean isValid(Entity e) {
		return !(e instanceof EntityLivingBase) ? false : (e instanceof EntityArmorStand ? false : (e instanceof EntityAnimal ? false : (e instanceof EntityMob ? false : (e.getDisplayName().getFormattedText().contains("[NPC]") ? false : (e == mc.thePlayer ? false : (e instanceof EntityVillager ? false : ((double) mc.thePlayer.getDistanceToEntity(e) > (Nightmare.instance.settingsManager.getSettingByName(this, "Range").getValDouble()) ? false : (e.getName().contains("#") ? false : (Nightmare.instance.settingsManager.getSettingByName(this, "Team").getValBoolean() && e.getDisplayName().getFormattedText().startsWith("\u00a7" + mc.thePlayer.getDisplayName().getFormattedText().charAt(1)) ? false : !e.getName().toLowerCase().contains("shop"))))))))));
	}
}
