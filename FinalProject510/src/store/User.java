package store;

import java.util.List;
import java.util.Map;

/**
 * Class User.
 * 
 * @author Francisco Rois Siso
 *
 */
public abstract class User {
	
	private String username, password, userType, name, address, email, phoneNumber;
	private double points;
	private Market market;
	private Map<Integer,Product> investments;
	private List<PaymentMethod> paymentMethods;
	private boolean admin;

	public User(){}

	public User(String username, String password, String userType, String name, String address,
			String email, String phoneNumber, double points, List<PaymentMethod> paymentMethods, 
			Market market) {
		this.points = points;
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.name = name;
		this.address = address;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.market = market;
		this.paymentMethods = paymentMethods;
	}

	/*
	 * Getters and setters
	 * 
	 */
	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public Map<Integer, Product> getInvestments() {
		return investments;
	}

	public void setInvestments(Map<Integer, Product> investments) {
		this.investments = investments;
	}

	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public void addPaymentMethod(PaymentMethod p){}
	public void removePaymentMethod(PaymentMethod p){}

}
