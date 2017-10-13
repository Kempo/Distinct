package kempodev.distinct.gui.screens;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;


import kempodev.distinct.main.Distinct;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Keyboard;


public class DistinctAltSwitcher extends GuiScreen{
	int showStatusTimer = 0;
    public String failed;
    public static boolean showStatus;
    private GuiScreen m_gParent;
    private GuiTextField textField;
    private GuiTextField passwordField;
    private boolean m_bSet;
    private String error = "Logged in!";
    private String type = "";
    private Distinct client = new Distinct();

    public DistinctAltSwitcher(GuiScreen var1)
    {
        this.m_gParent = var1;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.textField.updateCursorCounter();
        this.passwordField.updateCursorCounter();

        if (this.showStatusTimer > 0 && showStatus)
        {
            --this.showStatusTimer;
        }

        if (this.showStatusTimer <= 0)
        {
            showStatus = false;
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        int var2 = this.height / 4 + 80;
        this.textField = new GuiTextField(this.fontRendererObj, this.width / 2 - 100, var2 - 60, 200, 20);
       // this.textField.setMaxStringLength(50);
        this.passwordField = new GuiTextField(this.fontRendererObj, this.width / 2 - 100, var2 - 24, 200, 20);
        //this.passwordField).setMaxStringLength(50);
        var2 += 34;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var2, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var2 + 24, "Back"));
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.enabled)
        {
            if (var1.id == 0)
            {
                showStatus = true;
                this.showStatusTimer = 120;

                if (this.passwordField.getText().length() > 0)
                {
                    String var2 = this.textField.getText();
                    String var3 = this.passwordField.getText();

                    try
                    {
                        String var4 = "user=" + URLEncoder.encode(var2, "UTF-8") + "&password=" + URLEncoder.encode(var3, "UTF-8") + "&version=" + 13;
                        String var5 = excutePost("https://login.minecraft.net", var4);
                        System.out.println(var5);
                        type = var5;
                        if (var5 == null || !var5.contains(":"))
                        {
                            this.failed = var5;
                            Distinct.getInstance().getMinecraft().session = new Session(var2, "", "");
                            showStatus = true;
                            return;
                        }

                        String[] var6 = var5.split(":");
                        mc.session = new Session(var6[2].trim(), var6[4].trim(),var6[3].trim());
                        System.out.println(var6[2].trim() + "   " + var6[3].trim());
                    }
                    catch (Exception var7)
                    {
                        var7.printStackTrace();
                    }
                }
                else
                {
                    this.mc.session.username = this.textField.getText();
                }
            }

            if (var1.id == 1)
            {
                this.mc.displayGuiScreen(this.m_gParent);
                showStatus = false;
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        this.textField.textboxKeyTyped(var1, var2);
        this.passwordField.textboxKeyTyped(var1, var2);

        if (var1 == 9)
        {
            if (this.textField.isFocused())
            {
                this.textField.setFocused(false);
                this.passwordField.setFocused(true);
            }
            else
            {
                this.textField.setFocused(true);
                this.passwordField.setFocused(false);
            }
        }

        if (var1 == 13)
        {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        super.mouseClicked(var1, var2, var3);
        this.textField.mouseClicked(var1, var2, var3);
        this.passwordField.mouseClicked(var1, var2, var3);
    }

    public String excutePost(String var0, String var1)
    {
        HttpsURLConnection var2 = null;
        DataOutputStream var4 = null;

        try
        {
            URL var3;

            try
            {
                var3 = new URL(var0);
                var2 = (HttpsURLConnection)var3.openConnection();
                var2.setRequestMethod("POST");
                var2.setRequestProperty("Content-Type", "application/form-urlencoded");
                var2.setRequestProperty("Content-Length", Integer.toString(var1.getBytes().length));
                var2.setRequestProperty("Content-Launguage", "en-US");
                var2.setUseCaches(false);
                var2.setDoInput(true);
                var2.setDoOutput(true);
                var2.connect();
                var4 = new DataOutputStream(var2.getOutputStream());
                var4.writeBytes(var1);
                var4.flush();
                var4.close();
                InputStream var5 = var2.getInputStream();
                BufferedReader var6 = new BufferedReader(new InputStreamReader(var5));
                StringBuffer var7 = new StringBuffer();
                String var8;

                while ((var8 = var6.readLine()) != null)
                {
                    var7.append(var8);
                    var7.append('\r');
                }

                var6.close();
                String var9 = var7.toString();
                String var11 = var9;
                return var11;
            }
            catch (Exception var15)
            {
                var15.printStackTrace();
                var3 = null;
                //var4 = var3;
            }
        }
        finally
        {
            if (var2 != null)
            {
                var2.disconnect();
            }
        }
        
        return var4.toString();
    }
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        int var4 = this.height / 4 + 80;
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Account Switcher", this.width / 2, var4 - 92, 16777215);
        this.drawString(this.fontRendererObj, "Username", this.width / 2 - 100, var4 - 72, 10526880);
        this.drawString(this.fontRendererObj, "Password", this.width / 2 - 100, var4 - 36, 10526880);
        this.fontRendererObj.drawString("Logged in as: " + this.mc.session.username, this.width / 2 - 100, var4 + 4, 10526880);
        try{
        this.fontRendererObj.drawString("Status: " + (type.contains("deprecated") ? "\247aPremium" : "\247cOffline"), this.width / 2 - 100, var4 + 14, 10526880);
        }catch(Exception e) {
        	this.fontRendererObj.drawString("Status: \247cOffline", this.width / 2 - 100, var4 + 14, 10526880);
        }
        this.textField.drawTextBox();
        this.passwordField.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
}
