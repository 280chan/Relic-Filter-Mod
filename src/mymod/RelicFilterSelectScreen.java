package mymod;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RelicFilterSelectScreen extends RelicSelectScreen {
	
	public RelicFilterSelectScreen() {
		super(true, true, true);
		this.setDescription(null, null, null);
	}

	@Override
	protected void addRelics() {
		for (String id : RelicFilterMod.RELICS) {
			this.relics.add(RelicLibrary.getRelic(id));
		}
	}
	
	@Override
	protected void afterSelected() {
		RelicFilterMod.toggle(this.selectedRelic.relicId);
		this.sort();
	}

	@Override
	protected void afterCanceled() {
	}

	@Override
	protected String categoryOf(AbstractRelic r) {
		if (Settings.language == GameLanguage.ZHS) {
			if (RelicFilterMod.config.getBool(r.relicId))
				return "筛除";
			switch(r.tier) {
			case BOSS:
				return "Boss";
			case COMMON:
				return "普通";
			case DEPRECATED:
				return "废弃";
			case RARE:
				return "稀有";
			case SHOP:
				return "商店";
			case SPECIAL:
				return "事件";
			case STARTER:
				return "初始";
			case UNCOMMON:
				return "罕见";
			}
			return "其他";
		}
		if (RelicFilterMod.config.getBool(r.relicId))
			return "Filtered";
		switch(r.tier) {
		case BOSS:
			return "Boss";
		case COMMON:
			return "Common";
		case DEPRECATED:
			return "Deprecated";
		case RARE:
			return "Rare";
		case SHOP:
			return "Shop";
		case SPECIAL:
			return "Event";
		case STARTER:
			return "Starter";
		case UNCOMMON:
			return "Uncommon";
		}
		return "Other";
	}

	@Override
	protected String descriptionOfCategory(String category) {
		if (Settings.language == GameLanguage.ZHS) {
			switch (category) {
			case "Boss":
				return "只在Boss宝箱中出现的遗物。";
			case "普通":
				return "很容易找到的弱小遗物。";
			case "稀有":
				return "极为少见的独特且强大的遗物。";
			case "商店":
				return "只能从商人处购买到的遗物。";
			case "事件":
				return "只能通过事件获得的遗物。";
			case "初始":
				return "角色初始携带的遗物。";
			case "罕见":
				return "比普通遗物更强大也更少见的遗物。";
			case "筛除":
				return "已被移除遗物池的遗物。";
			}
			return "未被分类的，或mod自定义稀有度的特殊遗物。";
		}
		switch (category) {
		case "Boss":
			return "Relics found only within Boss chests.";
		case "Common":
			return "Weak relics that are found commonly.";
		case "Rare":
			return "Unique and powerful relics which are rarely seen.";
		case "Shop":
			return "Relics which can only be purchased from the Merchant.";
		case "Event":
			return "Relics which can only be obtained through events.";
		case "Starter":
			return "Characters can start with these relics.";
		case "Uncommon":
			return "Stronger relics which appear less often than common relics.";
		case "Filtered":
			return "Relics you filtered.";
		}
		return "Relics that are uncategorized, or belong to rarity from mods.";
	}
}