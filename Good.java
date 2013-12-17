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
public class Good {
	private String name;
	private int price;
	
	public Good(String name, int price){
		this.name = name;
		this.price = price;
	}
	
	public int getPrice(){
		return price;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setResPrice(int price){
		this.price = price;
	}
}
