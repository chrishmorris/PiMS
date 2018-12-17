Release Notes for xtalPIMS v1.3
===========================

xtalPiMS 1.3
========

xtalPiMS 1.3 ****
For more details see features.txt.

There is a demonstration at 
    http://cselnx4.dl.ac.uk:8080/xtalpims/Login
Please use username "demo", password "demo".

For further information please see
    https://www.pims-lims.org
and for further help in installing xtalPiMS see
    http://www.pims-lims.org/download/ 

xtalPIMS v1.3 is packaged as a WAR file. It is in the file:
    http://pims.structuralbiology.eu/pims.zip
The instructions below assume that you have copied it to /root/xtalpims.war.
       

PRECONDITIONS
=============

	You will need to install the following before you can run PIMS:
	- OpenJDK 6, or Java 1.6 JDK.
	- Postgresql 8.1 or later
	- Tomcat 6.0
	
Take special note of your superuser name and password 
during installation of postgreSQL, it will be needed during the xtalPIMS install.
The instructions below assume that the superuser is called "postgres".

	
DEPLOYING IN TOMCAT
===================

    There are several ways to deploy a web app in Tomcat. The most predictable 
    is to create a file:
        $CATALINA_HOME/conf/Catalina/localhost/xtalpims.xml
    with contents like in 
        http://www.pims-lims.org/download//current/context.xml.example

    The file should have
        <Context docBase="/root/xtalpims.war" path="/xtalpims">
        </Context>
    See below for the <Environment> elements that must appear in this <Context>.

    You are recommended to run Tomcat with the following Java options:
        -server -XX:MaxPermSize=512m -XX:+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled -enableassertions
    If you are running PiMs on a server with no graphics card, you also need:
        -Djava.awt.headless=true
    On Linux, you set these by 
        export JAVA_OPTS="-server -XX:MaxPermSize=512m -XX:+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled -enableassertions -Djava.awt.headless=true"
    
    If you have a server class machine, or are upgrading from a previous release of PiMS,
    please see Tomcat.txt for some other problems you may encounter.

	Developers should see Eclipse.txt for how to install PiMS as a development copy.


INSTALLATION OF THE xtalPIMS DATABASE
=================================
  
  If you are installing xtalPiMS for the first time, then you need to install a database as explained here.
These instructions assume you have already installed your database server but
have not created the xtalpims database or database users.

  Step 1. Download the database setup files for Postgresql:
   http://www.pims-lims.org/download/V3_2_0/pims-db-withxtalrefdata.sql   

  Step 2. Create the database and database users:
  
	    createdb -U postgres --encoding UTF-8 pims
	    createuser -U postgres -P pimsview
	    createuser -U postgres -P pimsupdate
	    createuser -U postgres -P pimsadmin
	    
    If you used a postgres superuser name other than 'postgres', use your
    postgres superuser name in place of 'postgres' in these commands.
    You may need to add an entry to pg_hba.conf. 
    See file postgresql.txt for details.
    
    You do not have to use 'pims' as the name of the database in the
    createdb command but if you do not, the subsequent steps will need
    adapting accordingly.
    
    All three users only need the default privileges - answer no the three
    questions asked by createuser (superuser, create databases and create
    roles).
    
    Keep a note of the passwords you specify - in particular you will need
    the password for pimsupdate in Step 4.
 	
  Step 3. Install the data model and reference data:
  **** TODO check this
	    psql -d pims -U postgres -f pims-database.sql



  Step 4. Configure xtalPIMS for your database connection.
  
    Edit your Tomcat Context configuration file as created in "DEPLOYING IN TOMCAT":
     	$CATALINA_HOME/conf/Catalina/localhost/xtalpims.xml
    You should add Environments in this file for your database Connection.
    eg:
     <Environment name="db.className" 
  		value="org.postgresql.Driver" type="java.lang.String" />
  	 <Environment name="db.url" 
		value="jdbc:postgresql://localhost/pims"  type="java.lang.String" />
  	 <Environment name="db.username" 
  		value="pimsupdate" type="java.lang.String" />
  	 <Environment name="db.password" 
  		value="abcd" type="java.lang.String"/>
   
   Your xtalPiMS installation includes PiMS, 
   which you can use to manage your DNA processing, expression, and purification.
   If you want only xtalPiMS then replace:
        <Environment name="customization.perspective" 
        value="standard,xtal,OPPF,CSIC" type="java.lang.String"/>
   with:
  		<Environment name="customization.perspective" 
        value="xtal,standard" type="java.lang.String"/>
  		
	 Then stop and restart Tomcat.
  			 			

CONFIGURATION OF OTHER xtalPIMS PARAMETERS
======================================
    Edit your Tomcat Context configuration file as created in "DEPLOYING IN TOMCAT":
     	$CATALINA_HOME/conf/Catalina/localhost/xtalpims.xml
    You should add some more Environments in this file for some xtalPIMS PARAMETERS:
    eg:
  
  	  <Environment name="uploadDirectory" 
  			value="/var/pims/pimsUploads" type="java.lang.String" />
  			  	  
  	So that PiMS users can upload files, you should
  	    mkdir /var/pims
  	    mkdir /var/pims/pimsUploads
  	    chown tomcat /var/pims/pimsUploads
  			
    
	
	The "uploadDirectory" is the directory to store the uploaded files.
		If you are installing PIMS on WIndows, create this directory. 
		If you are installing PIMS on Linux:
        	mkdir /var/pims
        	mkdir /var/pims/pimsUploads
        	chown tomcat /var/pims/pimsUploads
		and then edit this line to specify the location of the directory.
		
		
		
Other Configuration
================================
    For instructions on how to set up your userids, see:
        http://www.pims-lims.org/download/Installation.txt


DEFECTS
=======

	Please report defects to pims-defects@dl.ac.uk.


    



