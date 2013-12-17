import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class EnglishAuction extends Agent implements AuctionVocabulary {
	AMSAgentDescription [] agents = null; //All Bidders;
	Good [] goods = new Good[200]; //List of goods with reservation prices;
	int goodsnumber = 0; //Number of goods (starting from zero);
	
	int state = 1; //State of an Agent;
				   //1 - Choosing a Good;
				   //2 - Sending message to bidders;
				   //3 - Waiting for answers ;
	
	int currentgoodnumber = -1; //Number of current good on the auction;
	int currentprice; //The price of the current good;
	AID currentgoodbuyer = null; //Who buys current good for current price;
		
	private Codec codec = new SLCodec();
	private Ontology ontology = AuctionOnt.getInstance();
	
	//Searching for Bidders - result in agents[];
	private void findBidders(){
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults (new Long(-1));
			agents = AMSService.search( this, new AMSAgentDescription(), c );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(AID agent, int type) {
		//type - reserved;
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		try {
			ContentManager cm = getContentManager();
			NewGood newgood = new NewGood();
			newgood.setGoodName(goods[currentgoodnumber].getName());
			newgood.setGoodPrice(currentprice);
			if (currentgoodbuyer != null ){
				newgood.setAuthor(currentgoodbuyer.getLocalName());
			}
			Action a = new Action(agent, newgood);
			cm.fillContent(msg, a);
			msg.addReceiver(agent);
			if(currentgoodbuyer != null){
				//System.out.println(getLocalName() + " sends message to " + agent.getLocalName() + ": '"+ goods[currentgoodnumber].getName() +" for " + currentprice + " to " + currentgoodbuyer.getLocalName() + "'");
			} else {
				//System.out.println(getLocalName() + " sends message to " + agent.getLocalName() + ": '"+ goods[currentgoodnumber].getName() +" for starting price " + currentprice + "'");
			}
			send(msg);
		} catch (OntologyException ex) {
			ex.printStackTrace();
		} catch (CodecException ce) {
			ce.printStackTrace();
		}
	}
	
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
		
		addBehaviour(new EnglishAuctionBehaviour(this));
	}
	
	class EnglishAuctionBehaviour extends CyclicBehaviour{
		
		public EnglishAuctionBehaviour(Agent a){
			super(a);
		}
		
		public void action(){
			//Finding bidders;
			findBidders();
			//Doing something respectively to state;
			switch (state) {
				case 1: { //Trading new good; 
					if(currentgoodnumber < goodsnumber){
						currentgoodbuyer = null;
						currentgoodnumber++;
						currentprice = goods[currentgoodnumber].getPrice();
						System.out.println(getLocalName() + ": New good - " + goods[currentgoodnumber].getName() + " - on the auction");
						state = 2; //Switching to sending messageing state;
					} else {
						System.out.println(getLocalName() + ": Auction is finished");
						System.exit(0);
					}
					break;
				}
				case 2: { //Sending good+price;
					
					if(currentgoodbuyer != null){
						System.out.println(getLocalName() + ": " + goods[currentgoodnumber].getName() +" for " + currentprice + " to " + currentgoodbuyer.getLocalName());
					} else {
						System.out.println(getLocalName() + ": " + goods[currentgoodnumber].getName() +" for starting price " + currentprice);
					}
					
					for(int i=0; i < agents.length; i++){
					 	if(!( agents[i].getName().getLocalName().equals("ams") || agents[i].getName().getLocalName().equals("df") || agents[i].getName().getLocalName().equals("RMA") || agents[i].getName().getLocalName().equals(getLocalName()))){
					 		sendMessage(agents[i].getName(), 0);
					 	} 
					}
					state = 3; //Swithching to waiting-answers state;
					 break;
				}
				case 3: { //Waiting for answers;
										
					ACLMessage bid = null;
					int i = 0;
					ACLMessage mainbid = null;
					do {
						 if (i == 1){
						 	mainbid = bid; //Saving first bid; 
						 }
						 bid = blockingReceive(3000);
						 i++;
					}  while(bid != null); //And clearing quee of messages;
					
						
					
					if(i > 1) { //Some new prepositions are, main in mainbid;
						try {
							ContentElement content = getContentManager().extractContent(mainbid);
							Concept action = ((Action)content).getAction();
							currentprice = ((IBuyIt)action).getGoodPrice();
							System.out.println(mainbid.getSender().getLocalName() + ": I buy " + goods[currentgoodnumber].getName() +" for " + currentprice);
							currentgoodbuyer = mainbid.getSender();
						} catch (Exception e){
							e.printStackTrace();
						}
						state = 2;
					} else { //No new preposition
						if(currentgoodbuyer == null) {
							System.out.println(getLocalName()+": Nobody wants to buy " + goods[currentgoodnumber].getName() + " for starting price " + currentprice);
							state = 1;
						} else { //
							System.out.println(getLocalName()+": " + goods[currentgoodnumber].getName() + " is selled to " + currentgoodbuyer.getLocalName() + " for " + currentprice);
							state = 1;
							
						}
					}
					
				}										
			}
		}
	}	
}
