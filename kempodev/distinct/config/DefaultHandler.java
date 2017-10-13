package kempodev.distinct.config;

import java.io.File;

import kempodev.distinct.main.Distinct;

public class DefaultHandler extends FileHandler{
	private File file = new File(base + "/distinct.txt");
	
	public DefaultHandler() {
		Distinct.getInstance().lol.add("supdawg");
	}
	
	@Override
	public void loadInfo()
	{
		loadMainFolder();
	}
	private void loadMainFolder() {
		if (!base.exists())
		{
			System.out.println("Creating main directory");
			boolean var2 = base.mkdir();
			String var3 = var2 ? "Config directory created." : "Could not create config directory.";
		}
	}

}
