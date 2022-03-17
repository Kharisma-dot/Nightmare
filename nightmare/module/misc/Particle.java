package nightmare.module.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;

public class Particle extends Module{

	public Particle() {
		super("Particle", 0, Category.MISC);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("Critical", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Multiplier", this, 3, 1, 10, true));
	}
}
