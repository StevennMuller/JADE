import jade.content.AgentAction;

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

public class IBuyIt implements AgentAction {
	private String goodname;
	private int goodprice;
	
	public String getGoodName(){
		return goodname;
	}
	
	public void setGoodName(String goodname){
		this.goodname = goodname;
	}
	
	public int getGoodPrice() {
		return this.goodprice;
	}
	public void setGoodPrice(int goodprice) {
		this.goodprice = goodprice;
	}
}
