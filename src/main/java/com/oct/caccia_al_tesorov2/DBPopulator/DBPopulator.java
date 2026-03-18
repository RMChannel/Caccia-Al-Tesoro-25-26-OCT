package com.oct.caccia_al_tesorov2.DBPopulator;

import com.oct.caccia_al_tesorov2.Model.ActivateGameService;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoEntity;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.PasswordUtility;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class DBPopulator {
    private final CustomUserDetailsService customUserDetailsService;
    private final LuogoService luogoService;
    private final QuesitoService quesitoService;

    public DBPopulator(CustomUserDetailsService customUserDetailsService, LuogoService luogoService, QuesitoService quesitoService) {
        this.customUserDetailsService = customUserDetailsService;
        this.luogoService = luogoService;
        this.quesitoService = quesitoService;
    }

    private void addNord() {
        luogoService.salva(new LuogoEntity("NORD-BRA", 1, "Sala Polifunzionale Giovanni Arpino", 44.6952133f, 7.8441014f, "BRA-7V2",
                "La sfida ha inizio! Cercate un monumento che sembra sfidare il peso del tempo: un grande cuneo di pietra sospeso a pesanti catene. Ha la punta rivolta verso il basso e custodisce parole incise sulla sua faccia. Non fermatevi alla superficie: guardate dove la punta quasi sfiora il suolo o dietro i suoi legami d'acciaio. Li troverete il primo portale per proseguire!",
                "È il cuore pulsante della cultura moderna a Bra. Spazio versatile ricavato da vecchi insediamenti industriali, oggi ospita conferenze, concerti e manifestazioni. È intitolata al celebre scrittore Giovanni Arpino, che visse a lungo in città, rendendola protagonista di molti suoi racconti.",
                "https://static.cuneodice.it/cuneo/foto/84035/97743.jpg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 2, "Movicentro - Sede GeoBra", 44.6941138f, 7.8456651f, "BRA-4M9",
                "Lasciate le catene e cercate il luogo dove i viaggiatori s'incontrano. Davanti alle grandi pareti di vetro, cercate un segno della primavera che sta per arrivare. È una forma accogliente, senza spigoli, che custodisce la terra e la vita: non guardate tra le foglie, ma dove il cerchio tocca il suolo. Li si nasconde il prossimo passo.",
                "Nato come nodo di interscambio per i trasporti, il Movicentro è diventato un punto di riferimento culturale. Ospita \"GeoBra\", una sezione espositiva dedicata alla geologia e al territorio, e l'ufficio turistico, porta d'ingresso per chi vuole scoprire le bellezze di Langhe e Roero.",
                "https://www.targatocn.it/fileadmin/archivio/targatocn/blocchi_traffico_mondovi/BRA_movicentro_04_2023.jpg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 3, "Museo del Giocattolo", 44.6955659f, 7.8439905f, "BRA-1X5",
                "Salite dove il tempo si è fermato ai giochi di un tempo. Cercate una stanza dove rombano motori silenziosi e mille ruote piccole non girano mai. Tra pareti che profumano di sogni e metallo, cercate il codice dove la velocità è ferma e colorata.",
                "Situato nei locali sopra la Sala Arpino, questo museo è un viaggio nel tempo. Custodisce migliaia di oggetti: dalle bambole in porcellana ai soldatini di piombo, dai trenini elettrici ai giochi da tavolo. È una delle collezioni più ricche d'Italia e racconta come è cambiata l'infanzia negli ultimi due secoli.",
                "https://www.ideawebtv.it/wp-content/uploads/2024/10/museo-giocattolo-Bra.jpg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 4, "Biblioteca Civica \"Giovanni Arpino\"", 44.6953754f, 7.8439917f, "BRA-8Q3",
                "Salite dove le storie non hanno padroni e i libri viaggiano di mano in mano. Cercate il luogo dove ogni volume aspetta solo di essere preso e portato lontano, senza chiedere nulla in cambio. Tra pagine che cercano nuovi occhi per essere lette, cercate il codice dove le storie si liberano e ricominciano il loro viaggio.",
                "Condivide l'edificio con il Museo del Giocattolo. È un centro culturale attivo con un patrimonio librario immenso. Il suo cortile interno e le sale luminose sono il luogo dove gli studenti e i cittadini di Bra si ritrovano per studiare, leggere e partecipare a laboratori letterari.",
                "https://bra-api.cloud.municipiumapp.it/s3/874/media/biblioteca-bra-1024x576.jpeg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 5, "Giardini di Piazza Giolitti", 44.6959855f, 7.8436025f, "BRA-2W6",
                "Lasciate i libri e correte dove il gioco non ha mai fine. Cercate una grande spirale gialla che promette discese veloci e risate. Non salite in cima, ma guardate proprio dove la curva ha inizio, sotto la sua pancia colorata. È li che si nasconde la scia per la prossima tappa!",
                "Un grande polmone verde a pochi passi dal centro storico. La piazza, intitolata allo statista Giovanni Giolitti, è famosa per ospitare mercati e grandi eventi cittadini (come le edizioni di Cheese). I suoi vialetti alberati sono perfetti per nascondere indizi all'aperto.",
                "https://www.cuneocronaca.it/img/articoli/1637743646areabimbi.jpg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 6, "Cimitero Urbano parcheggio", 44.6872062f, 7.8422448f, "BRA-5Z1",
                "Siete davanti al palazzo dove si decide il destino della città, tra curve barocche e muri imponenti. Anche qui, tra la pietra solenne, c'è un angolo di natura che decora il cammino. Cercate la vasca che ospita il verde proprio di fronte all'ingresso principale: il codice non è tra i fiori, ma nascosto lungo il suo bordo, dove la mano dell'uomo incontra la terra.",
                "Non solo un luogo di memoria, ma un vero museo a cielo aperto. La parte monumentale ospita cappelle gentilizie in stile liberty e neogotico che raccontano la storia delle grandi famiglie braidesi. II lungo viale d'accesso, Viale Rimembranze, è uno dei percorsi più suggestivi della città.",
                "https://bra-api.cloud.municipiumapp.it/s3/720x960/s3/874/media/mappe/cimitero.jpg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 7, "Palazzo Mathis", 44.6979051f, 7.8525142f, "BRA-9N4",
                "Raggiungete ora il cuore nobile di Bra, dove i palazzi si fanno eleganti e la storia osserva il passaggio. Di fronte alle finestre di Palazzo Mathis, cercate un uomo di bronzo che ha dedicato la vita al bene. Avvicinatevi ai suoi piedi con rispetto: il segreto non è tra le mani del santo, ma tra i gradini di pietra che lo sorreggono. Cercate bene sulla faccia verticale di uno degli scalini... è lì che troverete la chiave per proseguire!",
                "Affacciato sulla piazza principale, questo palazzo barocco è uno degli edifici più eleganti di Bra. Le sue sale, riccamente affrescate e decorate, ospitano oggi la sede di prestigiose mostre d'arte temporanee e uffici comunali di rappresentanza.",
                "https://media-cdn.tripadvisor.com/media/photo-s/05/19/5f/d7/bra.jpg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 8, "Palazzo Municipale (Comune di Bra)", 44.6981343f, 7.8521005f, "BRA-3K8",
                "Salite ora i gradini del potere, fin sulla soglia del grande portone del Comune. Una volta in cima, fermatevi e volgete le spalle all'ingresso: ora la vostra faccia è rivolta alla statua che domina il centro della piazza. Da questa posizione privilegiata, iniziate a scendere: contate esattamente tre gradini verso il basso. Proprio li, sotto i vostri piedi, si nasconde il codice per continuare la sfida!",
                "Costruito su progetto dell'architetto Bernardo Vittone, il municipio è un capolavoro del barocco piemontese. La sua facciata curva e imponente domina la piazza e rappresenta il centro amministrativo della città sin dal XVIII secolo.",
                "https://www.sigecweb.beniculturali.it/images/fullsize/ICCD1039151/ICCD13638050_00002417_00.jpg"));

        luogoService.salva(new LuogoEntity("NORD-BRA", 9, "Chiesa di Sant'Andrea", 44.6979484f, 7.8519192f, "BRA-6L0",
                "Lasciate la piazza del Comune e raggiungete la grande facciata di Sant'Andrea. Davanti a questa maestosa chiesa, cercate dove si leggono gli avvisi per la comunità. Ci sono due bacheche: ignorate quella che porta i saluti più tristi e concentratevi su quella senza messaggi particolari. Il segreto non è scritto nel foglio, ma è nascosto lungo il bordo del metallo che lo protegge. Cercate bene tra gli angoli della cornice!",
                "È la chiesa principale della città bassa. Costruita tra il 1672 e il 1682 su disegno di Gian Lorenzo Bernini (realizzato poi da Guarino Guarini), vanta una facciata maestosa e un interno solenne che ospita pregevoli opere d'arte sacra e un organo monumentale.",
                "https://upload.wikimedia.org/wikipedia/commons/5/54/Bra-chiesa_sant%27andrea-facciata.jpg"));

        luogoService.salva(new LuogoEntity( "NORD-BRA", 10, "Teatro Politeama Boglione", 44.6953822f, 7.8470827f, "BRA-0J7",
                "Siete giunti all'ultima meta, dove il sipario sta per alzarsi sul vostro successo! Davanti alla facciata del Teatro, cercate chi illumina la scena dal basso verso l'alto. Ci sono dei piccoli soli incastonati nel pavimento che accendono la notte: non calpestateli, ma cercate lungo il loro bordo circolare di metallo. Li troverete l'ultimo codice che proclama la fine della vostra ricerca. Sipario, la vittoria è vostra!",
                "Inaugurato nel 1910, è il tempio dello spettacolo a Bra. Dopo un attento restauro, il teatro ha ripreso la sua funzione originale, offrendo stagioni di prosa, musica e danza di altissimo livello. La sua struttura classica \"all'italiana\" lo rende un gioiello acustico e visivo.",
                "https://www.turismoinbra.it/wp-content/uploads/2017/11/Teatro-politeama-FOTO-GERBALDO.jpg"));
    }

    private void addCentro() {
        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 1, "Piazza della Vita", 42.0895077f, 11.7925156f, "CIV-3B8",
                "Scendete di tre verso il mare, guardando verso il cerchio, al confine di un luogo con tanta novità nel nome",
                "L’ex Piazza degli Eventi di Civitavecchia, situata sul lungomare cittadino, è oggi ufficialmente denominata Piazza della Vita. La sua ridenominazione rappresenta il culmine di un processo di riqualificazione urbana che ha trasformato l'area da zona portuale a centro di aggregazione sociale e culturale.\n",
                "https://www.centumcellae.it/wp-content/uploads/2010/09/marina4.JPG"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 2, "Il \"Bacio della memoria di un porto\"", 42.0895927f, 11.7895445f, "CIV-9L1",
                "\"Journeys end in lovers’ meeting\"… but they shall begin first\n",
                "La statua è stata inaugurata il 16 settembre 2020, in occasione dei festeggiamenti per i 150 anni della Capitaneria di Porto, da un’idea di Ivana Puleo. L’opera è stata realizzata per ricordare tutti coloro che partirono dal porto di Civitavecchia, principalmente a seguito di eventi bellici, e in alcuni casi senza far più ritorno. Non solo, è stata realizzata con l’intento di ricostruire una “memoria” perduta il 14 maggio del 1943, quando un devastante bombardamento distrusse non soltanto l’edificio storico della Capitaneria, ma gran parte del porto e dell’abitato cittadino, cancellando secoli di storia.",
                "https://civitavecchia.portmobility.it/sites/default/files/nuova_statua_del_bacio_-_porto_di_civitavecchia_10.jpg"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 3, "Forte Michelangelo", 42.090284f, 11.790729f, "CIV-5K2",
                "La leggenda lo lega a un pittore. Cercate vicino al committente.\n",
                "Forte Michelangelo fu iniziato nel 1508 e completato nel 1537 sotto il pontificato di Paolo III, con la direzione di Antonio da Sangallo il Giovane. La tradizione locale vorrebbe che il maschio, ossia il torrione principale della fortezza, sia stato progettato da Michelangelo Buonarroti, da cui il nome della fortezza. Il porto di Civitavecchia veniva usato come approdo fin dal Duecento, ma con la riorganizzazione dello Stato della Chiesa, iniziata dopo il Concilio di Costanza (1414-18), il suo porto viene restaurato e la città si arricchisce di edifici che le fonti del tempo attribuiscono a Bernardo Rossellino.\n",
                "https://lovecivitavecchia.it/wp-content/uploads/2023/10/forte_michelangelo.jpg"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 4, "Antica Rocca di Civitavecchia", 42.0941119f, 11.7894046f, "CIV-1R7",
                "A difesa e guardia del porto, proprio sull’acqua, da più di seicento anni.\n",
                "Conosciuta anche come Fortezza, La Rocca di Civitavecchia è un vero e proprio castello, costruito intorno al 1400 sotto il pontificato di Papa Callisto III a completamento delle mura di protezione del borgo marittimo dell’antica Centumcellae (l’odierna Civitavecchia).\n",
                "https://civitavecchia.portmobility.it/sites/default/files/la_rocca_di_civitavecchia.jpg"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 5, "Piazza Leandra", 42.0935198f, 11.7920502f, "CIV-8G4",
                "Non sempre ‘Acqua’ significa essere lontani. Con una sola Stella a destra, dritti davanti a voi.\n",
                "Piazza Leandra, in pieno centro storico, è la più antica piazza della città. Al centro della piazza, inserita tra due edifici d’epoca, si trova una fontana di origine medioevale riattivata nel 2015. Su di essa si affaccia la piccola e splendida Chiesa della Stella (1688), una delle più antiche di Civitavecchia.\n",
                "https://lovecivitavecchia.it/wp-content/uploads/leandra3.jpg"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 6, "Vicolo dell’archetto", 42.0937353f, 11.7920762f, "CIV-2M9",
                "In un portale tra due spazi; nel primo sgorga la vita, il secondo conduce alla Morte\n",
                "Il Passaggio dell’Archetto, sovrastato dall’omonima torre, che collega Piazza Leandra con Piazza Saffi: è l’antica porta aperta nella cinta muraria del IX secolo, che dà il nome all’omonimo quartiere e dalla quale potete raggiungere anche la Chiesa della Morte, in assoluto la più antica di tutta la città.\n",
                "https://lovecivitavecchia.it/wp-content/uploads/archetto-1.jpg"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 7, "Piazza Aurelio Saffi", 42.0938219f, 11.7922886f, "CIV-4X0",
                "Varcato l’arco, lasciatevi i crociati alle spalle, e guardate avanti.\n",
                "Piazza Aurelio Saffi ha beneficiato di recenti ristrutturazioni; tuttavia sono ancora visibili le parti intatte di mura medievali. La Piazza è conosciuta anche come Piazza San Giovanni per la presenza dell’omonima Chiesa, eretta nel 1653 e destinata all’Ordine di Malta.\n",
                "https://civitavecchia.portmobility.it/sites/default/files/piazza_aurelio_saffi.jpg"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 8, "Chiesa di Santa Maria dell’Orazione e della Morte", 42.0945118f, 11.7919794f, "CIV-7Z6",
                "Guardando in faccio alla Morte troverete le vostre risposte",
                "Conosciuta comunemente come Chiesa della Morte, è tra le più antiche di tutta Civitavecchia. Costruita nel 1685, la chiesa è legata alla nascita e all’opera della Confraternita della Morte e Orazione, istituita nella seconda metà del XVI secolo per dare degna sepoltura e suffragio ai cadaveri abbandonati fuori le mura cittadine o dispersi in mare.\n",
                "https://lovecivitavecchia.it/wp-content/uploads/Chiesa_morte.jpg"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 9, "Cattedrale di San Francesco d’Assisi", 42.0924722f, 11.7920697f, "CIV-0H3",
                "Lasciando indietro la morte, andate verso chi cantava alla vita. Badate a chi vi accoglie.\n",
                "L’intitolazione a S. Francesco d’Assisi della cattedrale trasmette la memoria dei Minori conventuali, ai quali la chiesa rimane legata nel corso dei primi due secoli della sua storia.\n",
                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/09/be/8f/0b/20151212-144325-largejpg.jpg?w=900&h=500&s=1"));

        luogoService.salva(new LuogoEntity("CENTRO-CIVITAVECCHIA", 10, "Teatro Traiano", 42.0915166f, 11.7934324f, "CIV-6V5",
                "A caccia finita, andate a divertirvi. Sarete ospiti dell’optimus princeps.\n",
                "Pensato e voluto in occasione della venuta di Papa Gregorio XVI il 20 maggio del 1835, in sostituzione del vecchio Teatro Minozzi, Il Teatro Traiano viene inaugurato il 4 maggio 1844, dopo sei anni di progetti e lavori.\n",
                "https://upload.wikimedia.org/wikipedia/commons/a/a7/Facciata_Teatro_Traiano_Civitavecchia.JPG"));
    }

    private void addSud() {
        luogoService.salva(new LuogoEntity("SUD-MELFI", 1, "Villa Comunale", 40.994781f, 15.654852f, "MLF-2P6",
                "Il mio nome evoca antichi vaticini, ma la mia storia è scritta nel valore. Portai soccorso quando la terra tremò lontano e guidai questa città con la fascia del primo cittadino. Mi trovi dove il bronzo sorveglia il verde: cerca il riposo ai piedi del Generale per trovare la tua via.",
                "Il parco è ufficialmente intitolato ad Ascanio Sibilla, un illustre cittadino melfitano.",
                "https://www.comune.melfi.pz.it/sites/default/files/melfi-villa.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 2, "Porta Venosina", 40.993508f, 15.658311f, "MLF-8Z4",
                "Sono l'ultima rimasta a guardia della strada che guarda a levante, figlia del Re che volle mura invincibili. Sotto il mio arco passò la storia normanna, ma oggi il mio segreto si specchia dove l'acqua un tempo scorreva. Guarda avanti, verso il fossato, per trovare ciò che cerchi.\n",
                "È l'unica superstite delle antiche porte di accesso che un tempo si aprivano lungo la possente cinta muraria della città.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Melfi_Porta_Venosina.jpg/1024px-Melfi_Porta_Venosina.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 3, "Torre di Via Ronca Battista", 40.994512f, 15.659341f, "MLF-6Y1",
                "Sono la sentinella tonda vestita di lava, nata quando i Normanni fecero di queste mura una capitale. Non offro spigoli ai colpi del nemico, ma li lascio scivolare via come il tempo. Oggi ti parlo attraverso un leggio di metallo, ma ciò che cerchi non è nelle scritte: guarda alle spalle della mia storia.\n",
                "Edificata intorno all'XI secolo, faceva parte del complesso sistema difensivo voluto dai Normanni per proteggere la \"capitale\" del loro ducato.",
                "https://www.comune.melfi.pz.it/sites/default/files/melfi-torre.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 4, "Chiesa della Madonna del Carmine", 40.995421f, 15.659523f, "MLF-3D9",
                "Un tempo portavo il nome del pellegrino Giacomo, ma oggi vesto i colori del monte sacro. Non cercare tra i volumi o nei canti, ma segui lo sguardo della Signora. La sua mano non mente e ti indica la via: guarda dove punta il suo dito per svelare il segreto nascosto.",
                "Fondata originariamente nel XVI secolo, la chiesa era inizialmente dedicata a San Giacomo.",
                "https://www.diocesimelfi.it/wp-content/uploads/chiesa-carmine-melfi.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 5, "Piazza Umberto I", 40.995914f, 15.658821f, "MLF-1T5",
                "Un tempo qui si riunivano i Nobili per decidere le sorti della città, all'ombra del vecchio convento. Oggi sono il cuore dell'accoglienza, dove chi arriva cerca consiglio e chi parte aspetta il proprio carro. Cerca il varco dove la città si presenta al forestiero: il segreto è custodito tra le mura della Pro-loco.\n",
                "Storicamente nota come Piazza del Seggio, era il luogo dove si riuniva l'antico sedile dei nobili per amministrare la città.",
                "https://www.comune.melfi.pz.it/sites/default/files/piazza-umberto.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 6, "Piazza Duomo", 40.996781f, 15.658438f, "MLF-9X8",
                "Nacqui tra spade normanne e sogni bizantini, consacrata dal Guiscardo per restare eterna. Il fuoco mi ferì, ma gli Angioini mi rialzarono verso il cielo con grifoni e bifore che sfidano il vento. Oggi il mio segreto non sta in alto tra le campane, ma dove il cammino si fa dolce e senza scalini: cerca nel ferro della via agevole per trovare la tua musa.",
                "Voluta da Roberto il Guiscardo nell'XI secolo, fu consacrata nel 1067.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Cattedrale_Melfi_2022_dicembre.jpg/1024px-Cattedrale_Melfi_2022_dicembre.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 7, "Parco Federico II", 40.998412f, 15.651452f, "MLF-4S2",
                "Sulla terra dove lo Stupor Mundi dettò le sue leggi eterne, oggi il tempo vola tra le risa dei fanciulli. Non cercare tra le pergamene del 1231, ma dove il movimento si fa ritmo costante. Sotto il sedile che sfida il cielo e ritorna, la tua ricerca troverà il suo equilibrio. Guarda sotto ciò che dondola nel vento.\n",
                "Il parco è di concezione relativamente moderna (rispetto ai monumenti medievali circostanti), ma sorge su un'area storicamente strategica.",
                "https://www.melfilive.it/wp-content/uploads/parco-federico.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 8, "Statue Bronzee", 40.998612f, 15.652152f, "MLF-7N0",
                "Qui il tempo si è fermato nel mezzo di un salto. Due amici di bronzo giocano alla cavallina sulla via che sale al Castello, sospesi in un balzo che non finisce mai. Non guardare l’altezza del volo, ma osserva dove puntano i piedi di chi sta sopra: la loro direzione è la tua unica bussola. Cerca lì, dove il bronzo indica il terreno.\n",
                "Situate lungo la salita che conduce verso il Castello, queste sculture raccontano visivamente i protagonisti delle Costituzioni di Melfi (1231).",
                "https://www.basilicatanet.it/melfi-statue-bronzee.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 9, "Castello Di Melfi", 40.998333f, 15.652839f, "MLF-0A3",
                "Ecco il castello d'età medievale,\n" +
                        "con le sue torri e la corte reale.\n" +
                        "Tra stanze segrete e l'antico museo,\n" +
                        "trovare la traccia è il vostro trofeo.\n" +
                        "Siete già pronti a varcare il portone,\n" +
                        "per fare all'interno la grande ispezione...\n" +
                        "\u200BMa un cacciatore che ha l'occhio sincero,\n" +
                        "non entra di corsa in un grande maniero.\n" +
                        "Fermatevi un attimo, fate attenzione:\n" +
                        "per questo indizio non serve il salone.\n" +
                        "Non c'è un soffitto che copre il segreto,\n" +
                        "ma l'aria leggera e il sasso consueto.",
                "Edificato in posizione strategica su una collina vulcanica, fu la sede di cinque concili papali.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Melfi_Il_Castello.jpg/1024px-Melfi_Il_Castello.jpg"));

        luogoService.salva(new LuogoEntity("SUD-MELFI", 10, "Belvedere dei Normanni", 40.999012f, 15.653552f, "MLF-5H7",
                "Da questa terrazza lo sguardo va lontano,\n" +
                        "su valli, su tetti, su monte e su piano.\n" +
                        "La vista è un incanto, è un vero piacere,\n" +
                        "siete arrivati al famoso \"Belvedere\".\n" +
                        "Ma non guardate il paesaggio per trovare l'indizio,\n" +
                        "dovete cercare un diverso artifizio.\n" +
                        "Quando il sole tramonta e la luna discende,\n" +
                        "c'è un occhio di vetro che il muro accende.",
                "La caratteristica principale del Belvedere è la prospettiva frontale sul Castello di Melfi.",
                "https://www.melfi.com/media/belvedere-normanni.jpg"));
    }

    private void addEnna() {
        luogoService.salva(new LuogoEntity("SUD-ENNA", 1, "Belvedere Marconi", 37.566418f, 14.273612f, "ENN-1H3",
                "\"Scivola verso un panorama… mozzafiato\"",
                "Con l'arrivo dei Greci in Sicilia (tra I'VIII e il V secolo a.C.), la città fu chiamata Henna.",
                "https://cdn-processed.tmatic.travel/5e420975-a8b4-4c2a-9ccd-5f98a03bcf86.jpg"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 2, "Fontana del ratto di Proserpina", 37.567215f, 14.277312f, "ENN-4J9",
                "\"Dove l’amore oscuro ruba la primavera e il marmo ferma per sempre quell’istante, un lume illumina la strada\"",
                "Secondo la leggenda, Persefone stava raccogliendo fiori quando Ade la rapì.",
                "https://hub-api.comune.enna.it/Resources/Public/2945510770/proserpina_01.jpg"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 3, "Rocca di Cerere", 37.569421f, 14.288912f, "ENN-7K2",
                "\"Un’alta Roccia lega la fanciulla alla madre, un legame d’acciaio nel punto più alto\"",
                "Già luogo sacro in epoca pre-greca per riti legati alla fertilità.",
                "https://hub-api.comune.enna.it/Resources/Public/rocca-di-cerere-6-scaled.jpg--622525833..jpg"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 4, "Castello di Lombardia", 37.567542f, 14.287512f, "ENN-0W5",
                "\"Sotto le mura del castello un re schiavo ancora lancia un grido di libertà\"",
                "Una delle più grandi fortezze siciliane, ampliata dai Normanni e da Federico II.",
                "https://hub-api.comune.enna.it/Resources/Public/3542837591/DJI_0019.jpg"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 5, "Duomo di Enna", 37.567312f, 14.282815f, "ENN-3M8",
                "\"Ampio e solenne, custodisce secoli di fede e devozione\"",
                "Costruito nel 1307 in stile gotico-catalano, con tre navate e un importante soffitto ligneo decorato.",
                "https://hub-api.comune.enna.it/Resources/Public/13266271860/duomo%20(29).JPG"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 6, "Chiesa di Santa Chiara", 37.566842f, 14.279812f, "ENN-9L1",
                "\"Qui vivevano le monache di clausura; è la Chiesa che porta il nome della loro fondatrice\"",
                "Risalente al XVII secolo e legata al monastero delle Clarisse.",
                "https://lh6.googleusercontent.com/proxy/3tr3HI1KNmKAcdWyVdBw408G6iXnRSycppnleQseKwbkG5bl1aBieZJGDgHgW038OVyX3bDx0Rxni4CUdb01nPjA9Ud49UpIKghU_Q4"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 7, "Chiesa di San Giuseppe", 37.566212f, 14.278142f, "ENN-6Z4",
                "\"Tra le Chiese della città, cerca quella che porta il nome del santo del 19 marzo\"",
                "Eretta nel XVII secolo con decorazioni ricche e scenografiche.",
                "https://cdn.archilovers.com/projects/c_383_dea7e499-93c8-4e6e-b4cd-dfb460f604e3.jpg"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 8, "Piazza Giuseppe Garibaldi", 37.566342f, 14.276412f, "ENN-2B7",
                "\"Ai piedi di un palazzo del potere, su un piazzale che prende il nome da un eroe di due Mondi\"",
                "Legata alla Spedizione dei Mille, ospita una lapide all'esterno del Palazzo Varisano.",
                "https://lh4.googleusercontent.com/proxy/QlPW27uD7DFe4kfxgRr52BiYnre012Hb8MplVjpy3xvDXpgKMQSb7mzics8TzF4snJNTLEvvwH45CrgxAPvXY99fvj1SFoY30EruQdussizocDo8_hX3tNUfK6IEMIA"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 9, "Municipio (Palazzo di Città)", 37.566112f, 14.275142f, "ENN-5F0",
                "\"Andate nel posto in cui vengono prese le decisioni per la città\"",
                "Simbolo del ruolo politico acquisito nel 1927 quando Enna fu proclamata capoluogo di provincia.",
                "https://www.lorenzotaccioli.it/wp-content/uploads/2024/02/Teatro-Garibaldi-di-Enna-dentro-al-palazzo-di-Citta.jpg"));

        luogoService.salva(new LuogoEntity("SUD-ENNA", 10, "Teatro Comunale F. P. Neglia", 37.565512f, 14.274212f, "ENN-8R3",
                "\"Porta il nome di un musicista ennese, che trasformava note e armonie in emozioni. Oggi qui le storie continuano a vivere\"",
                "Rappresenta la trasformazione contemporanea della città, evolutasi in capoluogo moderno.",
                "https://hub-api.comune.enna.it/Resources/Public/teatro-garibaldi-scaled.jpg-1083728762..jpg"));
    }

    private void addQuesito() {
        quesitoService.salva(new QuesitoEntity(1, "torre-hanoi","HanoiTowerCode156329419OCT"));
        quesitoService.salva(new QuesitoEntity(2, "rebus","Microstati enclavimicrostati enclaviMicrostati Enclavimicrostati Enclavi"));
        quesitoService.salva(new QuesitoEntity(3, "morse","WolframiowolframioWOLFRAMIO"));
        quesitoService.salva(new QuesitoEntity(4, "film","Pulp Fictionpulp fictionpulp FictionPulp fiction"));
        quesitoService.salva(new QuesitoEntity(5, "canzone","EdSheraanYeah2109rfejnwjodnv120832"));
        quesitoService.salva(new QuesitoEntity(6, "tacham","Forza4LesgoskiLesgo"));
        quesitoService.salva(new QuesitoEntity(7, "mastermind","MasterMindFucksMyLife"));
        quesitoService.salva(new QuesitoEntity(8, "slidingImage","SlidingImageQuadroDani"));
        quesitoService.salva(new QuesitoEntity(9,"4images","4imagesDaniCumuloWtf"));
        quesitoService.salva(new QuesitoEntity(10,"sudoku","SudokuDoItYeah"));
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            ActivateGameService.setGameActive(false);
            try {
                customUserDetailsService.loadUserByUsername("rcito");
                System.out.println("Admin già esistente");
            } catch (UsernameNotFoundException e) {
                addQuesito();
                addNord();
                addCentro();
                addSud();
                addEnna();
                UserEntity user = new UserEntity("rcito", PasswordUtility.hashPassword("Legion7@"), "ADMIN");
                customUserDetailsService.addUser(user);
                System.out.println("Admin aggiunto");
            }
        };
    }
}