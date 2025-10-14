docker pull mysql:8.0
docker run --name mysql -e MYSQL_ROOT_PASSWORD=password -e -d -p 3306:3306 mysql:8.0