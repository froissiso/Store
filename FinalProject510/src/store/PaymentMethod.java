package store;

/**
 * Class PaymentMethod.
 * 
 * @author Francisco Rois Siso
 *
 */
public class PaymentMethod {
	private String name,data,type;
	private boolean verified;
	
	public PaymentMethod(){}

	public PaymentMethod(String name, String data, String type, boolean verified) {
		super();
		this.name = name;
		this.data = data;
		this.type = type;
		this.verified = verified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	
	public String toString(){
		return type;
	}
	
}
