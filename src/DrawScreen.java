import processing.core.*;

import java.util.*;
import ddf.minim.*;
public class DrawScreen implements View 
{
    public PImage backImage;
    public Branding branding;
    PApplet app;
    ArrayList<Drip> drips = new ArrayList<Drip>();
    boolean wasPressed = false;
    boolean toggleStickerMode = false;
    float newDistance,oldDistance = 0.0f;
    float newposx, newposy;
    
    Minim minim;
    AudioPlayer player;
    
    float oldx, oldy;
    PImage nozzleTex;
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
    public DrawScreen(PApplet a)
    {
      this.app = a;
      backImage = new PImage(this.app.width, this.app.height);
      tintColour = new PVector(255, 255, 255);
      //dripProbability = GlobalSettings.GetInstance().DripProbability;
      dripTime = GlobalSettings.GetInstance().DripTime;
      //EllipseVisLayer ellipseVis = new EllipseVisLayer(app, new PVector(1400, 800),new PVector(255, 255, 0), 100, 100);// = new EllipseVisLayer(app, )
      //EllipseBoundary ellipseHit = new EllipseBoundary(new PVector(1400, 800), 100, 100) ;
     /* menuButton = new Button(app, ellipseVis, ellipseHit,new Object());
      menuButton.addButtonPressed(new ButtonPressedEventListener() {
		    public void  buttonPressed(ButtonPressedEvent evt) {
		        // MyEvent was fired
		    	closeView(new CloseViewEvent(new Object()));
		    }
		});*/
      minim = new Minim(this.app);
      player = minim.loadFile(GlobalSettings.GetInstance().NozzleSound); 
      
    }
    
    protected javax.swing.event.EventListenerList listenerList =
        new javax.swing.event.EventListenerList();

    // This methods allows classes to register for MyEvents
    public void addCloseViewEvent(CloseViewEventListener listener) {
        listenerList.add(CloseViewEventListener.class, listener);
    }
 // This private class is used to fire MyEvents
    void closeView(CloseViewEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
        	if (listeners[i]==CloseViewEventListener.class) {
                ((CloseViewEventListener)listeners[i+1]).closeView(evt);
            }
        }
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
      
    public void MousePressed(int mouseX, int mouseY, boolean leftButton)
    {
    	
    	if(!leftButton)
    	{
    		closeView(new CloseViewEvent(new Object()));
    		return;
    	}
    	// menuButton.CheckIntersection(new PVector((float)mouseX, (float)mouseY));
    	if(stickerMode)
    	{
    		
    		this.app.background(0);
        	this.app.image(backImage, 0, 0, this.app.width, this.app.height);
        	float stickerScale = GlobalSettings.GetInstance().StickerPlaceScale;
        	app.image(sticker, app.mouseX-(sticker.width*stickerScale)/2, app.mouseY-(sticker.height*stickerScale)/2, (int)((float)sticker.width*stickerScale), (int)((float)sticker.height*stickerScale));
    		//app.image(sticker, mouseX-sticker.width/2, mouseY-sticker.height/2);
    		this.stickerMode = false;
    		return;
    	}
    	player.play(100);
    	player.loop();
    	dripSteps = 0;
      newposx = mouseX;
      newposy = mouseY;
      oldx = mouseX;
      oldy = mouseY;
      wasPressed = true;
      Circle tCirc = new Circle(50*nozzleScale, new PVector(newposx, newposy), app, tintColour, pressure);
      tCirc.Draw(nozzleTex);
     // circles.add(tCirc);
      //startDrip(tCirc);
     
    }
    public void MouseDragged(int mouseX, int mouseY)
    {
    	
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
    public void KeyPressed(char key)
    {
    	if(key == 'm' )
    	{
    		//app.println("what are we triggering?");
    		closeView(new CloseViewEvent(new Object()));	
    	}
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
    	app.println("drawing we are drawing all the time");
    	this.branding.Draw();
    }
    public void InitialDraw()
    {
    	this.app.background(0);
    	this.branding.Draw();
    	this.branding.DrawBackground();
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
     float alphaInc = 10.0f/dist;
     float distTable = distanceTable(dist);
     newDistance = distTable;
    
     for(float i = 0.0f; i <= 1.0f; i+=alphaInc)
     {
        float radius = (1.0f-i)*oldDistance + i*newDistance;
        radius *= nozzleScale;
         float x =  (1.0f-i)*oldx + i*newposx;
         float y = (1.0f-i)*oldy + i * newposy;
        // circles.add(new circle(radius, new PVector(x, y)));
         Circle circ = new Circle(radius, new PVector(x, y), app, tintColour, pressure);
         circ.Draw(nozzleTex);
         //circles.add(circ);
        // startDrip(circ);
        
     }
     oldDistance = newDistance;
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
    
    public void HandleClosing()
    {
    	if(stickerMode)
    	{
    		this.app.background(0);
        	this.app.image(backImage, 0, 0, this.app.width, this.app.height);
    	}
    	this.drips.clear();
    	backImage = getScreen();
    	player.pause();
    	
    }
    public void HandleOpening()
    {
    	
    }
    public void SetMenuArguments(MenuState args)
    {
    	this.stickerMode =args.IsStickerMode();
		this.SetSticker(args.GetSticker());
		this.SetColour(args.GetColour());
		this.wasPressed = false;

		GlobalSettings instance = GlobalSettings.GetInstance();
    	
    	nozzleScale = args.CapToggle ? instance.NozzleScaleMax : instance.NozzleScaleMin;
    	pressure = args.PressureToggle ? instance.PressureMax: instance.PressureMin;
    	//app.println("nozzle scale value is "+nozzleScale);
    	drips.clear();
    	if(args.BackgroundErased)
    	{
    		this.backImage = args.GetBackImage();
    		this.InitialDraw();
    		HandleClosing();
    	}
    	
    	
    }
    public PImage GetBackImage()
    {
    	return backImage;
    }
    public void SetBranding(Branding brand)
    {
    	this.branding = brand;
    }
}