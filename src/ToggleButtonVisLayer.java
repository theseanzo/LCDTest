import processing.core.*;
public interface ToggleButtonVisLayer 
{
	public void Draw();
	public void Draw(boolean selected);
	public void DrawClear();
	public void DrawAnimated(float step);
	public void DrawAnimated(float step, boolean selected);
	public void AddLabel(TextLabel label);
	

}
