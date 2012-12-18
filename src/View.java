import processing.core.*;
import java.util.*;

public interface View
{
  void Draw();
  void PrepareCanvas();
  void MousePressed(int mouseX, int mouseY, boolean leftButton);
  void MouseReleased(int mouseX, int mouseY);
  void MouseDragged(int mouseX, int mouseY);
  void KeyPressed(char c);
  void HandleClosing();
  void HandleOpening();
  void SetBranding(Branding brand);
}