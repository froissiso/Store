package store;

/**
 * Class Product.
 * 
 * @author Francisco Rois Siso
 *
 */
public class Product {
	private int idProduct;
	private String productName;
	private double productPrice;
	private String productImage;
	private String productType;
	
	public Product(){
	}

	public Product(int idProduct, String productName, double productPrice, String productImage, String productType) {
		this.idProduct = idProduct;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productImage = productImage;
		this.productType = productType;
	}


	public int getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	@Override
	public String toString() {
		return "Product [idProduct=" + idProduct + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productImage=" + productImage + ", productType=" + productType + "]";
	}
}
	