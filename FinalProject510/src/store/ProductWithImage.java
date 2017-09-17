package store;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Class to encapsulate a product, an ImageView of its image and a button for displaying purposes.
 * @author Francisco Rois Siso
 *
 */
public class ProductWithImage extends Product{
	
	ImageView image;
	Button button;
	Product product;
	Double remainingPrice;
	Boolean readyToBuy;

	public ProductWithImage() {
		super();
	}

	public ProductWithImage(Product p, ImageView i,Button b) {
		super(p.getIdProduct(), p.getProductName(), p.getProductPrice(), p.getProductImage(), p.getProductType());
		
		this.image = i;
		this.button = b;
		this.product = p;
		this.remainingPrice = p.getProductPrice();
		this.readyToBuy = false;
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}
	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getRemainingPrice() {
		return remainingPrice;
	}

	public void setRemainingPrice(Double remainingPrice) {
		this.remainingPrice = remainingPrice;
	}

	public Boolean getReadyToBuy() {
		return readyToBuy;
	}

	public void setReadyToBuy(Boolean readyToBuy) {
		this.readyToBuy = readyToBuy;
	}

	@Override
	public String toString() {
		return "ProductWithImage [image=" + image + ", button=" + button + ", product=" + product + ", remainingPrice="
				+ remainingPrice + ", readyToBuy=" + readyToBuy + "]";
	}	
}