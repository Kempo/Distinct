package kempodev.distinct.config;

import java.io.File;

import kempodev.distinct.main.Distinct;

public class FileHandler {
	String name;
	protected File base = new File(Distinct.getInstance().getMinecraft().mcDataDir + "/distinct");
	protected String space = System.getProperty("line.separator");

    public void loadInfo() {}

    public void saveInfo() {}

    protected void createFile(File var1)
    {
        if (!var1.exists())
        {
            try
            {
            	System.out.println("Created new file with directory of " + var1.getPath());
                var1.createNewFile();
            }
            catch (Exception var3)
            {
                var3.printStackTrace();
            }
        }
    }
    public String getName()
    {
        return this.name;
    }
}
