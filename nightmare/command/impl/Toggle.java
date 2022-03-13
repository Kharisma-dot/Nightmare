package nightmare.command.impl;

import nightmare.Nightmare;
import nightmare.command.Command;
import nightmare.module.Module;

public class Toggle extends Command{

	public Toggle() {
		super("Toggle", "Toggles a modul by name", "toggle <name>", "t");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String moduleName = args[0];
			
			boolean foundModule = false;
			
			for(Module module : Nightmare.instance.moduleManager.modules) {
				if(module.getName().equalsIgnoreCase(moduleName)) {
					module.toggle();
					foundModule = true;
					break;
				}
			}
		}
	}
}
