import processing.core.*;
import processing.opengl.*;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.math.*;
import java.nio.IntBuffer;
import java.text.*;
import java.awt.image.BufferedImage;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.media.opengl.GL;
import org.joda.*;
import org.apache.*;
import java.lang.Object;
import org.apache.commons.codec.binary.*;

import com.postmark.java.*;
import fullscreen.*; 


public class Main extends PApplet {

	float newposx, newposy;
	float oldx, oldy;
	float a;
	float olda;

	float odelx, odely;
	float delx, dely;
	float wid; 
	float texJump = 0.25f;
	PImage testImage;
	float texcox = 0.0f;
	float otexcox = 0.0f;
	float otexcoy = 0.0f;
	float texcoy = 0.0f;
	float maxVel = 100.0f;
	float oldDistance = 30.0f;
	float newDistance = 30.0f;
	int backColor = 0;
	PImage backImage;
	boolean getBackImage = false;
	FullScreen fs;
	Circle Circle;
	PImage circTex;
	PGraphics buffer;
	View currentView;
	DrawScreen drawHandler;
	Menu menu;
	KeyboardMenu keyMenu;
	Branding branding;
	class PBuffer
	{
	  float p0x, p0y;
	  float p1x, p1y;
	  float p2x, p2y;
	}

	boolean wasPressed = false;
	public void setup(){
		GlobalSettings instance = GlobalSettings.GetInstance();
		screenWidth = instance.ScreenWidth;
		screenHeight = instance.ScreenHeight;
		this.screen.height = screenHeight;
		this.screen.width = screenWidth;
		
	  size(screenWidth, screenHeight,OPENGL);
	  
	 
	//  stroke(0);
	  noStroke();
	  fill(100, 100,0);
	//  noFill();
	  smooth();
	  background(backColor);  
	  backImage = new PImage(width, height);
	  this.frame.resize(screenWidth, screenHeight);
	  branding = new Branding(this, instance.TopLeft, instance.TopRight, instance.BottomLeft, instance.BottomRight, instance.Background);
	 // x = oldx = width / 2;
	 // y = oldy = height / 2;
	  //circles.add(new circle(100,new PVector(0,0)));
	  odelx = odely = 0.0f;
	  testImage = loadImage("dots.png");
	  circTex = loadImage("circle3.png");
	  textureMode(NORMALIZED);
	  menu = new Menu(this);
	  menu.SetBranding(branding);
	  drawHandler = new DrawScreen(this);
	  drawHandler.SetNozzle(circTex);
	  drawHandler.SetColour(new PVector(255, 0, 255));
	  drawHandler.SetBranding(branding);
	  drawHandler.SetSticker(loadImage("stickerowl.png"));
	  drawHandler.PrepareCanvas();
	  keyMenu = new KeyboardMenu(this);
	  keyMenu.SetBranding(branding);
	  drawHandler.InitialDraw();
	  drawHandler.HandleClosing();
	  
	  menu.SetBackImage(drawHandler.GetBackImage());
	  currentView =menu;// drawHandler;
	  setupEvents();
	  menu.PrepareCanvas();
	  
	  // enter fullscreen mode
	  //sendTestEmail();
	}
	boolean sketchFullScreen() {
		  return true;
		}
	private void setupEvents()
	{
		drawHandler.addCloseViewEvent(new CloseViewEventListener() {
		    public void closeView(CloseViewEvent evt) {
		    	drawHandler.HandleClosing();
		        drawHandler.PrepareCanvas();
		        menu.SetBackImage(drawHandler.GetBackImage());
		    	currentView = menu;
		    	currentView.PrepareCanvas();
		    }
		});
		menu.addCloseViewEvent(new CloseViewEventListener() {
		    public void closeView(CloseViewEvent evt) {
		    	currentView.HandleClosing();
		    	if(menu.GetMenuState().OpenSendMenu)
		    	{
		    		keyMenu.SetMenuArguments(menu.GetMenuState());
		    		currentView = keyMenu;
		    	}
		    	else
		    	{
		    		drawHandler.SetMenuArguments(menu.GetMenuState());
		    		currentView = drawHandler;
		    	}
		    	currentView.PrepareCanvas();
		    	
		    }
		});
		keyMenu.addCloseViewEvent(new CloseViewEventListener() {
		    public void closeView(CloseViewEvent evt) {
		    	currentView.HandleClosing();
		    	if(keyMenu.OpenMenu)
		    	{
		    		currentView = menu;
		    		//currentView.PrepareCanvas();
		    	}
		    	else
		    	{
		    		drawHandler.SetMenuArguments(keyMenu.GetMenuState());
		    		currentView = drawHandler;
		    	}
		    	
		    	currentView.PrepareCanvas();
		    	menu.GetMenuState().OpenSendMenu = false;
		    }
		});
	}
	private void sendTestEmail()
	{
		List<NameValuePair> headers = new ArrayList<NameValuePair>();

		headers.add(new NameValuePair("HEADER", "test"));

		PostmarkMessage message = new PostmarkMessage("Postmark@Seanzo.ca",
		        "sean.zo.lynch@gmail.com",
		        "Postmark@Seanzo.ca",
		        null,
		        "SUBJECT",
		        "WORDS ARE BIG",
		        false,
		        null,
		        headers);

		PostmarkClient client = new PostmarkClient("21cfcc88-312d-4a46-8414-a46e1de5e1ef");

		try {
		       client.sendMessage(message);
		} catch (PostmarkException pe) {
		       System.out.println("An error has occured : " + pe.getMessage());
		}
	}
	private void sendAttachment()
	{
		List<NameValuePair> headers = new ArrayList<NameValuePair>();

		headers.add(new NameValuePair("HEADER", "test"));

		PostmarkMessage message = new PostmarkMessage("Postmark@Seanzo.ca",
		        "Alex Beim <alexb@tangibleinteraction.com>",
		        "Postmark@Seanzo.ca",
		        null,
		        "Postmark",
		        "This was sent to you by Postmark.",
		        false,
		        null,
		        headers);

		PostmarkClient client = new PostmarkClient("21cfcc88-312d-4a46-8414-a46e1de5e1ef");
		Attachment attachment = new Attachment();
		attachment.setContentType("image/png");
		attachment.setName("test.png");
		// convert file contents to base64
		
		
		try
		{
			File file = new File("test.png");
			byte[] ba = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(ba);
			fis.close(); 
			String valz = new String(Base64.encodeBase64(ba));//.toString();//""+new Base64().encodeBase64(ba);
			String ret0 = "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";
			attachment.setContent(valz);//    encode(ba).toString());
		}
		catch(Exception e)
		{
			
			
		}
	//encodeToString(ba));

		// can handle multiple attachments
		List<Attachment> v = new ArrayList<Attachment>();
		v.add(attachment);
		
		message.setAttachments(v);
		try {
		       client.sendMessage(message);
		} catch (PostmarkException pe) {
		       System.out.println("An error has occured : " + pe.getMessage());
		}
	}
	void update() {
	  if (mousePressed)
	  {
	    if (!wasPressed) 
	    {
	      wasPressed = true;
	     // x = oldx = mouseX;
	     // y = oldy = mouseY;
	    }
	  }
	  else 
	  {
	    wasPressed = false;
	  }
	  
	}
	public void mousePressed()
	{
		int mDX = (this.width-1440)/2;
		int mDY = (this.height-900)/2;
		int mX = (int)(((float)(mouseX-mDX)/1440.0f)*(float)this.width);
		int mY = (int)(((float)(mouseY-mDY)/900.0f)*(float)this.height);
		mX = mouseX;
		mY = mouseY;
		currentView.MousePressed(mX, mY, mouseButton == LEFT);  
	}
	public void mouseDragged() 
	{
		int mDX = (this.width-1440)/2;
		int mDY = (this.height-900)/2;
		int mX = (int)(((float)(mouseX-mDX)/1440.0f)*(float)this.width);
		int mY = (int)(((float)(mouseY-mDY)/900.0f)*(float)this.height);
		mX = mouseX;
		mY = mouseY;
		currentView.MouseDragged(mX, mY);	
	}
	public void mouseReleased()
	{
		int mX = (int)((float)mouseX/1440.0f)*this.width;
		int mY = (int)((float)mouseY/900.0f)*this.height;
		currentView.MouseReleased(mX, mY);
	}
	public void draw() 
	{
		if(mousePressed && mouseButton ==LEFT && currentView == drawHandler)
		{
			currentView.MouseDragged(mouseX, mouseY);
		}
		if(getBackImage)
		{
			getScreen(backImage);
			background(0);
			image(backImage, 0, 0);
			getBackImage = false;
		}
	  //update();
	 // background(backColor);
	//  float tMin = 0.0f;
	//  float tMax = 1.0f;
	//  image(backImage, 0, 0);
	 
	 /* for(int i =0; i < circles.size(); i++)
	  {
	   circles.get(i).Draw(circTex); 
	  }*/
	  currentView.Draw();
	}
	boolean cursorShown = true;
	public void keyPressed()
	{
		if(key == this.TAB)
		{
			cursorShown = !cursorShown;
			if(!cursorShown)
				this.noCursor();
			else
				this.cursor();
		}
	  currentView.KeyPressed(key); 
	}
	
	public void getScreen(PImage ret)
	{
		backImage = new PImage(width,height);
	  loadPixels();
	  for(int i = 0; i < pixels.length; i++)
	    backImage.pixels[i] = pixels[i];
	}


	public static void main(String args[]) 
	{
		    PApplet.main(new String[] { "--present", "Main" });
	}
}
