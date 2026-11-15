CREATE TABLE T_Ajudante_familiar_Medicamentos (
    id_medicamentos INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_medicamentos VARCHAR(20),
    via_administracao VARCHAR(20),
    vl_frquencia VARCHAR(50),
    ds_instrucao_uso VARCHAR(100),
    dt_medicamento DATE,
    hr_medicamento DATE NOT NULL,
    id_idoso INTEGER NOT NULL,
    CONSTRAINT fk_medicamento_idoso FOREIGN KEY (id_idoso)
        REFERENCES T_Ajudante_familiar_Idoso (id_idoso)
);
