CREATE DATABASE fb DEFAULT CHARACTER SET utf8;
GRANT select,insert,update,delete,execute ON fb.* TO webman@localhost IDENTIFIED BY 'webman';

USE fb;
