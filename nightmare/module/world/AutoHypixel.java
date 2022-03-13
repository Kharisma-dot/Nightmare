package nightmare.module.world;

import net.minecraft.item.Item;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S45PacketTitle;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventReceivePacket;
import nightmare.event.impl.EventSendPacket;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.TimerUtils;

public class AutoHypixel extends Module{

	private TimerUtils timer = new TimerUtils();
	private String playCommand;
	private boolean autogg = false;
	private boolean autoplay = false;
	
	public AutoHypixel() {
		super("AutoHypixel", 0, Category.WORLD);
		
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoGG", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoPlay", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Delay", this, 3, 0, 5, false));
	}

	@Override
	public void onEnable() {
		this.playCommand = "";
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "AutoGG").getValBoolean() && this.autogg == true) {
			mc.thePlayer.sendChatMessage("/achat gg");
			this.autogg = false;
		}
		
		if(Nightmare.instance.settingsManager.getSettingByName(this, "AutoPlay").getValBoolean() && this.autoplay == true) {
			if(timer.delay(Nightmare.instance.settingsManager.getSettingByName(this, "Delay").getValDouble() * 1000)) {
				mc.thePlayer.sendChatMessage(playCommand);
				this.autoplay = false;
				timer.reset();
			}
		}
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle packet = (S45PacketTitle) event.getPacket();
            String title = packet.getMessage().getFormattedText();
            
            if (title.startsWith("\2476\247l") && title.endsWith("\247r")) {
            	
            }
            
            if (title.startsWith("\2476\247l") && title.endsWith("\247r") || title.startsWith("\247c\247lY") && title.endsWith("\247r")) {
            	this.autoplay = true;
            	timer.reset();
            }
        }
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C0EPacketClickWindow) {
            C0EPacketClickWindow packet = (C0EPacketClickWindow) event.getPacket();
            String itemname = packet.getClickedItem().getDisplayName();
            if (packet.getClickedItem().getDisplayName().startsWith("\247a")) {
                int itemID = Item.getIdFromItem(packet.getClickedItem().getItem());
                if (itemID == 381 || itemID == 368) {
                    if (itemname.contains("SkyWars")) {
                        if (itemname.contains("Doubles")) {
                            if (itemname.contains("Normal")) {
                                playCommand = "/play teams_normal";
                            } else if (itemname.contains("Insane")) {
                                playCommand = "/play teams_insane";
                            }
                        } else if (itemname.contains("Solo")) {
                            if (itemname.contains("Normal")) {
                                playCommand = "/play solo_normal";
                            } else if (itemname.contains("Insane")) {
                                playCommand = "/play solo_insane";
                            }
                        }
                    }
                } else if (itemID == 355) {
                    if (itemname.contains("Bed Wars")) {
                        if (itemname.contains("4v4")) {
                            playCommand = "/play bedwars_four_four";
                        } else if (itemname.contains("3v3")) {
                            playCommand = "/play bedwars_four_three";
                        } else if (itemname.contains("Doubles")) {
                            playCommand = "/play bedwars_eight_two";
                        } else if (itemname.contains("Solo")) {
                            playCommand = "/play bedwars_eight_one";
                        }
                    }
                }
            }
        } else if (event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage packet = (C01PacketChatMessage) event.getPacket();
            if (packet.getMessage().startsWith("/play")) {
                playCommand = packet.getMessage();
            }
        }
	}
}