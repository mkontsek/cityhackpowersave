Get started with cityhackpowersave
-------------------------------------
This is a starter application for Java with Cloudant NoSQL DB service.

The sample is a Favorites Organizer application, that allows users to organize and manage their favorites and supports different types in each category. This sample demonstrates how to access the Cloudant NoSQL DB service that binds to the application, using Cloudant Java APIs.

1. [Install the cf command-line tool](https://www.ng.bluemix.net/docs/#starters/BuildingWeb.html#install_cf).
2. [Download the starter application package](https://console-classic.ng.bluemix.net:443/rest/../rest/apps/b36cbc5f-1821-4cfa-b890-ed25f12001b6/starter-download).
3. Extract the package and 'cd' to it.
4. Connect to Bluemix:

		cf api https://api.ng.bluemix.net

5. Log into Bluemix:

		cf login -u martin.kontsek@sk.ibm.com
		cf target -o martin.kontsek@sk.ibm.com -s dev
		
6. Compile the JAVA code and generate the war package using ant.
7. Deploy your app:

		cf push cityhackpowersave -p cityhackpowersave.war -m 512M

8. Access your app: [cityhackpowersave.mybluemix.net](http://cityhackpowersave.mybluemix.net)
