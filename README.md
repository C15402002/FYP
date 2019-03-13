# Final Year Project DT282 Computer Science International - Oui Restaurant Application
This is an android application for accessibilty in restaurants. This project contains two applications for restaurants and customers. The main objective of this project is to assist users when ordering dishes at a restaurant without the need of waiting for a waiter to take the order. This application can help users with social anxiety disorder to overcome their fears of eating at a public restaurants. With the implementation of QR codes we are able to retrieve menu items from a real-time database backend. Modern restaurants that are tech savy and want to find a new way to perform an efficient enviornment with the grow of their business can find this application to be of great interest.

## Client Side Application
Customers who uses this application are able to scan a QR code of a particular restaurant and order the dishes that they wont from that restaurant. They can see their order history for each restaurant they have ordered. Users can review dish items and see other customer reviews. Once customers have made their cart order an alert dialog will show and ask them to enter a table number and extra comments that they want to give to the kitchen staff. Payments can be made using paypal or cash, if the user chose to pay in paypal the application will bring the user to the paypal card payment activity. If the user chose to pay in cash a notification will be sent to the restaurant staff and a member of the staff will need to take cash of the customer and set their order status to paid. This will ensure that all orders placed have been paid before it gets sent to the kitchen. Users can cancel their order only if the status has just been sent to the kitchen, once a staff member changes the status from sent to kitchen to cooking the user will not be allowed to cancel.


## Server Side Application
Restaurant managers are able to control their business by using this application. They can manage their menu directly from this application which will be updated in real-time sequently with the client application. The manager can perform basic CRUD operations of the menu. They can also manage their incoming orders as using firebase messaging and retro2fit client we are able to send notificaiton from the client app to the server app when an order is placed. The status of the order can be changed from sent to kitchen, cooking and served. Each status change will send the customer a notification, notifying them of their order. Staff can also see order details of each order like table no, comments and quantity. 

## Job of The Administrator
Administrator helps the restaurant manager to set up their two applications. They manages mostly the backend side of this project. Admins will generate a QR code for the restaurant using the ID, they will set up staffIDs and future version updates.

## Developed with:
  - Front end: Android Studios XML
  - Back end: Firebase, SQLite Browser
  - Notification: Firebase Messaging, Firebase Services
  - Authentication: Firebase Auth for sign up
  - Paypal Sandbox, cash payments
  - ZXing Barcode Scanner 
  - Retro2fit Client, REST Api
  
  

 ## Demo
 #### OUI QR Code
 
