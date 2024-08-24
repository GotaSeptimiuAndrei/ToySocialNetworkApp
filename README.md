# <b>ChitChatChain - Toy Social Network</b>

Welcome to the Toy Social Network App! This project is a demonstration of a social network application developed to practice software development skills, including Object-Oriented Programming (OOP), software architecture, and user interface design. The application provides a platform where users can connect, interact, and communicate with each other in a secure and user-friendly environment.

# <b>Table of Contents</b>
1. Introduction
2. Features
3. Technologies used
4. Screenshots
5. Getting Started

<h2>Introduction</h2>
The Toy Social Network App is designed to mimic the core functionalities of a social media platform. It allows users to register, log in, add friends, send messages, and customize their profiles.

<h2>Features</h2>
<ul>
	<li><strong>User Registration and Login: </strong> Users can register using an email and password, and securely log in to their accounts.</li>
  <li><strong>Profile Customization: </strong> Users can customize their profile by adding a profile picture and updating their description.</li>
  <li><strong>Friend Management: </strong> Users can search for friends, send friend requests, accept or decline requests.</li>
  <li><strong>Real-Time Notifications: </strong> Users receive notifications in real time for friend requests and message updates.</li>
  <li><strong>Messaging System: </strong> Users can send messages to their friends, with real-time updates on message status (sent, delivered, seen).</li>
  <li><strong>Security: </strong> All passwords are encrypted using AES-CBC encryption with a 256-bit key to ensure user data privacy and security.</li>
</ul>

<h2>Screenshots</h2>
<ul>
  <li>Login Page</li>
	<p align="center"> <img src="https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp/blob/master/project_screenshots/login_page.jpg" height="500"/> </p>
  <li>Register Page</li>
	<p align="center"> <img src="https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp/blob/master/project_screenshots/register_page.jpg" height="500"/> </p>
  <li>Profile Page</li>
	<p align="center"> <img src="https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp/blob/master/project_screenshots/profile.jpg" height="500"/> </p>
  <li>Search Page - user can search for other users using their username or their names</li>
	<p align="center"> <img src="https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp/blob/master/project_screenshots/search_page.jpg" height="500"/> </p>
  <li>Messaging Page - the user can search for friends and talk to them</li>
	<p align="center"> <img src="https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp/blob/master/project_screenshots/chat.jpg" height="500"/> </p>
</ul>

<h2>Technologies Used</h2>
<ul>
  <li><strong>Java: </strong> Core programming language used for application development.</li>
  <li><strong>JavaFX: </strong> Used for building the graphical user interface.</li>
  <li><strong>PostgreSQL: </strong> Database management system used for data persistence.</li>
  <li><strong>Maven: </strong> Build automation tool used to manage project dependencies.</li>
</ul>

## Getting Started
To get a copy of the Toy Social Network App up and running on your local machine for development and testing purposes, follow these steps:
1. Clone the Repository:
	```
	git clone https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp.git
	```

2. Configure the Database Connection:
	- Navigate to the [config.properties](https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp/blob/master/src/main/java/java_projects/demo/config/config.properties) file in the project directory
	- Edit the file to match your database setup:
		```
		salt=BkDOU6uSJrc5XuRu9OJBpJCHe7JCE7ZQ4bZPEke3pBEo5vVHgNF50i7HMIlIQjsVyb40Hh4BKInqs1AXnfPuM1cAZLEJDRpR8imR
		key=my_very_strong_password
		logoPath=/java_projects/demo/pictures/logo.png
		incomingMessageColor=#87CEFA
		sendingMessageColor=#E7FEFF
		seenMessage=#00FF7F
		username=your_postgres_username
		password=your_postgres_password
		databaseUrl=jdbc:postgresql://localhost:5432/YourDatabaseName
		```
	- Replace `your_postgres_username`, `your_postgres_password`, and `YourDatabaseName` with your actual PostgreSQL credentials and desired database name

3. Run SQL Setup:
	- Before running the application, you need to set up the database schema
	- Run the SQL script provided in the project, you can find the SQL file [here](https://github.com/GotaSeptimiuAndrei/ToySocialNetworkApp/blob/master/queries.sql)
	- Execute this script in your PostgreSQL database. This can typically be done using a command-line tool like `psql` or a GUI tool like pgAdmin

4. Use Maven to install all necessary dependencies:
	```
   	mvn clean install
  	 ```
5. Run the Application:
   	```
   	mvn javafx:run
  	```
