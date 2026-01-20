#!/bin/bash

# 1. Assign CLI arguments to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check if all arguments are provided
if [ "$#" -ne 5 ]; then
    echo "Usage: ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password"
    exit 1
fi

# 2. Parse host hardware specifications
hostname=$(hostname -f)
lscpu_out=$(lscpu)

cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "Architecture" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | cut -d':' -f2 | xargs)
cpu_mhz=$(echo "$cpu_model" | grep -o "[0-9.]*GHz" | sed 's/GHz//')
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | sed 's/K//') # Clean units if needed
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}')
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# 3. Construct the INSERT statement
# Note: Ensure your table columns match these variable names
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, \"timestamp\") 
VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp');"

# 4. Execute the INSERT statement through the psql CLI tool
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
