# RatorPOC
POC automatizace Raptor
Po checkoutu zadat mvn eclipse:eclipse, vytvori se projekt pro Eclipse

**Dulezite!**
Do tridy Temp, pole je treba dat svuj login a heslo do clance domeny

## Prace s davkami
- Je nutne mit XEF soubor
- Format nazvu XXX_01.xef, kde XXX je unikatni cislo
- Soubor newFilename.txt musi obsahovat XXX cislo posledni davky
- Obecne se nerekomenduje se s tim moc hrat, maximalni pocet prijatych davek za **rok** je 999

**Struktura davky, stravitelna pro batchmaker**
...
2016|QWE|1|
PS|301|N|*000X-00301-1-01*|N|TISK01|pod1|701456|1|Vata|Karel|67887655|vata@zu.cz|1|NA-Latal|Bohdan|||ASDF|X|20120101|M|ZCC|Praha|CZ|OP|1336585888|20201230|CZ|MU Praha 1|MNB|2|N|Y|Y|Balcarova|239||Kladno|10000|CZ||||||||603741256||jkalny@centrum.cz|2|N||100||500|J|M||Y||Babka|Jiri|||9007076760|19900707|100|Plynarenska|155|36|Teplice|10000|CZ||||||||||||||||||||||||||||||||||||||||||||N|N|||||||CZ|CZ|BR|12345|Y|1223455|19|123|2200|
...

##Kontrola stazenych PDF
Je nutne zadat cestu, kam se soubory ukladaji