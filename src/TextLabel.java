import processing.core.*;
public class TextLabel 
{
	PVector position, colour;
	PFont font;
	int alignment;
	String text;
	PApplet app;
	public TextLabel(PApplet a, PVector pos, PVector col, PFont f, int align, String tex)
	{
		this.app = a;
		this.position = pos;
		this.colour = col;
		this.text = tex;
		this.alignment = align;
		this.font = f;
	}
	
	public void Draw()
	{
		//this.app.image(boxImage, position.x, position.y);
		app.textFont(font);       
		app.fill(colour.x, colour.y, colour.z, 255);
		app.textAlign(alignment);
		
		float tX = position.x;
		float tY = position.y;
		app.text(text,tX, tY); 
	}
	public void Draw(PVector pos)
	{
		app.textFont(font);       
		app.fill(colour.x, colour.y, colour.z, 255);
		app.textAlign(alignment);
		
		float tX = position.x + pos.x;
		float tY = position.y + pos.y;
		app.text(text,tX, tY); 
	}
	public void DrawAnimated(float step)
	{
		app.textFont(font);       
		app.fill(colour.x, colour.y, colour.z, (int)(step*255.0f));
		app.textAlign(alignment);
		
		float tX = position.x;
		float tY = position.y;
		app.text(text,tX, tY); 
	}
	
	public void DrawAnimated(float step,PVector pos)
	{
		app.textFont(font);       
		app.fill(colour.x, colour.y, colour.z, (int)(step*255.0f));
		app.textAlign(alignment);
		
		float tX = position.x + pos.x;
		float tY = position.y + pos.y;
		app.text(text,tX, tY); 
	}
	
}
