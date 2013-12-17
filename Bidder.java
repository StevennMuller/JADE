import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Bidder extends Agent implements AuctionVocabulary {
	Good [] goods = new Good[200]; //List of goods with reservation prices;
	int goodsnumber = 0; //Number of goods (starting from zero);
	String goodname; //Preposition good;
	int goodnumber; //Number of preposition good; 
	int goodprice; //Preposition price for this bidder;
	String goodbuer; //Preposition autor (Local Name);
	int epsilon = 10; //Addition to price;
	
	
	private Codec codec = new SLCodec();
	private Ontology ontology = AuctionOnt.getInstance();
	
	protected void setup(){
		//Loading information from file *.txt;
		String filename = new String(getLocalName()+".txt");
		File f = new File(filename);
		try {
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);
			String record;
			while ((record=dis.readLine()) != null ) {
				
				int i=0;
				while(record.charAt(i) != '=') i++;
				goods[goodsnumber]= new Good(record.substring(0, i), (new Integer(record.substring(i+1))).intValue());
				goodsnumber++;
			}
			goodsnumber--;
		} catch (Exception e) { 
				e.printStackTrace();
				System.exit(1);
		}
		
		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);
		
		addBehaviour(new BidderBehaviour(this));
	}
	
	class BidderBehaviour extends CyclicBehaviour{

		public BidderBehaviour(Agent a){
			super(a);
		}
		
		public void action(){
			ACLMessage msg = receive();
			if(msg != null){
				try {
         			ContentElement content = getContentManager().extractContent(msg);
         			Concept action = ((Action)content).getAction();
					goodname = ((NewGood)action).getGoodName();
					goodprice = ((NewGood)action).getGoodPrice();
					goodbuer = ((NewGood)action).getAuthor();
           			int flag = 0;
           			//Searching good in list;
           			for(int i=0; i <= goodsnumber; i++){
           				if(goods[i].getName().equals(goodname)){
           					flag = 1;
           					goodnumber = i;
           					break;
           				}
           			}
              		if(flag != 0){ //Good is found in list;             			
              			// Send message, if we are not the last bidder;
              			if(goods[goodnumber].getPrice() >= goodprice + epsilon && !(getLocalName().equals(goodbuer))){
              				ACLMessage answermsg = new ACLMessage(ACLMessage.INFORM);
              				answermsg.setLanguage(codec.getName());
              				answermsg.setOntology(ontology.getName());
              				ContentManager cm = getContentManager();
              				IBuyIt ibi = new IBuyIt();
              				ibi.setGoodName(goodname);
              				//New preposition;
              				if(goodbuer != null){
              					goodprice += epsilon; 
              				} else {
              					goodprice = goodprice;
              				}
             				ibi.setGoodPrice(goodprice);
              				Action a = new Action(msg.getSender(), ibi);
              				cm.fillContent(answermsg, a);
              				answermsg.addReceiver(msg.getSender());
              				//System.out.println(getLocalName() + " sends message to " + msg.getSender().getLocalName() + ": I buy " + goods[goodnumber].getName() + " for " + goodprice);
              				send(answermsg);
         				}
              		}
              			
           
						}
					catch(Exception ex) { ex.printStackTrace(); }
			}
		}
	}	
}
