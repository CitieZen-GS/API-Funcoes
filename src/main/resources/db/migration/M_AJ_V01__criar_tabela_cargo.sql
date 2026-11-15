CREATE TABLE T_Ajudante_familiar_Cargo (
    id_cargo INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_cargo VARCHAR(20),
    fg_permissao CHAR(1),
    CONSTRAINT ck_cargo CHECK (fg_permissao IN ('S', 'N'))
);
