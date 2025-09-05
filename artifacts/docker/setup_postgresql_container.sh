docker rm -f postgresDB

docker run -d --name postgresDB -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=discovery -p 5432:5432 postgres

echo "Waiting for PostgresSQL to be ready..."
until docker exec postgresDB pg_isready -U postgres -d discovery; do
  sleep 1
done

docker exec -i postgresDB psql -U postgres -d discovery -v ON_ERROR_STOP=1 < ../database/query-runner-postgres.sql

echo "✅ Done. Schema and tables should now exist in 'discovery'."
