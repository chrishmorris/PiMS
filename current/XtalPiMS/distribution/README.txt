Release Notes for xtalPIMS v1.2
===========================
xtalPiMS has very similar structure as PiMS. The configuration of xtalPiMS is mostly same as PiMS
xtalPiMS is freely available for academic use. 

There is a demonstration at:
    http://cselnx4.dl.ac.uk:8080/xtalpims
Please use username "demo", password "demo".
Please send comments to pims-defects@dl.ac.uk.

xtalPIMS v1.2 is packaged as a WAR file. You can download it from
    http://www.pims-lims.org/download/xtalPiMS_V1_2/xtalpims.war
The instructions below assume that you have copied it to /root/xtalpims.war,
for example by
    wget -c /root/pims.war http://www.pims-lims.org/download/xtalPiMS_V1_2/xtalpims.war
    
The highlighted improvements in this release are:
- compatible with pims3.3
- improved print layout of trialdrop view
- added salt condition info in trialdrop view
- improved plate experiment view
- reduced the amount of data displayed (last 7 days only) in homepage

If you are existing xtalPiMS user, you only need to download and replace xtalpims.war.
(Please remember to remove xtalPiMS directory from {TomcatHome}\work\Catalina\localhost and {TomcatHome}\webapps to ensure the new war file will be used)

PRECONDITIONS
=============

	You will need to install the following before you can run xtalPIMS:
	- Java 1.5 JDK
	- Postgresql 8.1 or Oracle 10g or later
	- Tomcat 5.5
If you have PiMS installed, this preconditions already have been met.

Take special note of your superuser name and password 
during installation of postgreSQL, it will be needed during the xtalPIMS install.
The instructions below assume that the superuser is called "postgres".


	
DEPLOYING IN TOMCAT
===================

    There are several ways to deploy a web app in Tomcat. The most predictable 
    is to create a file:
        $CATALINA_HOME/conf/Catalina/localhost/xtalpims.xml
    with contents like in 
        http://www.pims-lims.org/download/xtalPiMS_V1_2/context.xml.example

    The file should have
        <Context docBase="/root/xtalpims.war" path="/xtalpims">
        </Context>
    See below for the <Environment> elements that must appear in this <Context>.

    You are recommended to run Tomcat with the following Java options:
        -server -XX:MaxPermSize=512m -XX:+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled -enableassertions
    If you are running xtalPiMs on a server with no graphics card, you also need:
        -Djava.awt.headless=true
    On Linux, you set these by 
        export JAVA_OPTS="-server -XX:MaxPermSize=512m -XX:+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled -enableassertions -Djava.awt.headless=true"
   


   
INSTALLATION OF THE xtalPIMS DATABASE
=================================
If you have pims installed, you can use your pims database as your xtalpims database

If you do not have pims installed, then you need to install a database as explained here.
  This release is tested only Postgres and Oracle.
  These instructions assume you have already installed your database server but
have not created the xtalpims database or database users.

  Step 1. Download the database setup files for Postgresql:
   https://cselnx4.dl.ac.uk/download/V3_2_0/pims-db-withrefdata.sql

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
	    psql -d pims -U postgres -f pims-db-withxtalrefdata.sql

	Alternatively,
	
	    psql -d pims -U postgres -f install-example.sql
	    
	provides realistic test data. Thanks to the SSPF and YSBL for this data.


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
  		
	 Then stop and restart Tomcat.
	 
*If you want to use the example database, you should use following file with above process:
https://cselnx4.dl.ac.uk/download/V3_2_0/install-example.sql

CONFIGURATION OF OTHER xtalPIMS PARAMETERS
======================================
    Edit your Tomcat Context configuration file as created in "DEPLOYING IN TOMCAT":
     	$CATALINA_HOME/conf/Catalina/localhost/xtalpims.xml
    You should add some more Environments in this file for some xtalPIMS PARAMETERS:
    eg:
  
  	  <Environment name="uploadDirectory" 
  			value="/var/pims/xtalpimsUploads" type="java.lang.String" />
  			  	  

	The "uploadDirectory" is the directory to store the uploaded files by xtalPiMS user.
		If you are installing xtalPIMS on WIndows, create this directory. 
		If you are installing xtalPIMS on Linux:
        	mkdir /var/pims
        	mkdir /var/pims/xtalpimsUploads
        	chown tomcat /var/pims/xtalpimsUploads
		and then edit this line to specify the location of the directory.
		
		
		
Other Configuration
================================
see pims' README.txt 


DEFECTS
=======

	Please report defects to pims-defects@dl.ac.uk.

CREATE PROPERTIES FILE
========================
You need to provide the database connection information, in a file called Properties. 
There is an example at:
    http://www.pims-lims.org/download/xtalPiMS_V1_0/Properties.sample
You can do this by:

cat > Properties
project = chemcomp
db.className = org.postgresql.Driver
db.url = jdbc:postgresql://localhost/pims
db.name = pims
db.username = postgres
db.password =
CONTROL-D





