dropdb -U postgres pims3.2_xtalref
pause
createdb -U postgres  -E UTF8  pims3.2_xtalref
psql -U postgres  -d pims3.2_xtalref -f YSBL_ref_1plate+.sql
pause