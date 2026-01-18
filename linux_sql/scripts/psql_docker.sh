#!/bin/sh

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# 1. Start docker if it's not running
sudo systemctl status docker > /dev/null 2>&1 || sudo systemctl start docker

# 2. Check container status
docker container inspect jrvs-psql-1> /dev/null 2>&1
container_status=$?

# 3. Handle commands
case $cmd in 
  create)
    # Print error message if the container is already created
    if [ $container_status -eq 0 ]; then
        echo "Error: container is already created"
        exit 1
    fi

    # Print error message if username or password is not given
    if [ -z "$db_username" ] || [ -z "$db_password" ]; then
        echo "Error: username or password is not given"
        exit 1
    fi
  
    # Create a psql docker container with the given username and password
    docker create --name jrvs-psql-1 \
        -e POSTGRES_USER="$db_username" \
        -e POSTGRES_PASSWORD="$db_password" \
        postgres:9.6-alpine
    
    exit $?
    ;;

  start) 
    # Print error message if the container is not created
    if [ $container_status -ne 0 ]; then
        echo "Error: container is not created"
        exit 1
    fi

    # Start the stopped psql docker container
    docker container start jrvs-psql-1
    exit $?
    ;;

  stop)
    # Print error message if the container is not created
    if [ $container_status -ne 0 ]; then
        echo "Error: container is not created"
        exit 1
    fi

    # Stop the running psql docker container
    docker container stop jrvs-psql-1
    exit $?
    ;;
  
  *)
    echo "Usage: ./scripts/psql_docker.sh start|stop|create [db_username] [db_password]"
    exit 1
    ;;
esac
