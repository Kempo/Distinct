package kempodev.distinct.base;

import kempodev.distinct.main.Distinct;

public class BaseMacro {
	private String name;
    private int key;
    private String command;

    public BaseMacro(String var1, String var2, int var3)
    {
        this.name = var1;
        this.command = var2;
        this.key = var3;
        Distinct.getInstance().getMacroManager().getMacros().add(this);
    }

    public int getMacroKey()
    {
        return this.key;
    }

    public String getMacroCommand()
    {
        return this.command;
    }

    public String getMacroName()
    {
        return this.name;
    }

    public void sendCommand()
    {
        Distinct.getInstance().getPlayer().sendChatMessage(this.command);
    }
}
