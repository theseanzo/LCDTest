import processing.core.*;
public class ImageLabel 
{
	PVector position, colour;
	PImage image;
	PApplet app;
	public ImageLabel(PApplet a, PVector pos, PImage im)
	{
		this.app = a;
		this.position = pos;
		this.image = im;
		
	}
	
	public void Draw()
	{
		//this.app.image(boxImage, position.x, position.y);
		
		
		float tX = position.x;
		float tY = position.y;
		app.image(image, tX, tY); 
	}
	public void Draw(PVector pos)
	{     
		
		float tX = position.x + pos.x;
		float tY = position.y + pos.y;
		app.image(image, tX, tY);
	}
	public void DrawAnimated(float step)
	{       
		app.tint(255, 255, 255, (int)(step*255.0f));
		
		float tX = position.x;
		float tY = position.y;
		app.image(image, tX, tY); 
	}
	
	public void DrawAnimated(float step,PVector pos)
	{
		app.tint(255, 255, 255, (int)(step*255.0f));
		
		float tX = position.x + pos.x;
		float tY = position.y + pos.y;
		app.image(image, tX, tY); 
	}
	
}
