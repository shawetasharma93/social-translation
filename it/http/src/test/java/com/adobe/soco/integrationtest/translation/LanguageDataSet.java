/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2014 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package com.adobe.soco.integrationtest.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageDataSet {
    private static int MAX_STRING_COUNT = 1; // language string set length
    private static Map<String, String[]> languagedataSet = null;
    private static Map<String, String> unSupportedLanguagedataSet = null;
    private static List<String> defaultLanguageSet = null;

    private static String randomStrings_EN[] = {"I love my country. I hate my accountant", ""};

    private static String randomStrings_FR[] = {
        "J'aime mon pays. Je déteste mon comptable. J'espère que cette traduction va continuer à travailler", ""};

    private static String randomStrings_DE[] = {
        "Ich liebe mein Land. Ich hasse mein Buchhalter. Ich hoffe, dass diese Übersetzung weiterhin funktioniert",
        ""};

    private static String randomStrings_JP[] = {"私の国が大好きです。私の会計士を憎みます。この翻訳は引き続き動作を願っています", ""};

    private static String randomStrings_AR[] = {
        "أنا أحب بلدي. أنا أكره بلدي المحاسب. ويحدوني الأمل في هذه الترجمة وسوف تواصل العمل", ""};
    private static String randomStrings_CN[] = {"爱我的国家。我讨厌我的会计师。我希望这个翻译将继续工作", ""};

    private static String randomStrings_TW[] = {"我愛我的國家。我討厭我的會計師。我希望這個翻譯將繼續工作", ""};

    private static String randomStrings_CS[] = {
        "Miluji svou vlast. Nesnáším můj účetní. Doufám, že tento překlad bude pokračovat v práci", ""};

    private static String randomStrings_DA[] = {
        "Jeg elsker mit land. Jeg hader min revisor. Jeg håber, at denne oversættelse vil fortsætte med at arbejde",
        ""};

    private static String 
    randomStrings_NL[] = {
        "Ik hou van mijn land. Ik haat mijn accountant. Ik hoop dat deze vertaling zal blijven om te werken", ""};
    private static String randomStrings_ET[] = {
        "Ma armastan oma riiki. Ma vihkan raamatupidaja. Loodan, et see tõlge jätkab tööd", ""};
    private static String randomStrings_FI[] = {
        "Rakastan maatani. Vihaan minun kirjanpitäjä. Toivon, että tämä käännös jatkaa", ""};
    private static String randomStrings_EL[] = {
        "Εγώ αγαπώ τη χώρα μου. Μισώ λογιστής μου. Ελπίζω ότι αυτή η μετάφραση θα συνεχίσει να εργάζεται", ""};
    private static String randomStrings_HT[] = {
        "Mwen renmen peyi m. M rayi m' kontab. Mwen espere ke tradiksyon sa a pwal kontinye travay", ""};
    private static String randomStrings_HE[] = {
        "אני אוהב את המדינה שלי אני שונא את החשבון שלי. אני מקווה שתרגום זה ימשיכו לעבוד", ""};
    private static String randomStrings_HU[] = {
        "Szeretem a hazámat. Utálom az én könyvelő. Remélem, ez a fordítás továbbra is működni fognak", ""};
    private static String randomStrings_ID[] = {
        "Saya suka negara saya. Aku benci akuntan saya. Saya berharap terjemahan ini akan terus bekerja", ""};
    private static String randomStrings_IT[] = {
        "Amo il mio paese. Odio il mio commercialista. Spero che questa traduzione continuerà a lavorare", ""};
    private static String randomStrings_KO[] = {"내가 내 나라를 사랑 합니다. 내 회계사 싫어. 두이 일을 계속 희망", ""};
    private static String randomStrings_LV[] = {
        "Es mīlu savu valsti. Es naida manu grāmatvedis. Es ceru, ka šis tulkojums būs turpināt darbu", ""};
    private static String randomStrings_LT[] = {
        "Aš myliu savo šalį. Aš nekenčiu mano buhalteris. Tikiuosi, kad šis vertimas bus toliau dirbti", ""};
    private static String randomStrings_NO[] = {
        "Jeg elsker mitt land. Jeg hater min regnskapsfører. Jeg håper denne oversettelsen vil fortsette å arbeide",
        ""};
    private static String randomStrings_PL[] = {
        "Kocham mój kraj. Nienawidzę mojego księgowego. Mam nadzieję, że tłumaczenie będzie działać", ""};
    private static String randomStrings_PT[] = {
        "Eu amo meu país. Eu odeio meu contador. Espero que esta tradução vai continuar a trabalhar", ""};
    private static String randomStrings_RO[] = {
        "Îmi place ţara mea. Urasc contabil meu. Sper că această traducere va continua să lucreze", ""};
    private static String randomStrings_RU[] = {
        "Я люблю свою страну. Я ненавижу мой бухгалтер. Я надеюсь, что этот перевод будет продолжать работать", ""};
    private static String randomStrings_SK[] = {
        "Milujem svoju krajinu. Neznášam môj účtovník. Dúfam, že tento preklad bude pokračovať v práci", ""};
    private static String randomStrings_SL[] = {
        "Jaz ljubezen moja država. Sovražim moj računovodja. Upam, da ta prevod bo še naprej sodelovala", ""};
    private static String randomStrings_ES[] = {
        "Amo a mi país. Odio a mi contador. Espero que esta traducción seguirá trabajando", ""};
    private static String randomStrings_SV[] =
        {
            "Jag älskar mitt land. Jag hatar min revisor. Jag hoppas att denna översättning kommer att fortsätta att arbeta",
            ""};
    private static String randomStrings_TH[] = {
        "รักประเทศของฉัน ฉันเกลียดนักบัญชีของฉัน ฉันหวังว่า คำแปลนี้จะทำงานต่อไป", ""};
    private static String randomStrings_TR[] = {
        "Ülkemi seviyorum. Muhasebecim nefret ediyorum. Bu çeviri çalışmaya devam umuyoruz.", ""};
    private static String randomStrings_UK[] = {
        "Я люблю свою країну. Я ненавиджу мій бухгалтер. Я сподіваюся, що цей переклад буде продовжувати працювати",
        ""};
    private static String randomStrings_VI[] = {
        "Tôi yêu đất nước của tôi. Tôi ghét kế toán của tôi. Tôi hy vọng bản dịch này sẽ tiếp tục làm việc", ""};

    private static String randomStrings_BG[] = {
        "Обичам моята страна. Аз мразя счетоводител ми. Надявам се този превод ще продължи да работи", ""};

    public static String getSupportedLanguageString(final String strCurrentLang) {
        setupUnSupportedLangMap();
        String strRetVal = unSupportedLanguagedataSet.get(strCurrentLang);
        if (strRetVal == null) {
            strRetVal = strCurrentLang;
        }
        return strRetVal;
    }

    private static void setupUnSupportedLangMap() {
        if (unSupportedLanguagedataSet == null) {
            unSupportedLanguagedataSet = new HashMap<String, String>();
            unSupportedLanguagedataSet.put("gl", "pt");
            unSupportedLanguagedataSet.put("ms", "id");
            unSupportedLanguagedataSet.put("lt", "lt_lt");
            unSupportedLanguagedataSet.put("zh-CHT", "zh_tw");
            unSupportedLanguagedataSet.put("zh-CHS", "zh_cn");
        }

    }

    public static Map<String, String> getRandomStringMap() {
        setUpLanguageData();
        final int index = (int) Math.floor(MAX_STRING_COUNT * Math.random());
        final Map<String, String> retMap = new HashMap<String, String>();
        for (final String langCode : languagedataSet.keySet()) {
            final String[] languageData = languagedataSet.get(langCode);
            retMap.put(langCode, languageData[index]);
        }

        return retMap;
    }

    public static List<String> getLanguageArray() {
        setUpLanguageData();
        final ArrayList<String> languageList = new ArrayList<String>();
        languageList.addAll(languagedataSet.keySet());
        return languageList;
    }

    private static void setUpLanguageData() {
        if (languagedataSet == null) {
            languagedataSet = new HashMap<String, String[]>();
            languagedataSet.put("en", randomStrings_EN);
            languagedataSet.put("fr", randomStrings_FR);
            languagedataSet.put("de", randomStrings_DE);
            languagedataSet.put("it", randomStrings_IT);
            languagedataSet.put("es", randomStrings_ES);
            languagedataSet.put("zh_cn", randomStrings_CN);
            languagedataSet.put("ko", randomStrings_KO);
            languagedataSet.put("pt", randomStrings_PT);
            languagedataSet.put("ja", randomStrings_JP);
            languagedataSet.put("ar", randomStrings_AR);
            languagedataSet.put("bg", randomStrings_BG);
            languagedataSet.put("cs", randomStrings_CS);
            languagedataSet.put("zh_tw", randomStrings_TW);
            languagedataSet.put("da", randomStrings_DA);
            languagedataSet.put("nl", randomStrings_NL);
            /*
             * languagedataSet.put("et", randomStrings_ET); languagedataSet.put("fi", randomStrings_FI);
             * languagedataSet.put("el", randomStrings_EL); languagedataSet.put("ht", randomStrings_HT);
             * languagedataSet.put("he", randomStrings_HE); languagedataSet.put("hu", randomStrings_HU);
             * languagedataSet.put("id", randomStrings_ID); languagedataSet.put("lv", randomStrings_LV);
             * languagedataSet.put("no", randomStrings_NO); languagedataSet.put("pl", randomStrings_PL);
             * languagedataSet.put("ro", randomStrings_RO); languagedataSet.put("ru", randomStrings_RU);
             * languagedataSet.put("sk", randomStrings_SK); languagedataSet.put("sl", randomStrings_SL);
             * languagedataSet.put("lt_lt", randomStrings_LT); languagedataSet.put("sv", randomStrings_SV);
             * languagedataSet.put("th", randomStrings_TH); languagedataSet.put("tr", randomStrings_TR);
             * languagedataSet.put("uk", randomStrings_UK); languagedataSet.put("vi", randomStrings_VI);
             */
        }
    }

    public static List<String> getDefaultLanguageArray() {
        if (defaultLanguageSet == null) {
            defaultLanguageSet = new ArrayList<String>();
            defaultLanguageSet.add("en");
            defaultLanguageSet.add("fr");
            defaultLanguageSet.add("ja");
            defaultLanguageSet.add("de");
            defaultLanguageSet.add("es");
            defaultLanguageSet.add("zh_cn");
            defaultLanguageSet.add("it");
            defaultLanguageSet.add("ko");
            defaultLanguageSet.add("pt");
        }
        return defaultLanguageSet;
    }
}
