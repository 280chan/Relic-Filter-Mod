package mymod;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import basemod.BaseMod;
import basemod.ModLabeledButton;
import basemod.ModPanel;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

/**
 * @author 彼君不触
 * @version 1/22/2021
 * @since 1/22/2021
 */

@SpireInitializer
public class RelicFilterMod implements PostInitializeSubscriber, PostDungeonInitializeSubscriber {
	public static final Logger LOGGER = LogManager.getLogger(RelicFilterMod.class.getName());
	public static Properties configDefault = new Properties();
	public static SpireConfig config;
	public static ArrayList<String> RELICS;
	private ModPanel settingsPanel;
	
	public static void loadConfigData() {
		try {
			config = new SpireConfig("RelicFilterMod", "RelicFilterSaveData", configDefault);
			config.load();
		} catch (Exception var1) {
			var1.printStackTrace();
		}
	}
	
	public static void toggle(String id) {
		RelicFilterMod.config.setBool(id, !RelicFilterMod.config.getBool(id));
		try {
			config.save();
		} catch (Exception var1) {
			var1.printStackTrace();
		}
	}
	
	public static void initialize() {
		BaseMod.subscribe(new RelicFilterMod());
	}

	@Override
	public void receivePostInitialize() {
		RelicSelectScreen.initialize();
		RELICS = BaseMod.listAllRelicIDs();
		for (String id : RELICS) {
			configDefault.setProperty(id, "FALSE");
		}
		loadConfigData();
		this.initializeModPanel();
	}
	
	private void initializeModPanel() {
		Texture badgeTexture = new Texture("images/badge.png");
		this.settingsPanel = new ModPanel();
		ModLabeledButton filterRelics;
		if (Settings.language == GameLanguage.ZHS) {
			filterRelics = new ModLabeledButton("筛选遗物", 350.0F, 300.0F, this.settingsPanel, (button) -> {
				new RelicFilterSelectScreen().open();
			});
		} else {
			filterRelics = new ModLabeledButton("Relics Filter", 350.0F, 300.0F, this.settingsPanel, (button) -> {
				new RelicFilterSelectScreen().open();
			});
		}
		this.settingsPanel.addUIElement(filterRelics);
		if (Settings.language == GameLanguage.ZHS) {
			BaseMod.registerModBadge(badgeTexture, "RelicFilterMod", "彼君不触",
					"将任意遗物从遗物池中筛选出去。", this.settingsPanel);
		} else {
			BaseMod.registerModBadge(badgeTexture, "RelicFilterMod", "280chan",
					"Filter out relics you don't like from pool.", this.settingsPanel);
		}
	}

	public static void removeFromPool(String id) {
		AbstractDungeon.commonRelicPool.remove(id);
		AbstractDungeon.uncommonRelicPool.remove(id);
		AbstractDungeon.rareRelicPool.remove(id);
		AbstractDungeon.bossRelicPool.remove(id);
		AbstractDungeon.shopRelicPool.remove(id);
	}
	
	@Override
	public void receivePostDungeonInitialize() {
		for (String id : RELICS) {
			if (RelicFilterMod.config.getBool(id)) {
				removeFromPool(id);
			}
		}
	}
	

}
