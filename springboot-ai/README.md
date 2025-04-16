# springboot-ai
**Author:** Cristian Ghezzi

**AI Agent:** Augment Code

## Note
Ho deciso di usare Augment Code per testare la sua capacità di gestire grosse codebase. L'intenzione era di fargli prendere spunto dalla versione "springmvc" per creare una versione con tecnologia differente. Quindi i dati in input sono stati il folder springmvc e il readme dei requisiti generali.

## Architettura
Questa versione utilizza **Spring Boot** per il backend e **React** con **Tailwind CSS** per il frontend.
Tutto il codice è stato scritto usando Augment Code.
Il punto di partenza è stato far leggere il readme delle specifiche all'agent e scegliere un'architettura da lui proposta, con il vincolo di usare Spring Boot sul backend.

## Fase 0: setup iniziale (0:37)
Con "setup iniziale" intendo tutto ciò che occorre per creare un ambiente di sviluppo che mi consenta di visualizzare una pagina web vuota caricata da server. Generalmente questo comprende anche il database, ma visto che la persistenza dei voti è posticipata al requisito 3, ho aspettato.

Augment Code si è comportato abbastanza bene. Inizialmente aveva messo alcuni file nel folder padre ma dopo mia segnalazione ha corretto il problema. Si era anche dimenticato di mettere il maven wrapper ma ha risolto velocemente.

## Fase 2: poll cablato (0:15)
*Requisiti 1+2*

Ho chiesto ad Augment di guardare come era implementata la versione springmvc e di implementare i due requisiti in oggetto, sembre con un poll cablato e senza database.
Ha fatto tutto ciò che serviva.

![Screenshot](/springboot-ai/readme.files/fase1-2.jpg)

## Fase 2: salvataggio e modifica (1:48)
*Requisito 3*

Ho chiesto ad Augment di implementare la persistenza dando un'occhiata alla versione springmvc. Inizialmente aveva intenzione di usare H2 come database in sviluppo e MySQL in produzione. Gli ho imposto di usare MariaDB in sviluppo.
Nella prima versione usava Spring Data e gli ho chiesto di eliminarlo e usare un pattern DAO.
Di sua iniziativa ha anche implementato una schedulazione per il controllo dei poll, cosa che avrebbe dovuto fare in fase 4.
L'applicazione però non partiva perché stava usando la versione 2 di mariadb4j, troppo vecchia. Gli ho chiesto di vedere che versione fosse usata nel progetto springmvc e ha capito correttamente che doveva usare la versione 3, ma avendo io in gradle usato il wildcard 3+ ha dedotto la 3.0.0 che non esiste e ho dovuto sistemare a mano.
C'è da notare che gli ho semplicemente detto di controllare nel progetto springmvc e lui ha capito di dover guardare nel gradle.properties.

Al primo run non ha funzionato per un problema nello schema del DB, che ha risolto velocemente dopo avergli dato lo stack trace.
C'era però il problema che ai run successivi tentava sempre di creare da zero il database. Infatti non c'è alla data attuale un'API di MariaDB4J che permetta di aprire un database esistente. Ho indicato ad Augment di guardare i sorgenti dello Yada Framework, dove ho implementato un workaround, e lui ha fatto una sua implementazione ma non è riuscito a farla funzionare, finché gli ho dato i sorgenti della mia versione. Risolvere questo problema ha richiesto quasi un'ora ma solo perché avevo già la soluzione, altrimenti chissà...



