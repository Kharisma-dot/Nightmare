package nightmare.fonts.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import nightmare.fonts.api.FontFamily;
import nightmare.fonts.api.FontRenderer;
import nightmare.fonts.api.FontType;

final class SimpleFontFamily extends Int2ObjectAVLTreeMap<FontRenderer> implements FontFamily {

	private final FontType fontType;
	private final java.awt.Font awtFont;

	private SimpleFontFamily(FontType fontType, java.awt.Font awtFont) {
		this.fontType = fontType;
		this.awtFont = awtFont;
	}

	static FontFamily create(FontType fontType, java.awt.Font awtFont) {
		return new SimpleFontFamily(fontType, awtFont);
	}

	@Override
	public FontRenderer ofSize(int size) {
		return computeIfAbsent(size, ignored -> {
			return SimpleFontRenderer.create(awtFont.deriveFont(java.awt.Font.PLAIN, size));
		});
	}

	@Override
	public FontType font() { return fontType; }
}