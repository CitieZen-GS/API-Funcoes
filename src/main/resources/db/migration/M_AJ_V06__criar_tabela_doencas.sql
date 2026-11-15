CREATE TABLE T_Ajudante_familiar_Doencas (
    id_doenca INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_doenca VARCHAR(20),
    ds_doenca VARCHAR(20),
    id_idoso INTEGER NOT NULL,
    CONSTRAINT fk_doenca_idoso FOREIGN KEY (id_idoso)
        REFERENCES T_Ajudante_familiar_Idoso (id_idoso)
);
