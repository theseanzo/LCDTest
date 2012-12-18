import processing.core.*;
import processing.opengl.*;
import java.util.*;
import javax.media.opengl.GL;
public class Drip 
{
	ArrayList<Circle> circles = new ArrayList<Circle>();
	PApplet app;
	 public Drip(PApplet a, Circle baseCircle, Random generator, Boolean staticDrip)
	  {
		 this.app = a;
		  PVector origin = baseCircle.GetOrigin();
		  PVector colour = baseCircle.GetColour();
		  float radius = 4.0f*baseCircle.radius/5.0f;
		  GlobalSettings instance = GlobalSettings.GetInstance();
		  int dripAmount = generator.nextInt(instance.DripAmount); //amount of possible drips
		  
		  for(int i = 0; i < dripAmount; i++)
		  {
			  float xStart = (generator.nextFloat()*2.0f-1.0f)*radius/2.0f;
			  Circle circle = new Circle(2, new PVector(origin.x + xStart, origin.y), app, colour, 255);
			  circles.add(circle);
			  float dripDropMin = instance.DripDropMin;//30.0f;
			  float dripDropMax = instance.DripDropMax;//125.0f;
			  float dripDropGen = generator.nextFloat();
			  float dripHeight = dripDropMin * dripDropGen + dripDropMax * (1.0f-dripDropGen);
			  if(staticDrip)
				  circle.StartStaticDrip(baseCircle.radius + dripHeight);
			  else
				  circle.StartDrip(baseCircle.radius + dripHeight);
		  }
	    
	  }
	 public void UpdateDrip()
	 {
		 for(int i = 0; i < circles.size(); i++)
		 {
			 circles.get(i).UpdateDrip();
		 }
	 }
	 public void Draw(PImage nozzleTex)
	 {
		 for(int i = 0; i < circles.size(); i++)
		 {
			 circles.get(i).Draw(nozzleTex);
		 }
	 }
}
