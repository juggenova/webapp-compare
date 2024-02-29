# springmvc
**Author:** Cristian Ghezzi

## Architettura
Questa versione utilizza **Spring MVC** su Java 17 per implementare la web application.
Lo stack tecnologico, a fine lavori, sarà il seguente:

- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Hibernate ORM](https://hibernate.org/orm/)
- [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)/[MariaDB](https://mariadb.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Tomcat 8/10 (embedded)](https://tomcat.apache.org/)
- [Bootstrap 5](https://getbootstrap.com/)
- [JQuery](https://jquery.com/)
- [Gradle Build Tool](https://gradle.org/)
- [Eclipse IDE](https://eclipseide.org/)
- [Yada Framework](https://github.com/xtianus/yadaframework)

Lo Yada Framework non è che una mia raccolta di librerie, utility, snippet e pattern che mi consentono di velocizzare lo sviluppo di applicazioni web di questo tipo.

Altre librerie e tool che uso e vorrei menzionare sono [Commons Configuration](https://commons.apache.org/proper/commons-configuration/), [FlyWay](https://flywaydb.org/), [Logback](https://logback.qos.ch/), [Spring Security](https://spring.io/projects/spring-security).

Qui sotto descriverò una fase per ogni requisito previsto, indicando il tempo impiegato in hh:mm (escludendo la scrittura della documentazione).

## Fase 0: setup iniziale (1:04)
Con "setup iniziale" intendo tutto ciò che occorre per creare un ambiente di sviluppo che mi consenta di visualizzare una pagina web vuota caricata da server. Generalmente questo comprende anche il database, ma visto che la persistenza dei voti è posticipata al requisito 3, ho aspettato.

Chi volesse cimentarsi con l'installazione di tale setup può seguire le istruzioni dello Yada Framework al capitolo ["Getting Started"](https://yadaframework.net/en/newEclipseProject.html). Il tutto è pensato per girare su Eclipse ma credo servano pochi adattamenti per usare altri IDE.


