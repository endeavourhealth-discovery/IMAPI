docker rm -f postgresDB

docker run -d --name postgresDB \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=password \
    -e POSTGRES_DB=discovery \
    -p 5432:5432 \
    postgres

echo "Waiting for PostgreSQL to be ready..."
until docker exec postgresDB pg_isready -U postgres -d discovery; do
  sleep 1
done

docker cp ../database/query-runner-postgres.sql postgresDB:/database.sql
cat ../database/query-runner-postgres.sql | docker exec -i postgresDB psql -U postgres -d discovery -v ON_ERROR_STOP=1

echo "✅ Done. Schema and tables should now exist in 'discovery'."
