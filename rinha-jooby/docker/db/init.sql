create EXTENSION IF NOT EXISTS "pg_trgm";

create TABLE IF NOT EXISTS pessoas (
  id UUID NOT NULL,
  nome VARCHAR(100) NOT NULL,
  apelido VARCHAR(32) NOT NULL,
  nascimento DATE NOT NULL,
  stack TEXT,
  termo TEXT GENERATED ALWAYS AS (
    LOWER(nome || apelido || stack)
  ) STORED,
  CONSTRAINT pk_pessoa_id PRIMARY KEY (id),
  CONSTRAINT uk_apelido UNIQUE (apelido)
);

create index CONCURRENTLY IF NOT EXISTS IDX_PESSOAS_BUSCA_TGRM ON pessoas USING GIST (termo GIST_TRGM_OPS(SIGLEN=64));
