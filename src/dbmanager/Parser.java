package dbmanager;
import org.w3c.dom.*;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.*;


public class Parser {

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	 	DocumentBuilder db = null;
	 	Document d = null;
	 	Element element = null;
	 	
	 	
	 	public Parser(){
	 		
	 		
	 		try {
	 		    db = dbf.newDocumentBuilder();
	 		} 
	 		catch (ParserConfigurationException ex) {
	 		    ex.printStackTrace();  
	 		}
	 		try {
	 		    d = db.parse(new FileInputStream("src\\Xml\\database.xml"));
	 		} 
	 		catch (Exception ex) {
	 		    ex.printStackTrace();
	 		} 
	 		element = d.getDocumentElement();
	 		
	 	}
	 	
	    public String getDatabase(int i){
            NodeList n1 = element.getElementsByTagName("database");
            return n1.item(i).getTextContent();
        }
	 	
	 	public String getDriverName(int i){
	 		NodeList n1 = element.getElementsByTagName("drivernaam");
	 		return n1.item(i).getTextContent();
	 	}
	 	
	 	public String getUrl(int i){
	 		NodeList n1 = element.getElementsByTagName("url");
	 		return n1.item(i).getTextContent();
	 	}
	 	
	    public String getPort(int i){
            NodeList n1 = element.getElementsByTagName("port");
            return n1.item(i).getTextContent();
        }
	 	
	 	public String getUsername(int i){
	 		NodeList n1 = element.getElementsByTagName("username");
	 		return n1.item(i).getTextContent();
	 	}
	 	
	 	
	 	public String getPassword(int i){
	 		NodeList n1 = element.getElementsByTagName("password");
	 		return n1.item(i).getTextContent();
	 	}
	         
	      
	 }


