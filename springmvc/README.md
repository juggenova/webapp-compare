# springmvc
**Author:** Cristian Ghezzi

## Architettura
Questa versione utilizza **Spring MVC** su Java 17 per implementare la web application.
Lo stack tecnologico, a fine lavori, sarà il seguente:

- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Security](https://spring.io/projects/spring-security)
- [Hibernate ORM](https://hibernate.org/orm/) con JPA
- [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)/[MariaDB](https://mariadb.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Tomcat 8/10 (embedded)](https://tomcat.apache.org/)
- [Bootstrap 5](https://getbootstrap.com/)
- [JQuery](https://jquery.com/)
- [Eclipse IDE](https://eclipseide.org/)
- [Yada Framework](https://github.com/xtianus/yadaframework)

Lo Yada Framework non è che una mia raccolta di librerie, utility, snippet e pattern che mi consentono di velocizzare lo sviluppo di applicazioni web di questo tipo.

Altre librerie e tool che uso e vorrei menzionare sono [Commons Configuration](https://commons.apache.org/proper/commons-configuration/), [FlyWay](https://flywaydb.org/), [Logback](https://logback.qos.ch/), [Gradle Build Tool](https://gradle.org/), [SASS](https://sass-lang.com/).

Qui sotto descriverò i passi di sviluppo in fasi dove ogni fase implementa uno o più requisiti, indicando il tempo impiegato in hh:mm (escludendo la scrittura della documentazione). Su git metterò un tag per ogni fase in modo che si possano vedere facilmente le differenze.

## Fase 0: setup iniziale (1:04)
Con "setup iniziale" intendo tutto ciò che occorre per creare un ambiente di sviluppo che mi consenta di visualizzare una pagina web vuota caricata da server. Generalmente questo comprende anche il database, ma visto che la persistenza dei voti è posticipata al requisito 3, ho aspettato.

Chi volesse cimentarsi con l'installazione di tale setup può seguire le istruzioni dello Yada Framework al capitolo ["Getting Started"](https://yadaframework.net/en/newEclipseProject.html). Il tutto è pensato per girare su Eclipse ma credo servano pochi adattamenti per usare altri IDE.

Se invece si volesse lanciare il server senza ricorrere a un IDE, occorre fare il checkout del webapp-compare e dello Yada Framework in un root folder comune, quindi lanciare runSite.bat che si trova nel folder MyDoodle.

## Fase 1: poll cablato (0:54)
*Requisiti 1+2*

Il codice HTML del poll l'ha scritto ChatGPT con questa sequenza di prompt:

	- Make a radio with 3 elements: Yes, No, Maybe
	- Do the same but make one such group for each day from 1 to 7
	- I need "No" to be the default

Allo stesso modo ha scritto il CSS. Ho poi aggiunto Bootstrap per dare un default grafico decente e sistemato qualcosa. Con Thymeleaf ho estratto il form in un file isolato per avere un approccio modulare che potrebbe aiutarmi in seguito.

Il risultato è grezzo ma funzionale, per ora.

![Screenshot](/springmvc/readme.files/fase1-2.jpg)

## Fase 2: salvataggio e modifica (4:10)
*Requisito 3*

Ho configurato un database MariaDB embedded così chi vuole provare non deve avere MySQL ma basta che lanci l'applicazione e parte. Si abilita cambiando la configurazione in `conf.webapp.dev.xml`. Il db viene salvato in `/srv/wcpdev/embeddedDB` (path configurabile). E' tutto già implementato in Yada Framework.

Ho iniziato con la definizione degli @Entity da cui generare lo schema del database automaticamente con il task gradle "dbSchema". Ho copiato lo schema tra i resource files dell'applicazione (`V001__baseline.sql`) in modo che FlyWay lo carichi sul DB allo startup se manca. 

Il mio modello è costituito dalle classi `Poll` e `Vote`. Per il momento, visto che è tutto cablato, uso solo `Vote`.

Mentre faccio l'HTML mi sembra che avere tutto cablato renda l'implementazione eccessivamente laboriosa visto che si tratta di replicare tutto per sette volte (i sette giorni) e dopo qualche tentativo ho deciso che non conviene seguire la scaletta dei requisiti ma di passare direttamente al poll dinamico per non perdere troppo tempo in cose inutili.
Ho istanziato quindi un `Poll` di default con i suoi sette `Vote`, e scritto un HTML che mostra i `Vote` esistenti e accetta i voti dell'utente. Ho speso tre ore per fare e rifare tutto, poi mi sono pentito perché si perde lo scopo, ovvero il confronto con l'approccio adottato su altre piattaforme. Ho quindi riscritto l'HTML in maniera quasi-statica, nel senso che il poll è sì cablato ma grazie a Thymeleaf non devo ripetere tutto. 

Per memorizzare il voto invio la scelta al server via ajax (più moderno di un submit classico) e la salvo nel db.

Per fare solo la versione finale impiego un'ora, ma il tempo speso per il resto lo recupererò più avanti.

## Fase 3: utenti fake distinti (1:10)
*Requisito 4*

Questa fase mi è venuta abbastanza velocemente perché avevo già gli utenti nel db pronti per essere usati (creati da Yada in fase 0).
Ho quindi chiesto a ChatGPT il codice js per creare lo uuid e metterlo in un cookie (avevo inizialmente usato localstorage ma mi sono poi scontrato col fatto che al primo load della pagina, visto che il poll è subito lì, non potevo mandare lo uuid al server). Sempre ChatGPT ha scritto la funzione java per prelevare il cookie dalla request.

Ho poi fatto una piccola modifica al model, rigenerato lo schema, e creato un nuovo script per FlyWay in modo da aggiornare il database allo startup, allineando tutti gli eventuali ambienti.

Adesso ogni voto è associato a un utente per cui in teoria la webapp è multiutente, sempre con la limitazione che non c'è ancora il login e per entrare con un nuovo utente bisogna cancellare il cookie.

![Screenshot](/springmvc/readme.files/fase3.jpg)

## Fase 4: chiusura del poll (1:38)
*Requisiti 5+6*

Visto che il requisito 6 prevede che l'utente veda il giorno scelto quando si collega dopo la deadline, dovendo memorizzare la scelta da qualche parte ho optato per l'utilizzo dell'oggetto Poll che avevo già creato. L'ho quindi introdotto nel flusso, creando un poll di default e assegnandolo a ogni voto. 

Il controllo della deadline è gestito da Spring con una semplice annotation:
```
@Scheduled(cron = "0 59 23 * * ?")
```
Ogni notte viene eseguito il metodo che controlla se il poll è terminato e calcola il risultato, assegnandolo al poll stesso. Per comodità ho aggiunto un link in pagina che fa la stessa cosa, da usare per i test.

![Screenshot](/springmvc/readme.files/fase4.jpg)

## Fase 5: login (1:51)
*Requisito 7*

Ho inserito gli utenti cablati "user1", "user2"... "user5" aggiungendoli al file di configurazione `conf.webapp.dev.xml` in modo che vengano creati all'avvio del server. Ho poi deciso di spostare le pagine protette da login sotto "/user" e questo ha comportato un po' di refactoring, spostando il codice sia HTML che Java, anche per una questione di ordine.
La home è diventata un form di login. Il post è gestito da Spring Security e fa atterrare sulla pagina del poll. Gli errori di login sono mostrati sul form.
Tutto il codice per creare gli utenti random è stato rimosso.

![Screenshot](/springmvc/readme.files/fase5.jpg)

## Fase 6: invio email (0:46)
*Requisito 8*

Quando il poll viene chiuso, invia un'email di notifica a tutti gli utenti che hanno espresso il loro voto. Le credenziali del mail server sono nel file locale `/srv/wcpdev/bin/security.properties`. Alcune informazioni tecniche si possono trovare nella documentazione di [Yada Framework](https://yadaframework.net/en/emails.html).

![Screenshot](/springmvc/readme.files/fase6.jpg)

## Fase 7: i18n + responsive (1:22)
*Requisito 9+10*

Sia *i18n* che *responsive* erano sostanzialmente già implementati gratis: ho dovuto solo configurare i linguaggi in uso e implementare il menu di cambio lingua. La parte più laboriosa è stata sostituire i testi cablati in italiano con le chiavi di `message.properties`, cosa che in effetti si potrebbe evitare di fare esaustivamente in questo esercizio.

PS: al commit precedente mi ero dimenticato la traduzione delle mail. In questo commit ho tradotto la mail di chiusura poll per mostrare come la lingua venga scelta in automatico.

![Screenshot](/springmvc/readme.files/fase7.jpg)

## Fase 8: registrazione autonoma (1:10)
*Requisito 11*

Ho aggiunto una pagina di registrazione che invia un'email di conferma: cliccando sul link ricevuto, l'utente viene registrato.

Non c'è nessuna funzionalità di cambio password o gestione profilo. Eventuali link presenti sono rotti (in attesa di implementazione).

Non ho scritto tutte le traduzioni perché non è questo lo scopo.

## Fase 9: gestione poll (5:22)
*Requisito 12*

Questa è stata la fase più lunga perché la possibilità di aggiungere dinamicamente i poll ha anche richiesto la modifica di varie parti del codice che presumevano l'esistenza di un solo poll cablato, oltre al fatto che erano mesi che non mettevo mano al codice e ho dovuto ingranare.

Questa versione ha una pagina "dashboard" dove è possibile aggiungere nuovi poll, vedere i poll esistenti (solo i propri), modificarli e cancellarli. La pagina "home" mostra tutti i poll esistenti e consente di votarli o vedere il giorno più votato. La tabella dei poll è implementata con [DataTables](https://datatables.net/) sfruttando gli automatismi offerti da [Yada Framework](https://yadaframework.net/en/datatables.html) (attualmente non documentati).

![Screenshot](/springmvc/readme.files/fase9.jpg)

## Conclusioni
La versione attuale dell'applicazione soddisfa tutti i requisiti richiesti ed è stata implementata in 18 ore e 55 minuti, quindi in meno di tre giornate di lavoro. Qualche ora sarebbe stata risparmiata procedendo direttamente alla realizzazione dei requisiti finali senza passare per le fasi intermedie, concepite per favorire la collaborazione. Certo molte cose possono essere migliorate, nuove funzionalità possono essere aggiunte, e sicuramente ci sono dei bug che non ho rilevato, ma ritengo lo scopo raggiunto.


Spero che qualcuno colga lo spunto e provi a implementare qualche parte in maniera diversa, dando origine a un confronto, sicuramente istruttivo per tutti, che possa essere oggetto di una prossima riunione del [JUG Genova](https://juggenova.it/).

# Quick Run
L'applicazione può essere provata semplicemente scaricando la più recente versione dei sorgenti e lanciando `runSite.bat` su windows o `runSite.sh` su linux. Poi puntare il
browser su `localhost:8080`.

In fase di startup viene creata un'utenza predefinita `test@example.com` con password `qweqweqwe`. E' comunque possibile registrarsi con qualunque utenza tenendo presente che le email di conferma non verranno inviate (va prima configurato un mail server) per cui sarà necessario reperire nel log il link di conferma da copiare nel browser.

L'unico prerequisito per lanciare l'applicazione è che sia presente nel sistema una qualunque versione di Java (dalla 8 in su): verranno scaricate tutte le librerie necessarie, installato il DB e lanciato il server. Tutti i file aggiunti allo startup saranno contenuti o nel folder di lancio o in `/srv/wcpdev` per cui è anche facile disfarsene dopo aver stoppato il server e killato il processo `mariadbd`.

Più nel dettaglio, la procedura guidata è questa:

- assicurarsi di avere un java installato. Si può scaricare da [java.com](https://www.java.com/en/download/) oppure su linux ottenere con un package manager tipo `apt install openjdk-8-jre`
- scaricare i sorgenti, clonandoli via git oppure scompattando [lo zip](https://github.com/juggenova/webapp-compare/archive/refs/heads/main.zip)
- su linux sono necessarie queste operazioni aggiuntive dopo essere entrati nel folder `MyDoodle` via shell: 


	```
    chmod +x runSite.sh gradlew
	sudo mkdir /srv/wcpdev
	sudo chown iltuoutente /srv/wcpdev
	```

- lanciare `runSite.bat` oppure `./runSite.sh`, con doppio click oppure da command prompt
- aprire il browser su `localhost:8080`


    

