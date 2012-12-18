import processing.core.*;
import processing.opengl.*;
public class MenuClosedArguments 
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
	public int capSize;
	public boolean OpenSendMenu = false;
	public MenuClosedArguments(PVector col, PImage stick)
	{
		if(stick != null)
		{
			stickerMode = true;
			sticker = stick;
		}
		if(col != null)
		{
			stickerMode = false;
			colour = col;
		}
	}
	public void SetColour(PVector col)
	{
		this.colour = col;
		stickerMode = false;
	}
	public void SetSticker(PImage stick)
	{
		this.sticker = stick;
		stickerMode = true;
	}
	public boolean IsStickerMode()
	{
		return stickerMode;
	}
	public PVector GetColour()
	{
		return this.colour;
	}
	public PImage GetSticker()
	{
		return this.sticker;
	}
	public void SetOpenMenuEvent(boolean setter)
	{
		this.OpenSendMenu = setter;
	}
}
