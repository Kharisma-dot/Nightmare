package nightmare.module;

import java.util.ArrayList;

import nightmare.module.combat.AimAssist;
import nightmare.module.combat.AntiBot;
import nightmare.module.combat.AutoClicker;
import nightmare.module.combat.BowAimAssist;
import nightmare.module.combat.HitBox;
import nightmare.module.combat.KillAura;
import nightmare.module.combat.Reach;
import nightmare.module.combat.Velocity;
import nightmare.module.misc.AntiAtlas;
import nightmare.module.misc.FPSBoost;
import nightmare.module.misc.NameProtect;
import nightmare.module.misc.NoPotionShift;
import nightmare.module.misc.OldAnimation;
import nightmare.module.misc.Particle;
import nightmare.module.misc.Perspective;
import nightmare.module.misc.Spin;
import nightmare.module.movement.AutoWalk;
import nightmare.module.movement.Blink;
import nightmare.module.movement.FastBridge;
import nightmare.module.movement.GuiMove;
import nightmare.module.movement.KeepSprint;
import nightmare.module.movement.NoSlow;
import nightmare.module.movement.SafeWalk;
import nightmare.module.movement.Sprint;
import nightmare.module.movement.Step;
import nightmare.module.player.AutoArmor;
import nightmare.module.player.AutoFish;
import nightmare.module.player.AutoRespawn;
import nightmare.module.player.AutoRod;
import nightmare.module.player.AutoTool;
import nightmare.module.player.ChatBypass;
import nightmare.module.player.FastBow;
import nightmare.module.player.Freecam;
import nightmare.module.player.InvManager;
import nightmare.module.render.ActiveMods;
import nightmare.module.render.Blur;
import nightmare.module.render.Chams;
import nightmare.module.render.Chat;
import nightmare.module.render.ClickGUI;
import nightmare.module.render.Fullbright;
import nightmare.module.render.HUD;
import nightmare.module.render.Inventory;
import nightmare.module.render.ItemPhysic;
import nightmare.module.render.NameTags;
import nightmare.module.render.PotionStatus;
import nightmare.module.render.TargetHUD;
import nightmare.module.render.Trajectories;
import nightmare.module.render.ViewClip;
import nightmare.module.world.AutoHypixel;
import nightmare.module.world.ChestStealer;
import nightmare.module.world.FastBreak;
import nightmare.module.world.FastPlace;
import nightmare.module.world.LightningTracker;
import nightmare.module.world.StaffAnalyser;
import nightmare.module.world.TimeChanger;
import nightmare.module.world.Timer;

public class ModuleManager {
	
    public ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleManager() {

    	//Combat
    	modules.add(new AntiBot());
    	modules.add(new AimAssist());
    	modules.add(new AutoClicker());
    	modules.add(new BowAimAssist());
    	modules.add(new Reach());
    	modules.add(new HitBox());
    	modules.add(new Velocity());
    	modules.add(new KillAura());
    	
    	//Movement
    	modules.add(new AutoWalk());
    	modules.add(new Blink());
    	modules.add(new FastBridge());
    	modules.add(new GuiMove());
    	modules.add(new KeepSprint());
    	modules.add(new NoSlow());
    	modules.add(new SafeWalk());
    	modules.add(new Sprint());
    	modules.add(new Step());
    	
    	//Render
    	modules.add(new ActiveMods());
    	modules.add(new Blur());
    	modules.add(new Chat());
    	modules.add(new Chams());
    	modules.add(new ClickGUI());
    	modules.add(new Fullbright());
    	modules.add(new HUD());
    	modules.add(new Inventory());
    	modules.add(new ItemPhysic());
    	modules.add(new NameTags());
    	modules.add(new PotionStatus());
    	modules.add(new TargetHUD());
    	modules.add(new Trajectories());
    	modules.add(new ViewClip());
    	
    	//Player
    	modules.add(new AutoArmor());
    	modules.add(new AutoFish());
    	modules.add(new AutoRespawn());
    	modules.add(new AutoRod());
    	modules.add(new AutoTool());
    	modules.add(new ChatBypass());
    	modules.add(new FastBow());
    	modules.add(new Freecam());
    	modules.add(new InvManager());
    	
    	//World
    	modules.add(new AutoHypixel());
    	modules.add(new ChestStealer());
    	modules.add(new FastBreak());
    	modules.add(new FastPlace());
    	modules.add(new LightningTracker());
    	modules.add(new StaffAnalyser());
    	modules.add(new TimeChanger());
    	modules.add(new Timer());
    	
    	//Misc
    	modules.add(new AntiAtlas());
    	modules.add(new FPSBoost());
    	modules.add(new NameProtect());
    	modules.add(new NoPotionShift());
    	modules.add(new OldAnimation());
    	modules.add(new Particle());
    	modules.add(new Perspective());
    	modules.add(new Spin());
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

	public ArrayList<Module> getModulesInCategory(Category c) {
		ArrayList<Module> module = new ArrayList<Module>();
		for (Module m : this.modules) {
			if (m.getCategory() == c) {
				module.add(m);
			}
		}
		return module;
	}
}