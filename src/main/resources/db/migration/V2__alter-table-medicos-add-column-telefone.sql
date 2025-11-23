-- 1) adiciona a coluna permitindo nulos
alter table medicos add telefone varchar(20);

-- 2) preenche um valor padrão para quem já existe
update medicos set telefone = '000000000';

-- 3) agora sim, trava a coluna como NOT NULL
alter table medicos alter column telefone set not null;
