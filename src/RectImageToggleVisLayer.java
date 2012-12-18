import java.util.ArrayList;

import processing.core.*;
public class RectImageToggleVisLayer implements ToggleButtonVisLayer
{
	PVector corner;
	float width;
	float height;
	PImage image;
	PApplet app;
	PVector colour;
	int opacity = 255;
	ArrayList<TextLabel> textLabels = new ArrayList<TextLabel>();
	public RectImageToggleVisLayer(PApplet a, PImage im, PVector cor, PVector col, float w, float h)
	{
		this.app = a;
		this.image = im;
		this.corner = cor;
		this.width = w;
		this.height = h;
		this.colour = col;
	}
	public void Draw(boolean selected)
	{
		if(selected)
		{
			this.app.noFill();
			this.app.stroke(0xFFFFFFFF);
			this.app.rect(this.corner.x, this.corner.y, width, height);
			this.app.fill(255);
			this.app.noStroke();
		}
		Draw();
	}
	public void Draw()
	{
		this.app.tint(this.colour.x, this.colour.y, this.colour.z, this.opacity);
		this.app.image(image, corner.x, corner.y);
		this.app.tint(255, 255, 255, opacity);
		for(int i = 0; i < textLabels.size(); i++)
		{
			textLabels.get(i).DrawAnimated((float)opacity/255.0f,this.corner);
		}
		
	}
	public void DrawAnimated(float step)
	{
		opacity = (int)(step*255.0f);
		Draw();
		opacity = 255;
	}
	public void DrawAnimated(float step, boolean selected)
	{
		opacity = (int)(step*255.0f);
		Draw(selected);
		opacity = 255;
	}
	public void AddLabel(TextLabel label)
	{
		textLabels.add(label);
	}
	public void DrawClear()
	{
		
	}
}
