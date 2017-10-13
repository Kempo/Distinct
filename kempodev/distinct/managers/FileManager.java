package kempodev.distinct.managers;

import java.util.ArrayList;

import kempodev.distinct.config.BindHandler;
import kempodev.distinct.config.DefaultHandler;
import kempodev.distinct.config.FileHandler;
import kempodev.distinct.config.FriendHandler;
import kempodev.distinct.config.MacroHandler;
import kempodev.distinct.config.SettingsHandler;

public class FileManager {
	private ArrayList<FileHandler> handlers = new ArrayList<FileHandler>();
	public void onStart() {
		handlers.add(new DefaultHandler());
		handlers.add(new FriendHandler());
		handlers.add(new BindHandler());
		handlers.add(new MacroHandler());
		handlers.add(new SettingsHandler());
	}
	public void save() {
		for(FileHandler e : handlers) {
			e.saveInfo();
		}
	}
	public void load() {
		for(FileHandler e : handlers) {
			e.loadInfo();
		}
	}
}
