package nightmare.fonts.api;

public enum FontType {

	REGULAR("regular.ttf"),
	ICON("icon.ttf");

	private final String fileName;

	FontType(String fileName) {
		this.fileName = fileName;
	}

	public String fileName() { return fileName; }
}
