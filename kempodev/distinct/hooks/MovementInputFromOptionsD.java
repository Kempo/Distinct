package kempodev.distinct.hooks;

import kempodev.distinct.main.Distinct;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class MovementInputFromOptionsD extends MovementInput{
	private GameSettings gameSettings;
    

    public MovementInputFromOptionsD(GameSettings par1GameSettings)
    {
        this.gameSettings = par1GameSettings;
    }

    public void updatePlayerMoveState()
    {
    	if(Distinct.getInstance().getModuleByName("Freecam").enabled) {
    		return;
    	}
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (this.gameSettings.keyBindForward.getIsKeyPressed())
        {
            ++this.moveForward;
        }

        if (this.gameSettings.keyBindBack.getIsKeyPressed())
        {
            --this.moveForward;
        }

        if (this.gameSettings.keyBindLeft.getIsKeyPressed())
        {
            ++this.moveStrafe;
        }

        if (this.gameSettings.keyBindRight.getIsKeyPressed())
        {
            --this.moveStrafe;
        }

        this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
