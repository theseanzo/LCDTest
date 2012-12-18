import processing.core.*;
public class EllipseBoundary implements HitBoundary {
	PVector origin;
	float radiusX;
	float radiusY;
	public EllipseBoundary(PVector orig, float rX, float rY)
	{
		this.origin = orig;
		this.radiusX = rX;
		this.radiusY = rY;
	}
	public boolean CheckIntersection(PVector point)
	{
		float dist = (float)Math.sqrt(Math.pow((double)(point.x-origin.x), 2.0) + Math.pow((double)(point.y-origin.y), 2.0));
		if(dist < radiusX)
		{
			return true;
		}
		return false;
	}

}
