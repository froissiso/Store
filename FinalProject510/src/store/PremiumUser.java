package store;

import java.util.List;

/**
 * Class PremiumUser. Extends User.
 * 
 * @author Francisco Rois Siso
 *
 */
public class PremiumUser extends User {
	public PremiumUser() {
		super();
	}

	public PremiumUser(String username, String password, String userType, String name, String address, String email,
			String phoneNumber, double points, List<PaymentMethod> paymentMethods, Market market) {
		super(username, password, userType, name, address, email, phoneNumber, points, paymentMethods, market);
	}
}
