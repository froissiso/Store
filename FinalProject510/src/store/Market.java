package store;

import java.util.Map;

/**
 * Class Market
 * @author Francisco Rois Siso
 *
 */
public class Market {
	int idMarket;
	public int getIdMarket() {
		return idMarket;
	}
	public void setIdMarket(int idMarket) {
		this.idMarket = idMarket;
	}
	Map<Integer,Product> productsMap;
	
	public Market(){
		this.idMarket = 1;
	}
	public Market(int idMarket){
		this.idMarket = idMarket;
	}
	public Market(int idMarket, Map<Integer,Product> productsMap){
		this.idMarket = idMarket;
		this.productsMap = productsMap;
	}
	
	public Map<Integer, Product> getProductsMap() {
		return productsMap;
	}
	public void setProductsMap(Map<Integer, Product> productsMap) {
		this.productsMap = productsMap;
	}
}
