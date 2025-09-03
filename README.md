# **ToDo App - Progetto Object-Oriented**
l'obiettivo del progetto è la realizzazione di un sistema informativo per la gestione di attivit`a personali, denominate “ToDo”, ispirato al funzionamento
del software Trello. Il sistema si compone di una base di dati relazionale e
di un’applicazione Java con interfaccia grafica sviluppata in Swing.

L’accesso avviene tramite autenticazione basata su credenziali univoche
(username e password). Ogni utente dispone di tre bacheche personali predefinite, corrispondenti alle categorie Universit`a, Lavoro e Tempo Libero,
ciascuna caratterizzata da un titolo e da una descrizione modificabile.

All’interno delle bacheche `e presente una lista ordinata di ToDo, ognuno
identificato da un ID univoco e associato a vari attributi, ovvero titolo, descrizione, data di scadenza, link, immagine allegata, colore, e stato (Completato o Non Completato, con default su Non Completato).
Gli utenti possono creare, modificare, eliminare e riorganizzare i ToDo,
oltre che spostarli tra le bacheche. Inoltre, ogni ToDo pu`o essere condiviso
con altri utenti, che lo visualizzeranno automaticamente nella loro bacheca
corrispondente mantenendo le stesse caratteristiche.

Il sistema permette inoltre la ricerca per titolo o nome, la visualizzazione
dei ToDo in scadenza entro una data specifica o nella giornata corrente e
segnala graficamente i ToDo scaduti colorandone in rosso il titolo.
