import java.util.ArrayList;

import processing.core.*;
public class RectImageButtonVisLayer implements ButtonVisLayer {
	PVector corner;
	float width;
	float height;
	PImage image;
	PImage selectedImage;
	PApplet app;
	PVector colour;
	PFont font = null;
	TextDraw textDraw;
	String text;
	ArrayList<TextLabel> textLabels = new ArrayList<TextLabel>();
	ArrayList<ImageLabel> imageLabels = new ArrayList<ImageLabel>();
	ArrayList<PImage> frames = new ArrayList<PImage>();
	int opacity = 255;
	int currFrame = 0;
	public RectImageButtonVisLayer(PApplet a, PImage im,TextDraw tDraw, PVector cor, PVector col, float w, float h)
	{
		this.app = a;
		this.image = im;
		this.selectedImage = im;
		this.corner = cor;
		this.width = w;
		this.height = h;
		this.colour = col;
		this.textDraw = tDraw;
	
	}
	public RectImageButtonVisLayer(PApplet a, PImage im,PVector cor, PVector col, float w, float h)
	{
		this.app = a;
		this.image = im;
		this.selectedImage = im;
		this.corner = cor;
		this.width = w;
		this.height = h;
		this.colour = col;
	}
	public RectImageButtonVisLayer(PApplet a, PImage im,PImage im2, PVector cor, PVector col, float w, float h)
	{
		this.app = a;
		this.image = im;
		this.corner = cor;
		this.width = w;
		this.height = h;
		this.colour = col;
		this.selectedImage = im2;
	}
	public RectImageButtonVisLayer(PApplet a, PImage im,PImage im2,TextDraw tDraw,  PVector cor, PVector col, float w, float h)
	{
		this.app = a;
		this.image = im;
		this.corner = cor;
		this.width = w;
		this.height = h;
		this.colour = col;
		this.selectedImage = im2;
		this.textDraw = tDraw;
	}
	public void AddFrame(PImage frame)
	{
		frames.add(frame);
	}
	/*public void Draw(boolean selected)
	{
		if(selected)
		{
			this.app.tint(this.colour.x, this.colour.y, this.colour.z);
			this.app.image(selectedImage, corner.x, corner.y);
			this.app.tint(255, 255, 255);
		}
		else
			Draw();
		//Draw();
	}*/
	public void Draw(boolean selected)
	{
		this.app.tint(this.colour.x, this.colour.y, this.colour.z, this.opacity);
		if(selected)
			DrawSelected();
		else
			DrawUnselected();
		this.app.tint(255, 255, 255);
		
	}
	private void DrawSelected()
	{
		this.app.image(selectedImage, corner.x, corner.y);
		if(textDraw != null)
		{
			app.textFont(textDraw.font);       
			app.fill(255, 255, 255, this.opacity);
			app.textAlign(textDraw.alignment);
			
			float tX = corner.x + (float)this.image.width/2;
			float tY = corner.y + (float)this.image.height/2;
			app.text(textDraw.text,tX,tY); 
		}
		for(int i = 0; i < textLabels.size(); i++)
		{
			textLabels.get(i).DrawAnimated((float)opacity/255.0f,this.corner);
		}
		for(int i = 0; i < imageLabels.size(); i++)
		{
			imageLabels.get(i).DrawAnimated((float)opacity/255.0f,this.corner);
		}
		
	}
	private void DrawUnselected()
	{
		this.app.image(image, corner.x, corner.y);
		if(textDraw != null)
		{
			app.textFont(textDraw.font);       
			app.fill(255, 255, 255, this.opacity);
			app.textAlign(textDraw.alignment);
			
			float tX = corner.x + (float)this.image.width/2;
			float tY = corner.y + (float)this.image.height/2 -2;
			app.text(textDraw.text,tX,tY); 
		}
		for(int i = 0; i < textLabels.size(); i++)
		{
			textLabels.get(i).DrawAnimated((float)opacity/255.0f, this.corner);
		}
		for(int i = 0; i < imageLabels.size(); i++)
		{
			imageLabels.get(i).DrawAnimated((float)opacity/255.0f,this.corner);
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
		
		int frame = 0;
		float flips = GlobalSettings.GetInstance().Flips;
		frame = (int)(animationStep*flips*(float)frames.size()) % frames.size();
		if(frames.get(frame) == null)
			return;
		this.app.tint(this.colour.x, this.colour.y, this.colour.z, this.opacity);
		this.app.image(frames.get(frame), corner.x, corner.y);
		if(textDraw != null)
		{
			app.textFont(textDraw.font);       
			app.fill(255, 255, 255, this.opacity);
			app.textAlign(textDraw.alignment);
			
			float tX = corner.x + (float)this.image.width/2;
			float tY = corner.y + (float)this.image.height/2;
			app.text(textDraw.text,tX,tY); 
		}
		for(int i = 0; i < textLabels.size(); i++)
		{
			textLabels.get(i).DrawAnimated((float)opacity/255.0f,this.corner);
		}
		this.app.tint(255,255,255);
	}
	public void DrawNextFrame()
	{
		DrawFrame(currFrame);
		currFrame++;
		currFrame %= frames.size();
	}
	public void AddLabel(TextLabel label)
	{
		textLabels.add(label);
	}
	public void AddImageLabel(ImageLabel label)
	{
		imageLabels.add(label);
	}
	public void Draw()
	{
		Draw(false);
	}
	public void DrawClear()
	{
	}

}
