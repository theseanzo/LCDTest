import processing.core.*;
public interface ButtonVisLayer 
{
	public void Draw();
	public void Draw(boolean selected);
	public void DrawClear();
	public void DrawAnimated(float step);
	public void DrawAnimated(float step, boolean selected);
	public void AddLabel(TextLabel label);
	public void DrawFrame(float animationStep);
	public void DrawNextFrame();
}
