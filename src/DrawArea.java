import processing.core.*;
import processing.opengl.*;
import java.util.*;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
public class DrawArea 
{
	PApplet app;
    ArrayList<Drip> drips = new ArrayList<Drip>();
    boolean wasPressed = false;
    boolean toggleStickerMode = false;
    float newDistance,oldDistance = 0.0f;
    float newposx, newposy;
    
    Minim minim;
    AudioPlayer player;
    
    float oldx, oldy;
    PImage nozzleTex, background, backImage;
    PVector tintColour;
    Button menuButton;
    boolean stickerMode = false;
    PImage sticker;
    int mX;
    int mY;
    float nozzleScale = 1.0f;
    int pressure = 255;
    Random generator = new Random(System.currentTimeMillis());
    int dripProbability;
    int dripSteps = 0;
    int dripStepMax = 10;
    int dripTime; 
    PVector cornerPosition;
    float width, height;
    
    boolean StoreStrokes = false;
    ArrayList<Circle> circles = new ArrayList<Circle>();
    public DrawArea(PApplet a, PVector cPosition, float w, float h)
    {
    	this.app = a;
        backImage = new PImage(this.app.width, this.app.height);
        tintColour = new PVector(255, 255, 255);
        //dripProbability = GlobalSettings.GetInstance().DripProbability;
        dripTime = GlobalSettings.GetInstance().DripTime;
        cornerPosition = cPosition;
        this.width = w;
        this.height = h;
        minim = new Minim(this.app);
        player = minim.loadFile(GlobalSettings.GetInstance().NozzleSound); 
    }
    public void SetSticker(PImage stick)
    {
    	sticker = stick;
    }
    public void ToggleStickerMode()
    {
    	stickerMode = !stickerMode;
    }
    public void SetNozzle(PImage tex)
    {
      nozzleTex = tex;
    }
    public void SetColour(PVector colour)
    {
      tintColour = colour;
    }
    public void SetBackground(PImage back)
    {
    	this.background = back;
    }
    public void ClearStrokes()
    {
    	this.circles.clear();
    }
    public void MousePressed(int mouseX, int mouseY)
    {
    	if(!CheckIntersection(new PVector(mouseX, mouseY)))
    		return;
    	// menuButton.CheckIntersection(new PVector((float)mouseX, (float)mouseY));
    	player.play(100);
    	player.loop();
    	dripSteps = 0;
      newposx = mouseX;
      newposy = mouseY;
      oldx = mouseX;
      oldy = mouseY;
      wasPressed = true;
      Circle tCirc = new Circle(40*nozzleScale, new PVector(newposx, newposy), app, tintColour, pressure);
      AddCircle(tCirc);
      //tCirc.Draw(nozzleTex);
     // circles.add(tCirc);
      //startDrip(tCirc);
     
    }
    public boolean CheckIntersection(PVector point)
	{
    	float rad = (float)40.0f*nozzleScale;
		if (point.x > cornerPosition.x +rad && point.y > cornerPosition.y + rad + 7 && point.x < cornerPosition.x + width - rad && point.y < cornerPosition.y + height - rad -7)
			return true;
		return false;
	}
    public void MouseDragged(int mouseX, int mouseY)
    {
    	
    	if(!CheckIntersection(new PVector(mouseX, mouseY)))
    	{
    		MouseReleased(mouseX, mouseY);
    		return;
    	}
    	else if (wasPressed == false)
    	{
    		MousePressed(mouseX, mouseY);
    	}
    	if(stickerMode || !wasPressed)
    	{
    		//app.image(sticker, mouseX-sticker.width/2, mouseY-sticker.height/2);
    		return;
    	}
    	newposx = mouseX;
    	newposy = mouseY;
    	addCircles();
    	oldx = newposx;
    	oldy = newposy;
    	//app.println(mouseX+", "+mouseY);
    }
    public void MouseReleased(int mouseX, int mouseY)
    {
    	player.pause();
    	wasPressed = false;
    }
    public void Draw()
    {
    	if(stickerMode)
    	{
    		this.app.background(0);
        	this.app.image(backImage, 0, 0, this.app.width, this.app.height);
        	float stickerScale = GlobalSettings.GetInstance().StickerFloatScale;
        	app.image(sticker, app.mouseX-(sticker.width*stickerScale)/2, app.mouseY-(sticker.height*stickerScale)/2, (int)((float)sticker.width*stickerScale), (int)((float)sticker.height*stickerScale));
    	}
    	else
    	{
    		for(int i = 0; i < drips.size(); i++)
    		{
    			drips.get(i).UpdateDrip();
    			drips.get(i).Draw(nozzleTex);
    		}
    		//menuButton.Draw(false);
    	}
    	if(StoreStrokes)
    	{
    		this.app.image(background, cornerPosition.x, cornerPosition.y, width, height);
    		for(int i = 0; i < circles.size(); i++)
    		{
    			circles.get(i).Draw(nozzleTex);
    		}
    	}
    }
    public void AddCircle(Circle circle)
    {
    	if(this.StoreStrokes)
    	{
    		circles.add(circle);
    	}
    	else
    	{
    		circle.Draw(nozzleTex);
    	}
    }
    public void InitialDraw()
    {
    	this.app.background(0);
    	if(this.background != null)
    	{
    		this.app.image(background, cornerPosition.x, cornerPosition.y, width, height);
    	}
    }
    public void PrepareCanvas()
    {
    	this.app.background(0);
    	this.app.image(backImage, 0, 0, this.app.width, this.app.height);
    	
    	
    }
    private PImage getScreen()
    {
    	//menuButton.DrawClear(); //so this doesn't show up on screen
    	PImage ret = new PImage(app.width,app.height);
    	app.loadPixels();
    	for(int i = 0; i < app.pixels.length; i++)
    		ret.pixels[i] = app.pixels[i];
    	return ret;
    }

    public void addCircles()
    {
     float dist = (float)Math.sqrt(Math.pow((double)(newposx-oldx), 2.0) + Math.pow((double)(newposy-oldy), 2.0));
     if(dist < 5.0f)
     {
       //circles.add(new circle(30.0f, new PVector(oldx, oldy)));
       Circle circ = new Circle(40.0f*nozzleScale, new PVector(oldx, oldy), app, tintColour, pressure);
       //circles.add(circ);
       //circ.Draw(nozzleTex);
       dripSteps++;
       if(dripSteps > dripTime)
       {
    	   startDrip(circ);
    	   dripSteps = 0;
       }
       //startDrip(circ);
       
       newposx = oldx;
       newposy = oldy;
       return;
     }
     else
     {
    	 dripSteps = 0;
     }
     float alpha = 0.0f;
     float alphaInc = (10.0f*nozzleScale)/dist;
     float distTable = distanceTable(dist);
     newDistance = distTable;
    
     for(float i = 0.0f; i <= 1.0f; i+=alphaInc)
     {
        float radius = 40.0f;//(1.0f-i)*oldDistance + i*newDistance;
        radius *= nozzleScale;
         float x =  (1.0f-i)*oldx + i*newposx;
         float y = (1.0f-i)*oldy + i * newposy;
        // circles.add(new circle(radius, new PVector(x, y)));
         Circle circ = new Circle(radius, new PVector(x, y), app, tintColour, pressure);
         AddCircle(circ);
         //circ.Draw(nozzleTex);
         //circles.add(circ);
        // startDrip(circ);
        
     }
     oldDistance = newDistance;
     //this.app.println("distance is "+dist);
    }
    private void startDrip(Circle circle)
    {
    	 //int number = generator.nextInt()%dripProbability;
    	 //drips.add(circle);
    	drips.add(new Drip(this.app,circle,generator,false));
         //circle.StartDrip(generator.nextInt(400)+100);
    }
    float distanceTable(float dist)
    {
      float dVal = dist/200.0f;
      if(dVal >= 1.0f)
        dVal = 1.0f;
      return dVal *40.0f + (1.0f-dVal)*60.0f;
    }
    public void SetMenuArguments(MenuState args)
    {
    	this.stickerMode =args.IsStickerMode();
		this.SetSticker(args.GetSticker());
		this.SetColour(args.GetColour());
		this.wasPressed = false;
    	/*if(args.IsStickerMode())
    	{
    		this.stickerMode = true;
    		this.SetSticker(args.GetSticker());
    	}
    	else
    	{
    		this.stickerMode = false;
    		this.SetColour(args.GetColour());
    	}*/
		GlobalSettings instance = GlobalSettings.GetInstance();
    	this.backImage = args.GetBackImage();
    	nozzleScale = args.CapToggle ? instance.NozzleScaleMax : instance.NozzleScaleMin;
    	pressure = args.PressureToggle ? instance.PressureMax: instance.PressureMin;
    	//app.println("nozzle scale value is "+nozzleScale);
    	drips.clear();
    	this.circles.clear();
    	
    }
}
