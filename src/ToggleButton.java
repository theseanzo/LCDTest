import processing.core.*;


public class ToggleButton 
{
	PVector origin;
	PVector colour;
	PApplet app;
	float radius;
	Object eventReturn;
	public boolean IsSelected = false;
	ToggleButtonVisLayer visual;
	HitBoundary boundary;
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	Button state1;
	Button state2;
	Button currentState;
	ButtonPressedEvent event;
	public boolean Toggle = false;
	public ToggleButton()
	{
		
	}
	public ToggleButton(PApplet a, ToggleButtonVisLayer vis, HitBoundary bound, Button s1, Button s2)
	{
		this.app = a;
		this.visual = vis;
		this.state1 = s1;
		this.state2 = s2;
		this.currentState = this.state1;
		this.boundary = bound;
		this.state1.addButtonPressed(new ButtonPressedEventListener() {
 			    public void  buttonPressed(ButtonPressedEvent evt) {
 			        // MyEvent was fired
 			    	if(currentState != state1)
 			    	{
	 			    	currentState = state1;
	 			    	Toggle = true;
	 			    	toggleButtonPressed(new ButtonPressedEvent(this));
 			    	}
 			    }
 			});
		this.state2.addButtonPressed(new ButtonPressedEventListener() {
			    public void  buttonPressed(ButtonPressedEvent evt) {
			        // MyEvent was fired
			    	if(currentState != state2)
 			    	{
				    	currentState = state2;
				    	Toggle = false;
				    	toggleButtonPressed(new ButtonPressedEvent(this));
 			    	}
			    }
			});
		
	}
	public void addButtonPressed(ButtonPressedEventListener listener) {
		listenerList.add(ButtonPressedEventListener.class, listener);
	}
	
	void toggleButtonPressed(ButtonPressedEvent evt) {
        // MyEvent was fired
    	
    	Object[] listeners = listenerList.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == ButtonPressedEventListener.class) {
				((ButtonPressedEventListener) listeners[i + 1])
						.buttonPressed(evt);
			}
		}
    	//clickedButton = (Button)(evt.getSource());
    	//currentState.StickerButtonClicked((PImage)clickedButton.GetPropertyFromEventReturn());
    }
	void buttonPressed(ButtonPressedEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == ButtonPressedEventListener.class) {
				((ButtonPressedEventListener) listeners[i + 1]).buttonPressed(evt);
			}
		}
	}
	public void CheckIntersection(PVector point)
	{
		//state1.CheckIntersection(point);
		//state2.CheckIntersection(point);
		if(boundary.CheckIntersection(point))
		{
			if(currentState != state1)
		    {
			    	currentState = state1;
			    	Toggle = true;
			    	toggleButtonPressed(new ButtonPressedEvent(this));
		    }
			else 
		    {
		    	currentState = state2;
		    	Toggle = false;
		    	toggleButtonPressed(new ButtonPressedEvent(this));
		    }
		}
		
	}
	public void Draw()
	{
		this.visual.Draw();
		this.currentState.Draw(false);
	}
	
	public void Draw(boolean selected)
	{
		currentState.Draw(selected);
		
	}
	public void DrawAnimated(float step, boolean selected)
	{
		this.visual.DrawAnimated(step, selected);
		this.currentState.DrawAnimated(step, selected);
	}
	public void DrawAnimated(float step)
	{
		this.visual.DrawAnimated(step);
		this.currentState.DrawAnimated(step);
	}

}
