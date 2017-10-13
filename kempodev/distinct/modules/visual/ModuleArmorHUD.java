package kempodev.distinct.modules.visual;

import java.util.ArrayList;
import java.util.List;

import kempodev.distinct.base.BaseModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import bspkrs.client.util.ColorThreshold;
import bspkrs.client.util.HUDUtils;

public class ModuleArmorHUD extends BaseModule{
	private final static String        DEFAULT_COLOR_LIST   = "100,f; 80,7; 60,e; 40,6; 25,c; 10,4";
    public static String               alignMode            = "bottomleft";
    public static boolean              enableItemName       = false;
    public static boolean              showItemOverlay      = true;

    public static String               damageColorList      = DEFAULT_COLOR_LIST;
    public static String               damageDisplayType    = "percent";
    public static boolean              showMaxDamage        = false;
    public static boolean              showEquippedItem     = true;
  
    public static int                  xOffset              = 2;
    
    public static int                  yOffset              = 2;
   
    public static int                  yOffsetBottomCenter  = 41;

    public static boolean              applyXOffsetToCenter = false;
    
    public static boolean              applyYOffsetToMiddle = false;
   
    public static boolean              showInChat           = false;
    private boolean                    allowUpdateCheck;
    private static RenderItem          itemRenderer         = new RenderItem();
    protected float                    zLevel               = 0.0F;
    private ScaledResolution           scaledResolution;
    private List<ColorThreshold> colorList;
    
    
	public ModuleArmorHUD() {
		super("ArmorHUD", -1, -1, null);
		this.visible = false;
		this.enabled = true;
		colorList = new ArrayList<ColorThreshold>();
		addHookType("RENDERUI");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onRenderUI() {
		if ((getClient().getMinecraft().getMinecraft().inGameHasFocus || getClient().getMinecraft().currentScreen == null || (getClient().getMinecraft().currentScreen instanceof GuiChat && showInChat)) && !getClient().getMinecraft().gameSettings.showDebugInfo && !getClient().getMinecraft().gameSettings.keyBindPlayerList.pressed)
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            scaledResolution = new ScaledResolution(getClient().getMinecraft().gameSettings, getClient().getMinecraft().getMinecraft().displayWidth, getClient().getMinecraft().displayHeight);
            displayArmorStatus(getClient().getMinecraft().getMinecraft());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
	}
	private int getX(int width)
    {
        if (alignMode.toLowerCase().contains("center"))
            return scaledResolution.getScaledWidth() / 2 - width / 2 + (applyXOffsetToCenter ? xOffset : 0);
        else if (alignMode.toLowerCase().contains("right"))
            return scaledResolution.getScaledWidth() - width - xOffset;
        else
            return xOffset;
    }
    
    private int getY(int rowCount, int height)
    {
        if (alignMode.toLowerCase().contains("middle"))
            return (scaledResolution.getScaledHeight() / 2) - ((rowCount * height) / 2) + (applyYOffsetToMiddle ? yOffset : 0);
        else if (alignMode.equalsIgnoreCase("bottomleft") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffset;
        else if (alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffsetBottomCenter;
        else
            return yOffset;
    }
    
    public static boolean playerHasArmorEquipped(EntityPlayer player)
    {
        return player.inventory.armorItemInSlot(0) != null || player.inventory.armorItemInSlot(1) != null || player.inventory.armorItemInSlot(2) != null || player.inventory.armorItemInSlot(3) != null;
    }
    
    public static int countOfDisplayableItems(EntityPlayer player)
    {
        int i = 0;
        i += canDisplayItem(player.inventory.armorItemInSlot(0)) ? 1 : 0;
        i += canDisplayItem(player.inventory.armorItemInSlot(1)) ? 1 : 0;
        i += canDisplayItem(player.inventory.armorItemInSlot(2)) ? 1 : 0;
        i += canDisplayItem(player.inventory.armorItemInSlot(3)) ? 1 : 0;
        i += showEquippedItem && canDisplayItem(player.getCurrentEquippedItem()) ? 1 : 0;
        return i;
    }
    
    public static boolean canDisplayItem(ItemStack item)
    {
        return item != null;
    }
    
    private void displayArmorStatus(Minecraft mc)
    {
        int lol = 2; // X
        int poop = scaledResolution.getScaledHeight() - 20; // Y
        if (playerHasArmorEquipped(getClient().getMinecraft().thePlayer) || (showEquippedItem && canDisplayItem(getClient().getMinecraft().thePlayer.getCurrentEquippedItem())))
        {
            int yOffset = enableItemName ? 18 : 16;
            
            int yBase = getY(countOfDisplayableItems(getClient().getMinecraft().thePlayer), yOffset);
            
            for (int i = 3; i >= -1; i--)
            {
                ItemStack item = null;
                if (i == -1 && showEquippedItem)
                    item = getClient().getMinecraft().thePlayer.getCurrentEquippedItem();
                else if (i != -1)
                    item = getClient().getMinecraft().thePlayer.inventory.armorInventory[i];
                else
                    item = null;
                
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                
                if (canDisplayItem(item))
                {
                    int xBase = 0;
                    int damage;
                    int maxDamage;
                    String itemDamage = "";
                    
                    xBase = getX(18 + 4 + getClient().getMinecraft().fontRenderer.getStringWidth(HUDUtils.stripCtrl(itemDamage)));
                    
                    String itemName = "";
                    if (enableItemName)
                    {
                        itemName = item.getDisplayName();
                        xBase = getX(18 + 4 + getClient().getMinecraft().fontRenderer.getStringWidth(itemName));
                    }
                   
                    GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT *//* GL_RESCALE_NORMAL_EXT */);
                    RenderHelper.enableStandardItemLighting();
                    RenderHelper.enableGUIStandardItemLighting();
                    itemRenderer.zLevel = 200.0F;
                    
                    	xBase = getX(0);
                        GL11.glPushMatrix();
                        itemRenderer.renderItemIntoGUI(getClient().getMinecraft().fontRenderer, getClient().getMinecraft().getTextureManager(), item, lol, poop);
                        HUDUtils.renderItemOverlayIntoGUI(getClient().getMinecraft().fontRenderer, item, lol, poop);
                        GL11.glPopMatrix();
                   
                    //yBase += yOffset;
                        lol += 18;
                }
                
            }
        }
    }
}
