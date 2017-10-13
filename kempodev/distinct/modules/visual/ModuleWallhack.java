package kempodev.distinct.modules.visual;

import java.util.ArrayList;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

import org.lwjgl.input.Keyboard;

public class ModuleWallhack extends BaseModule{
	private ArrayList<Integer> renderedBlocks = new ArrayList<Integer>();
	public ModuleWallhack() {
		super("Wallhack", Keyboard.KEY_X, 0xE0DD1B, "!xray add (name) | remove (name) | clear");
		renderedBlocks.add(16);
		renderedBlocks.add(56);
		renderedBlocks.add(15);
		renderedBlocks.add(14);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable() {
		Distinct.getInstance().getMinecraft().renderGlobal.loadRenderers();
	}
	@Override
	public void onDisable() {
		Distinct.getInstance().getMinecraft().renderGlobal.loadRenderers();
	}
	@Override
	public boolean onShouldSideBeRendered(int var1)
    {
        return this.containsBlock(var1);
    }

    private boolean containsBlock(int var1)
    {
        return this.renderedBlocks.contains(Integer.valueOf(var1));
    }
    public void onCommand(String var1)
    {
        String[] var2 = var1.split(" ");

        if (var2[0].equalsIgnoreCase(this.setCommand("xray")))
        {
            if (var2[1].equalsIgnoreCase("add"))
            {
            	int b = Integer.parseInt(var2[2]);
                this.renderedBlocks.add(b);
                sendClientMessage("Block " + b + " has been added to list.");
            }

            if (var2[1].equalsIgnoreCase("clear"))
            {
                this.renderedBlocks.clear();
            }
            
            if(var2[1].equalsIgnoreCase("remove")) {
            	int b = Integer.parseInt(var2[2]);
            	if(this.renderedBlocks.contains(b)) {
            	this.renderedBlocks.remove(this.renderedBlocks.indexOf(b));
            	sendClientMessage("Block " + b + " has been removed from the list.");
            	}
            }
        }
    }
}
