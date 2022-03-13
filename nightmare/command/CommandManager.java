package nightmare.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nightmare.command.impl.Bind;
import nightmare.command.impl.Toggle;
import nightmare.command.impl.Vclip;
import nightmare.event.impl.EventChat;

public class CommandManager {
	
	public List<Command> commands = new ArrayList<Command>();
	public String prefix = ".";
	
	public CommandManager() {
		setup();
	}
	
	public void setup() {
		commands.add(new Bind());
		commands.add(new Toggle());
		commands.add(new Vclip());
	}
	
	public void handleChat(EventChat event) {
		String message = event.getMessage();
		
		if(!message.startsWith(prefix))
				return;
		
		event.setCancelled(true);
		
		message = message.substring(prefix.length());
		
		if(message.split(" ").length > 0) {
			String commandName = message.split(" ")[0];
			
			for(Command c : commands) {
				if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
				}
			}
		}
	}

}
