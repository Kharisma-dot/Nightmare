package nightmare.command.impl;

import org.lwjgl.input.Keyboard;

import nightmare.Nightmare;
import nightmare.command.Command;
import nightmare.module.Module;

public class Bind extends Command{

	public Bind() {
		super("Bind", "Binds a modul by name", "bind <name> <key> | clear", "b");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		if(args.length == 2) {
			String moduleName = args[1];
			
			if(args[0].equalsIgnoreCase("clear")) {
				for(Module module  : Nightmare.instance.moduleManager.modules) {
					if(module.getName().equalsIgnoreCase(moduleName)) {
						module.setKey(Keyboard.KEY_NONE);
					}
				}
			}
		}
		
		if(args.length == 3) {
			String moduleName = args[1];
			String keyName = args[2];
			
			if(args[0].equalsIgnoreCase("add")) {
				for(Module module : Nightmare.instance.moduleManager.modules) {
					if(module.getName().equalsIgnoreCase(moduleName)) {
						module.setKey(Keyboard.getKeyIndex(keyName.toUpperCase()));
						break;
					}
				}
			}
		}
	}
}
