package kempodev.distinct.utilities;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.ChatAllowedCharacters;

public class CFontRenderer {
	private final Random fontRandom = new Random();
	private final Color[] customColorCodes = new Color[256];
    private final int[] colorCode = new int[32];
	private CFont font, boldFont, italicFont, boldItalicFont;
	private String colorcodeIdentifiers = "0123456789abcdefklmnor";
	
	/**
	 * Constructs a new font renderer.
	 * @param fontName
	 * @param fontSize
	 * @param antiAlias
	 * */
	public CFontRenderer(String fontName, int fontSize, boolean antiAlias) {
		setFont(fontName, fontSize, antiAlias);
		customColorCodes['q'] = new Color(0, 90, 163);
		colorcodeIdentifiers = setupColorcodeIdentifier();
		setupMinecraftColorcodes();
	}
	public CFontRenderer(Font c,boolean alias) {
		setFont(c.getFontName(), c.getSize(), alias);
		customColorCodes['q'] = new Color(0, 90, 163);
		colorcodeIdentifiers = setupColorcodeIdentifier();
		setupMinecraftColorcodes();
	}
	public void drawString(String s, double x, double y, int color) {
		drawString(s, x, y, color, false);
	}

	public void drawStringWithShadow(String s, double x, double y, int color) {
		drawString(s, x + 1, y + 1, color, true);
		drawString(s, x, y, color, false);
	}
	
    public void drawCenteredString(String s, int x, int y, int color) {
    	drawStringWithShadow(s, x - getStringWidth(s) / 2, y, color);
    }
	
	private synchronized void drawString(String text, double x, double y, int color, boolean shadow) {
		if(text == null)
			return;
		glPushMatrix();
		glTranslated(x, y, 0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if ((color & -67108864) == 0)
			color |= -16777216;
		
        if (shadow)
        	color = (color & 16579836) >> 2 | color & -16777216;
        	
		final float red = (color >> 16 & 0xff) / 255F;
		final float green = (color >> 8 & 0xff) / 255F;
		final float blue = (color & 0xff) / 255F;
		final float alpha = (color >> 24 & 0xff) / 255F;
		Color c = new Color(red, green, blue, alpha);
		if(text.contains("\247")) {
			String[] parts = text.split("\247");
			
			Color currentColor = c;
			CFont currentFont = font;
			int width = 0;
			boolean randomCase = false, bold = false, 
			italic = false, strikethrough = false, 
			underline = false;
			
			for(int index = 0; index < parts.length; index++) {
				if(parts[index].length() <= 0)
					continue;
				if(index == 0) {
					
					currentFont.drawString(parts[index], width, 0, currentColor, shadow);
					width += currentFont.getStringWidth(parts[index]);
					
				} else {
					String words = parts[index].substring(1);
					char type = parts[index].charAt(0);
					final int colorIndex = colorcodeIdentifiers.indexOf(type);
					if(colorIndex != -1) {
						
						if (colorIndex < 16) { // coloring
							int colorcode = colorCode[colorIndex];
							currentColor = getColor(colorcode, alpha);
							bold = false;
							italic = false;
							randomCase = false;
							underline = false;
							strikethrough = false;
						} else if(colorIndex == 16) { // random case
							randomCase = true;
						} else if(colorIndex == 17) { // bold
							bold = true;
						} else if(colorIndex == 18) { // strikethrough
							strikethrough = true;
						} else if(colorIndex == 19) { // underline
							underline = true;
						} else if(colorIndex == 20) { // italic
							italic = true;
						} else if(colorIndex == 21) { // reset
							bold = false;
							italic = false;
							randomCase = false;
							underline = false;
							strikethrough = false;
							currentColor = c;
						} else  if(colorIndex > 21){ // custom mang
							Color customColor = this.customColorCodes[type];
							currentColor = new Color((float) customColor.getRed() / 255F, (float) customColor.getGreen() / 255F, (float) customColor.getBlue() / 255F, alpha);
						}
					}
					
					if(bold && italic) {
						boldItalicFont.drawString(randomCase ? toRandom(currentFont, words):words, width, 0, currentColor, shadow);
						currentFont = boldItalicFont;
					} else if(bold) {
						boldFont.drawString(randomCase ? toRandom(currentFont, words):words, width, 0, currentColor, shadow);
						currentFont = boldFont;
					} else if(italic) {
						italicFont.drawString(randomCase ? toRandom(currentFont, words):words, width, 0, currentColor, shadow);
						currentFont = italicFont;
					} else {
						 font.drawString(randomCase ? toRandom(currentFont, words):words, width, 0, currentColor, shadow);
						 currentFont = font;
					}
					if(strikethrough) {
						drawLine(width, currentFont.getStringHeight(words) / 2 + 1, width + currentFont.getStringWidth(words), currentFont.getStringHeight(words) / 2 + 1, 1.5f);
					}
					
					if(underline) {
						drawLine(width, currentFont.getStringHeight(words), width + currentFont.getStringWidth(words), currentFont.getStringHeight(words), 1.5f);
					}
					width += currentFont.getStringWidth(words);
				}
			}
		} else
			font.drawString(text, 0, 0, c, shadow);
		
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}
	
	/**
	 * Make dis
	 * */
	private String toRandom(CFont font, String text) {
		String newText = "";
		for(char c : text.toCharArray()) {
			if(ChatAllowedCharacters.allowedCharacters.length > 0) {
				int index = fontRandom.nextInt(ChatAllowedCharacters.allowedCharacters.length);
				if(ChatAllowedCharacters.allowedCharacters != null && index != 0 && ChatAllowedCharacters.allowedCharacters.toString().toCharArray().length > index) {
				newText += ChatAllowedCharacters.allowedCharacters.toString().toCharArray()[index];
				}
			}
		}
		return newText;
	}
	
	
	public int getStringHeight(String text) {
		if(text == null)
			return 0;
		return font.getStringHeight(text);
	}
	
	public synchronized int getStringWidth(String text) {
		if(text == null)
			return 0;
		if(text.contains("\247")) {
			String[] parts = text.split("\247");
			CFont currentFont = font;
			int width = 0;
			boolean randomCase = false, bold = false, 
			italic = false, strikethrough = false, 
			underline = false;

			for(int index = 0; index < parts.length; index++) {
				if(parts[index].length() <= 0)
					continue;
				if(index == 0) {
					width += currentFont.getStringWidth(parts[index]);
				} else {
					String words = parts[index].substring(1);
					char type = parts[index].charAt(0);
					final int colorIndex = colorcodeIdentifiers.indexOf(type);
					if(colorIndex != -1) {
						
						if (colorIndex < 16) { // coloring
							bold = false;
							italic = false;
							randomCase = false;
							underline = false;
							strikethrough = false;
						} else if(colorIndex == 16) { // random case
							randomCase = true;
						} else if(colorIndex == 17) { // bold
							bold = true;
						} else if(colorIndex == 18) { // strikethrough
							strikethrough = true;
						} else if(colorIndex == 19) { // underline
							underline = true;
						} else if(colorIndex == 20) { // italic
							italic = true;
						} else if(colorIndex == 21) { // reset
							bold = false;
							italic = false;
							randomCase = false;
							underline = false;
							strikethrough = false;
						}
						
					}
					if(bold && italic)
						currentFont = boldItalicFont;
					else if(bold)
						currentFont = boldFont;
					else if(italic)
						currentFont = italicFont;
					else
						currentFont = font;

					width += currentFont.getStringWidth(words);
				}
			}
			return width;
		} else
			return font.getStringWidth(text);
	}

	public void setFont(String fontName, int fontSize, boolean antiAlias) {
		synchronized (this) {
			font = new CFont(new Font(fontName, Font.TRUETYPE_FONT, fontSize), antiAlias);
			boldFont = new CFont(new Font(fontName, Font.BOLD, fontSize), antiAlias);
			italicFont = new CFont(new Font(fontName, Font.ITALIC, fontSize), antiAlias);
			boldItalicFont = new CFont(new Font(fontName, Font.BOLD | Font.ITALIC, fontSize), antiAlias);
		}
	}
	
	/**
	 * @return CFont instance.
	 * */
	public CFont getFont() {
		return font;
	}
	
	
	public synchronized List<String> wrapWords(String text, double width) {
		List<String> finalWords = new ArrayList<String>();
		if (getStringWidth(text) > width) {
			String[] words = text.split(" ");
			String currentWord = "";
			int stringCount = 0;
			char lastColorCode = (char) -1;

			for (String word : words) {
				for(int i = 0; i < word.toCharArray().length; i++) {
					char c = word.toCharArray()[i];
					
					if(c == '\247' && i < word.toCharArray().length - 1) {
						lastColorCode = word.toCharArray()[i + 1];
					}
				}
				if (getStringWidth(currentWord + word + " ") < width) {
					currentWord += word + " ";
				} else {
					finalWords.add(currentWord);
					currentWord = (lastColorCode == -1 ? word + " " : "\247" + lastColorCode + word + " ");
					stringCount++;
				}
			}
			if (!currentWord.equals("")) {
				if (getStringWidth(currentWord) < width) {
					finalWords.add((lastColorCode == -1 ? currentWord + " " : "\247" + lastColorCode + currentWord + " "));
					currentWord = "";
					stringCount++;
				} else {
					for (String s : formatString(currentWord, width))
						finalWords.add(s);
				}
			}
		} else
			finalWords.add(text);
		return finalWords;
	}
	
	public synchronized List<String> formatString(String s, double width) {
		List<String> finalWords = new ArrayList<String>();
		String currentWord = "";
		char lastColorCode = (char) -1;
		for (int i = 0; i < s.toCharArray().length; i++) {
			char c = s.toCharArray()[i];
			
			if(c == '\247' && i < s.toCharArray().length - 1) {
				lastColorCode = s.toCharArray()[i + 1];
			}
			
			if (getStringWidth(currentWord + c) < width) {
				currentWord += c;
			} else {
				finalWords.add(currentWord);
				currentWord = (lastColorCode == -1 ? String.valueOf(c):"\247" + lastColorCode + String.valueOf(c));
			}
		}

		if (!currentWord.equals("")) {
			finalWords.add(currentWord);
		}

		return finalWords;
	}
    
	/**
	 * Renders a line.
	 * */
	private void drawLine(double x, double y, double x1, double y1, float width) {
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);
		glBegin(GL_LINES);
		glVertex2d(x, y);
		glVertex2d(x1, y1);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	/**
	 * Sets up the color codes.
	 * */
	private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index)
        {
            int var6 = (index >> 3 & 1) * 85;
            int var7 = (index >> 2 & 1) * 170 + var6;
            int var8 = (index >> 1 & 1) * 170 + var6;
            int var9 = (index >> 0 & 1) * 170 + var6;

            if (index == 6)
            {
                var7 += 85;
            }

            if (index >= 16)
            {
                var7 /= 4;
                var8 /= 4;
                var9 /= 4;
            }

            this.colorCode[index] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
        }
	}
	
	public Color getColor(int colorCode, float alpha) {
		return new Color((colorCode >> 16) / 255F, (colorCode >> 8 & 0xff) / 255F, (colorCode & 0xff) / 255F, alpha);
	}
	
	/**
	 * Sets up the color code identifier.
	 * */
    private String setupColorcodeIdentifier() {
    	String minecraftColorCodes = "0123456789abcdefklmnor";
    	for(int i = 0; i < this.customColorCodes.length; i++) {
    		if(customColorCodes[i] != null)
    			minecraftColorCodes += (char) i;
    	}
    	return minecraftColorCodes;
    }
}
