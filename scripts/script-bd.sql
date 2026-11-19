-- =====================================================================
-- Ajudante Familiar - API de funções
-- script-bd.sql: objetos de banco (Oracle) desta API.
-- Cria todas as tabelas base do Ajudante Familiar. Rode este script primeiro.
-- Gerado a partir das migrations do Flyway em src/main/resources/db/migration,
-- na ordem em que são aplicadas. Na aplicação, o Flyway executa esses mesmos
-- arquivos sozinho; este script serve para criar o schema manualmente.
-- =====================================================================

-- ---------------------------------------------------------------------
-- M_AJ_V01__criar_tabela_cargo.sql
-- ---------------------------------------------------------------------
CREATE TABLE T_Ajudante_familiar_Cargo (
    id_cargo INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_cargo VARCHAR(20),
    fg_permissao CHAR(1),
    CONSTRAINT ck_cargo CHECK (fg_permissao IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- M_AJ_V02__criar_tabela_perfil.sql
-- ---------------------------------------------------------------------
CREATE TABLE T_Ajudante_familiar_Perfil (
    id_perfil INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_perfil VARCHAR(20) NOT NULL,
    id_cargo INTEGER NOT NULL,
    CONSTRAINT fk_perfil_cargo FOREIGN KEY (id_cargo)
        REFERENCES T_Ajudante_familiar_Cargo (id_cargo)
);

-- ---------------------------------------------------------------------
-- M_AJ_V03__criar_tabela_cuidador.sql
-- ---------------------------------------------------------------------
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

-- ---------------------------------------------------------------------
-- M_AJ_V04__criar_tabela_idoso.sql
-- ---------------------------------------------------------------------
CREATE TABLE T_Ajudante_familiar_Idoso (
    id_idoso INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_idoso VARCHAR(20) NOT NULL,
    cpf_idoso VARCHAR(14),
    vlr_altura DECIMAL(3,2),
    vlr_peso DECIMAL(5,2),
    dt_nascimento DATE NOT NULL,
    ds_comportamento VARCHAR(100),
    id_cuidador INTEGER NOT NULL,
    CONSTRAINT fk_idoso_cuidador FOREIGN KEY (id_cuidador)
        REFERENCES T_Ajudante_familiar_Cuidador (id_cuidador)
);

-- ---------------------------------------------------------------------
-- M_AJ_V05__criar_tabela_parente.sql
-- ---------------------------------------------------------------------
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

-- ---------------------------------------------------------------------
-- M_AJ_V06__criar_tabela_doencas.sql
-- ---------------------------------------------------------------------
CREATE TABLE T_Ajudante_familiar_Doencas (
    id_doenca INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_doenca VARCHAR(20),
    ds_doenca VARCHAR(20),
    id_idoso INTEGER NOT NULL,
    CONSTRAINT fk_doenca_idoso FOREIGN KEY (id_idoso)
        REFERENCES T_Ajudante_familiar_Idoso (id_idoso)
);

-- ---------------------------------------------------------------------
-- M_AJ_V07__criar_tabela_atividades.sql
-- ---------------------------------------------------------------------
CREATE TABLE T_Ajudante_familiar_Atividades (
    id_atividade INTEGER GENERATED ALWAYS AS IDENTITY,
    nm_atividade VARCHAR(20) NOT NULL,
    ds_atividade VARCHAR(80),
    dt_atividade DATE NOT NULL,
    hr_atividade_inicio DATE,
    hr_atividade_fim DATE,
    id_idoso INTEGER NOT NULL,
    CONSTRAINT pk_atividades PRIMARY KEY (id_atividade),
    CONSTRAINT fk_atividade_idoso FOREIGN KEY (id_idoso)
        REFERENCES T_Ajudante_familiar_Idoso (id_idoso)
);

-- ---------------------------------------------------------------------
-- M_AJ_V08__criar_tabela_alimentacao.sql
-- ---------------------------------------------------------------------
CREATE TABLE T_Ajudante_familiar_Alimentacao (
    id_alimentacao INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nm_alimentacao VARCHAR(20) NOT NULL,
    tp_alimentacao VARCHAR(20) NOT NULL,
    dt_consumo DATE NOT NULL,
    hr_consumo DATE NOT NULL,
    vlr_caloria DECIMAL(6,2),
    ds_preparacao VARCHAR(100),
    id_idoso INTEGER NOT NULL,
    CONSTRAINT ck_alimentacao_tipo CHECK
        (tp_alimentacao IN ('Cafe da manha', 'Almoco', 'Lanche de tarde', 'Jantar')),
    CONSTRAINT fk_lembrente_familia_idoso FOREIGN KEY (id_idoso)
        REFERENCES T_Ajudante_familiar_Idoso (id_idoso)
);

-- ---------------------------------------------------------------------
-- M_AJ_V09__criar_tabela_lembrete_consulta.sql
-- ---------------------------------------------------------------------
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

-- ---------------------------------------------------------------------
-- M_AJ_V10__criar_tabela_medicamentos.sql
-- ---------------------------------------------------------------------
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

-- ---------------------------------------------------------------------
-- M_AJ_V11__criar_tabela_consulta_medicamento.sql
-- ---------------------------------------------------------------------
CREATE TABLE T_Ajudante_familiar_Consulta_Medicamento (
    id_consulta_medicamento INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_lembrete_consulta INTEGER NOT NULL,
    id_medicamentos INTEGER NOT NULL,
    CONSTRAINT fk_cm_lembrente_consulta FOREIGN KEY (id_lembrete_consulta)
        REFERENCES T_Ajudante_familiar_Lembrete_Consulta (id_lembrete_consulta),
    CONSTRAINT fk_cm_medicamentos FOREIGN KEY (id_medicamentos)
        REFERENCES T_Ajudante_familiar_Medicamentos (id_medicamentos)
);

-- ---------------------------------------------------------------------
-- M_AJ_V12__criar_tabela_logs.sql
-- ---------------------------------------------------------------------
CREATE TABLE LOGS (
    id_log INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    nm_procedure VARCHAR(100),
    nm_usuario VARCHAR(50),
    dt_ocorrencia DATE,
    codigo_erro VARCHAR(50),
    mensagem_erro VARCHAR(400)
);
