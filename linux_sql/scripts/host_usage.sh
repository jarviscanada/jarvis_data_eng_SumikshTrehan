# Setup and validate arguments (again, don't copy comments)
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check # of args
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi
# Save machine statistics in MB
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Extracting metrics from the variables
cpu_user=$(echo "$vmstat_mb"| tail -n1 | awk '{print $13}')
memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -n1 | awk '{print $15}')
cpu_kernel=$(echo "$vmstat_mb" | tail -n1 | awk '{print $14}')

# Disk metrics
disk_io=$(vmstat -d | tail -n1 | awk '{print $10}')
disk_available=$(df -m / | tail -n1 | awk '{print $4}')

# Current time in UTC format
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

# Subquery to find matching id
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')"

# PSQL command (Using ONLY your variables)
insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, cpu_user, disk_io, disk_available) 
VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $cpu_user, $disk_io, $disk_available);"

# Execute
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?

