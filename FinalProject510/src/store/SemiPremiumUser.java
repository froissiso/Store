package store;

import java.util.List;

import javafx.scene.image.Image;

/**
 * Class SemiPremiumUser. Implements Advertisable, therefore the user sees ads.
 * 
 * @author Francisco Rois Siso
 *
 */
public class SemiPremiumUser extends User implements Advertisable {

	
	public SemiPremiumUser() {
		super();
	}

	public SemiPremiumUser(String username, String password, String userType, String name, String address, String email,
			String phoneNumber, double points, List<PaymentMethod> paymentMethods, Market market) {
		super(username, password, userType, name, address, email, phoneNumber, points, paymentMethods, market);
	}

	@Override
	public Image showAds() {
		return new Image("/images/banner_ad_semipremium2.gif");
	}
}
