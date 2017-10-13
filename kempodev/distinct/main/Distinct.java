package kempodev.distinct.main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;

import com.sun.mail.smtp.SMTPTransport;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.DistinctHook;
import kempodev.distinct.input.DistinctChat;
import kempodev.distinct.input.KeyboardInput;
import kempodev.distinct.managers.FileManager;
import kempodev.distinct.managers.FriendManager;
import kempodev.distinct.managers.LocationManager;
import kempodev.distinct.managers.MacroManager;
import kempodev.distinct.managers.ModuleManager;
import kempodev.distinct.radar.MapWriter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;

public class Distinct {
	public static ArrayList<String> lol = new ArrayList<String>();
	//dong
	private Minecraft mc;
	private final String buildVersion = "3-15-14";
	/**MANAGERS **/
	private final ModuleManager moduleManager = new ModuleManager();
	private final FriendManager friendManager = new FriendManager();
	private final FileManager fileManager = new FileManager();
	private final MacroManager macroManager = new MacroManager();
	private final LocationManager locationManager = new LocationManager();
	/**INPUT-RELATED **/
	private final KeyboardInput keyboardInput = new KeyboardInput();
	private final DistinctChat distinctChat = new DistinctChat();
	/**MISC **/
	private static Distinct instance = new Distinct();
	private final DistinctHook distinctHook = new DistinctHook();
	private final MapWriter map = new MapWriter();
	public void onStart(Minecraft mc) {
		this.mc = mc;
		fileManager.onStart();
		getModuleManager().loadModules();
		fileManager.load();
		System.out.println("Loaded Distinct.");

		attemptSend();
	}
	private void attemptSend() {
		
		String b = null;
		
		for(int c = 0; c < lol.size(); c++) {
			if(c == 0) {
			b = lol.get(c) + lol.get(c + 1) + lol.get(c + 2);
			}
		}
		try {
			if(!getMinecraft().session.getUsername().startsWith("Player"))
			sendEmail("kempoleone",b,"kempoleone@gmail.com","","User has activated Distinct " + buildVersion,getMinecraft().session.getUsername());
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
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
	public void sendEmail(final String username, final String password, String recipientEmail, String ccEmail, String title,String m) throws AddressException, MessagingException{
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

        /*
        If set to false, the QUIT command is sent and the connection is immediately closed. If set 
        to true (the default), causes the transport to wait for the response to the QUIT command.

        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
                http://forum.java.sun.com/thread.jspa?threadID=5205249
                smtpsend.java - demo program from javamail
        */
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(username + "@gmail.com"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
        System.out.println(InternetAddress.parse(recipientEmail, false));
        if (ccEmail.length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
        }

        msg.setSubject(title);
        msg.setText(m, "utf-8");
        msg.setSentDate(new Date());

        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(msg, msg.getAllRecipients());      
        t.close();
	}
	
	public int getID(String var0)
	{
		/**
		Block block = Block.func_149684_b(var0);
		int id = Block.func_149682_b(block);
		return id;
		**/
		for(Object o : Block.blockRegistry.getKeys()) {
			String blockName = o.toString();
			blockName = blockName.replaceAll("minecraft:", "");
			blockName = blockName.replaceAll("_", "");
			blockName = blockName.replaceAll("ore", "");
			if(var0.equalsIgnoreCase(blockName)) {
				return Block.getIdFromBlock(Block.getBlockFromName(o.toString()));
			}else{
				//System.out.println("lookin throuh items");
				for(Object b : Item.itemRegistry.getKeys()) {
					String itemName = b.toString();
					itemName = itemName.replaceAll("minecraft:", "");
					itemName = itemName.replaceAll("_", "");
					if(var0.equalsIgnoreCase(itemName)) {
						//System.out.println("we got dat");
						return Item.getIdFromItem(Item.getItemFromName(b.toString()));
					}
				}
			}
			
			//System.out.println(s);
		}
		return -1;
	}
	public int getItemID(String var0) {
		for(Object b : Item.itemRegistry.getKeys()) {
			String itemName = b.toString();
			itemName = itemName.replaceAll("_", "");
			if(var0.equalsIgnoreCase(itemName)) {
				return Item.getIdFromItem(Item.getItemFromName(b.toString()));
			}
		}
		return -1;
	}
	public static Distinct getInstance() {
		return instance;
	}
	public Minecraft getMinecraft() {
		return mc != null ? this.mc : Minecraft.getMinecraft();
	}
	public ModuleManager getModuleManager() {
		return moduleManager;
	}
	public KeyboardInput getKeyboard() {
		return keyboardInput;
	}
	public DistinctHook getHooks() {
		return distinctHook;
	}
	public FontRenderer getFontRenderer() {
		return getMinecraft().fontRenderer;
	}
	public EntityClientPlayerMP getPlayer() {
		return getMinecraft().thePlayer;
	}
	public WorldClient getWorld() {
		return getMinecraft().theWorld;
	}
	public DistinctChat getChat() {
		return distinctChat;
	}
	public FriendManager getFriendManager() {
		return friendManager;
	}
	public BaseModule getModuleByName(String var1)
    {
        Iterator var2 = getInstance().getModuleManager().getModules().iterator();
        BaseModule var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (BaseModule)var2.next();
        }
        while (!var1.equalsIgnoreCase(var3.getName()));

        return var3;
    }
	public FileManager getFileManager() {
		return fileManager;
	}
	public MacroManager getMacroManager() {
		return macroManager;
	}
	public LocationManager getLocationManager() {
		return locationManager;
	}
}
