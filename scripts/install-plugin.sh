echo "Installing MYSQLX Plugin"
mysql -u root -proot -e "INSTALL PLUGIN mysqlx SONAME 'mysqlx.so';"