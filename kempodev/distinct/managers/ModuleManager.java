package kempodev.distinct.managers;

import java.util.ArrayList;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.modules.ModuleMacro;
import kempodev.distinct.modules.ModulePotion;
import kempodev.distinct.modules.ModuleScrape;
import kempodev.distinct.modules.ModuleSpawn;
import kempodev.distinct.modules.ModuleVanilla;
import kempodev.distinct.modules.chat.ModuleBind;
import kempodev.distinct.modules.chat.ModuleChat;
import kempodev.distinct.modules.chat.ModuleFriend;
import kempodev.distinct.modules.chat.ModuleHelp;
import kempodev.distinct.modules.combat.ModuleAssist;
import kempodev.distinct.modules.combat.ModuleAura;
import kempodev.distinct.modules.combat.ModuleSoup;
import kempodev.distinct.modules.combat.ModuleTrigger;
import kempodev.distinct.modules.combat.ModuleVelocity;
import kempodev.distinct.modules.player.ModuleAntiAFK;
import kempodev.distinct.modules.player.ModuleAutoFish;
import kempodev.distinct.modules.player.ModuleFastPlace;
import kempodev.distinct.modules.player.ModuleFlight;
import kempodev.distinct.modules.player.ModuleFreecam;
import kempodev.distinct.modules.player.ModuleItemDropper;
import kempodev.distinct.modules.player.ModuleJesus;
import kempodev.distinct.modules.player.ModulePhase;
import kempodev.distinct.modules.player.ModuleSpeedMine;
import kempodev.distinct.modules.player.ModuleSprint;
import kempodev.distinct.modules.player.ModuleStep;
import kempodev.distinct.modules.visual.ModuleArmorHUD;
import kempodev.distinct.modules.visual.ModuleBrightness;
import kempodev.distinct.modules.visual.ModuleChestFinder;
import kempodev.distinct.modules.visual.ModuleItemFinder;
import kempodev.distinct.modules.visual.ModuleList;
import kempodev.distinct.modules.visual.ModuleNameProtect;
import kempodev.distinct.modules.visual.ModuleNoRender;
import kempodev.distinct.modules.visual.ModulePotionEffects;
import kempodev.distinct.modules.visual.ModuleRadar;
import kempodev.distinct.modules.visual.ModuleSearch;
import kempodev.distinct.modules.visual.ModuleTracers;
import kempodev.distinct.modules.visual.ModuleWallhack;

public final class ModuleManager {
	private final ArrayList loadedModules = new ArrayList();
	public void loadModules() {
		addModule(new ModuleBrightness());
		addModule(new ModuleList());
		addModule(new ModuleTracers());
		addModule(new ModuleVelocity());
		addModule(new ModuleFreecam());
		addModule(new ModuleFriend());
		addModule(new ModuleAura());
		addModule(new ModuleSprint());
		addModule(new ModuleWallhack());
		addModule(new ModuleNameProtect());
		addModule(new ModuleBind());
		addModule(new ModuleChestFinder());
		addModule(new ModuleSpawn());
		addModule(new ModuleAssist());
		addModule(new ModuleSoup());
		addModule(new ModulePotionEffects());
		addModule(new ModuleVanilla());
		addModule(new ModuleMacro());
		addModule(new ModuleFlight());
		addModule(new ModuleSpeedMine());
		addModule(new ModuleStep());
		addModule(new ModuleArmorHUD());
		addModule(new ModuleSearch());
		addModule(new ModuleFastPlace());
		addModule(new ModuleItemDropper());
		addModule(new ModulePhase());
		addModule(new ModuleHelp());
		addModule(new ModuleTrigger());
		addModule(new ModuleNoRender());
		addModule(new ModuleChat());
		addModule(new ModuleJesus());
		addModule(new ModuleAntiAFK());
		addModule(new ModulePotion());
		addModule(new ModuleRadar());
		addModule(new ModuleItemFinder());
		addModule(new ModuleScrape());
		for(BaseModule m : getModules()) {
			System.out.println(m.getName());
		}
	}
	public ArrayList<BaseModule> getModules() {
		return loadedModules;
	}
	private void addModule(BaseModule m) {
		getModules().add(m);
	}
}
