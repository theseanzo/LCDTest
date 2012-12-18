import processing.core.PVector;
public class RectangleBoundary implements HitBoundary {
	PVector corner;
	float width;
	float height;
	public RectangleBoundary(PVector blCorner, float w, float h)
	{
		this.corner = blCorner;
		this.width = w;
		this.height = h;
	}
	public boolean CheckIntersection(PVector point)
	{
		if (point.x > corner.x && point.y > corner.y && point.x < corner.x + width && point.y < corner.y + height)
			return true;
		return false;
	}

}
