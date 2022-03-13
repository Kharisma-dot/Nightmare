package nightmare.command.impl;

import net.minecraft.client.Minecraft;
import nightmare.command.Command;

public class Vclip extends Command{

	public Vclip() {
		super("Vclip", "Moves the specified number of blocks.", "vclip <number of blocks>", "vc");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		if(args.length < 1) {
			return;
		}
		
		int posY;
		
		try {
			posY = Integer.parseInt(args[0]);
			Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + posY, Minecraft.getMinecraft().thePlayer.posZ);
		}catch(NumberFormatException e){
		}
	}

}
