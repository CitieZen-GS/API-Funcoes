CREATE TABLE T_Ajudante_familiar_Cuidador (
    id_cuidador INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_cuidador VARCHAR(20) NOT NULL,
    cpf_cuidador VARCHAR(14) UNIQUE,
    ds_email VARCHAR(100) UNIQUE,
    ds_senha VARCHAR(100),
    id_perfil INTEGER NOT NULL,
    CONSTRAINT fk_cuidador_perfil FOREIGN KEY (id_perfil)
        REFERENCES T_Ajudante_familiar_Perfil (id_perfil)
);
