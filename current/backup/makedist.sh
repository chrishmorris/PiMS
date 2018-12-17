rm -f filterdb.jar
ant -DanUser=demo makedbbackup
rm -f log/install-db-copy.out
touch log/install-db-copy.out
tar cvfz pims-backup.tgz ../pims-backup/conf/Properties ../pims-backup/README ../pims-backup/build.xml ../pims-backup/filterdb.jar ../pims-backup/log/install-db-copy.out

