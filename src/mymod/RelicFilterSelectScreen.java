package mymod;

import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RelicFilterSelectScreen extends RelicSelectScreen {
	private static final String UIID = "RelicFilterSelectScreen";
	private static final UIStrings strings = RelicFilterMod.getStrings(UIID);
	private static final String[] TEXT = strings.TEXT;
	
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
		if (RelicFilterMod.config.getBool(r.relicId))
			return TEXT[0];
		switch(r.tier) {
		case BOSS:
			return TEXT[1];
		case COMMON:
			return TEXT[2];
		case DEPRECATED:
			return TEXT[3];
		case RARE:
			return TEXT[4];
		case SHOP:
			return TEXT[5];
		case SPECIAL:
			return TEXT[6];
		case STARTER:
			return TEXT[7];
		case UNCOMMON:
			return TEXT[8];
		}
		return TEXT[9];
	}

	@Override
	protected String descriptionOfCategory(String category) {
		int index = 0;
		for (int i = 0; i < 10; i++) {
			if (category.equals(TEXT[i])) {
				index = i;
				break;
			}
		}
		switch (index) {
		case 1:
			return TEXT[10];
		case 2:
			return TEXT[11];
		case 4:
			return TEXT[12];
		case 5:
			return TEXT[13];
		case 6:
			return TEXT[14];
		case 7:
			return TEXT[15];
		case 8:
			return TEXT[16];
		case 0:
			return TEXT[17];
		case 3:
		default:
			return TEXT[18];
		}
	}
}