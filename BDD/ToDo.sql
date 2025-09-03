CREATE TABLE IF NOT EXISTS utente (
    login VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);


CREATE OR REPLACE FUNCTION check_duplicati_login()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM utente WHERE login = NEW.login
    ) THEN
        RAISE EXCEPTION 'Username già esistente: %, inserirne 
		uno diverso', NEW.login;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_login ON utente;

CREATE TRIGGER trg_check_login
BEFORE INSERT ON utente
FOR EACH ROW
EXECUTE FUNCTION check_duplicati_login();




CREATE TABLE IF NOT EXISTS bacheca(
id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    titolo VARCHAR(100) NOT NULL,
	descrizione TEXT NOT NULL,
	numero SMALLINT NOT NULL CHECK (numero BETWEEN 1 AND 3),
	proprietario VARCHAR(100) NOT NULL,
	FOREIGN KEY (proprietario) REFERENCES utente(login),
	UNIQUE (proprietario, numero)
);


CREATE OR REPLACE FUNCTION crea_bacheche()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO bacheca (titolo, descrizione, numero, proprietario)
    VALUES
    ('Università', 'Bacheca per la Università', 1, NEW.login),
    ('Lavoro', 'Bacheca per il Lavoro', 2, NEW.login),
    ('Tempo Libero', 'Bacheca per il Tempo Libero', 3, NEW.login);
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS crea_bacheche_trigger ON utente;

CREATE TRIGGER crea_bacheche_trigger
AFTER INSERT ON utente
FOR EACH ROW
EXECUTE FUNCTION crea_bacheche();



CREATE TABLE IF NOT EXISTS todo(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	titolo VARCHAR(100) NOT NULL,
	stato BOOLEAN DEFAULT FALSE, 
	link TEXT,
	immagine TEXT,
	descrizione TEXT,
	scadenza DATE,
	colore VARCHAR(7),
	posizione INTEGER,
	autore VARCHAR(100) NOT NULL,
	idB INTEGER NOT NULL,
	FOREIGN KEY (idB) REFERENCES bacheca(id),
	FOREIGN KEY (autore) REFERENCES utente(login),
	CHECK (colore ~ '^#([0-9a-fA-F]{6})$')
);

CREATE OR REPLACE FUNCTION set_posizione_todo()
RETURNS TRIGGER AS $$
DECLARE
    max_pos INTEGER;
BEGIN
    IF NEW.posizione IS NULL THEN
        SELECT COALESCE(MAX(posizione), 0) INTO max_pos
        FROM todo
        WHERE idB = NEW.idB;
    
        NEW.posizione := max_pos + 1;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_set_posizione_todo ON todo;

CREATE TRIGGER trg_set_posizione_todo
BEFORE INSERT ON todo
FOR EACH ROW
EXECUTE FUNCTION set_posizione_todo();



CREATE TABLE IF NOT EXISTS condivisione(
   ricevente VARCHAR(100) NOT NULL,
   idT INTEGER NOT NULL,
   PRIMARY KEY (ricevente, idT),
   FOREIGN KEY (ricevente) REFERENCES utente(login),
   FOREIGN KEY (idT) REFERENCES todo(id)
   );
   
   
  CREATE OR REPLACE FUNCTION check_bacheche_standard()
RETURNS TRIGGER AS $$
DECLARE
    n_bacheche INTEGER;
BEGIN
    IF NEW.numero NOT IN (1, 2, 3) THEN
        RAISE EXCEPTION 'Tipo bacheca non valido: % 
		(consentiti: 
		1=Università, 2=Lavoro, 3=Tempo Libero)',
		NEW.numero;
    END IF;
    SELECT COUNT(*) INTO n_bacheche
    FROM bacheca
    WHERE proprietario = NEW.proprietario AND numero = NEW.numero;

    IF n_bacheche > 0 THEN
        RAISE EXCEPTION 'L''utente % ha già una bacheca di tipo %',
		NEW.proprietario, NEW.numero;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_bacheche ON bacheca;

CREATE TRIGGER trg_check_bacheche
BEFORE INSERT ON bacheca
FOR EACH ROW
EXECUTE FUNCTION check_bacheche_standard();
  
  
  
  
   CREATE OR REPLACE FUNCTION sposta_todo_tra_bacheche(
    p_id_todo      INTEGER,
    idB_dest      INTEGER,
    p_login_richiedente VARCHAR
)
RETURNS VOID AS $$
DECLARE
    idB_orig   INTEGER;
    old_posizione    INTEGER;
    new_posizione    INTEGER;
    proprietario_orig TEXT;
BEGIN
    SELECT idB, posizione
    INTO idB_orig, old_posizione
    FROM todo
    WHERE id = p_id_todo;
    IF NOT FOUND THEN RAISE EXCEPTION 'ToDo con id % non esiste.', p_id_todo;
    END IF;

    SELECT proprietario INTO proprietario_orig
    FROM bacheca
    WHERE id = idB_orig;

    IF proprietario_orig IS DISTINCT FROM p_login_richiedente THEN
        RAISE EXCEPTION 'Accesso negato: solo il proprietario 
		della bacheca può spostare i ToDo.';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM bacheca WHERE id = idB_dest
    ) THEN RAISE EXCEPTION 'Bacheca di destinazione con id % 
		non esiste.', idB_dest;
    END IF;

    UPDATE todo
    SET posizione = posizione - 1
    WHERE idB = idB_orig AND posizione > old_posizione;

    SELECT COALESCE(MAX(posizione), 0) + 1
    INTO new_posizione
    FROM todo
    WHERE idB = idB_dest;

    UPDATE todo
    SET idB = idB_dest,
        posizione = new_posizione
    WHERE id = p_id_todo;
END; $$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION elimina_bacheca(
    p_id_bacheca INTEGER,
    p_login_richiedente VARCHAR
)
RETURNS VOID AS $$
DECLARE
    proprietario_bacheca VARCHAR;
BEGIN
    SELECT proprietario INTO proprietario_bacheca
    FROM bacheca
    WHERE id = p_id_bacheca;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Bacheca con id % non esiste.', p_id_bacheca;
    END IF;

    IF proprietario_bacheca IS DISTINCT FROM p_login_richiedente THEN
        RAISE EXCEPTION 'Accesso negato: solo il proprietario può 
		eliminare la bacheca.';
    END IF;

    DELETE FROM condivisione
    WHERE idT IN (
        SELECT id FROM todo WHERE idB = p_id_bacheca
    );

    DELETE FROM todo
    WHERE idB = p_id_bacheca;

    DELETE FROM bacheca
    WHERE id = p_id_bacheca;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION elimina_todo(
    p_id_todo INTEGER,
    p_login_richiedente VARCHAR
)
RETURNS VOID AS $$
DECLARE
    v_autore VARCHAR;
    v_idB INTEGER;
    v_posizione INTEGER;
    v_proprietario_bacheca VARCHAR;
BEGIN
    SELECT autore, idB, posizione
    INTO v_autore, v_idB, v_posizione
    FROM todo
    WHERE id = p_id_todo;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'ToDo con id % non esiste.', p_id_todo;
    END IF;

    SELECT proprietario INTO v_proprietario_bacheca
    FROM bacheca
    WHERE id = v_idB;

    IF p_login_richiedente IS DISTINCT FROM v_autore
       AND p_login_richiedente IS DISTINCT FROM v_proprietario_bacheca 
	   THEN
        RAISE EXCEPTION 'Accesso negato: solo autore o proprietario 
		della bacheca possono eliminare il ToDo.';
    END IF;

    DELETE FROM condivisione WHERE idT = p_id_todo;

    DELETE FROM todo WHERE id = p_id_todo;

    UPDATE todo
    SET posizione = posizione - 1
    WHERE idB = v_idB AND posizione > v_posizione;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION set_stato_todo(
    p_id_todo INTEGER,
    p_login_richiedente VARCHAR,
    p_stato BOOLEAN DEFAULT TRUE
)
RETURNS VOID AS $$
DECLARE
    v_autore VARCHAR;
    v_idB INTEGER;
    v_proprietario_bacheca VARCHAR;
    v_condiviso BOOLEAN;
BEGIN
    SELECT autore, idB INTO v_autore, v_idB
    FROM todo
    WHERE id = p_id_todo;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'ToDo con id % non esiste.', p_id_todo;
    END IF;

    SELECT proprietario INTO v_proprietario_bacheca
    FROM bacheca
    WHERE id = v_idB;

    IF p_login_richiedente IS DISTINCT FROM v_autore
       AND p_login_richiedente IS DISTINCT FROM v_proprietario_bacheca THEN

        SELECT EXISTS (
            SELECT 1 FROM condivisione 
            WHERE idT = p_id_todo AND ricevente = p_login_richiedente
        ) INTO v_condiviso;

        IF NOT v_condiviso THEN
            RAISE EXCEPTION 'Accesso negato: il ToDo non è visibile da %',
			p_login_richiedente;
        END IF;
    END IF;

    UPDATE todo
    SET stato = p_stato
    WHERE id = p_id_todo;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION condividi_todo(
    p_id_todo INTEGER,
    p_ricevente VARCHAR,
    p_login_richiedente VARCHAR,
    p_azione TEXT  -- 'aggiungi' o 'rimuovi'
)
RETURNS VOID AS $$
DECLARE
    v_autore VARCHAR;
BEGIN
    SELECT autore INTO v_autore
    FROM todo
    WHERE id = p_id_todo;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'ToDo con id % non esiste.', p_id_todo;
    END IF;

    IF v_autore IS DISTINCT FROM p_login_richiedente THEN
        RAISE EXCEPTION 'Solo l''autore del ToDo può modificarne le 
		                 condivisioni.';
    END IF;

    IF p_ricevente = v_autore THEN
        RAISE EXCEPTION 'Non ha senso condividere un ToDo con sé stessi.';
    END IF;

    IF p_azione = 'aggiungi' THEN
        INSERT INTO condivisione (idT, ricevente)
        VALUES (p_id_todo, p_ricevente)
        ON CONFLICT DO NOTHING;

    ELSIF p_azione = 'rimuovi' THEN
        DELETE FROM condivisione
        WHERE idT = p_id_todo AND ricevente = p_ricevente;

    ELSE
        RAISE EXCEPTION 'Azione non valida: % (consentite: 
		                 aggiungi, rimuovi)', p_azione;
    END IF;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION lista_condivisioni_todo(
    p_id_todo INTEGER,
    p_login_richiedente VARCHAR
)
RETURNS TABLE(utente_ricevente VARCHAR) AS $$
DECLARE
    v_autore VARCHAR;
    v_idB INTEGER;
    v_proprietario VARCHAR;
    v_accesso BOOLEAN;
BEGIN
    SELECT autore, idB INTO v_autore, v_idB
    FROM todo
    WHERE id = p_id_todo;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'ToDo con id % non esiste.', p_id_todo;
    END IF;

    SELECT proprietario INTO v_proprietario
    FROM bacheca
    WHERE id = v_idB;

    v_accesso := (
        p_login_richiedente = v_autore OR
        p_login_richiedente = v_proprietario OR
        EXISTS (
            SELECT 1 FROM condivisione
            WHERE idT = p_id_todo AND ricevente = p_login_richiedente
        )
    );

    IF NOT v_accesso THEN
        RAISE EXCEPTION 'Accesso negato al ToDo %', p_id_todo;
    END IF;

    RETURN QUERY
    SELECT ricevente
    FROM condivisione
    WHERE idT = p_id_todo;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION modifica_todo(
    id_todo INTEGER,
    username TEXT,
    ntitolo TEXT DEFAULT NULL,
    ndescrizione TEXT DEFAULT NULL,
    nscadenza DATE DEFAULT NULL,
    ncolore TEXT DEFAULT NULL,
    nlink TEXT DEFAULT NULL,
    nimmagine TEXT DEFAULT NULL
)
RETURNS VOID AS $$
BEGIN
    UPDATE todo
    SET                             
        titolo = COALESCE(ntitolo, titolo),
        descrizione = COALESCE(ndescrizione, descrizione),
        scadenza = COALESCE(nscadenza, scadenza),
        colore = COALESCE(ncolore, colore),
        link = COALESCE(nlink, link),
        immagine = COALESCE(nimmagine, immagine)
    WHERE id = id_todo AND autore = username;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Impossibile modificare poichè il ToDo 
		non è stato trovato oppure non si dispone dei permessi';
    END IF;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION modifica_bacheca(
    id_bacheca INTEGER,
    username TEXT,
    ntitolo TEXT DEFAULT NULL,
    ndescrizione TEXT DEFAULT NULL
)
RETURNS VOID AS $$
BEGIN
    UPDATE bacheca
    SET
        titolo = COALESCE(ntitolo, titolo),
        descrizione = COALESCE(ndescrizione, descrizione)
    WHERE id = id_bacheca AND proprietario = username;
	IF NOT FOUND THEN
        RAISE EXCEPTION 'Impossibile modificare poichè la bacheca 
		non è stata trovata oppure non si dispone dei permessi';
    END IF;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION todo_in_scadenza(
    p_login VARCHAR,
    p_data_fine DATE DEFAULT CURRENT_DATE
)
RETURNS TABLE (
    id INTEGER,
    titolo VARCHAR(100),
    descrizione TEXT,
    scadenza DATE,
    autore VARCHAR(100),
    condiviso BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        t.id,
        t.titolo,
        t.descrizione,
        t.scadenza,
        t.autore,
        FALSE AS condiviso
    FROM todo t
    WHERE t.autore = p_login
      AND t.scadenza IS NOT NULL
      AND t.scadenza <= p_data_fine

    UNION

    SELECT 
        t.id,
        t.titolo,
        t.descrizione,
        t.scadenza,
        t.autore,
        TRUE AS condiviso
    FROM todo t
    JOIN condivisione c ON t.id = c.idT
    WHERE c.ricevente = p_login
      AND t.scadenza IS NOT NULL
      AND t.scadenza <= p_data_fine;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION registra_utente(login TEXT, password TEXT)
RETURNS VOID AS $$
BEGIN
    INSERT INTO utente(login, password) VALUES (login, password);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION inverti_posizione_todo(id1 INT, id2 INT)
RETURNS VOID AS $$
DECLARE
    pos1 INT;
    pos2 INT;
BEGIN
    SELECT posizione INTO pos1 FROM todo WHERE id = id1;
    SELECT posizione INTO pos2 FROM todo WHERE id = id2;

    IF pos1 IS NULL OR pos2 IS NULL THEN
        RAISE EXCEPTION 'Almeno uno dei ToDo non esiste';
    END IF;

    UPDATE todo SET posizione = pos2 WHERE id = id1;
    UPDATE todo SET posizione = pos1 WHERE id = id2;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION cerca_todo_per_titolo(titolo_input TEXT)
RETURNS TABLE (
    id INT,
    titolo VARCHAR(100),
    descrizione TEXT,
    autore VARCHAR(100),
    scadenza DATE,
    completato BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT t.id, t.titolo, t.descrizione, t.autore, t.scadenza, t.stato
    FROM todo t
    WHERE t.titolo ILIKE titolo_input;  
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION lista_funzioni()
RETURNS TABLE (
    nome TEXT, descrizione TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM (VALUES
        ('modifica_todo', 'Modifica i campi di un ToDo già esistente 
		 (solo autore può farlo)'),

        ('modifica_bacheca', 'Modifica titolo o descrizione di una bacheca 
		 (solo proprietario può farlo)'),

        ('elimina_todo', 'Elimina un ToDo se sei autore o proprietario
     	 della bacheca'),

        ('elimina_bacheca', 'Elimina una bacheca e tutti i suoi ToDo 
		 (solo proprietario)'),
		 
		 ('registra_utente', 'Inserisce un nuovo utente all''interno 
		  del sistema'),

        ('sposta_todo_tra_bacheche', 'Sposta un ToDo da una 
		 bacheca a un''altra dello stesso proprietario'),
         
        ('set_stato_todo', 'Serve ad impostare lo stato del 
		 ToDo: TRUE = completato (sottinteso), FALSE = non completato'),

        ('condividi_todo', 'Permette all''autore di condividere o rimuovere
		 la condivisione di un ToDo'),
         
        ('lista_condivisioni_todo', 'Mostra tutti gli utenti con cui è
		 condiviso quel determinato ToDo'),

        ('todo_in_scadenza', 'Mostra ToDo dell''utente o quelli condivisi 
		 che sono in scadenza nella data odierna o entro una data a scelta'),
		 
		('cerca_todo_per_titolo', 'Permette di cercare un ToDo all''interno
		del sistema inserendone il titolo (solo se esistente)')
		
    ) AS elenco(nome, descrizione);
END;
$$ LANGUAGE plpgsql;









