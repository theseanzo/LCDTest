import processing.core.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import org.apache.commons.codec.binary.Base64;

import com.postmark.java.Attachment;
import com.postmark.java.NameValuePair;
import com.postmark.java.PostmarkClient;
import com.postmark.java.PostmarkException;
import com.postmark.java.PostmarkMessage;

public class Menu implements View
{
    PApplet app;
    public Branding branding;
    ArrayList<Button> buttons = new ArrayList<Button>();
    ArrayList<Button> bigButtons = new ArrayList<Button>();
    ArrayList<ToggleButton> toggles= new ArrayList<ToggleButton>();
    ArrayList<TextLabel> textLabels = new ArrayList<TextLabel>();
    
    Button menuButton;
    protected javax.swing.event.EventListenerList listenerList =
        new javax.swing.event.EventListenerList();
    PVector returnColour;
    Button clickedButton;
    Button bigClickedButton;
    MenuState currentState;
    MenuClosedArguments menuClosed;
    PImage backgroundImage, woodPanel, woodCircleTex;
    PVector woodPanelPosition;
    Circle woodCircle;
    Drip woodDrip;
    RectangleBoundary menuBoundary;
    DrawArea drawArea;
    Random generator;
    
    boolean isOpening = false;
    boolean isClosing = false;
    boolean noDraw = false;
    float animationStep = 0.0f;
    float animationSpeed = 0.02f;
    //float animationStep = 0.0f;
    
    public Menu(PApplet a)
    {
      this.app = a;
      generator = new Random(System.currentTimeMillis());
      this.backgroundImage = this.app.loadImage("backMenuImage.png");
      currentState = new MenuState();
      createClearButton();
      createSaveButton();
      createSendButton();
      createColourButtons();
      createStickerButtons();
      createThicknessToggle();
      createPressureToggle();
      createWoodPanel();
      animationSpeed = GlobalSettings.GetInstance().AnimationSpeed;
      this.menuBoundary = new RectangleBoundary(new PVector((app.width-this.backgroundImage.width)/2, (app.height-this.backgroundImage.height)/2), this.backgroundImage.width, this.backgroundImage.height);
    }

    private void createTextLabels()
    {
    	
    }
    private void createWoodPanel()
    {
    	int bIX = (app.width-this.backgroundImage.width)/2 + 190;
    	int bIY = (app.height-this.backgroundImage.height)/2 + 230;
    	woodPanelPosition = new PVector(bIX, bIY);
    	woodPanel = app.loadImage("woodpanel.png");
    	woodCircleTex = app.loadImage("circle3.png");
    	PFont font = app.createFont("MyriadPro-bold", 20, true);
    	TextLabel label = new TextLabel(this.app, new PVector(bIX+10, bIY+5), new PVector(0, 0, 0), font, app.LEFT, "TEST");
    	textLabels.add(label);
    	createWoodCircle();
    	
    	drawArea = new DrawArea(this.app, woodPanelPosition, woodPanel.width, woodPanel.height);
    	drawArea.SetBackground(woodPanel);
    	drawArea.SetNozzle(app.loadImage("circle3.png"));
    	drawArea.StoreStrokes = true;
    	drawArea.SetMenuArguments(this.currentState);
    }
    private void createWoodCircle()
    {
    	PVector origin = new PVector(woodPanelPosition.x + woodPanel.width/2 - 50, woodPanelPosition.y +woodPanel.height/2 - 10);
    	GlobalSettings instance = GlobalSettings.GetInstance();
    	float nozzleScale = currentState.CapToggle ? instance.NozzleScaleMax : instance.NozzleScaleMin;
    	int pressure = currentState.PressureToggle ? instance.PressureMax: instance.PressureMin;
    	PVector colour = currentState.GetColour();
    	woodCircle = new Circle(50.0f*nozzleScale, origin,this.app, colour, pressure);
    	woodDrip = new Drip(app, woodCircle, generator, true);
    	
    	//woodCircle.StartStaticDrip(generator.nextInt(400)+100);
    	//woodCircle.KeepDrip = true;
    }
    private void createMenuButton()
    {
    	 PVector position = new PVector(1000, 400);
         EllipseVisLayer ellipseVis = new EllipseVisLayer(app, position,new PVector(255, 255, 0), 100, 100);// = new EllipseVisLayer(app, )
         EllipseBoundary ellipseHit = new EllipseBoundary(position, 100, 100) ;
         menuButton = new Button(app, ellipseVis, ellipseHit,new Object());
         menuButton.addButtonPressed(new ButtonPressedEventListener() {
   		    public void  buttonPressed(ButtonPressedEvent evt) {
   		        // MyEvent was fired
   		    	closeView(new CloseViewEvent(new Object()));
   		    }
   		});
         
        buttons.add(menuButton);
    }
    private void createClearButton()
    {
    	
    	PVector closePos = new PVector(900, 600);
    	int closeRad = 100;
    	
    	PImage image = app.loadImage("red button not selected.png");
    	PImage selImage = app.loadImage("red button selected.png");
    	int bIX = (app.width-this.backgroundImage.width)/2 + 150;
    	int bIY = (app.height-this.backgroundImage.height)/2 + this.backgroundImage.height-image.height-30;
    	PVector origin = new PVector(bIX, bIY);
    	 RectangleBoundary bound0 = new RectangleBoundary(origin,image.width, image.height);
		 RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, image,selImage, origin,new PVector(255,255,255), image.width, image.height);
	    
    	Button button = new Button(app, vis0, bound0, new Object());
    	button.addButtonPressed(new ButtonPressedEventListener() {
		    public void  buttonPressed(ButtonPressedEvent evt) {
		        // MyEvent was fired
		    	bigClickedButton = (Button)(evt.getSource());
		    	currentState.Clear(app);
		    }
		});
    	PFont font = app.createFont("MyriadPro-bold", 20, true);
    	TextLabel label = new TextLabel(this.app, new PVector(image.width/2, -5), new PVector(0, 0, 0), font, app.CENTER, "CLEAR");
    	vis0.AddLabel(label);
    	buttons.add(button);
    }
    private void createSaveButton()
    {
    	PVector closePos = new PVector(900, 600);
    	int closeRad = 100;
    	
    	PImage image = app.loadImage("green button not selected.png");
    	PImage selImage = app.loadImage("green button selected.png");
    	int bIX = (app.width-this.backgroundImage.width)/2 + 50;
    	int bIY = (app.height-this.backgroundImage.height)/2 + this.backgroundImage.height-image.height-30;
    	PVector origin = new PVector(bIX, bIY);
    	 RectangleBoundary bound0 = new RectangleBoundary(origin,image.width, image.height);
		 RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, image,selImage, origin,new PVector(255,255,255), image.width, image.height);
	    
    	Button button = new Button(app, vis0, bound0, new Object());
    	button.addButtonPressed(new ButtonPressedEventListener() {
		    public void  buttonPressed(ButtonPressedEvent evt) {
		        // MyEvent was fired
		    	bigClickedButton = (Button)(evt.getSource());
		    	saveImageToFile();
		    }
		});
    	PFont font = app.createFont("MyriadPro-bold", 20, true);
    	TextLabel label = new TextLabel(this.app, new PVector(image.width/2, -5), new PVector(0, 0, 0), font, app.CENTER, "SAVE");
    	vis0.AddLabel(label);
    	buttons.add(button);
    }
    private void createSendButton()
    {
    	PVector closePos = new PVector(900, 600);
    	int closeRad = 100;
    	
    	PImage image = app.loadImage("blue button not selected.png");
    	PImage selImage = app.loadImage("blue button selected.png");
    	int bIX = (app.width-this.backgroundImage.width)/2 + 250;
    	int bIY = (app.height-this.backgroundImage.height)/2 + this.backgroundImage.height-image.height-30;
    	PVector origin = new PVector(bIX, bIY);
    	 RectangleBoundary bound0 = new RectangleBoundary(origin,image.width, image.height);
		 RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, image,selImage, origin,new PVector(255,255,255), image.width, image.height);
	    
    	Button button = new Button(app, vis0, bound0, new Object());
    	button.addButtonPressed(new ButtonPressedEventListener() {
		    public void  buttonPressed(ButtonPressedEvent evt) {
		        // MyEvent was fired
		    	bigClickedButton = (Button)(evt.getSource());
		    	currentState.SetOpenMenuEvent(true);
		    	isClosing = true;
		    	animationStep = 1.0f;
		    	//closeView(new CloseViewEvent(new Object()));
		    }
		});
    	PFont font = app.createFont("MyriadPro-bold", 20, true);
    	TextLabel label = new TextLabel(this.app, new PVector(image.width/2 - 5, -5), new PVector(0, 0, 0), font, app.CENTER, "SEND");
    	ImageLabel imLabel = new ImageLabel(this.app, new PVector(68, -20), app.loadImage("email.png"));
    	vis0.AddImageLabel(imLabel);
    	vis0.AddLabel(label);
    	buttons.add(button);
    }
    
    
    private void createColourButtons()
    {
    	 PImage b0 = app.loadImage("tab static.png");
    	 PImage b1 = app.loadImage("tab selected.png");
    	int rows = 16;
    	int columns = 2;
    	int bIX = (app.width-this.backgroundImage.width)/2; 
    	int bIY = (app.height-this.backgroundImage.height)/2 + 30;
    	float dX = (float)this.backgroundImage.width-(((float)rows+2.0f)*(float)b0.width);
    	dX/=(float)(rows+2);
    	bIX += 2.0f*dX +b0.width;
    	Random random = new Random();
       ArrayList<Button> colourButtons = new ArrayList<Button>();
     //  app.println("<colourswatches>");
       int colourCount = 0;
        for(int i = 0; i < rows; i++)
        {
      	  
      	  for(int j = 0; j < columns; j++)
      	  {
      		  int r = Math.abs(random.nextInt())%255;
      		  int g = Math.abs(random.nextInt())%255;
      		  int b = Math.abs(random.nextInt())%255;
      		//<note id="p502">
      		  //app.println("\t<coulor id=\""+colourCount+"\">"
      			//	  +"\n\t\t<val>"+r+","+g+","+b+"</val>\n\t</coulor>");
      		  PVector colour = GlobalSettings.GetInstance().Colours.get(colourCount);//new PVector(r, g, b);//
      		  PVector p0 = new PVector(bIX+i*(b0.width + dX), bIY+ j*(b0.height+2));
      		  RectangleBoundary bound0 = new RectangleBoundary(p0,b0.width, b0.height);
      		  RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0,b1, p0,colour, b0.width, b0.height);
      		  vis0.AddFrame(app.loadImage("tab static.png"));
      		  vis0.AddFrame(app.loadImage("tab animation.png"));
      		  vis0.AddFrame(app.loadImage("tab selected.png"));
      	      Button button = new Button(app, vis0, bound0,  colour);
      	      button.AnimateOnClick = true;
      	      button.addButtonPressed(new ButtonPressedEventListener() 
      	      {
      			    public void  buttonPressed(ButtonPressedEvent evt) 
      			    {
      			        // MyEvent was fired
      			    	clickedButton = (Button)(evt.getSource());
      			    	PVector colourReturned = (PVector)clickedButton.GetPropertyFromEventReturn();
      			    	currentState.ColourButtonClicked(colourReturned);
      			    	drawArea.SetMenuArguments(currentState);
      			    	
      			    	
      			    }
      			});
      		  buttons.add(button);
      		  colourButtons.add(button);
      		  colourCount++;
      	  }
        }
      //  app.println("</colourswatches>");
        clickedButton =colourButtons.get(0);
        currentState.ColourButtonClicked((PVector)clickedButton.GetPropertyFromEventReturn());
    }
    private void createThicknessToggle()
    {
    	int bIX = (app.width-this.backgroundImage.width)/2+ 90; 
    	int bIY = (app.height-this.backgroundImage.height)/2+ 250; 
    	PImage bImg = app.loadImage("nub.png");
    	PImage sImg = app.loadImage("rail.png");
    	PVector origin = new PVector(bIX, bIY);
    	RectImageButtonVisLayer bV0 = new RectImageButtonVisLayer(app, bImg,new PVector(origin.x, origin.y),new PVector(255,255,255), bImg.width, bImg.height);
    	RectImageButtonVisLayer bV1 = new RectImageButtonVisLayer(app, bImg, new PVector(origin.x+sImg.width-bImg.width, origin.y),new PVector(255,255,255), bImg.width, bImg.height);
    	RectangleBoundary bH0 = new RectangleBoundary(new PVector(origin.x, origin.y),bImg.width, bImg.height);
    	RectangleBoundary bH1 = new RectangleBoundary(new PVector(origin.x+sImg.width-bImg.width, origin.y),bImg.width, bImg.height);
    	Button b0 = new Button(app, bV0, bH0, new Object());
    	Button b1 = new Button(app, bV1, bH1, new Object());
    	RectImageToggleVisLayer tV = new RectImageToggleVisLayer(app, sImg,origin,new PVector(255,255,255), sImg.width, sImg.height);
    	RectangleBoundary boundary = new RectangleBoundary(new PVector(origin.x, origin.y),sImg.width, sImg.height);
    	ToggleButton toggle = new ToggleButton(app, tV,boundary, b0, b1);
    	toggle.addButtonPressed(new ButtonPressedEventListener() {
		public void  buttonPressed(ButtonPressedEvent evt) {
		        // MyEvent was fired
		    	//app.println("toggle toggled");
		    	 //ToggleButton toggleButton = (ToggleButton)(evt.getSource());
		    	 currentState.CapToggle = !currentState.CapToggle;//toggleButton.Toggle;
		    	 drawArea.SetMenuArguments(currentState);
		    	 createWoodCircle();
		    }
		});
    	PFont fontTop = app.createFont("MyriadPro", 20, true);
    	TextLabel labelTop = new TextLabel(this.app, new PVector(sImg.width/2, -5), new PVector(0, 0, 0), fontTop, app.CENTER, "CAP");
    	tV.AddLabel(labelTop);
    	PFont fontSides = app.createFont("MyriadPro", 12, true);
    	TextLabel labelLeft = new TextLabel(this.app, new PVector(-4, sImg.height/2 + 1), new PVector(255, 255, 255), fontSides, app.RIGHT, "fat");
    	tV.AddLabel(labelLeft);
    	TextLabel labelRight = new TextLabel(this.app, new PVector(sImg.width + 4, sImg.height/2 + 1), new PVector(255, 255, 255), fontSides, app.LEFT, "skinny");
    	tV.AddLabel(labelRight);
    	this.toggles.add(toggle);
    }
    private void createPressureToggle()
    {
    	int bIX = (app.width-this.backgroundImage.width)/2+90; 
    	int bIY = (app.height-this.backgroundImage.height)/2+310; 
    	PImage bImg = app.loadImage("nub.png");
    	PImage sImg = app.loadImage("rail.png");
    	PVector origin = new PVector(bIX, bIY);
    	RectImageButtonVisLayer bV0 = new RectImageButtonVisLayer(app, bImg,new PVector(origin.x, origin.y),new PVector(255,255,255), bImg.width, bImg.height);
    	RectImageButtonVisLayer bV1 = new RectImageButtonVisLayer(app, bImg, new PVector(origin.x+sImg.width-bImg.width, origin.y),new PVector(255,255,255), bImg.width, bImg.height);
    	RectangleBoundary bH0 = new RectangleBoundary(new PVector(origin.x, origin.y),bImg.width, bImg.height);
    	RectangleBoundary bH1 = new RectangleBoundary(new PVector(origin.x+sImg.width-bImg.width, origin.y),bImg.width, bImg.height);
    	Button b0 = new Button(app, bV0, bH0, new Object());
    	Button b1 = new Button(app, bV1, bH1, new Object());
    	RectImageToggleVisLayer tV = new RectImageToggleVisLayer(app, sImg,origin,new PVector(255,255,255), sImg.width, sImg.height);
    	RectangleBoundary boundary = new RectangleBoundary(new PVector(origin.x, origin.y),sImg.width, sImg.height);
    	ToggleButton toggle = new ToggleButton(app, tV, boundary, b0, b1);
    	toggle.addButtonPressed(new ButtonPressedEventListener() {
			    public void  buttonPressed(ButtonPressedEvent evt) {
			        // MyEvent was fired
			    	//app.println("toggle toggled");
			    	 //ToggleButton toggleButton = (ToggleButton)(evt.getSource());
			    	 currentState.PressureToggle = !currentState.PressureToggle;//toggleButton.Toggle;
			    	 drawArea.SetMenuArguments(currentState);
			    	 createWoodCircle();
			    }
			});
    	PFont fontTop = app.createFont("MyriadPro", 20, true);
    	TextLabel labelTop = new TextLabel(this.app, new PVector(sImg.width/2, -5), new PVector(0, 0, 0), fontTop, app.CENTER, "PRESSURE");
    	tV.AddLabel(labelTop);
    	PFont fontSides = app.createFont("MyriadPro", 12, true);
    	TextLabel labelLeft = new TextLabel(this.app, new PVector(-4, sImg.height/2 + 1), new PVector(255, 255, 255), fontSides, app.RIGHT, "high");
    	tV.AddLabel(labelLeft);
    	TextLabel labelRight = new TextLabel(this.app, new PVector(sImg.width + 4, sImg.height/2 + 1), new PVector(255, 255, 255), fontSides, app.LEFT, "low");
    	tV.AddLabel(labelRight);
    	this.toggles.add(toggle);
    }
    private void createStickerButtons()
    {
    	int bIX = (app.width-this.backgroundImage.width)/2; 
    	int bIY = (app.height-this.backgroundImage.height)/2; 
    	PImage i0 = app.loadImage("hellosticker.png");
    	PImage i1 = app.loadImage("questionmarksticker.png");
    	PImage i2 = app.loadImage("catsticker.png");
    	PImage i3 = app.loadImage("tonguesticker.png");
    	PImage i4 = app.loadImage("owlsticker.png");
    	PImage i5 = app.loadImage("robotsticker.png");
    	createSticker(i0, new PVector(bIX + 500, bIY + 260));
    	createSticker(i1, new PVector(bIX + 630, bIY +  260));
    	createSticker(i2, new PVector(bIX + 760, bIY +  260));
    	createSticker(i3, new PVector(bIX + 500, bIY + 390));
    	createSticker(i4, new PVector(bIX + 630, bIY +  390));
    	createSticker(i5, new PVector(bIX + 760, bIY +  390));
    	PFont font = app.createFont("MyriadPro-bold", 20, true);
    	TextLabel label = new TextLabel(this.app, new PVector(bIX+500, bIY+235), new PVector(0, 0, 0), font, app.LEFT, "STICKERS");
    	textLabels.add(label);
    }
    private void createSticker(PImage image, PVector origin)
    {
    	 RectangleBoundary bound0 = new RectangleBoundary(origin,image.width, image.height);
 		  RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, image, origin,new PVector(255,255,255), image.width, image.height);
 	      Button button = new Button(app, vis0, bound0,  image);
 	      button.addButtonPressed(new ButtonPressedEventListener() {
 			    public void  buttonPressed(ButtonPressedEvent evt) {
 			        // MyEvent was fired
 			    
 			    
 			    	Button cButton = (Button)(evt.getSource());
 			    	currentState.StickerButtonClicked((PImage)cButton.GetPropertyFromEventReturn());
 			    	closeView(new CloseViewEvent(new Object()));
 			    	
 			    }
 			});
 		  buttons.add(button);
    }
    private void handleAnimation()
    {
    	if(isOpening)
    	{
    		this.animationStep += this.animationSpeed;
    		if(this.animationStep > 1.0f)
    		{
    			isOpening = false;
    			this.animationStep = 0.0f;
    		}	
    	}
    	if(isClosing)
    	{
    		this.animationStep -= this.animationSpeed;
    		if(this.animationStep <= 0.0f)
    		{
    			this.animationStep = 0.0f;
    			this.isClosing = false;
    			this.noDraw = true;
    			closeView(new CloseViewEvent(new Object()));
    		}
    	}
    }
    public void Draw()
    {
    	
    	this.handleAnimation();
    	if(this.isOpening || this.isClosing)
    	{
    		drawAnimated();
    		return;
    	}
    	if(this.noDraw)
    	{
    		this.noDraw = false;
    		return;
    	}
    	this.app.background(0);
    	
    	this.app.image(this.currentState.GetBackImage(), 0, 0);//this.currentState.backImage
    	this.branding.Draw();
    	if(currentState.BackgroundErased)
    		this.branding.DrawBackground();
    	//this.branding.Draw();
    	int backImageX = (app.width-this.backgroundImage.width)/2;
    	int backImageY = (app.height-this.backgroundImage.height)/2;
    	this.app.image(this.backgroundImage, backImageX, backImageY);
    	
      for(int i = 0; i < buttons.size(); i++)
      {
    	  boolean selected = false;
    	  if(buttons.get(i) == clickedButton || buttons.get(i) == bigClickedButton)
    		  selected = true;
    	  buttons.get(i).Draw(selected);
    	  //buttons.get(i).DrawAnimated(0.5f,selected);
      }
      for(int i = 0; i < toggles.size(); i++)
      {
    	  toggles.get(i).Draw();
      }
      for(int i = 0; i < textLabels.size(); i++)
      {
    	  textLabels.get(i).Draw();
      }
      //this.app.image(woodPanel, woodPanelPosition.x, woodPanelPosition.y);
      int drawTimes = 5;
      //this.woodCircle.UpdateDrip();
      /*this.woodDrip.UpdateDrip();
      for(int i = 0; i < drawTimes; i++)
    	  this.woodCircle.Draw(woodCircleTex);
      this.woodDrip.Draw(woodCircleTex);*/
      drawArea.Draw();
     // menuButton.Draw(false);
    }
    private void drawAnimated()
    {
    	this.app.background(0);
    	
    	this.app.tint(255, 255, 255, 255);
    	this.app.image(this.currentState.GetBackImage(), 0, 0);//this.currentState.backImage
    	this.branding.Draw();
    	if(currentState.BackgroundErased)
    		this.branding.DrawBackground();
    	//this.branding.Draw();
    	int backImageX = (app.width-this.backgroundImage.width)/2;
    	int backImageY = (app.height-this.backgroundImage.height)/2;
    	this.app.tint(255, 255, 255, (int)(animationStep*255.0f));
    	this.app.image(this.backgroundImage, backImageX, backImageY);
    	
      for(int i = 0; i < buttons.size(); i++)
      {
    	  boolean selected = false;
    	  
    	  if(buttons.get(i) == clickedButton || buttons.get(i) == bigClickedButton)
    		  selected = true;
    	  buttons.get(i).DrawAnimated(animationStep,selected);
      }
      for(int i = 0; i < toggles.size(); i++)
      {
    	  toggles.get(i).DrawAnimated(animationStep);
      }
      for(int i = 0; i < textLabels.size(); i++)
      {
    	  textLabels.get(i).DrawAnimated(animationStep);
      }
      this.app.tint(255, 255, 255, (int)(animationStep*255.0f));
      this.app.image(woodPanel, woodPanelPosition.x, woodPanelPosition.y);
      this.app.tint(255, 255, 255);
      
      //this.woodCircle.UpdateDrip();
    }
    public void PrepareCanvas()
    {
    	//this.app.background(0);
    	this.isOpening = true;
    	this.noDraw = false;
    	this.animationStep = 0.0f;
    	this.currentState.stickerMode = false;
    	this.currentState.BackgroundErased = false;
    	drawArea.ClearStrokes();
    }
    public void MousePressed(int mouseX, int mouseY, boolean leftButton)
    {
    	if(isClosing || isOpening)
    		return;
    	if(!leftButton)
    	{
    		isClosing = true;
			this.animationStep = 1.0f;
    		return;
    		//closeView(new CloseViewEvent(new Object()));
    	}
    	if(!menuBoundary.CheckIntersection(new PVector((float)mouseX, (float)mouseY)))
    	{
    		isClosing = true;
    		this.animationStep = 1.0f;
    		
    		return;
    		//closeView(new CloseViewEvent(new Object()));
    	}
    	drawArea.MousePressed(mouseX, mouseY);
    	for(int i = 0; i < buttons.size(); i++)
    	{
    		buttons.get(i).CheckIntersection(new PVector(mouseX, mouseY));
    	}
    	for(int i = 0; i < toggles.size(); i++)
    	{
    		toggles.get(i).CheckIntersection(new PVector(mouseX, mouseY));
    	}
    	//menuButton.CheckIntersection(new PVector(mouseX, mouseY));
    }
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
    public void MouseReleased(int mouseX, int mouseY)
    {
    	bigClickedButton = null;
    	drawArea.MouseReleased(mouseX, mouseY);
    }
    public void MouseDragged(int mouseX, int mouseY)
    {
    	drawArea.MouseDragged(mouseX, mouseY);
    }
    public void KeyPressed(char c){}
    public void HandleClosing()
    {
    	bigClickedButton = null;
    	//menuClosed = new MenuClosedArguments(this.currentState.GetColour(),this.currentState.GetSticker());
    }
    public MenuState GetMenuState()
    {
    	return currentState;
    }
    public MenuClosedArguments GetClosingArguments()
    {
    	return menuClosed;
    }
    
    public void SetBackImage(PImage image)
    {
    	currentState.SetBackImage(image);
    }
    
   
    private void saveImageToFile()
    {
    	Date timeStamp = new Date();
    	this.currentState.GetBackImage().save(app.savePath("recordedimages/")+timeStamp.toLocaleString()+".png");
    	//this.currentState.GetBackImage().save(app.savePath("")+timeStamp.getDate()+"|"+timeStamp.getMonth()++"|"+timeStamp.getHours()+"_"+timeStamp.getMinutes()+"_"+timeStamp.getSeconds()+"image.png");
    }
    public void HandleOpening()
    {
    	this.isOpening = true;
    	this.noDraw = false;
    }
    public void SetBranding(Branding brand)
    {
    	this.branding = brand;
    }
    
    
  
}