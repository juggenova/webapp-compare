# webapp-compare
*Implementare un clone di Doodle in linguaggi e framework differenti*

## Introduzione
Questo repository è a disposizione di chi volesse contribuire alla realizzazione di un clone di [Doodle](https://doodle.com/) in un qualunque linguaggio o framework: Spring, Vaadin, Flutter...

Lo scopo è **confrontare insieme i diversi approcci** ed evidenziare punti di forza e debolezze, senza pregiudizi, in una o più riunioni del [JUG Genova](https://juggenova.it/).

Lo scopo quindi non è di trovare la soluzione migliore ma creare anche solo una bozza di codice da usare come base per la discussione al JUG.

## Requisiti
Quella che segue è la lista ipotetica dei requisiti che l'applicazione potrebbe implementare, allo scopo di avere una base comune da confrontare. Li elenco in ordine di precedenza in modo che anche un'implementazione parziale possa essere confrontata con tutte le altre.

1. l'utente che apre il sito deve poter vedere un poll che presenta, per la prima settimana di Novembre 2024, la lista dei sette giorni a cui assegnare una preferenza: SI/NO/FORSE. Il default è NO.
   - il poll può essere inizialmente cablato, ovvero fisso e non letto da db
2. gli attributi del poll sono titolo, descrizione, giorno di inizio, giorno di fine, giorno di deadline per il voto
3. la preferenza deve essere memorizzata da qualche parte (sul client o sul server) in modo che al reload sia visibile la scelta fatta in precedenza e possa essere modificata
   - chi non ha tempo può usare localStorage o simile
4. la preferenza deve essere inviata al server e memorizzata in maniera distinta da quelle fatte dagli altri utenti
   - dato che non c'è ancora login, possiamo assegnare come id utente un numero random molto grande generato e salvato sul browser, sapendo che non andrebbe fatto :-)
5. alle 23:59 del giorno di deadline il server deve scegliere il giorno più votato assegnando +2 a SI, +1 a FORSE, 0 a NO. Se ci sono più giorni con lo stesso punteggio, prende il primo (per semplificare).
6. quando un utente si collega al sito dopo la deadline, vede il giorno scelto e non può più votare
7. implementare il login con cinque utenze cablate. Lo username è un'email (anche finta)
   1. la home del sito presenta solo il form di login, passato il quale si accede al poll
   2. se si tenta di accedere alla pagina del poll tramite bookmark senza aver fatto login, viene presentato il form di login dopo il quale si viene reindirizzati alla pagina inizialmente richiesta
8. quando il server sceglie il giorno più votato, manda un'email a tutti i votanti
9. aggiungere un menu di scelta lingua: it/en
   - la scelta deve in qualche modo essere persistente
10. assicurarsi che il sito sia il più possibile responsive
11. implementare la registrazione spontanea degli utenti con email e password
    - con o senza invio di email per verifica
12. implementare una dashboard di amministrazione in cui creare e gestire i poll

La lista è lunga ma credo si possa imparare molto anche solo implementando il primo requisito.

Un *requisito di processo* che sarebbe utile adottare è la memorizzazione del tempo impiegato per implementare ciascun punto. Infatti uno dei principali scopi dei framework è quello di ridurre il tempo di sviluppo, per cui questo valore deve essere un fattore di discussione durante il confronto. Il tempo può essere facilmente cronometrato con strumenti quali [Toggl Track](https://track.toggl.com/) che forniscono gratuitamente applicazioni native con cui gestire i timer e creare report finali.

## Partecipazione
Chiunque può partecipare, ma solo se è intenzionato a presentare la sua implementazione al JUG.

Il canale principale di comunicazione è il topic ["webapp-compare"](https://t.me/jug_genova/336) del [canale Telegram del JUG Genova](https://t.me/jug_genova).

Esiste una sola regola, ovvero che si usi questo repository GitHub per li codice durante lo sviluppo, in modo da favorire la partecipazione e la condivisione. Mandatemi quindi la vostra richiesta e vi aggiungerò come utenti del repository in modo da poter fare push direttamente in un folder a voi dedicato.

## Deadline
Considerati vari fattori, non viene fissata alcuna deadline. I risultati verranno proposti nei prossimi incontri man mano che saranno disponibili. Si può quindi considerare questa come una piattaforma aperta a cui è sempre possibile contribuire.

## Premio
Tutti i partecipanti riceveranno un certificato di partecipazione creato da ChatGPT e i loro nomi saranno incisi in queste pagine a perenne memoria. 

