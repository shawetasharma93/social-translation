package com.day.cq.social.comments;

/**
 * Small Class to produce pseudo random I18N strings to be used for testing.
 */
public class RandomI18NString {

    /**
     * Small unfinished method to randomly select a language, then grabs and returns random string from selected
     * language. Returns String
     */
    public static String randomI18NLanguageAndString() {
        String language;
        String randomString = null;

        String[] randomLanguage = new String[5];
        randomLanguage[0] = "RandomFrenchString";
        randomLanguage[1] = "RandomSpanishString";
        randomLanguage[2] = "RandomGermanString";
        randomLanguage[3] = "RandomSimpflifiedChineseString";
        randomLanguage[4] = "RandomJapaneseString";
        int i = (int) Math.floor(randomLanguage.length * Math.random());

        language = randomLanguage[i];

        // To do - add reflective method so random language can be used to call randomlangstring function here

        return randomString;
    }

    /**
     * Generate's random French String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomFrenchString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Jamais je n'avais senti si cruellement sa solitude et la mienne.";
        randomString[1] = "Voulez-vous cesser de me cracher dessus pendant que vous parlez!";
        randomString[2] = "Faire d'une pierre deux coups";
        randomString[3] = "C'est la fin des haricots";
        randomString[4] = "Sept cents migrants secourus dans le canal de Sicile";
        randomString[5] = "Bonjour aux chiens de nos voisins";
        randomString[6] = "Je voudrais un paquet de biscuits";
        randomString[7] = "Le vrai philosphe n'attend rien des homes";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Spanish String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomSpanishString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Para todo mal, mezcal, para toda bien, tambien";
        randomString[1] = "Experiencia es lo que obtienes cuando no obtiene lo que quería";
        randomString[2] = "Con el dinero baila el perro.";
        randomString[3] = "Una copa de vino blanco, por favor.";
        randomString[4] = "La habitacion esta vacia";
        randomString[5] = "Para todo mal, mezcal, para toda bien, tambien. ";
        randomString[6] = "Un solo idioma nunca es suficiente";
        randomString[7] = "Mi bebida se siente sola";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random German String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomGermanString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Kiefer Faktor Wolle Wissen";
        randomString[1] = "Das nennen Sie ein Schinken-Sandwich?";
        randomString[2] = "Gibt es eine Zwischenpause";
        randomString[3] = "Aus Schaden wird man klug";
        randomString[4] = "Das Ei will kluger als die Henne sein";
        randomString[5] = "Nie schneller als dein Schutzengel fliegen kann fahren";
        randomString[6] = "Der Drachen besteht darauf, dass wir ihm eine Jungfrau als Opfer bringen";
        randomString[7] = "die volkswagon hat wieder abgebaut";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Traditional Chinese String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomTChineseString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "我們去郊遊吧";
        randomString[1] = "今天天氣不好";
        randomString[2] = "我想去玩遊戲";
        randomString[3] = "他喜歡打乒乓球";
        randomString[4] = "我要買房";
        randomString[5] = "我肚子疼";
        randomString[6] = "明天霧霾天氣";
        randomString[7] = "今天早晨有會";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Korean String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomKoreanString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "운동 선수들은 무료 전화 득점자의 경우 득점하지 않았다";
        randomString[1] = "모든 필드 골은 소계 있습니다";
        randomString[2] = "오늘 바람";
        randomString[3] = "최근 자주 만나";
        randomString[4] = "다음 주에 집은 내 꺼야";
        randomString[5] = "신체적 불편";
        randomString[6] = "메리 크리스마스";
        randomString[7] = "이 화면은 꽤 충분하지 않습니다";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Swedish String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomSwedishString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Den här skärmen är inte tillräckligt söt";
        randomString[1] = "Det finns en hel del vacker tunnelbana";
        randomString[2] = "Det är en lång väg";
        randomString[3] = "Fram och tillbaka till en timme";
        randomString[4] = "Jag köpte en ny mobiltelefon";
        randomString[5] = "Jag satte minnet upp till 16G";
        randomString[6] = "Endast söt hund Teddy";
        randomString[7] = "Hemlängtan som sin mor";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Turkish String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomTurkishString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Annesi gibi hasreti";
        randomString[1] = "Ben kahvaltı için bir kase aldım";
        randomString[2] = "Ben yeni bir adaptör için uygulanan";
        randomString[3] = "Istasyon gürültülü yanındaki";
        randomString[4] = "Ben ikiyüzlülük inatçı adamı sevmiyorum";
        randomString[5] = "Maltoz tatlı tat";
        randomString[6] = "Yarın diş hekimine gitmek için";
        randomString[7] = "Benim cüzdan bozuldu";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Dutch String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomDutchString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Mijn portemonnee is gebroken";
        randomString[1] = "vandaag drukke";
        randomString[2] = "We willen ruimen het weekend";
        randomString[3] = "Onlangs is een goede eetlust";
        randomString[4] = "Eigenaar goede houding";
        randomString[5] = "Deze test write erg lang";
        randomString[6] = "Ik ben hoofdvak in Software Development";
        randomString[7] = "Ik hou van Coke";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Russian String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomRussianString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Яйцо ролл";
        randomString[1] = "Сегодня, в игре есть деятельность";
        randomString[2] = "Интернет-магазин легко";
        randomString[3] = "Эта дорога небезопасно";
        randomString[4] = "Пищевые добавки";
        randomString[5] = "собака ванны";
        randomString[6] = "Лекарства для бабушки";
        randomString[7] = "Ее деменция становится все хуже";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Polish String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomPolishString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "Lubię colę";
        randomString[1] = "Lubię grać w trzech zabitych";
        randomString[2] = "Kupiłem dużo pyszne";
        randomString[3] = "Uwielbiam owoce morza";
        randomString[4] = "Jest plan gry";
        randomString[5] = "Wietrznie dzisiaj";
        randomString[6] = "Pikantny smak ryb";
        randomString[7] = "roll jaj";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Italian String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomItalianString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "La sua demenza sta peggiorando";
        randomString[1] = "In grado di rilevare automaticamente la lingua";
        randomString[2] = "La tecnologia è molto avanzata";
        randomString[3] = "Non può essere l'automazione over-confident";
        randomString[4] = "mal di schiena";
        randomString[5] = "Martedì prossimo il bus";
        randomString[6] = "Formalità per andare a scuola";
        randomString[7] = "conti di Pechino";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Port String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomPortString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "contas Pequim";
        randomString[1] = "Desconforto físico";
        randomString[2] = "Correu por um longo tempo";
        randomString[3] = "Falei na aprendizagem de Inglês";
        randomString[4] = "Gramática do Inglês é difícil";
        randomString[5] = "Eu não tenho nenhum talento para línguas";
        randomString[6] = "Eu sou uma pessoa carinhosa, mas não é uma pessoa sensível";
        randomString[7] = "Para levantar cedo amanhã";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Simplified Chinese String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomSChineseString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "一个好朋友从风暴盾牌";
        randomString[1] = "对不起";
        randomString[2] = "我想吃饭";
        randomString[3] = "单束不能支持大房子";
        randomString[4] = "乌鸦是黑色的世界各地";
        randomString[5] = "老马知道的方式";
        randomString[6] = "你皮子痒";
        randomString[7] = "我学汉语一个月了";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random Japanese String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomJapaneseString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "そのカエルをなめるな";
        // bad
        randomString[1] = "わたしは元気です";
        randomString[2] = "迷ってしまいました";
        randomString[3] = "ちょっと待ってください";
        randomString[4] = "井の中の蛙大海を知らず";
        randomString[5] = "植木に水をやる";
        randomString[6] = "若いうちに楽しい時間を過ごすべきです";
        randomString[7] = "今日の天気予報は";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

    /**
     * Generate's random English String which is CQ random node compatible.
     * @Returns String
     */
    public static String randomEnglishString() {
        String escapedString = null;

        String[] randomString = new String[8];
        randomString[0] = "The evil that men do lives after them; the good is oft interred with their bones.";
        randomString[1] = "All work and no play makes Jack a dull boy";
        randomString[2] = "Necessity is the mother of invention";
        randomString[3] = "The proof of the pudding is in the eating";
        randomString[4] = "One disaster rarely comes alone";
        randomString[5] = "To catch bears you need honey";
        randomString[6] = "You Press the Button, We Do the Rest";
        randomString[7] = "It takes a licking and keeps on ticking";
        int i = (int) Math.floor(randomString.length * Math.random());

        escapedString = randomString[i];

        return escapedString;
    }

}
