package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.OrdersDAO;
import dao.ProductDAO;
import dao.UpdatedMarketsDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Connector;
import store.User;
import store.Product;
import store.ProductWithImage;
import store.RegularUser;
import store.SemiPremiumUser;

/** 
 * MainViewController controls the main screen of the application, in which the user sees the products of the particular market,
 * he can invest on them, add them to the order, save the market, delete saved markets, see the order, and access other market-related information.
 */
public class MainViewController {
	// data needed to connect to the database
	final static String DB_URL = "jdbc:mysql://www.papademas.net:3306/fp?autoReconnect=true&useSSL=false";
	final static String USERNAME = "dbfp";
	final static String PASSWORD = "510";

	@FXML private Button logoutButton;
	//@FXML private Label  sessionLabel;
	@FXML private ImageView image1;
	@FXML private ImageView image2;
	@FXML private ImageView image3;
	@FXML private Label pointsLabel;
	@FXML private Label marketTypeLabel;
	@FXML private Label userTypeLabel;
	@FXML private TableView<ProductWithImage> productsTableView;
	@FXML private Button orderButton;
	@FXML private Button saveMarketButton;
	@FXML private Button resetMarketButton;
	@FXML private ImageView ad_imageView;
	
	// List of products with the images loaded. This list will store the updated prices when the user invests in products.
	List<ProductWithImage> listUpdatedProductsWithImage = new ArrayList<>();
	// List with the user order. List of product ids
	List<Integer> orderList = new ArrayList<>();


	public void initialize() {}

	/**
	 * Initializes the session for a particular user that has logged in.
	 * Performs the main actions to set the window and the elements of the market.
	 * @param loginManager
	 * @param sessionID
	 * @param user
	 */
	public void initSessionID(final LoginManager loginManager, String sessionID, User user) {
		//sessionLabel.setText(sessionID);
		Double points = Math.round(user.getPoints()*100.0)/100.0;
		pointsLabel.setText(String.valueOf(points) + " points");
		userTypeLabel.setText(user.getUserType());
		if(user.getUserType().equals("Admin")){
			marketTypeLabel.setText("Type 1,2 and 3");
		}
		else{
			marketTypeLabel.setText("Type "+String.valueOf(user.getMarket().getIdMarket()));
		}
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("\nLOGOUT");
				for(ProductWithImage p :listUpdatedProductsWithImage){
					System.out.println(p);
				}
				loginManager.logout();
			}
		});
		
		saveMarketButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("\nSAVE MARKET");
				deleteSavedMarketFromDB(user);
				saveUpdatedMarketInDB(listUpdatedProductsWithImage,user);
			}
		});
		
		resetMarketButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("\nRESET MARKET");
				deleteSavedMarketFromDB(user);
			}
		});
		
		orderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				System.out.println("\nORDER");
				System.out.println("Product ids: ");
				for(int id :orderList){
					System.out.println(id+", ");
				}
				deletePreviousOrdersInDB(user);
				createOrderInDB(user);
				showOrder(user);
			}
		});

		// Check if the user has been already in the system and has a market saved
		Boolean marketSaved = checkIfMarketSaved(user);
		System.out.println("\nmarketSaved: "+marketSaved);
		
		List<Product> listOfProducts = null;
		
		if(marketSaved){ // saved market for the specific user is loaded
			listOfProducts = getSavedListOfProductsFromDB(user);
		}
		else{ // first time in the system. Default market is loaded
			//show products of the correspondent market for the user type
			listOfProducts = getListOfProductsFromDB(user);
		}
		
		System.out.println("\nLIST OF PRODUCTS");
		for(Product p:listOfProducts){
			System.out.println(p);
		}
		
		setProductsTableView(listOfProducts, user);
		
		showAdsIfAdvertisableUser(user);
	}
	
	/**
	 * Get from products' database the list of products that a particular user has access to.
	 * @param user
	 * @return
	 */
	private List<Product> getListOfProductsFromDB(User user){
		List<Product> list = new ArrayList<>();
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		ProductDAO pdao = new ProductDAO();

		// Extract data from result set
		try {
			int market_id = user.getMarket().getIdMarket();
			if(user.getUserType().equals("Admin"))
				market_id = 0;
			System.out.print("market_id: " + market_id);

			ResultSet rs = pdao.getMarketProducts(connection, "f_rois_products", market_id);
			while(rs.next()){
				//Retrieve by column name
				int idProduct = rs.getInt("idProduct");
				String productName = rs.getString("productName");
				double productPrice = rs.getDouble("productPrice");
				String productImage = rs.getString("productImage");
				String productType = rs.getString("productType");

				//Display values
				System.out.print("idProduct: " + String.valueOf(idProduct));
				System.out.println(", productName: " + productName);
				System.out.print("productPrice: " + String.valueOf(productPrice));
				System.out.println(", productImage: " + productImage);
				System.out.print("productType: " + productType);

				// Introduce product in the list
				list.add(new Product(idProduct,productName,productPrice,productImage,productType));
			}
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}
		
		// Close connection to database
		try {
			connection.close();
			System.out.println("\nConnection to db closed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return list;
	}

	/**
	 * Sets the TableView to show the products available in the market.
	 * @param list
	 * @param user
	 */
	private void setProductsTableView(List<Product> list, User user){
		//ObservableList<Product> data = FXCollections.observableArrayList(list);
		for(Product p:list){
			
			ImageView i = new ImageView(new Image("/images/"+p.getProductImage()));
			i.setFitHeight(40);
			i.setFitWidth(40);
			/*i.setOnMouseClicked(new EventHandler<MouseEvent>(){
	            @Override
	            public void handle(MouseEvent event) {
	                System.out.println("EUREKA!!");
	            }
	        });
	        */
			Button b = new Button();
			b.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
	                double n = 10;
	                updatePoints(n,user,p.getIdProduct(),list);
			    }
			});
			b.setGraphic(i);
			listUpdatedProductsWithImage.add(new ProductWithImage(p,i,b));
		}
		
		addTableColumns(listUpdatedProductsWithImage);

	}
	
	/**
	 * Updates the number of points on the tableView and also in the user points' counter.
	 * @param n
	 * @param user
	 * @param idProduct
	 * @param list
	 */
	private void updatePoints(double n, User user, int idProduct, List<Product> list){
		// Map of ProductWithImage. Key is the idProduct
		Map<Integer,ProductWithImage> mapProductsWithImage = new LinkedHashMap<>();
		for(ProductWithImage p:listUpdatedProductsWithImage){
			mapProductsWithImage.put(p.getIdProduct(), p);
		}
		int indexToChange = list.indexOf(mapProductsWithImage.get(idProduct).getProduct());

		// check that the user has enough points to spend
		if((user.getPoints()>0) && (user.getPoints() >= n)){
			if(!listUpdatedProductsWithImage.get(indexToChange).getReadyToBuy()){
				productsTableView.getColumns().clear();

				double unupdatedRemainingProductPrice = 0.0;
				if(mapProductsWithImage.containsKey(idProduct)){
					//System.out.println("indexToChange: "+indexToChange);
					// Update price of the product
					unupdatedRemainingProductPrice = listUpdatedProductsWithImage.get(indexToChange).getRemainingPrice();
					double newProductPrice = unupdatedRemainingProductPrice - new Double(n);
					// Store it again in the list.
					ProductWithImage updatedProduct = listUpdatedProductsWithImage.get(indexToChange);
					// Paint button in red
					updatedProduct.getButton().setStyle("-fx-base: #ff5400;");
					// In case that the price is totally paid with this last investment.
					if(newProductPrice <= 0.0){
						newProductPrice = 0.0;
						updatedProduct.setReadyToBuy(true);
						updatedProduct.getButton().setStyle("-fx-base: #b6e7c9;");
					}
					// Round double to show it on screen
					newProductPrice = Math.round(newProductPrice*100.0)/100.0;
					updatedProduct.setRemainingPrice(newProductPrice);
					listUpdatedProductsWithImage.set(indexToChange, updatedProduct);
				}

				//  Show again the table columns.
				addTableColumns(listUpdatedProductsWithImage);

				// Subtract points from user's account and show the new value
				double newPoints = user.getPoints();
				System.out.println("unupdatedRemainingProductPrice "+unupdatedRemainingProductPrice);
				System.out.println("new Double(n) "+ new Double(n));
				if(unupdatedRemainingProductPrice >= ((new Double(n)))){
					newPoints = user.getPoints() - new Double(n);
				}
				else{
					newPoints -= unupdatedRemainingProductPrice;
				}

				System.out.println("newPoints "+newPoints);
				user.setPoints(newPoints);
				
				Double points2 = Math.round(user.getPoints()*100.0)/100.0;
				pointsLabel.setText(String.valueOf(points2)+" points");
			}
			else{
				System.out.println("The product's remaining price is 0. The user can already buy the product.");
				showAlertBuyProduct(listUpdatedProductsWithImage.get(indexToChange).getProduct().getIdProduct());
			}

		}
		else{
			System.out.println("The user has no more points to spend");
			showAlertNoMorePoints();
		}
	}

	/*
	private void showAlert(){
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + "selection" + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
		    //do stuff
		} 
	}
	*/
	
	/**
	 * Shows an alert to the user when there are no more points to spend on his counter.
	 */
	private void showAlertNoMorePoints(){
		Alert alert = new Alert(AlertType.ERROR, "You need to stop buying, you don't have enough points.", ButtonType.OK);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
		} 
	}
	
	/**
	 * Informs the user that the product is already available to purchase, and therefore he can add it to the order.
	 * @param productID
	 */
	private void showAlertBuyProduct(int productID){
		Alert alert = new Alert(AlertType.INFORMATION, "The remaining price to pay for this product is 0.\n Do you want to add it to the order?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			// Add product id to the order
			orderList.add(productID);
			System.out.println("Added product with the ID "+productID+" to the order.");
		} 
	}
	
	/**
	 * Add table columns to tableView from a certain list of products.
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addTableColumns(List<ProductWithImage> list){
		ObservableList<ProductWithImage> data = FXCollections.observableArrayList(list);

		TableColumn productIDCol = new TableColumn("ID");
		productIDCol.setMinWidth(100);
		productIDCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));

		TableColumn productNameCol = new TableColumn("Product");
		productNameCol.setMinWidth(100);
		productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));

		TableColumn productPriceCol = new TableColumn("Price");
		productPriceCol.setMinWidth(100);
		productPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

		TableColumn remainingPriceCol = new TableColumn("Remaining");
		remainingPriceCol.setMinWidth(100);
		remainingPriceCol.setCellValueFactory(new PropertyValueFactory<>("remainingPrice"));

		TableColumn productTypeCol = new TableColumn("Type");
		productTypeCol.setMinWidth(100);
		productTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));

		TableColumn imageButtonCol = new TableColumn("Invest");
		imageButtonCol.setMinWidth(50);
		imageButtonCol.setCellValueFactory(new PropertyValueFactory<>("image"));

		imageButtonCol.setCellValueFactory(new PropertyValueFactory<>("button"));
		
		TableColumn readyToBuyCol = new TableColumn("Ready to buy");
		readyToBuyCol.setMinWidth(100);
		readyToBuyCol.setCellValueFactory(new PropertyValueFactory<>("readyToBuy"));

		productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		productsTableView.setItems(data);
		productsTableView.getColumns().addAll(productIDCol,productNameCol,productPriceCol,remainingPriceCol,productTypeCol,imageButtonCol,readyToBuyCol);
	}
	
	/**
	 * Delete previously saved market from the database of updated markets. Therefore, the next time that the user logs into the system, 
	 * the market will be restored and no changes or investments will be saved.
	 * @param user
	 */
	private void deleteSavedMarketFromDB(User user){
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		UpdatedMarketsDAO umdao = new UpdatedMarketsDAO();
		
		umdao.deleteSavedMarket(connection,"f_rois_updated_markets2",user.getUsername());
	}
	
	/**
	 * Saves the updated market in the database for updated markets, for future accesses of the same user.
	 * @param listUpdatedProductsWithImage
	 */
	private void saveUpdatedMarketInDB(List<ProductWithImage> listUpdatedProductsWithImage, User user) {
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		UpdatedMarketsDAO umdao = new UpdatedMarketsDAO();
		umdao.createTable(connection, "f_rois_updated_markets2");
		
		umdao.inserts(connection, user, "f_rois_updated_markets2", listUpdatedProductsWithImage);
		
		// Close connection to database
		try {
			connection.close();
			System.out.println("\nConnection to db closed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Checks in the databes for updated markets, if there is a market already saved for a particular user.
	 * @param user
	 * @return
	 */
	private boolean checkIfMarketSaved(User user){
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		UpdatedMarketsDAO umdao = new UpdatedMarketsDAO();
		
		Boolean bool = umdao.isThereAMarketSaved(connection, "f_rois_updated_markets2",user.getUsername());
		return bool;
	}
	
	/**
	 * Retrieve from the updated-markets' database the products from a market that a particular user previously saved, and their features.
	 * This information is stored in a map. Then, the products' database is queried and a list of the products in the market is retrieved,
	 * and its fields updated for the updated market.
	 * @param user
	 * @return
	 */
	private List<Product> getSavedListOfProductsFromDB(User user){
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		UpdatedMarketsDAO umdao = new UpdatedMarketsDAO();

		// Obtain map of product ids (Integer) and updated prices (Double) from saved market for the user.
		Map<Integer,Double> savedProductsMap = new LinkedHashMap<>();
		
		// Extract data from result set
		try {
			
			ResultSet rs = umdao.obtainMapOfSavedProducts(connection,"f_rois_updated_markets2",user.getUsername());

			while(rs.next()){
				//Retrieve by column name
				int productID = rs.getInt("productID");
				double priceToPay = rs.getDouble("priceToPay");

				//Display values
				System.out.print("productID: " + String.valueOf(productID));
				System.out.println(", priceToPay: " + String.valueOf(priceToPay));

				// Introduce product in the map
				savedProductsMap.put(productID, priceToPay);
			}
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}
		
		List<Product> list = new ArrayList<>();

		// daoModel object created in order to execute CRUD functions
		ProductDAO pdao = new ProductDAO();
		
		// Go through the map to get every Product in the list
		for(int prodID : savedProductsMap.keySet()){
			Double updatedPrice = savedProductsMap.get(prodID);
			// Extract data from result set
			try {
				ResultSet rs2 = pdao.obtainProductFromDB(connection,"f_rois_products",prodID);

				while(rs2.next()){
					//Retrieve by column name
					int productID = prodID;
					String productName = rs2.getString("productName");
					double productPrice = updatedPrice;
					String productImage = rs2.getString("productImage");
					String productType = rs2.getString("productType");

					//Display values
					System.out.print("productID: " + String.valueOf(productID));
					System.out.print(", productName: " + productName);
					System.out.print(", productPrice: " + String.valueOf(productPrice));
					System.out.print(", productImage: " + productImage);
					System.out.println(", productType: " + productType);

					// Introduce product in the map
					list.add(new Product(productID, productName, productPrice, productImage, productType));
					System.out.println("-----------------------------------");

				}
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}

		// Close connection to database
		try {
			connection.close();
			System.out.println("\nConnection to db closed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	
	/**
	 * Shows a tableView containing the current state of the order that a user has made.
	 * @param user
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showOrder(User user) {
		@SuppressWarnings({ "rawtypes" })

		//TABLE VIEW AND DATA
		TableView tableview = new TableView();
		ObservableList<ObservableList> data;

		//CONNECTION DATABASE
		Connection connection;
		data = FXCollections.observableArrayList();
		try{
			// create connection to the database using the access data
			Connector connector = new Connector();
			connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

			// daoModel object created in order to execute CRUD functions
			OrdersDAO ordao = new OrdersDAO();



			ResultSet rs = ordao.obtainOrderProductsFromDB(connection,"f_rois_orders2",user.getUsername());

			/**********************************
			 * TABLE COLUMN ADDED DYNAMICALLY *
			 **********************************/
			for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
				//We are using non property style for making dynamic table
				final int j = i;                
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
				col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
					public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
						return new SimpleStringProperty(param.getValue().get(j).toString());                        
					}                    
				});

				tableview.getColumns().addAll(col); 
				//System.out.println("Column ["+i+"] ");
			}

			/********************************
			 * Data added to ObservableList *
			 ********************************/
			while(rs.next()){
				//Iterate Row
				ObservableList<String> row = FXCollections.observableArrayList();
				for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
					//Iterate Column
					row.add(rs.getString(i));
				}
				//System.out.println("Row [1] added "+row );
				data.add(row);

			}

			//FINALLY ADDED TO TableView
			tableview.setItems(data);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error on Building Data");             
		}

		//Create Main Scene (pop up)
		tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		Scene scene = new Scene(tableview);        
		Stage stage = new Stage();
		stage.setWidth(500);

		stage.setScene(scene);

		stage.show();
	}
	
	/**
	 * Delete from the orders' database previous orders that the user made.
	 * @param user
	 */
	private void deletePreviousOrdersInDB(User user){
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		OrdersDAO ordao = new OrdersDAO();
		ordao.deletePreviousOrder(connection, "f_rois_orders2", user.getUsername());
	}

	/**
	 * Creates a new order in the orders' database.
	 * @param user
	 */
	private void createOrderInDB(User user){
		// create connection to the database using the access data
		Connector connector = new Connector();
		Connection connection = connector.createConnection(DB_URL, USERNAME, PASSWORD);

		// daoModel object created in order to execute CRUD functions
		OrdersDAO ordao = new OrdersDAO();
		ordao.createTable(connection, "f_rois_orders2");
		ordao.insertOrder(connection, "f_rois_orders2", user.getUsername(), orderList);
	}
	
	/**
	 * Shows advertisements in the case that the user is Advertisable. This depends on the type of user he is.
	 * @param u
	 */
	private void showAdsIfAdvertisableUser(User u){
		Image im = null;
		switch (u.getUserType()){
		case "Regular User":
			RegularUser reg = (RegularUser) u;
			im = reg.showAds();
			ad_imageView.setImage(im);
			break;
		case "Semipremium User":
			SemiPremiumUser sprem = (SemiPremiumUser) u;
			im = sprem.showAds();
			ad_imageView.setImage(im);
			break;
		default:
			ad_imageView.resize(0, 0);
			break;
		}
		ad_imageView.setScaleX(3);
		ad_imageView.setScaleY(3);
	}
}