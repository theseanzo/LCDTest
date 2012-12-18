import processing.core.*;
import processing.opengl.*;
import java.util.*;
import javax.media.opengl.GL;
public class Circle
{
  float radius;
  
  PVector origin;
  int divisions = 50;
  PVector[] points = new PVector[divisions];
  PVector p0, p1, p2, p3;
  PApplet app;
  PImage texture;
  int pressure, originalPressure;
  PVector tintColour;
  Timer dripTimer;
  float dripSteps = 0.0f;
  float width;
  float originX;
  int dripDuration;
  float dripHeight = 100.0f;
  int drips = 0;
  int pressureDripStart = 100;
  int pressureDripMod;
  int dripEndDuration;
  float dripSpeed;
  float ThicknessIncreaser;
  boolean KeepDrip = false;
  boolean staticDrip = false;
  float dripOriginY;
  float dripWidth = 0;
  float dripGrowthFinalSize = 1000.0f;
  ArrayList<Circle> subCircles = new ArrayList<Circle>();
  
  public Circle(float rad, PVector orig, PApplet a, PVector colour, int press)
  {
	  tintColour = new PVector(colour.x, colour.y, colour.z);
	  this.app = a;
    radius = rad;
    width = radius/2;
    origin = orig;
    originX = orig.x;
    dripOriginY = origin.y;
    createCircle();
    dripGrowthFinalSize = 1000.0f;
    this.pressure = press;
    this.originalPressure = pressure;
    
  }
  private void createCircle()
  {
     p0 = new PVector(origin.x - radius, origin.y - radius);
     p1 = new PVector(origin.x - radius, origin.y + radius);
     p2 = new PVector(origin.x + radius, origin.y + radius);
     p3 = new PVector(origin.x + radius, origin.y - radius);
  }
  public void ShiftDown(float amount)
  {
	  p0.y -= amount;
	  p1.y -= amount;
	  p2.y -= amount;
	  p3.y -= amount;
  }
  public void UpdateDrip()
  {
	  //this.pressure -= 1;
	  drips++;
	 // if(drips > dripDuration)
	  if(this.origin.y > dripHeight + dripOriginY)
	  {
		  ThickenDrip();
		  return;
	  }
	  //pressure = 100. when pressure = 0 drips == dripDuration.
	  //so, if dripDuration = 500, pressure will reduce to 0.
	 // if(drips %pressureDripMod==0)
		//  pressure--;
	  float dripWidth = 40;
	  this.origin.y += dripSpeed;//0.25f;
	  //this.radius = app.noise(dripSteps)*width; // - radius;
	  this.origin.x = originX + (dripWidth/2 -app.noise(dripSteps)*dripWidth);
	  //this.radius /= 1.01f;
	  if(radius < 0)
		  radius = 0.0f;
	  if(!KeepDrip)
		  createCircle();
	  else
	  {
		  subCircles.add(new Circle(this.radius,this.origin, this.app, this.tintColour,this.pressure));
	  }
	  dripSteps+=0.01f;
  }
  public void ThickenDrip()
  {
	 // if(drips > dripEndDuration)
		//  return;
	  
	  if(radius > dripGrowthFinalSize)
		  return;
	  this.origin.y += 0.01f;
	  //this.radius = app.noise(dripSteps)*width; // - radius;
	  //this.origin.x = originX + (width/2 -app.noise(dripSteps)*width);
	  this.radius += .05f;
	  if(radius < 0)
		  radius = 0.0f;
	  if(!KeepDrip)
		  createCircle();
	  else
	  {
		  subCircles.add(new Circle(this.radius,this.origin, this.app, this.tintColour,this.pressure));
	  }
		  
	  dripSteps+=0.01f;
  }
  public void Draw(PImage tex)
  {
	  if(radius > dripGrowthFinalSize && !KeepDrip)
		  return;
	  this.texture = tex;
	  app.noStroke();
    float tMin = 0.0f;
    float tMax = 1.0f;
    //this.app.tint(0xFF0000FF);
    if(staticDrip)
    	this.app.tint(tintColour.x, tintColour.y, tintColour.z, this.originalPressure);
    else
    	this.app.tint(tintColour.x, tintColour.y, tintColour.z, this.pressure);
    this.app.beginShape();
    this.app.texture(tex);
    this.app.vertex(p0.x, p0.y, tMin, tMax);
    this.app.vertex(p1.x, p1.y, tMin, tMin);
    this.app.vertex(p2.x, p2.y, tMax, tMin);
    this.app.vertex(p3.x, p3.y, tMax, tMax);
    this.app.endShape();
    this.app.tint(0xFFFFFFFF);
    if(KeepDrip)
    {
    	for(int i = 0; i < subCircles.size(); i++)
    	{
    		subCircles.get(i).Draw(tex);
    	}
    }
   //fill(255, 255, 255, 150);
    //ellipse(origin.x, origin.y, radius*2.0, radius*2.0);
  }
  public void StartStaticDrip(float dHeight)
  {
	  this.staticDrip = true;
	  this.KeepDrip = true;
	  StartDrip(dHeight);
  }
  public void StartDrip(float dHeight)
  {
	  Random gen = new Random();
	  GlobalSettings instance = GlobalSettings.GetInstance();
	  this.dripSteps = gen.nextFloat();
	 // this.dripDuration = (int)this.radius + dDuration;//instance.DripDuration;//dDuration;
	  this.dripHeight = dHeight;
	  this.pressure = 20;//this.pressure/(int)instance.PressureDivisor;  //3;
	  pressureDripMod = this.dripDuration/this.pressure;
	  if(pressureDripMod < 1)
		  pressureDripMod = 1;
	  //this.origin.y ;+= radius/2;
	  //*= 1.0f/instance.ThicknessDivisor;
	  float dripSpeedMin = instance.DripSpeedMin;//0.10f;
	  float dripSpeedMax = instance.DripSpeedMax;//0.20f;
	  float dripSpeedGen = gen.nextFloat();
	  this.dripSpeed = dripSpeedGen*dripSpeedMin + (1.0f-dripSpeedGen)*dripSpeedMax; //instance.DripSpeed;
	  
	 
	  
	  float dripWidthMin = instance.DripWidthMin;//20.0f;
	  float dripWidthMax = instance.DripWidthMax;//40.0f;
	  float dripWidthGen = gen.nextFloat();
	  dripWidth = dripWidthMin * dripWidthGen + dripWidthMax * (1.0f-dripWidthGen);
	  float dripGrowthMin = instance.DripGrowthMin;//1.0f;
	  float dripGrowthMax = instance.DripGrowthMax;//6.0f;
	  float dripGrowthGen = gen.nextFloat();
	  this.dripGrowthFinalSize = dripGrowthMin*dripGrowthGen + dripGrowthMax*(1.0f-dripGrowthGen);
	  
	  
	  float dripThickMin = instance.DripThickMin;//2.0f;
	  float dripThickMax = instance.DripThickMax;//6.0f;
	  float dripThickGen = gen.nextFloat();
	  this.radius = dripThickMin*dripThickGen + dripThickMax * (1.0f-dripThickGen);
	  if(dripGrowthFinalSize < this.radius)
	  {
		  this.dripGrowthFinalSize = this.radius + 0.01f;
	  }
  
  }
  public PVector GetOrigin()
  {
	  return this.origin;
  }
  public PVector GetColour()
  {
	  return this.tintColour;
  }
}
