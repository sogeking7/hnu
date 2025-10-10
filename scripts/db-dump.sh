#!/bin/bash
pg_dump --file=scripts/init/nu_ddl.sql --dbname=postgresql://postgres:postgres@127.0.0.1:5432/hnu --schema-only -n public

if [[ "$OSTYPE" == "darwin"* ]]; then
  sed -i '' 's/CREATE SCHEMA public;/CREATE SCHEMA if not exists public;\nCREATE EXTENSION pgcrypto SCHEMA public;/g' scripts/init/nu_ddl.sql  # macOS
else
  sed -i 's/CREATE SCHEMA public;/CREATE SCHEMA if not exists public;\nCREATE EXTENSION pgcrypto SCHEMA public;/g' scripts/init/nu_ddl.sql  # Linux
fi

psql -U postgres -d hnu -a -f scripts/init/nu_flyway_export.sql

pg_dump --column-inserts --data-only --table=flyway_schema_history_export --dbname=postgresql://postgres:postgres@127.0.0.1:5432/fcs | sed "s/flyway_schema_history_export/flyway_schema_history/g" >> scripts/init/nu_ddl.sql

psql -U postgres -d hnu -c "drop table flyway_schema_history_export;"
