# 🗺️ Caccia Al Tesoro V2 - OCT

![Version](https://img.shields.io/badge/version-v2.0-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-green)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-All_Rights_Reserved-red)

**Caccia Al Tesoro V2** è un'applicazione web progettata per la gestione e il coordinamento simultaneo del gioco "Caccia al Tesoro" con un'affluenza di accessi in contemporanea nell'ordine delle centinaia geolocalizzate su scala nazionale.
L'applicazione, sviluppata amatorialmente è stata ideata in occasione dell'edizione 2025/26 "Campionati della Cultura e del Talento", concorso indetto dall'Associazione Olimpiadi della Cultura e del Talento. 
Il sistema possiede un meccanismo di smistaggio che permette alle singole squadre di essere associata, automaticamente, alla loro zona di competenza (*la sede locale dove si svolge la competizione, la quale è suddivisa in 3 macroaree nord, centro e sud*).

> [!WARNING]
> **Copyright & Licenza:** Questo software è protetto da diritto d'autore (All Rights Reserved). Non è consentita la copia, distribuzione, modifica, o alcun uso commerciale o personale del codice senza esplicita autorizzazione. Il codice è pubblico esclusivamente a scopo di visualizzazione.

> [!IMPORTANT]
> **Stato del Progetto:** L'applicazione è stata utilizzata attivamente con successo per le semifinali OCT 2025-2026. Per info sull'iter delle competizioni, consultare i canali ufficiali.

---

#### 🔗 Link Utili
* **Sito Ufficiale OCT:** [olimpiadidellacultura.it](https://www.olimpiadidellacultura.it/)
* **Repository:** [RMChannel/Caccia-Al-Tesoro-25-26-OCT](https://github.com/RMChannel/Caccia-Al-Tesoro-25-26-OCT)

---

#### 📑 Indice
*   [Caratteristiche Principali](#-caratteristiche-principali)
*   [Dinamiche di Gioco](#-dinamiche-di-gioco)
*   [Tech Stack](#-tech-stack)
*   [Struttura del Progetto](#-struttura-del-progetto)
*   [Installazione e Configurazione (Java)](#-installazione-e-configurazione-java)
*   [Deployment con Docker](#-deployment-con-docker)
*   [Utilizzo e Credenziali](#-utilizzo-e-credenziali)

---

#### 🚀 Caratteristiche Principali
*   **Gestione Multi-Team (CSV Import):** Autenticazione sicura dei team partecipanti, suddivisi per macro-regioni (es. NORD, CENTRO, SUD). Il caricamento e la generazione massiva degli "*Account Squadra*" avviene in automatico e comodamente dal pannello Admin, processando un file CSV.
*   **Classifica Real-Time:** Aggiornamento asincrono della classifica tramite AJAX polling ogni 10 secondi, offrendo alle squadre un feedback in diretta senza dover ricaricare la pagina web. Inoltre, la webapp, permette di consultare la classifica filtrando la macroarea di appartenenza o visualizzando la classifica globale.
*   **Spareggio di Precisione:** Tie-breaker automatico basato sul timestamp esatto dell'ultimo elemento sbloccato.
*   **Role-Based Access Control:** Autorizzazioni di sicurezza stratificate tramite Spring Security per marginare netti confini tra i normali giocatori (Player) e gli amministratori (Admin).

---

#### 🗺️ Dinamiche e Funzionamento del Gioco

Il gioco è strutturato con una **progressione a incastro** basata su velocità di risoluzione tramite problem-solving.
La risoluzione degli enigmi richiede cultura generale e capacità di orientamento tramite la consultazione della mappa fornita.
Il gioco comprende un totale di **20 livelli, suddivisi in 10 Luoghi esplorabili e 10 Quesiti da risolvere sulla webapp**.

*   **📍 Scansione QR Code (Luoghi):** La squadra, consultando la mappa, raggiunge il luogo indicato il quale tramite la risoluzione di un enigma, troverà il QR Code nascosto. Una volta trovato il QR, tramite app, dovrà scannerizzarlo o inserire il codice, scritto al di sotto del QR, per sbloccare lo step successivo sull'app.
*   **🧩 Risoluzione Enigmi (Quesiti):** Il QR Code abilita un quiz logico/culturale. Risolvendo il quesito, il sistema verificherà la risposta e sbloccherà il luogo successivo che verrà visualizzato sulla mini-mappa.
*   **🏆 Punteggio e Premi:** La competizione termina al completamento della 10° tappa. Sulla graduatoria saranno visualizzate prima le tappe completate e poi il *timestamp* di completamento. Per ogni sede di svolgimento, la squadra che si classifica al primo posto vince.
*   **📜 Regole di Condotta:** È consentito l'uso di internet (salvo diversa indicazione del singolo quesito) ed è severamente vietato manomettere o rimuovere i QR Code nell'ambiente per ostacolare gli avversari.

---

#### 💻 Tech Stack

##### Backend
*   **Java 21**
*   **Spring Boot 3.5.7** (Core Framework)
*   **Spring Security:** Gestione degli accessi (Role-Based Access Control per percorsi `/admin` e `/caccia`), crypting delle password in **BCrypt** e Session Management avanzato (tramite `SessionRegistryImpl`).
*   **Gestione Mail Asincrona:** Utilizzo di `JavaMailSender` e `@Async` per smistare task di sistema senza bloccare i thread del controller HTTP.
*   **Spring Data JPA / Hibernate:** Gestione persistenza e interrogazione del DB relazionale.
*   **Spring Boot Actuator:** Endpoint esposti per il monitoring applicativo (es. metriche Prometheus).
*   **Maven** (Dependency Management)

##### Frontend
*   **Thymeleaf** (Server-side Java Template Engine)
*   **HTML5 / CSS3** (Custom Stylesheet in `src/main/resources/static/css`)
*   **JavaScript Vanilla** (Logica client-side, visualizzazione real-time in `src/main/resources/static/js`)

##### Database & Storage
*   **MySQL 8.0+** (Per l'ambiente di produzione)
*   **H2 Database** (Utilizzato in ambiente di test)

---

#### 📂 Struttura del Progetto

La struttura del codice sorgente segue il robusto pattern MVC e ricalca la logica in domini concentrici:

```text
src
└── main
    └── java
        └── com
            └── oct
                └── caccia_al_tesorov2
                    ├── Controller
                    │   ├── Admin       (Gestione Utenti, Aggiunta Team, Ticket)
                    │   ├── Caccia      (Logica dei Luoghi e controllo password dei Quesiti)
                    │   ├── Classifica  (Leaderboard con aggiornamenti Asincroni)
                    │   ├── Login       (Autenticazione utenze)
                    │   └── Support     (Gestione assistenza e ticket)
                    ├── DBPopulator     (Inizializzazione del set di dati predefinito)
                    └── Model
                        ├── Email       (Servizio invio mail di utilità)
                        ├── Luogo       (Entità e strato di servizio per i checkpoint geografici)
                        ├── Quesito     (Entità e strato di servizio per le domande web)
                        ├── Relazioni   (Entità associative fra Utenti, Luoghi e Quesiti sbloccati)
                        ├── Support     (Logica di gestione richieste)
                        └── Users       (Gestione Auth e CustomUserDetails)
```

---

#### ☕ Installazione e Configurazione (Java)

Questa sezione guida alla configurazione manuale dell'ambiente di sviluppo locale.

**Requisiti:**
*   Java SDK (JDK) 21.
*   Database MySQL in ascolto (predisporre lo schema `cacciaaltesoro` o variare l'URI in `application.properties`).
*   Apache Maven.
*   Git.

**Passaggi:**

1.  **Clonare il repository:**
    ```bash
    git clone https://github.com/RMChannel/Caccia-Al-Tesoro-25-26-OCT.git
    ```

2.  **Configurazione Properties:**
    Assicurarsi di aver impostato le seguenti variabili di ambiente richieste nel file `application.properties`:
    *   `DB_URL` (es. `jdbc:mysql://localhost:3306/cacciaaltesoro`)
    *   `DB_USER`
    *   `DB_PASS`
    *   `EMAIL` (Indirizzo email per l'invio e la ricezione)
    *   `EMAIL_PASSWORD` (Password per l'SMTP)

3.  **Compilazione:**
    Posizionarsi nella root del progetto ed eseguire:
    ```bash
    mvn clean package
    ```
    *(verrà generato un eseguibile `.jar` nella cartella `/target`)*

4.  **Esecuzione Locale:**
    ```bash
    java -jar target/CacciaAlTesoro_V2-0.0.1-SNAPSHOT.jar
    ```
    Oppure sfruttare il Maven Wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```
    L'applicazione risponderà all'indirizzo `http://localhost:8080` (salvo override della _server.port_).

---

#### 🐳 Deployment con Docker

L'applicazione permette di essere containerizzata e distribuita utilizzando il `Dockerfile` incluso nella radice del progetto, assicurando una porta di esecuzione replicabile, stabile e portatile.

**Build e Run:**

```bash
# Avviare la build dell'immagine Docker locale
docker build -t caccia-al-tesoro-v2 .

# Eseguire un container in background inoltrando la porta 8080
docker run -d --name caccia-app \
-e DB_URL="jdbc:mysql://host.docker.internal:3306/cacciaaltesoro" \
-e DB_USER="root" \
-e DB_PASS="tua_password" \
-e EMAIL="tua_email@gmail.com" \
-e EMAIL_PASSWORD="tua_password_app_email" \
-p 8080:8080 \
caccia-al-tesoro-v2
```

---

#### 🔑 Utilizzo e Credenziali

Il database all'avvio garantisce meccanismi di seeding e popolamento di utenze base per test (tramite la classe `DBPopulator`).

**Ruoli implementati:**

| Privilegio / Ruolo | Funzionalità |
| :--- | :--- |
| **ADMIN** | Amministrazione generale: import massivo e automatico delle squadre tramite file CSV, modifica manuale, visualizzazione tabulati dei log per le location sbloccate, revisione ticket di supporto. Non compete in classifica. |
| **PLAYER** | Account di tipo "Team" per i licei ospitanti: possiedono l'assegnamento univoco ad una **Regione**, risolvono gli enigmi e il loro punteggio accresce i contatori realtime della classifica. |
