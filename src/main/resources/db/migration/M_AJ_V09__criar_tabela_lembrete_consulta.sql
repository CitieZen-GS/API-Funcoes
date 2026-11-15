CREATE TABLE T_Ajudante_familiar_Lembrete_Consulta (
    id_lembrete_consulta INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ds_titulo_lembrente_consulta VARCHAR(20),
    nm_consulta_medico VARCHAR(20),
    dt_consulta DATE,
    hr_consulta DATE,
    ds_consulta VARCHAR(100),
    st_consulta VARCHAR(10) DEFAULT 'Pendente' NOT NULL,
    id_idoso INTEGER NOT NULL,
    CONSTRAINT ck_consulta_status CHECK (st_consulta IN ('Pendente', 'Realizado')),
    CONSTRAINT fk_lembrente_consulta_idoso FOREIGN KEY (id_idoso)
        REFERENCES T_Ajudante_familiar_Idoso (id_idoso)
);
