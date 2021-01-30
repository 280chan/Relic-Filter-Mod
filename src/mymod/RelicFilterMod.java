package mymod;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.BaseMod;
import basemod.ModLabeledButton;
import basemod.ModPanel;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

/**
 * @author 彼君不触
 * @version 1/29/2021
 * @since 1/22/2021
 */

@SpireInitializer
public class RelicFilterMod implements PostInitializeSubscriber, PostDungeonInitializeSubscriber, EditStringsSubscriber {
	public static final String MOD_ID = "RelicFilterMod";
	public static Properties configDefault = new Properties();
	public static SpireConfig config;
	public static ArrayList<String> RELICS;
	private ModPanel settingsPanel;
	private static final String UIID = "ModPanel";
	private static UIStrings strings;
	private static String[] text;
	
	public static String makeID(String id) {
		return MOD_ID + ":" + id;
	}
	
	public static void loadConfigData() {
		try {
			config = new SpireConfig(MOD_ID, "RelicFilterSaveData", configDefault);
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
	
	private static String lanFix(String type) {
		String lan = "eng";
		switch(Settings.language) {
		case ZHS:
		case ENG:
			lan = Settings.language.toString().toLowerCase();
		default:
			break;
		}
		return type + "_" + lan + ".json";
	}
	
	private static String pathFix(String lan) {
		return "localizations/" + lan;
	}
	
	private static String readString(String type) {
		return Gdx.files.internal(pathFix(lanFix(type))).readString(String.valueOf(StandardCharsets.UTF_8));
	}
	
	@Override
	public void receiveEditStrings() {
		BaseMod.loadCustomStrings(UIStrings.class, readString("ui"));
	}
	
	public static void initialize() {
		BaseMod.subscribe(new RelicFilterMod());
	}

	public static UIStrings getStrings(String id) {
		return CardCrawlGame.languagePack.getUIString(makeID(id));
	}
	
	private static void initModPanelString() {
		strings = getStrings(UIID);
		text = strings.TEXT;
	}
	
	@Override
	public void receivePostInitialize() {
		RelicSelectScreen.initialize();
		RELICS = BaseMod.listAllRelicIDs();
		for (String id : RELICS) {
			configDefault.setProperty(id, "FALSE");
		}
		loadConfigData();
		initModPanelString();
		this.initializeModPanel();
	}
	
	private void initializeModPanel() {
		Texture badgeTexture = new Texture("images/badge.png");
		this.settingsPanel = new ModPanel();
		ModLabeledButton filterRelics = new ModLabeledButton(text[0], 350.0F, 300.0F, this.settingsPanel, (button) -> {
			new RelicFilterSelectScreen().open();
		});
		this.settingsPanel.addUIElement(filterRelics);
		BaseMod.registerModBadge(badgeTexture, MOD_ID, text[1], text[2], this.settingsPanel);
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
