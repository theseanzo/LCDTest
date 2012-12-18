import processing.core.*;

public class TextDraw {
	public PFont font;
	public String text;
	public int alignment;

	public TextDraw(PFont f, String t, int align) {
		this.font = f;
		this.text = t;
		this.alignment = align;
	}
}
