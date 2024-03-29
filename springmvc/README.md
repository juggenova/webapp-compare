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

