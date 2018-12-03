# Thesis - 176210
### Realizzazione di un Web Crawler di News sfruttando il protocollo Sitemap

L'obiettivo è realizzare un web crawler per le news sfruttando il protocollo sitemap. 
I compiti che deve svolgere sono:

+ Estrazione sitemap dal bucket di CommonCrawl. 
+ Riconoscimento di una Google News Sitemap.
+ Estrazione informazioni da una sitemap.

L'app fa uso del cloud di Amazon e di alcune funzioni AmazonLambda. L'estrazione dei dati dal bucket è realizzata 
tramite ApacheSpark. E' stata realizzata anche una demo grafica utilizzando ReactJS che consente all'utente di seguire
una serie di siti e ricevere notifiche sulle news.

La repository si divide in 3 parti:

+ Server Apache che contiene il controller e la gestione del db
+ Parte di gestione dell'UI in ReactJS
+ Parte di analisi e costruzione del training set

Sono inoltre presenti una serie di tabelle che contengono test effettuati durante la fase di studio.

Per poter eseguire l'app è necessario disporre di un account Amazon AWS per inserire accessKey e secretKey.

Per lanciare l'UI nella cartella è necessario lanciare
```javascript 
npm install 
```
ed in seguito il comando:
```javascript 
npm start
```
