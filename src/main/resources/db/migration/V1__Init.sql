CREATE TABLE operations (
  operation_id UUID PRIMARY KEY ,
  transaction_date TIMESTAMP,
  status VARCHAR,
  pay_system VARCHAR,
  pay_method VARCHAR,
  terminal_id UUID
);

CREATE OR REPLACE FUNCTION gen_date(min date) RETURNS date AS $$
  SELECT CURRENT_DATE - (random() * (CURRENT_DATE - $1))::int;
$$ LANGUAGE sql STRICT VOLATILE;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION gen_random_number_in_range(min INTEGER , max integer) RETURNS INTEGER AS $$
  SELECT (random() * $2 + $1)::int
$$ LANGUAGE sql STRICT VOLATILE;

CREATE OR REPLACE FUNCTION gen_random_status() RETURNS VARCHAR AS $$
  SELECT
    CASE WHEN gen_random_number_in_range(0,1) = 1 THEN 'CLOSED'
         ELSE 'OPEN'
    END ;
$$ LANGUAGE sql STRICT VOLATILE;

CREATE OR REPLACE FUNCTION gen_random_pay_system() RETURNS VARCHAR AS $$
  SELECT
    CASE WHEN gen_random_number_in_range(0,3) = 1 THEN 'VISA'
         WHEN gen_random_number_in_range(0,3) = 2 THEN 'MASTER_CARD'
         WHEN gen_random_number_in_range(0,3) = 3 THEN 'MIR'
         ELSE 'OPEN'
       END ;
$$ LANGUAGE sql STRICT VOLATILE;

CREATE OR REPLACE FUNCTION gen_random_pay_method() RETURNS VARCHAR AS $$
  SELECT
    CASE WHEN gen_random_number_in_range(0,1) = 1 THEN 'CONTACT'
         ELSE 'NON_CONTACT'
    END ;
$$ LANGUAGE sql STRICT VOLATILE;

do $$
begin
  for r in 1..500000 loop
    insert into operations(
      operation_id,
      transaction_date,
      status,
      pay_system,
      pay_method,
      terminal_id
    )
    values(
      uuid_generate_v4(),
      gen_date('2010-01-01')::TIMESTAMP,
      gen_random_status() ,
      gen_random_pay_system() ,
      gen_random_pay_method(),
      uuid_generate_v4()
    );
  end loop;
end;
$$;