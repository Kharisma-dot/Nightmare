package nightmare.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.MathHelper;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.RotationUtils;
import nightmare.utils.WorldUtils;

public class BowAimAssist extends Module{

    public EntityLivingBase target;
    
	public BowAimAssist() {
		super("BowAimAssist", 0, Category.COMBAT);
		
		ArrayList<String> options = new ArrayList<>();
		
		options.add("Angle");
		options.add("Range");
		
		Nightmare.instance.settingsManager.rSetting(new Setting("FOV", this, 180, 10, 360, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Range", this, 100, 1, 200, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Speed", this, 4.2, 0, 6, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Mode", this, "Angle", options));
	}

    @EventTarget
    public void onTick(EventTick event) {
    	
    	if(Nightmare.instance.moduleManager.getModuleByName("Blink").isToggled() || Nightmare.instance.moduleManager.getModuleByName("Freecam").isToggled()) {
    		return;
    	}

        if(mc.gameSettings.keyBindUseItem.isKeyDown() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            target = this.getTarget();
        }else {
        	target = null;
        }

        if (target == null) {
            return;
        }
        
        this.faceTarget(target, (float) Nightmare.instance.settingsManager.getSettingByName(this, "Speed").getValDouble());
    }
    
    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
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

	private void faceTarget(Entity target, float speed) {
		EntityPlayerSP player = this.mc.thePlayer;
        final float[] rotation = this.getPlayerRotations(target);
		float yaw = rotation[0];
		float pitch = rotation[1];
		player.rotationYaw = this.getRotation(player.rotationYaw, yaw, speed);
		player.rotationPitch = this.getRotation(player.rotationPitch, pitch, speed);
	}
	
    private float[] getPlayerRotations(final Entity entity) {
        double distanceToEnt = mc.thePlayer.getDistanceToEntity(entity);
        double predictX = entity.posX + (entity.posX - entity.lastTickPosX) * (distanceToEnt * 0.8);
        double predictZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) * (distanceToEnt * 0.8);

        double x = predictX - mc.thePlayer.posX;
        double z = predictZ - mc.thePlayer.posZ;
        double h = entity.posY + 1.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());

        double h1 = Math.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;

        float pitch = -RotationUtils.getTrajAngleSolutionLow((float) h1, (float) h, 1);

        return new float[]{yaw, pitch};
    }

    private EntityLivingBase getTarget() {
    	
    	String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
    	
        Stream<EntityPlayer> stream = WorldUtils.getLivingPlayers().stream().filter(mc.thePlayer::canEntityBeSeen).filter(e -> RotationUtils.isVisibleFOV(e, (float) Nightmare.instance.settingsManager.getSettingByName(this, "FOV").getValDouble()));

        if (mode.equals("Range")) {
            stream = stream.sorted(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));
        } else if (mode.equals("Angle")) {
            stream = stream.sorted(Comparator.comparingDouble(RotationUtils::getYawToEntity));
        }
        List<EntityPlayer> list = stream.collect(Collectors.toList());
        if (list.size() <= 0)
            return null;

        return list.get(0);
    }
}
