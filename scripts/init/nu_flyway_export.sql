create table flyway_schema_history_export as
select row_number() over () as "installed_rank",
	   version,
	   description,
	   type,
	   script,
	   checksum,
	   installed_by,
	   '2025-01-01 00:00:00.000000' as installed_on,
	   1 as execution_time,
	   success
from flyway_schema_history
where (script like 'V%' or type = 'BASELINE')
order by installed_rank;
