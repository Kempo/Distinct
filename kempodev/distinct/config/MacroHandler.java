package kempodev.distinct.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import kempodev.distinct.base.BaseMacro;
import kempodev.distinct.main.Distinct;

import org.lwjgl.input.Keyboard;

public class MacroHandler extends FileHandler{
	private File file = new File(base + "/macros.txt");
	@Override
	public void loadInfo() {
		if(file.exists()) {
			try {
				BufferedReader r = new BufferedReader(new FileReader(file));
				String var4 = "";

	            while ((var4 = r.readLine()) != null)
	            {
	                String[] var5 = var4.split(":");
	                new BaseMacro(var5[0],var5[1],Keyboard.getKeyIndex(var5[2]));
	            }

	            r.close();
	            System.out.println("Macros loaded.");
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
				for(BaseMacro m : Distinct.getInstance().getMacroManager().getMacros()) {
					r.write(m.getMacroName() + ":" + m.getMacroCommand() + ":" + Keyboard.getKeyName(m.getMacroKey()) + space);
				}
				r.close();
				System.out.println("Macros saved.");
			}catch(Exception e) {
				e.printStackTrace();
			}
			}else{
				this.createFile(file);
			}
	}
}
