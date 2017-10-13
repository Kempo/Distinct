package kempodev.distinct.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.modules.combat.ModuleTrigger;
import kempodev.distinct.modules.player.ModuleFreecam;

public class SettingsHandler extends FileHandler{
	private File file = new File(base + "/settings.txt");
	@Override
	public void loadInfo() {
		if(file.exists()) {
			try {
				BufferedReader r = new BufferedReader(new FileReader(file));
				String var4 = "";
				while ((var4 = r.readLine()) != null)
				{
					for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
						String s[] = var4.split("_enabled:");
						String b[] = var4.split("_visible:");
						if(m.getName().equalsIgnoreCase(s[0]) && canBeSet(m)) {
							m.enabled = Boolean.parseBoolean(s[1]);
							//System.out.println("module" + m.getName() + " is " + m.enabled);
						}
						for(Object o : m.fields.keySet()) {
							String name = o.toString();
							Object lol[] = var4.split(":");
							String pop[] = var4.split("_");
							if(pop[1].contains(name) && pop[0].contains(m.getName().toLowerCase())) {
								m.fields.put(name, lol[1]); // Replaces the old value and name
								///System.out.println("Set " + m.getName().toLowerCase() + " variable " + name + " to " + lol[1]);
							}
						}
						if(m.getName().equalsIgnoreCase(b[0])) {
							m.visible = Boolean.parseBoolean(b[1]);
						}
					}

				}

				r.close();
				System.out.println("Loaded module settings.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			this.createFile(file);
		}
	}
	private boolean canBeSet(BaseModule m) {
		return m.getToggleable() || !(m instanceof ModuleFreecam || m instanceof ModuleTrigger);
	}
	private boolean disableOnClose(BaseModule m) {
		return m.disableOnClose;
	}
	@Override
	public void saveInfo() {
		if(file.exists()) {
			try{
				BufferedWriter r = new BufferedWriter(new FileWriter(file));
				for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
					if(disableOnClose(m)) {
						m.enabled = false;
					}
					if(canBeSet(m)) {
						r.write(m.getName().toLowerCase() + "_enabled:" + (m.enabled ? "true" : "false") + space);
						r.write(m.getName().toLowerCase() + "_visible:" + (m.visible ? "true" : "false") + space);
						for(Object o : m.fields.keySet()) {
							String name = o.toString();
							Object value = m.fields.get(name).toString();
							r.write(m.getName().toLowerCase() + "_" + name + ":" + value + space);
						}
					}
				}
				r.close();
				System.out.println("Saved module settings.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else{
			this.createFile(file);
		}
	}
}
