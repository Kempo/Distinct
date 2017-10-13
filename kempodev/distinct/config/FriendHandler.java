package kempodev.distinct.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import kempodev.distinct.main.Distinct;

public class FriendHandler extends FileHandler{
	private File file = new File(base + "/friends.txt");
	@Override
	public void loadInfo() {
		if(file.exists()) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String var4 = "";

            while ((var4 = r.readLine()) != null)
            {
                String[] var5 = var4.split(":");
                Distinct.getInstance().getFriendManager().getFriends().put(var5[0], var5[1]);
                
            }

            r.close();
            System.out.println("Friends loaded.");
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
			for(Object o : Distinct.getInstance().getFriendManager().getFriends().keySet()) {
				String real = o.toString();
				String nick = Distinct.getInstance().getFriendManager().getFriends().get(real).toString();
				r.write(real + ":" + nick + space);
			}
			r.close();
			System.out.println("Friends saved.");
		}catch(Exception e) {
			e.printStackTrace();
		}
		}else{
			this.createFile(file);
		}
	}
}
