#!/bin/sh

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# 1. Start docker if it's not running
sudo systemctl status docker > /dev/null 2>&1 || sudo systemctl start docker

# 2. Check container status
docker container inspect jrvs-psql-1 > /dev/null 2>&1
container_status=$?

# 3. Handle commands
case $cmd in
  create)
    if [ $container_status -eq 0 ]; then
        echo "Error: container is already created"
        exit 1
    fi

    if [ -z "$db_username" ] || [ -z "$db_password" ]; then
        echo "Error: username or password is not given"
        exit 1
    fi

    # THE FIX: Create volume first to prevent mapping glitches
    docker volume create pgdata

    # THE FIX: Explicitly map the port and use the correct image
    docker create --name jrvs-psql-1 \
        -e POSTGRES_USER="$db_username" \
        -e POSTGRES_PASSWORD="$db_password" \
        -v pgdata:/var/lib/postgresql/data \
        -p 5432:5432 \
        postgres:9.6-alpine
    
    echo "Container created. Now run: ./psql_docker.sh start"
    exit $?
    ;;

  start|stop)
    if [ $container_status -ne 0 ]; then
        echo "Error: container is not created"
        exit 1
    fi

    # THE FIX: This executes the start or stop command properly
    docker container $cmd jrvs-psql-1
    
    # Show the user the port status immediately after starting
    if [ "$cmd" = "start" ]; then
        sleep 1
        docker ps -f name=jrvs-psql-1
    fi
    exit $?
    ;;

  *)
    echo "Usage: $0 {create|start|stop} [db_username] [db_password]"
    exit 1
    ;;
esac
