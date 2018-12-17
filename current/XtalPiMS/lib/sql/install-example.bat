dropdb -U postgres pims3.1_xtalexample
pause
createdb -U postgres  -E UTF8 pims3.1_xtalexample
psql -U postgres  -d pims3.1_xtalexample -f xtalPims-example.sql
pause