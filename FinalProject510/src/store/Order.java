package store;

/**
 * Class Order.
 * 
 * @author Francisco Rois Siso
 *
 */
public class Order {
	int idOrder;
	String orderStatus;
	public Order(){
		this.idOrder = 1;
	}
	
	public Order(int idOrder){
		this.idOrder = idOrder;
	}
	
	public Order(int idOrder, String orderStatus) {
		super();
		this.idOrder = idOrder;
		this.orderStatus = orderStatus;
	}
	public int getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
