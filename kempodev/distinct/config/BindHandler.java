package kempodev.distinct.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

import org.lwjgl.input.Keyboard;

public class BindHandler extends FileHandler{
	private File file = new File(base + "/keybinds.txt");
	@Override
	public void loadInfo() {
		if(file.exists()) {
			try {
				BufferedReader r = new BufferedReader(new FileReader(file));
				String var4 = "";

	            while ((var4 = r.readLine()) != null)
	            {
	                String[] var5 = var4.split(":");
	                for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
	                	if(m.getName().equalsIgnoreCase(var5[0]) && m.getBindable()) {
	                		int key = Keyboard.getKeyIndex(var5[1]);
	                		m.setKey(key);
	                	}
	                }
	                
	            }

	            r.close();
	            System.out.println("Keybinds loaded.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}else{
				this.createFile(file);
			}
	}

	@Override
	public void saveInfo() {
		if(file.exists()) {
			try{
				BufferedWriter r = new BufferedWriter(new FileWriter(file));
				for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
					if(m.getBindable()) {
					String s = m.getKey() != -1 ? Keyboard.getKeyName(m.getKey()) : "NONE";
					r.write(m.getName() + ":" + s + space);
					}
				}
				r.close();
				System.out.println("Keybinds saved.");
			}catch(Exception e) {
				e.printStackTrace();
			}
			}else{
				this.createFile(file);
			}
	}

}
