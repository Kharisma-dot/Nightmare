package nightmare.fonts.impl;

import nightmare.Nightmare;
import nightmare.fonts.api.FontFamily;
import nightmare.fonts.api.FontManager;
import nightmare.fonts.api.FontRenderer;
import nightmare.fonts.api.FontType;

public interface Fonts {

	FontManager FONT_MANAGER = Nightmare.instance.fontManager;

	interface REGULAR {

		FontFamily REGULAR = FONT_MANAGER.fontFamily(FontType.REGULAR);

		final class REGULAR_14 { public static final FontRenderer REGULAR_14 = REGULAR.ofSize(14); private REGULAR_14() {} }
		final class REGULAR_15 { public static final FontRenderer REGULAR_15 = REGULAR.ofSize(15); private REGULAR_15() {} }
		final class REGULAR_16 { public static final FontRenderer REGULAR_16 = REGULAR.ofSize(16); private REGULAR_16() {} }
		final class REGULAR_17 { public static final FontRenderer REGULAR_17 = REGULAR.ofSize(17); private REGULAR_17() {} }
		final class REGULAR_18 { public static final FontRenderer REGULAR_18 = REGULAR.ofSize(18); private REGULAR_18() {} }
		final class REGULAR_19 { public static final FontRenderer REGULAR_19 = REGULAR.ofSize(19); private REGULAR_19() {} }
		final class REGULAR_20 { public static final FontRenderer REGULAR_20 = REGULAR.ofSize(20); private REGULAR_20() {} }
		final class REGULAR_21 { public static final FontRenderer REGULAR_21 = REGULAR.ofSize(21); private REGULAR_21() {} }
		final class REGULAR_22 { public static final FontRenderer REGULAR_22 = REGULAR.ofSize(22); private REGULAR_22() {} }
		final class REGULAR_23 { public static final FontRenderer REGULAR_23 = REGULAR.ofSize(23); private REGULAR_23() {} }
		final class REGULAR_24 { public static final FontRenderer REGULAR_24 = REGULAR.ofSize(24); private REGULAR_24() {} }
		final class REGULAR_25 { public static final FontRenderer REGULAR_25 = REGULAR.ofSize(25); private REGULAR_25() {} }
		final class REGULAR_26 { public static final FontRenderer REGULAR_26 = REGULAR.ofSize(26); private REGULAR_26() {} }
		final class REGULAR_27 { public static final FontRenderer REGULAR_27 = REGULAR.ofSize(27); private REGULAR_27() {} }
		final class REGULAR_28 { public static final FontRenderer REGULAR_28 = REGULAR.ofSize(28); private REGULAR_28() {} }
		final class REGULAR_29 { public static final FontRenderer REGULAR_29 = REGULAR.ofSize(29); private REGULAR_29() {} }
		final class REGULAR_30 { public static final FontRenderer REGULAR_30 = REGULAR.ofSize(30); private REGULAR_30() {} }
		final class REGULAR_31 { public static final FontRenderer REGULAR_31 = REGULAR.ofSize(31); private REGULAR_31() {} }
		final class REGULAR_40 { public static final FontRenderer REGULAR_40 = REGULAR.ofSize(40); private REGULAR_40() {} }
		final class REGULAR_50 { public static final FontRenderer REGULAR_50 = REGULAR.ofSize(45); private REGULAR_50() {} }
	}
	
	interface ICON {

		FontFamily ICON = FONT_MANAGER.fontFamily(FontType.ICON);

		final class ICON_16 { public static final FontRenderer ICON_16 = ICON.ofSize(16); private ICON_16() {} }
		final class ICON_20 { public static final FontRenderer ICON_20 = ICON.ofSize(20); private ICON_20() {} }
		final class ICON_24 { public static final FontRenderer ICON_24 = ICON.ofSize(24); private ICON_24() {} }
		final class ICON_35 { public static final FontRenderer ICON_35 = ICON.ofSize(35); private ICON_35() {} }
		final class ICON_40 { public static final FontRenderer ICON_40 = ICON.ofSize(40); private ICON_40() {} }
		final class ICON_45 { public static final FontRenderer ICON_45 = ICON.ofSize(45); private ICON_45() {} }
		final class ICON_50 { public static final FontRenderer ICON_50 = ICON.ofSize(50); private ICON_50() {} }
	}
}
