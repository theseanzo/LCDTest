import processing.core.*;
import processing.opengl.*;
import java.util.*;
import javax.media.opengl.GL;
public class Branding 
{
	PImage topLeft, topRight, bottomLeft, bottomRight, background;
	PApplet app;
	public Branding(PApplet a, String tLeft, String tRight, String bLeft, String bRight, String back)
	{
		topLeft = null;
		topRight = null;
		bottomLeft = null;
		bottomRight = null;
		background = null;
		app = a;
		if(!tLeft.equals("null"))
		{
			topLeft = app.loadImage(tLeft);
		}
		if(!tRight.equals("null"))
		{
			topRight = app.loadImage(tRight);
		}
		if(!bLeft.equals("null"))
		{
			bottomLeft = app.loadImage(bLeft);
		}
		if(!bRight.equals("null"))
		{
			bottomRight = app.loadImage(bRight);
		}
		if(!back.equals("null"))
		{
			background = app.loadImage(back);
		}
	}
	public void Draw()
	{
		if(topLeft != null)
			app.image(topLeft, 0, 0);
		if(topRight != null)
			app.image(topRight, app.width-topRight.width, 0);
		if(bottomLeft != null)
			app.image(bottomLeft, 0, app.height-bottomLeft.height);
		if(bottomRight != null)
			app.image(bottomRight, app.width-bottomRight.width, app.height-bottomRight.height);
		
	}
	public void DrawBackground()
	{
		if(background!= null)
			app.image(background, 0, 0,app.width, app.height);
	}
}
