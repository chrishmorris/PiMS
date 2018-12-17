Release Notes for PIMS v4.4
=============================


PiMS is now developed in partnership between STFC, Emerald Bio, and other academic contributors (currently Oxford University and the University of Oulu). More details of this partnership are here:
    http://pims.structuralbiology.eu/docs/PiMS%20Press%20Release%20Final.pdf 

As a result of this collaboration, the Instruct hosted PiMS service has been updated to version 4.4. This version is now available for local installations. 

The license agreement for PiMS has changed. Academic groups are asked to sign the appropriate licence: 
    http://pims.structuralbiology.eu/docs/community model pimmsacademicuseronly FINAL.docx
and return a copy to:
    Chris Morris, Daresbury Laboratory,  Daresbury,  Warrington,  UK,  WA4 4AD
    Fax: +44 (0)1925 603634
Then you will receive a link to download the new pims.zip. It is used in the upgrade procedure described below.

There is a separate contributor licence for academic groups wanting access to the PiMS source code. Please contact:
   pims@dl.ac.uk

PiMSPro(TM) is available for beta test by industrial users. Please contact pnollert@embios.com.

PiMS 4.4
==========

PiMS 4.4 requires Java1.6 or later. It will not run with Java1.5.

PiMS 4.4 runs in Tomcat7 or Tomcat6. PiMS5.0 will not run in Tomcat6. 

PiMS 4.4 includes the following changes:

* A new, more modern look. Thanks to Emerald Bio for these changes.
* Support for recording crystal treatments. Thanks to Ed Daniel, University of Oulu, for these changes.
* Performance improvements for search pages. Thanks to Jon Diprose, University of Oxford, for these changes.
* Support for sample preparation for NMR. Thanks to Antonio Rosato, CERM, for guidance.
* Support for planning 
* Records of completed experiments are now locked, and permission to unlock them can be limited to a few userids.
* Improved help files, including links to training videos.
* faster display of diagrams, without requiring Java on the user's computer.
* support for touch screens, except that the plate experiment functionality does not yet work on touch screens.
* updates to the facilities for downloading target details, to keep up to date with changes in the public databases.
 
98 issues have been closed. A list of changes in PiMS4.4 is at:
     http://pims.structuralbiology.eu/docs/changes/V4_4.html
 
The known defects in PiMS4.4 are listed at:
    http://pims.structuralbiology.eu/docs/changes/V4_4_known_issues.html
If you have problems with PiMS, please contact:
     pims-defects@dl.ac.uk

First, sign the new license. Then download the release from the link you will receive.
Unzip it, for example by logging on as root and
    cd /srv/tomcat7/
    wget -c /srv/tomcat7/ http://www.pims-lims.org/pims.zip
    unzip pims4_4_0
This large file contains everything you need to update PiMS 
or install PiMS for the first time.

If you want to make a new PiMS installation, please contact
    pims@dl.ac.uk
 For new academic licensees, PiMS is limited to 6 users.
 This limitation does not apply to academic groups who began using PiMS before 2013.

---------------------------------------------------------------    
UPDATING THE CONFIGURATION
---------------------------------------------------------------
    
To draw diagrams, PiMS needs to find your GraphViz executable. 
By default it uses the command "dot". You might have to change this to a full path.
In PiMS4.4 onwards, you specify this in the pims.xml file, 
e.g. for Linux::
      <Environment name="dot_path" value="/usr/bin/dot" type="java.lang.String"/>
and for Windows, something like:
      <Environment name="dot_path" value="C:\\Program Files\\Graphviz2.26\\bin\\dot.exe" type="java.lang.String"/>  
    
    
---------------------------------------------------------------    
UPDATING THE DATABASE STRUCTURE
---------------------------------------------------------------
    
There are some changes to the structure of the PiMS database for this release.    

First please take a backup, e.g. by
   pg_dump pims -U postgres -f pims.sql   

You need to provide the database connection information, in a file called Properties. 
You can do this by editing the file conf/Properties to add the password for your database.

Note that the database user you provide must be a superuser.
User "pimsupdate" does not have sufficient permissions to upgrade your database.
Supply the password if you have set one.

Stop Tomcat, e.g. by:
    /etc/init.d/tomcat stop

Then run:
   java -jar -Xms64m -Xmx256m upgrader.jar 
The final line of output should be
   OK (1 test)
In that case you can continue with the instructions below. 
Otherwise, please restart Tomcat (your database is unchanged)
and contact the PiMS team.

    
---------------------------------------------------------------    
UPDATING THE WEB APPLICATION
---------------------------------------------------------------

If you have have followed the instructions above, this PiMS web application is now at:
    /srv/tomcat7/pims4_4_0/webapp
Ensure that the start of the file:
    $CATALINA_HOME/conf/Catalina/localhost/pims.xml
reads:
<Context docBase="/srv/tomcat7/pims4_4_0/webapp"
         privileged="true" antiResourceLocking="false" antiJARLocking="false">

(You will notice a change in this. Previous PiMS versions were delivered as a .war.
This new approach works better Tomcat7, and still works in Tomcat6.)
    
Then clean up your previous installation of PiMS by:
    rm -rf $CATALINA_HOME/webapps/pims
There is also a work directory that must be deleted, e.g.:
    rm -rf $CATALINA_HOME/work/Catalina/localhost/pims
or in some distros:
    /var/cache/tomcat6/Catalina/localhost/pims/
    
Restart Tomcat by entering:
    /etc/init.d/tomcat start

---------------------------------------------------------------    
UPDATING THE DATABASE CONTENTS
---------------------------------------------------------------

There is some additional reference data needed for this release. 
You update this by running: 
   java -Xms64m -Xmx256m -jar RefDataLoader.jar  
You can use same properties file as above.
    
You have updated your PiMS installation.


INSTALLATION PROBLEMS
=====================

After the update, some pages may be laid out incorrectly when first viewed. 
If any users complain about this, please asked them to press CONTROL-F5.

If you get an error message viewing targets after the update, 
please compare the error details with:
    http://pimstrak1.dl.ac.uk:8080/jira/browse/PIMS-3573
If it matches, please check that you updated the database contents successfully.

If you encounter a problem installing or updating PIMS,
you may find some hints in:
        http://www.pims-lims.org/svn/pims/download/current/docs/
Alternatively please contact pims-users@dl.ac.uk.
   

FURTHER INFORMATION
===================

There is a demonstration at
    http://pimstrak1.dl.ac.uk:8080/demo/Login
Please use username "demo", password "demo".

For further information please see
    https://www.pims-lims.org

There is a bulletin board for PiMS users. To subscribe please mail to pims-users@dl.ac.uk.
The archive is available at:
    http://www.dl.ac.uk/list-archive-public/pims-users/threads.html

DEFECTS
=======

Please report problems to pims-users@dl.ac.uk. 
We intend to reply to emails within three working days.
There is a list of known problems at:
    http://pims.structuralbiology.eu/docs/changes/V4_4_known_issues.html






