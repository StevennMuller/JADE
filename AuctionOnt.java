/*
 * Created on 13.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;


public class AuctionOnt extends Ontology implements AuctionVocabulary {
	   
	public static final String ONTOLOGY_NAME = "Auction-Ont";
	private static Ontology instance = new AuctionOnt();

	public static Ontology getInstance() 
	{ 
		return instance; 
	}	
   
   private AuctionOnt() {
   		
   	super(ONTOLOGY_NAME, BasicOntology.getInstance());
   		try {
   			AgentActionSchema as = new AgentActionSchema(NEW_GOOD);
   			add(as, NewGood.class);
   			as.add(NEW_GOOD_GOOD_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
   			as.add(NEW_GOOD_GOOD_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
   			as.add(NEW_GOOD_GOOD_AUTHOR, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
   			as.add(NEW_GOOD_GOOD_FLAG, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
   			
   			
   			as = new AgentActionSchema(NEW_PRICE);
   			add(as, NewPrice.class);
   			as.add(NEW_PRICE_GOOD_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
   			as.add(NEW_PRICE_GOOD_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
   			
   			
   			
   			as = new AgentActionSchema(I_BUY_IT);
   			add(as, IBuyIt.class);
   			as.add(I_BUY_IT_GOOD_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
   			as.add(I_BUY_IT_GOOD_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
   			
   			
   			as = new AgentActionSchema(YOU_GET_IT);
   			add(as, YouGetIt.class);
   			as.add(YOU_GET_IT_GOOD_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
   			as.add(YOU_GET_IT_GOOD_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
   			
   			
   			as = new AgentActionSchema(SELLED);
   			add(as, Selled.class);
   			
   			as = new AgentActionSchema(YOU_GET_IT);
   			add(as, YouGetIt.class);
   			as.add(YOU_GET_IT_GOOD_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
   			as.add(YOU_GET_IT_GOOD_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
   			
   			
   			} catch (Exception e){
   				e.printStackTrace();
   			}
   }
} 
