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

public class KeyboardMenu implements View {
	PApplet app;
	 public Branding branding;
	ArrayList<Button> buttons = new ArrayList<Button>();
	ArrayList<ToggleButton> toggles = new ArrayList<ToggleButton>();
	Button menuButton;
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	private String email = "";
	PImage backgroundImage;
	PImage drawnImage;
	TextEntry textEntry;
	boolean sendingEmail = false;
	boolean sendEmailToggle = false;
	float squareKeyShift = 10.0f;
	float circleKeyShift = 20.0f;
	int keyboardShiftVertical = 240;
	int textDisplayMode = 0;
	final int textShowEmail = 0;
	final int textShowError = 1;
	final int textShowSendingEmail = 2;
	final int textShowEmailSent = 3;
	final int textShowNotAgreed = 4;
	private String sendingEmailText = "Sending e-mail...";
	private String errorEmailText = "Error processing e-mail";
	private String sentEmailText = "E-mail successfully sent!";
	private String notAgreedText = "Please agree first.";
	boolean OpenMenu = true;
	Button clickedButton;
	boolean Agree = true;
	private RectangleBoundary menuBoundary;
	PFont warningFont;
	MenuState currentState;
	String warningString = "This is a test of the emergency broadcast system";
	
	 boolean isOpening = false;
	 boolean isClosing = false;
	 boolean noDraw = false;
	 float animationStep = 0.0f;
	 float animationSpeed = 0.02f;
	boolean isWaitText = false;
	int waitStep = 0;
	int waitMax = 100;
	public KeyboardMenu(PApplet a) {
		this.app = a;
		this.backgroundImage = this.app.loadImage("backMenuImage.png");
		createNumbersRow();
		createQToQuoteRow();
		createAToSingleQuoteRow();
		createZToEnterRow();
		createSpecialKeysRow();
		createOkButton();
		createNoButton();
		createTextEntry();
		createAgreementToggle();
		
		warningFont = app.createFont("Helvetica",12);
		this.warningString = GlobalSettings.GetInstance().ApprovalMessage;
		this.menuBoundary = new RectangleBoundary(new PVector(
				(app.width - this.backgroundImage.width) / 2,
				(app.height - this.backgroundImage.height) / 2),
				this.backgroundImage.width, this.backgroundImage.height);
		
		animationSpeed = GlobalSettings.GetInstance().AnimationSpeed;

	}

	// This methods allows classes to register for MyEvents
	public void addCloseViewEvent(CloseViewEventListener listener) {
		listenerList.add(CloseViewEventListener.class, listener);
	}

	/**
	 * Button creation overrides
	 */
	private void createTextEntry() {
		PImage boxImage = app.loadImage("textenter2.png");
		int bIX = (app.width - this.backgroundImage.width) / 2
				+ this.backgroundImage.width / 2 - boxImage.width / 2;
		int bIY = (app.height - this.backgroundImage.height) / 2 + 80;
		PVector position = new PVector(bIX, bIY);
		PFont font = app.createFont("Alterebro Pixel Font", 32, true);
		TextDraw textDraw = new TextDraw(font, "", app.LEFT);
		this.textEntry = new TextEntry(this.app, boxImage, position, textDraw);

	}

	private void createOkButton() {
		PImage b0 = app.loadImage("ok button up.png");
		PImage b1 = app.loadImage("ok button down.png");
		int bIX = (app.width - this.backgroundImage.width) / 2 + 675;
		int bIY = (app.height - this.backgroundImage.height) / 2 + 85;
		PFont font = app.createFont("Helvetica", 20, true);
		TextDraw textDraw = new TextDraw(font, "", app.CENTER);
		PVector p0 = new PVector(bIX, bIY);
		RectangleBoundary bound0 = new RectangleBoundary(p0, b0.width,
				b0.height);
		RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0, b1,
				textDraw, p0, new PVector(255, 255, 255), b0.width, b0.height);
		Button button = new Button(app, vis0, bound0, "");
		button.addButtonPressed(new ButtonPressedEventListener() {
			public void buttonPressed(ButtonPressedEvent evt) {
				sendingEmail = true;
				//clickedButt
				// MyEvent was fired
				 //clickedButton = (Button)(evt.getSource());
				// currentState.ColourButtonClicked((PVector)clickedButton.GetPropertyFromEventReturn());
			}
		});
		buttons.add(button);

	}

	private void createNoButton() {
		PImage b0 = app.loadImage("no button up.png");
		PImage b1 = app.loadImage("no button down.png");
		int bIX = (app.width - this.backgroundImage.width) / 2 + 720;
		int bIY = (app.height - this.backgroundImage.height) / 2 + 85;
		PFont font = app.createFont("Helvetica", 20, true);
		TextDraw textDraw = new TextDraw(font, "", app.CENTER);
		PVector p0 = new PVector(bIX, bIY);
		RectangleBoundary bound0 = new RectangleBoundary(p0, b0.width,
				b0.height);
		RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0, b1,
				textDraw, p0, new PVector(255, 255, 255), b0.width, b0.height);
		Button button = new Button(app, vis0, bound0, "");
		button.addButtonPressed(new ButtonPressedEventListener() {
			public void buttonPressed(ButtonPressedEvent evt) {
				textEntry.ClearText();
				// MyEvent was fired
				// clickedButton = (Button)(evt.getSource());

				// currentState.ColourButtonClicked((PVector)clickedButton.GetPropertyFromEventReturn());
			}
		});
		buttons.add(button);
	}

	private void createNumbersRow() {
		PImage b0 = app.loadImage("key up.png");
		PImage b1 = app.loadImage("key down.png");
		int rows = 12;
		int bIX = (app.width - this.backgroundImage.width) / 2;
		int bIY = (app.height - this.backgroundImage.height) / 2
				+ keyboardShiftVertical;
		float dX = squareKeyShift;
		bIX += this.backgroundImage.width / 2 - (float) rows * (b0.width + dX)
				/ 2.0f;
		PVector colour = new PVector(255, 255, 255);
		Random random = new Random();
		PFont font = app.createFont("Helvetica", 16, true);
		// int dX = 20;
		String letters[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
				"@", "del" };
		for (int i = 0; i < rows; i++) {
			TextDraw textDraw = new TextDraw(font, letters[i], app.CENTER);
			PVector p0 = new PVector(bIX + i * (b0.width + dX), bIY);
			RectangleBoundary bound0 = new RectangleBoundary(p0, b0.width,
					b0.height);
			RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0,
					b1, textDraw, p0, colour, b0.width, b0.height);
			Button button = new Button(app, vis0, bound0, letters[i]);
			button.addButtonPressed(new ButtonPressedEventListener() {
				public void buttonPressed(ButtonPressedEvent evt) {
					// MyEvent was fired
					Button clicked = (Button) (evt.getSource());
					String text = (String) clicked.GetPropertyFromEventReturn();
					if (text.equals("del"))
						textEntry.DeleteCharacter();
					else
						textEntry.AddCharacter(text);
					textDisplayMode = textShowEmail;
					clickedButton = (Button)(evt.getSource());
					// currentState.ColourButtonClicked((PVector)clickedButton.GetPropertyFromEventReturn());
				}
			});
			buttons.add(button);
		}

	}

	private void createQToQuoteRow() {
		PImage b0 = app.loadImage("key up.png");
		PImage b1 = app.loadImage("key down.png");
		int rows = 11;
		int bIX = (app.width - this.backgroundImage.width) / 2;
		int bIY = (app.height - this.backgroundImage.height) / 2
				+ keyboardShiftVertical + 60;
		// float dX =
		// (float)this.backgroundImage.width-(((float)rows+4.0f)*(float)b0.width);
		// dX/=(float)(rows+2);
		float dX = squareKeyShift;
		bIX += this.backgroundImage.width / 2 - (float) rows * (b0.width + dX)
				/ 2.0f;/* 4.0f*dX +b0.width */
		;
		PVector colour = new PVector(255, 255, 255);
		PFont font = app.createFont("Helvetica", 16, true);
		// int dX = 20;
		String letters[] = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
				"\"" };
		for (int i = 0; i < rows; i++) {
			TextDraw textDraw = new TextDraw(font, letters[i], app.CENTER);
			PVector p0 = new PVector(bIX + i * (b0.width + dX), bIY);
			RectangleBoundary bound0 = new RectangleBoundary(p0, b0.width,
					b0.height);
			RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0,
					b1, textDraw, p0, colour, b0.width, b0.height);
			Button button = new Button(app, vis0, bound0, letters[i]);
			button.addButtonPressed(new ButtonPressedEventListener() {
				public void buttonPressed(ButtonPressedEvent evt) {
					Button clicked = (Button) (evt.getSource());
					String text = (String) clicked.GetPropertyFromEventReturn();
					textEntry.AddCharacter(text);
					textDisplayMode = textShowEmail;
					clickedButton = clicked;

				}
			});
			buttons.add(button);
		}
	}

	private void createAToSingleQuoteRow() {
		PImage b0 = app.loadImage("key up.png");
		PImage b1 = app.loadImage("key down.png");
		int rows = 12;
		int bIX = (app.width - this.backgroundImage.width) / 2;
		int bIY = (app.height - this.backgroundImage.height) / 2
				+ keyboardShiftVertical + 120;
		// float dX =
		// (float)this.backgroundImage.width-(((float)rows+4.0f)*(float)b0.width);
		// dX/=(float)(rows+2);
		float dX = squareKeyShift;
		bIX += this.backgroundImage.width / 2 - (float) rows * (b0.width + dX)
				/ 2.0f;/* 4.0f*dX +b0.width */
		;
		PVector colour = new PVector(255, 255, 255);
		PFont font = app.createFont("Helvetica", 16, true);
		// int dX = 20;
		String letters[] = { "A", "S", "D", "F", "G", "H", "J", "K", "L", ":",
				";", "'" };
		for (int i = 0; i < rows; i++) {
			TextDraw textDraw = new TextDraw(font, letters[i], app.CENTER);
			PVector p0 = new PVector(bIX + i * (b0.width + dX), bIY);
			RectangleBoundary bound0 = new RectangleBoundary(p0, b0.width,
					b0.height);
			RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0,
					b1, textDraw, p0, colour, b0.width, b0.height);
			Button button = new Button(app, vis0, bound0, letters[i]);
			button.addButtonPressed(new ButtonPressedEventListener() {
				public void buttonPressed(ButtonPressedEvent evt) {
					Button clicked = (Button) (evt.getSource());
					String text = (String) clicked.GetPropertyFromEventReturn();
					textEntry.AddCharacter(text);
					textDisplayMode = textShowEmail;
					clickedButton = clicked;
				}
			});
			buttons.add(button);
		}
	}

	private void createZToEnterRow() {
		PImage b0 = app.loadImage("key up.png");
		PImage b1 = app.loadImage("key down.png");
		int rows = 11;
		int bIX = (app.width - this.backgroundImage.width) / 2;
		int bIY = (app.height - this.backgroundImage.height) / 2
				+ keyboardShiftVertical + 180;
		// float dX =
		// (float)this.backgroundImage.width-(((float)rows+4.0f)*(float)b0.width);
		// dX/=(float)(rows+2);
		float dX = squareKeyShift;
		bIX += this.backgroundImage.width / 2 - (float) rows * (b0.width + dX)
				/ 2.0f;/* 4.0f*dX +b0.width */
		;
		PVector colour = new PVector(255, 255, 255);
		PFont font = app.createFont("Helvetica", 16, true);
		// int dX = 20;
		String letters[] = { "Z", "X", "C", "V", "B", "N", "M", "_", "-", ".",
				"Enter" };
		for (int i = 0; i < rows - 1; i++) {
			TextDraw textDraw = new TextDraw(font, letters[i], app.CENTER);
			PVector p0 = new PVector(bIX + i * (b0.width + dX), bIY);
			RectangleBoundary bound0 = new RectangleBoundary(p0, b0.width,
					b0.height);
			RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0,
					b1, textDraw, p0, colour, b0.width, b0.height);
			Button button = new Button(app, vis0, bound0, letters[i]);
			button.addButtonPressed(new ButtonPressedEventListener() {
				public void buttonPressed(ButtonPressedEvent evt) {
					Button clicked = (Button) (evt.getSource());
					String text = (String) clicked.GetPropertyFromEventReturn();
					textEntry.AddCharacter(text);
					clickedButton = clicked;
				}
			});
			buttons.add(button);
		}
		PImage enter0 = app.loadImage("large key up.png");
		PImage enter1 = app.loadImage("large key down.png");
		TextDraw textDraw = new TextDraw(font, letters[rows - 1], app.CENTER);
		PVector p0 = new PVector(bIX + (rows - 1) * (b0.width + dX), bIY);
		RectangleBoundary bound0 = new RectangleBoundary(p0, enter0.width,
				enter0.height);
		RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, enter0,
				enter1, textDraw, p0, colour, enter0.width, enter0.height);
		Button button = new Button(app, vis0, bound0, letters[rows - 1]);
		button.addButtonPressed(new ButtonPressedEventListener() {
			public void buttonPressed(ButtonPressedEvent evt) {
				sendingEmail = true;
				clickedButton = (Button)evt.getSource();
			}
		});
		buttons.add(button);
	}

	private void createSpecialKeysRow() {
		PImage b0 = app.loadImage("round key up.png");
		PImage b1 = app.loadImage("round key down.png");
		int rows = 12;
		int bIX = (app.width - this.backgroundImage.width) / 2;
		int bIY = (app.height - this.backgroundImage.height) / 2
				+ keyboardShiftVertical + 240;
		
		float dX = circleKeyShift;
		bIX += this.backgroundImage.width / 2 - (float) rows * (b0.width + dX)
				/ 2.0f;/* 4.0f*dX +b0.width */
		;
		PVector colour = new PVector(255, 255, 255);
		PFont font = app.createFont("Helvetica", 16, true);

		// int dX = 20;
		String letters[] = { "[", "]", "}", "~", "#", "?", "+", "*", "=", "&",
				"%", "$" };
		for (int i = 0; i < rows; i++) {
			TextDraw textDraw = new TextDraw(font, letters[i], app.CENTER);
			PVector p0 = new PVector(bIX + i * (b0.width + dX), bIY);
			RectangleBoundary bound0 = new RectangleBoundary(p0, b0.width,
					b0.height);
			RectImageButtonVisLayer vis0 = new RectImageButtonVisLayer(app, b0,
					b1, textDraw, p0, colour, b0.width, b0.height);
			Button button = new Button(app, vis0, bound0, letters[i]);
			button.addButtonPressed(new ButtonPressedEventListener() {
				public void buttonPressed(ButtonPressedEvent evt) {
					Button clicked = (Button) (evt.getSource());
					String text = (String) clicked.GetPropertyFromEventReturn();
					textEntry.AddCharacter(text);
					textDisplayMode = textShowEmail;
					clickedButton = clicked;
				}
			});
			buttons.add(button);
		}
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
	/**
	 * View overrides
	 */
	public void Draw() 
	{
		handleAnimation();
		if(this.isClosing || this.isOpening)
		{
			this.drawAnimated();
			return;
		}
		if(this.noDraw)
		{
			this.noDraw = false;
			return;
		}
		if (sendingEmail) {
			textDisplayMode = textShowSendingEmail;
			if (this.sendEmailToggle)
				this.sendEmail();
			else
				this.sendEmailToggle = true;
			// this.DrawSendEmailMessage();
			// return;
		}
		this.app.background(0);
		//this.branding.Draw();
		this.app.image(this.currentState.GetBackImage(), 0, 0);
		this.branding.Draw();
		if(this.currentState.BackgroundErased)
			this.branding.DrawBackground();
		int backImageX = (app.width - this.backgroundImage.width) / 2;
		int backImageY = (app.height - this.backgroundImage.height) / 2;
		this.app.image(this.backgroundImage, backImageX, backImageY);
		for (int i = 0; i < buttons.size(); i++) {
			boolean selected = false;
	    	 if(buttons.get(i) == clickedButton)
	    		 selected = true;
			buttons.get(i).Draw(selected);
		}
		for(int i = 0; i < toggles.size(); i++)
		{
			toggles.get(i).Draw();
		}
		
			
		DrawEmailBoxText(false);
		DrawBox();
		if(isWaitText)
		{
			waitStep++;
			if(waitStep > waitMax)
			{
				waitStep = 0;
				isWaitText = false;
				textDisplayMode = textShowEmail;
			}
		}
		
		
	}
	private void drawAnimated()
	{
		if (sendingEmail) {
			textDisplayMode = textShowSendingEmail;
			if (this.sendEmailToggle)
				this.sendEmail();
			else
				this.sendEmailToggle = true;
			// this.DrawSendEmailMessage();
			// return;
		}
		this.app.background(0);
		//this.branding.Draw();
		this.app.tint(255, 255, 255);
		this.app.image(this.currentState.GetBackImage(), 0, 0);
		this.branding.Draw();
		if(this.currentState.BackgroundErased)
			this.branding.DrawBackground();
		int backImageX = (app.width - this.backgroundImage.width) / 2;
		int backImageY = (app.height - this.backgroundImage.height) / 2;
		this.app.tint(255, 255, 255, (int)(animationStep*255.0f));
		this.app.image(this.backgroundImage, backImageX, backImageY);
		for (int i = 0; i < buttons.size(); i++) {
			boolean selected = false;
	    	 if(buttons.get(i) == clickedButton)
	    		 selected = true;
			buttons.get(i).DrawAnimated(animationStep, selected);
		}
		for(int i = 0; i < toggles.size(); i++)
		{
			toggles.get(i).DrawAnimated(animationStep);
		}
		DrawEmailBoxText(true);
		DrawBoxAnimated();
		this.app.tint(255, 255, 255);
	}
	private void DrawBox()
	{
		this.app.noFill();
		this.app.stroke(255, 255, 255);
		this.app.strokeWeight(5);
		this.app.strokeJoin(app.MITER);
		int bIX = (app.width - this.backgroundImage.width) / 2 + 130;
		int bIY = (app.height - this.backgroundImage.height) / 2 + 140;
		this.app.rect(bIX, bIY, 660, 80);
		int widthChange = 480;
		this.app.rect(bIX+widthChange, bIY, 660-widthChange, 80);
		
		app.textFont(this.warningFont);
		app.fill(255);
		float tX = bIX + 130;
		float tY = bIY + 20;
		app.text(this.warningString,tX,tY);
	}
	private void DrawBoxAnimated()
	{

		this.app.noFill();
		this.app.stroke(255, 255, 255, (int)(animationStep*255.0f));
		this.app.strokeWeight(5);
		this.app.strokeJoin(app.MITER);
		int bIX = (app.width - this.backgroundImage.width) / 2 + 130;
		int bIY = (app.height - this.backgroundImage.height) / 2 + 140;
		this.app.rect(bIX, bIY, 660, 80);
		int widthChange = 480;
		this.app.rect(bIX+widthChange, bIY, 660-widthChange, 80);
		
		app.textFont(this.warningFont);
		app.fill(255, 255, 255, (int)(animationStep*255.0f));
		float tX = bIX + 130;
		float tY = bIY + 20;
		app.text(this.warningString,tX,tY);
	}
	private void createAgreementToggle()
	{
		int bIX = (app.width-this.backgroundImage.width)/2+660; 
    	int bIY = (app.height-this.backgroundImage.height)/2+170; 
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
			    	Agree = !Agree;
			    	 //ToggleButton toggleButton = (ToggleButton)(evt.getSource());
			    	
			    }
			});
    	PFont fontSides = app.createFont("Helvetica", 12, true);
    	TextLabel labelLeft = new TextLabel(this.app, new PVector(-4, sImg.height/2 + 1), new PVector(255, 255, 255), fontSides, app.RIGHT, "agree");
    	tV.AddLabel(labelLeft);
    	TextLabel labelRight = new TextLabel(this.app, new PVector(sImg.width + 4, sImg.height/2 + 1), new PVector(255, 255, 255), fontSides, app.LEFT, "disagree");
    	tV.AddLabel(labelRight);
    	this.toggles.add(toggle);
	}
	public void DrawSendEmailMessage() {
		// this.app.background(0);

		// app.textFont(app.createFont("Helvetica",36, true));
		// app.fill(255);
		// app.textAlign(app.CENTER);

		// float tX = this.app.width/2;
		// float tY = this.app.height/2;
		// app.text("Sending e-mail",tX,tY);
		if (this.sendEmailToggle)
			this.sendEmail();
		else
			this.sendEmailToggle = true;
	}

	private void DrawEmailBoxText(boolean isAnimated) {

		String text = "";
		switch (textDisplayMode) {
		case textShowEmail:
			text = this.textEntry.GetEmail();
			break;
		case textShowError:
			text = errorEmailText;
			break;
		case textShowSendingEmail:
			text = sendingEmailText;
			break;
		case textShowEmailSent:
			text = sentEmailText;
			break;
		case textShowNotAgreed:
			text = notAgreedText;
		}
		if(isAnimated)
			this.textEntry.Draw(animationStep, text);
		else
			this.textEntry.Draw(1.0f, text);
	}

	public void PrepareCanvas() {
		this.isOpening = true;
	}

	public void MousePressed(int mouseX, int mouseY, boolean leftButton) {
		
		if(isClosing || isOpening)
    		return;
		if (!leftButton) {
			isClosing = true;
    		this.animationStep = 1.0f;
    		return;
			//closeView(new CloseViewEvent(new Object()));
		}
		if (!menuBoundary.CheckIntersection(new PVector((float) mouseX,
				(float) mouseY))) {
			this.OpenMenu = false;
			isClosing = true;
    		this.animationStep = 1.0f;
    		return;
			//closeView(new CloseViewEvent(new Object()));
			
		}
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).CheckIntersection(new PVector(mouseX, mouseY));
		}
		for(int i = 0; i < toggles.size(); i++)
		{
			toggles.get(i).CheckIntersection(new PVector(mouseX, mouseY));
		}
	}

	public void MouseReleased(int mouseX, int mouseY) 
	{
		clickedButton = null;
	}

	public void MouseDragged(int mouseX, int mouseY) {

	}

	public void KeyPressed(char key) {

		if (key == app.BACKSPACE) {
			this.textEntry.DeleteCharacter();
		} else if (key == app.ENTER) {
			this.sendingEmail = true;
		} else if (key == app.SHIFT)
		{
			return;
		}
		else if (key == '￿')
		{
			return;
		}
		else {
			this.textDisplayMode = textShowEmail;
			this.textEntry.AddCharacter(Character.toUpperCase(key));
		}
	}

	public void HandleClosing() {
		
	}

	public void HandleOpening() {
		this.OpenMenu = true;
	}

	/**
	 * Public functions
	 */
	void closeView(CloseViewEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == CloseViewEventListener.class) {
				((CloseViewEventListener) listeners[i + 1]).closeView(evt);
			}
		}
	}

	public void SetBackgroundImage(PImage image) {
		this.backgroundImage = image;
	}


	public void SetMenuArguments(MenuState state) {
		this.currentState = state;
		this.drawnImage = state.GetBackImage();
		if(this.drawnImage == null)
			this.drawnImage = new PImage(GlobalSettings.GetInstance().ScreenWidth,GlobalSettings.GetInstance().ScreenHeight);
		this.drawnImage.save(app.savePath("")+"emailimage.jpg");
		this.OpenMenu = true;
	}
	public MenuState GetMenuState()
	{
		return this.currentState;
	}
	/**
	 * Private functions
	 */
	private void sendEmail() {
		// PImage saveImage = this.drawnImage.get(0, 0, this.drawnImage.width,
		// this.drawnImage.height);
		// saveImage.resize(this.app.screenWidth, this.app.screenHeight);
		// saveImage.save(app.savePath("")+"test.png");
		// this.DrawSendEmailMessage();
		if(!Agree)
		{
			this.sendingEmail = false;
			this.sendEmailToggle = false;
			textDisplayMode = textShowNotAgreed;
			isWaitText = true;
			waitStep = 0;
			return;
		}
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		GlobalSettings instance = GlobalSettings.GetInstance();
		headers.add(new NameValuePair("HEADER", "test"));

		PostmarkMessage message = new PostmarkMessage(/*"Postmark@Seanzo.ca"*/instance.PostmarkEmail,
				textEntry.GetEmail(), /*"Postmark@Seanzo.ca"*/instance.PostmarkEmail, null,
				instance.PostmarkID/*"Postmark"*/, instance.PostmarkText/*"This was sent to you by Postmark."*/, false, null,
				headers);

		PostmarkClient client = new PostmarkClient(instance.PostmarkClient
				/*"21cfcc88-312d-4a46-8414-a46e1de5e1ef"*/);
		Attachment attachment = new Attachment();
		attachment.setContentType("image/jpeg");
		attachment.setName("emailimage.jpg");
		// convert file contents to base64

		try {
			File file = new File("emailimage.jpg");
			byte[] ba = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(ba);
			fis.close();
			String valz = new String(Base64.encodeBase64(ba));// .toString();//""+new
																// Base64().encodeBase64(ba);
			//String ret0 = "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";
			attachment.setContent(valz);// encode(ba).toString());
		} catch (Exception e) {

		}
		// encodeToString(ba));

		// can handle multiple attachments
		List<Attachment> v = new ArrayList<Attachment>();
		v.add(attachment);

		message.setAttachments(v);
		try {
			client.sendMessage(message);

			textDisplayMode = textShowEmailSent;
			this.textEntry.ClearText();
			isWaitText = true;
		} catch (PostmarkException pe) {
			textDisplayMode = textShowError;
			isWaitText = true;
			this.textEntry.ClearText();
			System.out.println("An error has occured : " + pe.getMessage());
		}
		this.sendingEmail = false;
		this.sendEmailToggle = false;
	}
	public void SetBranding(Branding brand)
    {
    	this.branding = brand;
    }

}
