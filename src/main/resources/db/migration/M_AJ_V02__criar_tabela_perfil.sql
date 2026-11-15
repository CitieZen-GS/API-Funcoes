CREATE TABLE T_Ajudante_familiar_Perfil (
    id_perfil INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_perfil VARCHAR(20) NOT NULL,
    id_cargo INTEGER NOT NULL,
    CONSTRAINT fk_perfil_cargo FOREIGN KEY (id_cargo)
        REFERENCES T_Ajudante_familiar_Cargo (id_cargo)
);
