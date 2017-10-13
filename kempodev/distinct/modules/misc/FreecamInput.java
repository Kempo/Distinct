package kempodev.distinct.modules.misc;

import kempodev.distinct.main.Distinct;
import net.minecraft.util.MovementInput;

import org.lwjgl.input.Keyboard;

public class FreecamInput extends MovementInput{
	public void updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (Distinct.getInstance().getMinecraft().inGameHasFocus)
        {
            if (Keyboard.isKeyDown(17))
            {
                ++this.moveForward;
            }

            if (Keyboard.isKeyDown(31))
            {
                --this.moveForward;
            }

            if (Keyboard.isKeyDown(32))
            {
                --this.moveStrafe;
            }

            if (Keyboard.isKeyDown(30))
            {
                ++this.moveStrafe;
            }
        }
    }
}
