import java.util.ArrayList;

import processing.core.*;
public class EllipseVisLayer implements ButtonVisLayer
{
	PVector colour;
	PVector origin;
	PApplet app;
	float radiusX;
	float radiusY;
	int opacity;
	ArrayList<TextLabel> textLabels = new ArrayList<TextLabel>();
	public EllipseVisLayer(PApplet a, PVector orig, PVector col, float rX, float rY)
	{
		this.app = a;
		this.origin = orig;
		this.colour = col;
		this.radiusX = rX;
		this.radiusY = rY;
		opacity = 255;
	}
	public void Draw()
	{
		this.app.fill(this.colour.x, this.colour.y,this.colour.z, this.opacity);
		this.app.ellipse(origin.x, origin.y, radiusX, radiusY);
	}
	public void Draw(boolean selected)
	{
		this.app.fill(this.colour.x, this.colour.y,this.colour.z, this.opacity);
		this.app.ellipse(origin.x, origin.y, radiusX, radiusY);
		for(int i = 0; i < textLabels.size(); i++)
		{
			textLabels.get(i).Draw(this.origin);
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
	public void DrawFrame(float animationStep)
	{
		
	}
	public void DrawNextFrame()
	{
		
	}
	public void DrawClear()
	{
		this.app.fill(0);
		this.app.ellipse(origin.x, origin.y, radiusX, radiusY);
	}
	public void AddLabel(TextLabel label)
	{
		textLabels.add(label);
	}
}
