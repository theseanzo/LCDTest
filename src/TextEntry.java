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
public class TextEntry 
{
	PImage boxImage;
	TextDraw textView;
	PVector position;
	PApplet app;
	public TextEntry()
	{
		
	}
	public TextEntry(PApplet a, PImage bImage, PVector pos, TextDraw tView)
	{
		this.app = a;
		this.boxImage = bImage;
		this.position = pos;
		this.textView = tView;
	}
	public void AddCharacter(String charToAdd)
	{
		this.textView.text += charToAdd;
	}
	public void AddCharacter(char charToAdd)
	{
		this.textView.text += charToAdd;
	}
	public void DeleteCharacter()
	{
		if(this.textView.text.length() > 1)
			this.textView.text = this.textView.text.substring(0, this.textView.text.length()-1);
		else this.textView.text = "";
	}
	public void ClearText()
	{
		this.textView.text = "";
	}
	public void Draw(float opacity)
	{
		this.app.tint(255, 255, 255, (int)(opacity*255.0f));
		this.app.image(boxImage, position.x, position.y);
		app.textFont(textView.font);       
		app.fill(255, 255, 255, (int)(opacity*255.0f));
		app.textAlign(textView.alignment);
		
		float tX = position.x;
		float tY = position.y;
		app.text(textView.text,tX+this.boxImage.width/2,tY+this.boxImage.height/2); 
	}
	public void Draw(float opacity, String text)
	{
		this.app.tint(255, 255, 255, (int)(opacity*255.0f));
		this.app.image(boxImage, position.x, position.y);
		String displayText = new String(text);
		app.textFont(textView.font);       
		app.fill(0, 0, 0, (int)(opacity*255.0f));
		app.textAlign(textView.alignment);
		float maxWidth = 3.0f*(float)boxImage.width/4.5f;
		boolean wasTooLarge = false;
		while(app.textWidth(displayText) > maxWidth)
		{
			displayText = displayText.substring(1, displayText.length());
			wasTooLarge = true;
		}
		if(wasTooLarge)
		{
			displayText = "..." + displayText;
		}
		float tX = position.x;
		float tY = position.y;
		app.text(displayText,tX/*+this.boxImage.width/2*/+78,tY+this.boxImage.height/2 + 8); 
	}
	public String GetEmail()
	{
		return this.textView.text;
	}
	public PFont GetFont()
	{
		return this.textView.font;
	}
	public PVector GetPosition()
	{
		return this.position;
	}
}
