# SwingPaintProgram

Egyszerű vektoros rajzprogram Java Swinggel. Vonalat, téglalapot és ellipszist lehet rajzolni, a meglévő alakzatokat kijelölni, mozgatni, átszínezni és törölni. A felhasználói műveleteket a Command minta kezeli, ezért visszavonhatók és újra végrehajthatók.

## Követelmények

- Java 26 vagy újabb
- Maven 3.9 vagy újabb

## Futtatás

Nyisd meg a projektet NetBeansben Maven projektként, majd futtasd a `com.mycompany.swingpaintprogram.SwingPaintProgram` osztályt. Parancssorból a tesztek futtatása:

```sh
mvn test
```

## Használat

- Válassz eszközt a felső eszköztáron, majd húzd az egeret a vásznon.
- A **Kijelölés** eszközzel kattints egy alakzatra, majd húzd az új helyére.
- A **Szín** gomb a következő alakzat színét állítja be; kijelölt alakzatnál azonnal, visszavonható paranccsal módosítja annak színét.
- A kijelölt alakzat a **Törlés** gombbal vagy a `Delete` billentyűvel törölhető.
- Visszavonás: `Ctrl+Z`. Újra: `Ctrl+Y`.

## Felépítés

- `model`: Swingtől független `Rajz` modell, absztrakt `Alakzat`, `Vonal`, `Teglalap`, `Ellipszis`.
- `command`: a `Parancs` interfész, a négy konkrét művelet és a két veremmel működő `ParancsKezelo`.
- `ui`: Swing ablak, eszköztár és `paintComponent`-et használó rajzvászon.
- `src/test`: JUnit 5 tesztek a parancskezelő és a parancsok működésére, Swing felület nélkül.

Az osztálydiagram a [`docs/class-diagram.puml`](docs/class-diagram.puml) fájlban található.

## Bemutatóvideó

A körülbelül egyperces felvételhez készült [forgatókönyv](docs/demo-script.md). A videót `demo/paint-demo.mp4` néven kell elmenteni a repóba.
