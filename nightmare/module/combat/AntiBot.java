package nightmare.module.combat;

import net.minecraft.entity.Entity;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;

public class AntiBot extends Module{

	public AntiBot() {
		super("AntiBot", 0, Category.COMBAT);
	}
	
    @EventTarget
    public void onUpdate(EventUpdate event) {
        for(Object entity : mc.theWorld.loadedEntityList) {
            if(((Entity)entity).isInvisible() && entity != mc.thePlayer) {
                mc.theWorld.removeEntity((Entity)entity);
            }
        }
    }
}