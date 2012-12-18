import java.util.*;
import processing.core.*;

public class Button {
	PVector origin;
	PVector colour;
	PApplet app;
	float radius;
	Object eventReturn;
	public boolean IsSelected = false;
	public boolean AnimateOnClick = false;
	ButtonVisLayer visual;
	HitBoundary boundary;
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	float animationStep = 0.0f;
    float animationSpeed = 0.10f;
    float flips;
	public Button()
	{
		
	}
	
	public Button(PApplet a,
			ButtonVisLayer vis, HitBoundary bound, Object eReturn) {
		this.app = a;
		this.eventReturn = eReturn;
		this.visual = vis;
		this.boundary = bound;
		animationSpeed = GlobalSettings.GetInstance().AnimationSpeed*2.0f;
		flips = (float)GlobalSettings.GetInstance().Flips;
	}
	
	public Object GetPropertyFromEventReturn()
	{
		return this.eventReturn;
	}
	
	public void addButtonPressed(ButtonPressedEventListener listener) {
		listenerList.add(ButtonPressedEventListener.class, listener);
	}
	
	void buttonPressed(ButtonPressedEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == ButtonPressedEventListener.class) {
				((ButtonPressedEventListener) listeners[i + 1])
						.buttonPressed(evt);
			}
		}
	}

	public void CheckIntersection(PVector point) {
		if (boundary.CheckIntersection(point)) {
			if(this.AnimateOnClick)
				this.IsSelected = true;
			buttonPressed(new ButtonPressedEvent(this));
		}
	}

	public void Draw(boolean selected) {
		
		/*if (selected) {
			this.app.stroke(255, 255, 255);
		} else {
			this.app.noStroke();
		}*/
		if(IsSelected)
		{
			visual.DrawFrame(animationStep);
			animationStep += animationSpeed;
			if(animationStep >= 1.0f)
			{
				this.IsSelected = false;
				this.animationStep = 0.0f;
			}
		}
		else
			visual.Draw(selected);
		/*
		 * this.app.fill(colour.x, colour.y, colour.z);
		 * this.app.ellipse(origin.x, origin.y, radius, radius);
		 */
	}
	public void DrawAnimated(float step, boolean selected)
	{
		visual.DrawAnimated(step, selected);
	}
	public void DrawAnimated(float step)
	{
		visual.DrawAnimated(step);
	}
	public void DrawClear() {
		visual.DrawClear();
	}
}
