import processing.core.*;
import processing.opengl.*;
public class MenuState 
{
/***
 * things needed to return:
 * colour (if colour chosen)
 * sticker (if sticker chosen)
 * nozzle pressure
 * cap size
 */
	public boolean stickerMode = false;
	public PImage sticker;
	public PVector colour;
	public 	int nozzleSize;
	public boolean clear = false;
	public int capSize;
	private PImage backImage;
	public boolean OpenSendMenu;
	public Boolean PressureToggle = true;
	public Boolean CapToggle = true;
	public Boolean BackgroundErased = true;
	public MenuState()
	{
		this.backImage = new PImage(GlobalSettings.GetInstance().ScreenWidth,GlobalSettings.GetInstance().ScreenHeight);
	}
	public void SetBackImage(PImage image)
	{
		this.backImage = image;
	}
	public void Clear(PApplet app)
	{
		this.backImage = new PImage(app.width,app.height);
		BackgroundErased = true;
	}
	public void ColourButtonClicked(PVector col)
	{
		sticker = null;
		stickerMode = false;
		colour = col;
	}
	public void StickerButtonClicked(PImage stick)
	{
		sticker = stick;
		stickerMode = true;
	}
	public PVector GetColour()
	{
		return this.colour;
	}
	public PImage GetSticker()
	{
		return this.sticker;
	}
	public PImage GetBackImage()
	{
		return this.backImage;
	}
	public boolean IsStickerMode()
	{
		return this.stickerMode;
	}
	public void SetOpenMenuEvent(boolean setter)
	{
		this.OpenSendMenu = setter;
	}
	
}
