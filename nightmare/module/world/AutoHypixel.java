package nightmare.module.world;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import nightmare.Nightmare;
import nightmare.event.EventTarget;
import nightmare.event.impl.EventSendPacket;
import nightmare.event.impl.EventUpdate;
import nightmare.module.Category;
import nightmare.module.Module;
import nightmare.settings.Setting;
import nightmare.utils.TimerUtils;

public class AutoHypixel extends Module{

	private TimerUtils timer = new TimerUtils();
	
    public String playCommand = "";
    
	public AutoHypixel() {
		super("AutoHypixel", 0, Category.WORLD);
		
        ArrayList<String> options = new ArrayList<>();
        options.add("Solo-Normal");
        options.add("Solo-Insane");
        
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoGG", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("AutoPlay", this, false));
		Nightmare.instance.settingsManager.rSetting(new Setting("Mode", this, "Solo-Normal", options));
		Nightmare.instance.settingsManager.rSetting(new Setting("Delay", this, 3, 0, 5, false));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		int time = (int) Nightmare.instance.settingsManager.getSettingByName(this, "Delay").getValDouble();
		String mode = Nightmare.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		
		if(GuiNewChat.autoplay == true) {
			if(timer.delay(1000 * time)) {
				mc.thePlayer.sendChatMessage(playCommand);
				timer.reset();
				GuiNewChat.autoplay = false;
			}
		}else {
			timer.reset();
		}
	}
	
    @EventTarget
    public void onPacket(EventSendPacket e) {
    	
        if (playCommand.startsWith("/play ")) {
            String display = playCommand.replace("/play ", "").replace("_", " ");
            boolean nextUp = true;
            String result = "";
            for (char c : display.toCharArray()) {
                if (c == ' ') {
                    nextUp = true;
                    result += " ";
                    continue;
                }
                if (nextUp) {
                    nextUp = false;
                    result += Character.toUpperCase(c);
                } else {
                    result += c;
                }
            }
        }

        if (e.getPacket() instanceof C0EPacketClickWindow) {
            C0EPacketClickWindow packet = (C0EPacketClickWindow) e.getPacket();
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
        } else if (e.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage packet = (C01PacketChatMessage) e.getPacket();
            if (packet.getMessage().startsWith("/play")) {
                playCommand = packet.getMessage();
            }
        }
    }
}