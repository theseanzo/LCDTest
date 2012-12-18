import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import processing.core.*;
import processing.opengl.*;
import java.util.*;
public class GlobalSettings 
{
	private static GlobalSettings instance = null;
	public float DripGrowthMin, DripGrowthMax;
	public float DripSpeedMin, DripSpeedMax;
	public float DripWidthMin, DripWidthMax;
	public float DripDropMin, DripDropMax;
	public float DripThickMin, DripThickMax;
	public int DripTime, DripAmount;
	
	public float NozzleScaleMax, NozzleScaleMin;
	public String NozzleSound ="";
	public int ScreenWidth, ScreenHeight;
	public int PressureMin, PressureMax;
	public ArrayList<PVector> Colours = new ArrayList<PVector>();
	public float StickerFloatScale, StickerPlaceScale;
	
	
	public float AnimationSpeed;
	public int Flips;
	public String TopLeft, TopRight, BottomLeft, BottomRight, Background;
	public String PostmarkEmail, PostmarkID, PostmarkText, PostmarkClient, ApprovalMessage;
	   protected GlobalSettings() 
	   {
	      readXML();// Exists only to defeat instantiation.
	   }
	   
	   public static GlobalSettings GetInstance() {
	      if(instance == null) 
	      {
	         instance = new GlobalSettings();
	      }
	      return instance;
	   }
	   
	   private void readXML()
	   {
		   try {
			   
				File fXmlFile = new File("globalsettings.xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
		 
				//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				Element root = doc.getDocumentElement();
				// = root.getChildNodes();
				NodeList drip = doc.getElementsByTagName("dripsetting");
				DripGrowthMin = new Float(getTagValue("dripGrowthMin", (Element)drip.item(0) ));
				DripGrowthMax= new Float(getTagValue("dripGrowthMin", (Element)drip.item(0) ));
				DripSpeedMin = new Float(getTagValue("dripSpeedMin", (Element)drip.item(0) ));
				DripSpeedMax = new Float(getTagValue("dripSpeedMax", (Element)drip.item(0) ));
				DripWidthMin = new Float(getTagValue("dripWidthMin", (Element)drip.item(0) ));
				DripWidthMax= new Float(getTagValue("dripWidthMax", (Element)drip.item(0) ));
				DripDropMin = new Float(getTagValue("dripDropMin", (Element)drip.item(0) ));
				DripDropMax= new Float(getTagValue("dripDropMax", (Element)drip.item(0) ));
				DripThickMin = new Float(getTagValue("dripThickMin", (Element)drip.item(0) ));
				DripThickMax= new Float(getTagValue("dripThickMax", (Element)drip.item(0) ));
				DripTime = new Integer(getTagValue("dripTime", (Element)drip.item(0) ));
				DripAmount = new Integer(getTagValue("dripAmount", (Element)drip.item(0) ));
				
				
				NodeList nozzle = doc.getElementsByTagName("nozzlesetting");
				//nozzle settings
				NozzleScaleMax = new Float(getTagValue("nozzlescalemax", (Element)nozzle.item(0)));
				NozzleScaleMin =  new Float(getTagValue("nozzlescalemin", (Element)nozzle.item(0) ));
				PressureMin = new Integer(getTagValue("pressuremin", (Element)nozzle.item(0) ));
				PressureMax = new Integer(getTagValue("pressuremax", (Element)nozzle.item(0) ));
				NozzleSound = getTagValue("nozzlesound", (Element)nozzle.item(0));
				
				NodeList screen = doc.getElementsByTagName("screensetting");
				ScreenWidth = new Integer(getTagValue("screenwidth", (Element)screen.item(0)));
				ScreenHeight = new Integer(getTagValue("screenheight", (Element)screen.item(0)));
				
				NodeList sticker = doc.getElementsByTagName("stickersetting");
				StickerFloatScale = new Float(getTagValue("stickerfloatscale", (Element)sticker.item(0) ));
				StickerPlaceScale = new Float(getTagValue("stickerplacescale", (Element)sticker.item(0) ));
				
				NodeList colorSwatches = doc.getElementsByTagName("colorswatches");
				createColorSwatches(root);
				
				NodeList logo = doc.getElementsByTagName("logosettings");
				TopLeft = getTagValue("topleft", (Element)logo.item(0) );
				TopRight = getTagValue("topright", (Element)logo.item(0) );
				BottomLeft = getTagValue("bottomleft", (Element)logo.item(0) );
				BottomRight= getTagValue("bottomright", (Element)logo.item(0) );
				Background = getTagValue("background", (Element)logo.item(0) );
				
				NodeList keyMenu = doc.getElementsByTagName("keymenusetting");
				PostmarkEmail = getTagValue("postmarkemail", (Element)keyMenu.item(0) );
				PostmarkID = getTagValue("postmarkid", (Element)keyMenu.item(0) );
				PostmarkText= getTagValue("postmarktext", (Element)keyMenu.item(0) );
				PostmarkClient= getTagValue("postmarkclient", (Element)keyMenu.item(0) ); 
				ApprovalMessage= getTagValue("approvalmessage", (Element)keyMenu.item(0) );
				
				NodeList animation = doc.getElementsByTagName("animationsetting");
				AnimationSpeed = new Float(getTagValue("animationspeed", (Element)animation.item(0) ));
				Flips =new Integer(getTagValue("flips", (Element)animation.item(0) ));

				/*for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				   Node nNode = nList.item(temp);
				   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
				      Element eElement = (Element) nNode;
		 
				      System.out.println("First Name : " + getTagValue("firstname", eElement));
				      System.out.println("Last Name : " + getTagValue("lastname", eElement));
			              System.out.println("Nick Name : " + getTagValue("nickname", eElement));
				      System.out.println("Salary : " + getTagValue("salary", eElement));
		 
				   }
				}*/
			  } catch (Exception e) {
				e.printStackTrace();
			  }
	   }
	   private void createColorSwatches(Element root)
	   {   
		   NodeList colourList = root.getElementsByTagName("colour");
		   for(int i = 0; i < 32; i++)
		   {
			   String[] colourValues = getTagValue("val",(Element)colourList.item(i)).split(",");
			   int r = new Integer(colourValues[0]);
			   int g = new Integer(colourValues[1]);
			   int b = new Integer(colourValues[2]);
			   Colours.add(new PVector(r, g, b));
		   }
		  /* for(int i = 0; i < 32; i++)
		   {
			   Element color = (Element)colorList.item(i).getChildNodes().item(0);//colorListitem(i);
			   
			   String[] colorValues = getTagValue("val",color).split(",");//(Element)colorSwatches.item(0).getAttributes().getNamedItem(""+i)).split(",");//    getTagValue("color", (Element)colorSwatches.item(0)).split(","); 
			   int r = new Integer(colorValues[0]);
			   int g = new Integer(colorValues[1]);
			   int b = new Integer(colorValues[2]);
			   Colors.add(new PVector(r, g, b));
		   }*/
		   
	   }
	   private static String getTagValue(String sTag, Element eElement) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		 
		        Node nValue = (Node) nlList.item(0);
		 
			return nValue.getNodeValue();
		  }
}
