package store;

import java.util.List;

import javafx.scene.image.Image;

/**
 * Class RegularUser. Implements Advertisable, therefore the user sees ads.
 * 
 * @author Francisco Rois Siso
 *
 */
public class RegularUser extends User implements Advertisable {

	public RegularUser() {
		super();
	}

	public RegularUser(String username, String password, String userType, String name, String address, String email,
			String phoneNumber, int points, List<PaymentMethod> paymentMethods, Market market) {
		super(username, password, userType, name, address, email, phoneNumber, points, paymentMethods, market);
	}

	@Override
	public Image showAds() {
		return new Image("/images/banner_ad_regular2.jpg");
	}

}
