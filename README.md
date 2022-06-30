docker run -d -p 6666:3306 --name=mysql --env="MYSQL_ROOT_PASSWORD=root_pass" --env="MYSQL_USER=mpecel" --env="MYSQL_PASSWORD=mpecel_pass" --env="MYSQL_DATABASE=my_db" mysql:8.0.23              
