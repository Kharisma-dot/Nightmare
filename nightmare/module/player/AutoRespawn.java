package nightmare.module.player;

import nightmare.event.EventTarget;
import nightmare.event.impl.EventTick;
import nightmare.module.Category;
import nightmare.module.Module;

public class AutoRespawn extends Module{

	public AutoRespawn() {
		super("AutoRespawn", 0, Category.PLAYER);
	}

	@EventTarget
	public void onTick(EventTick event) {
        if (mc.thePlayer.isDead) {
        	mc.thePlayer.respawnPlayer();
        }
    }
}
