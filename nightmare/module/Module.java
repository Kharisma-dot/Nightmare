package nightmare.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import nightmare.Nightmare;
import nightmare.gui.notification.NotificationManager;
import nightmare.utils.ChatUtils;

public class Module {
    protected static Minecraft mc = Minecraft.getMinecraft();

    private String name, displayName;
    private int key;
    private Category category;
    protected boolean toggled;
	public boolean visible = true;
	
    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
        toggled = false;
    }

    public void onEnable() {
    	Nightmare.instance.eventManager.register(this);
    }
    public void onDisable() {
    	Nightmare.instance.eventManager.unregister(this);
    }

    public void onToggle() {}
    public void toggle() {
        toggled = !toggled;
        onToggle();
        if(toggled) {
            onEnable();
			NotificationManager.show("Module", ChatUtils.GREEN + "Toggle " + ChatUtils.RESET + name, 2500);
        }
        else {
            onDisable();
			NotificationManager.show("Module", ChatUtils.RED + "Disable " + ChatUtils.RESET + name, 2500);
        }
		if (Nightmare.instance.config != null) {
			Nightmare.instance.config.save();
		}
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
    public boolean isToggled() {
        return toggled;
    }
    public boolean isDisabled() {
        return !toggled;
    }
    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
		
		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
		if (Nightmare.instance.config != null) {
			Nightmare.instance.config.save();
		}
	}
	public Category getCategory() {
		return this.category;
	}
}