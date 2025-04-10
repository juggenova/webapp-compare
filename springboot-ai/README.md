# springboot-ai
**Author:** Cristian Ghezzi
**AI Agent:** Augment Code

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
