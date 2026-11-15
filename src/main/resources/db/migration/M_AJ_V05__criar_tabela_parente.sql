CREATE TABLE T_Ajudante_familiar_Parente (
    id_parente INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_parente VARCHAR(20),
    nr_telefone VARCHAR(18) UNIQUE,
    ds_email VARCHAR(100) UNIQUE,
    ds_senha VARCHAR(100),
    id_idoso INTEGER NOT NULL,
    id_perfil INTEGER NOT NULL,
    CONSTRAINT fk_parente_idoso FOREIGN KEY (id_idoso)
        REFERENCES T_Ajudante_familiar_Idoso (id_idoso),
    CONSTRAINT fk_perfil FOREIGN KEY (id_perfil)
        REFERENCES T_Ajudante_familiar_Perfil (id_perfil)
);
