# FinalProject510
The program is a store in which users can register, login, invest in products and buy them with points, and other store-related functionalities.

There are four types of users: 
- Regular User: can buy products from market type 1 and sees regular advertisements.
- Semipremium User: can buy products from market type 2 and sees semipremium advertisements.
- Premium User: can buy products from market type 3 and does not see any advertisements.
- Admin: can see all the types of markets together and does not see advertisements.

The program allows the user to register in the databases, login into the system by introducing the correct data,
see the market that corresponds to his type of user, invest points on products, add products to the order, and 
also the user can save the data from the investments he made on the market in order to recover it the next time
he logs into the system. Moreover, he can delete the saved data from a previous market, so when the user comes
back to the market it is restored.

The application makes use of 5 tables on the database, performing all the needed CRUD operations for their entities:
- f_rois_users4: stores all the information from the users previously registered in the system, including their username and password.
- f_rois_markets: stores the different types of markets that are available in the system, and their features.
- f_rois_updated_markets2: this table is meant to store the data saved from a market, when a user decides to save the current version of the market for future visits to the system.
- f_rois_products: table with all the available products in the system, including fields such as the product id and the type of market which they are attached to.
- f_rois_orders2: each of the registers on this table shows the orders made and the user that made them. Each includes a list of the product ids that are in the order.

- Programmer: Francisco Rois Siso
- Date: 04/27/2017 
- Source File Name: Login.java
