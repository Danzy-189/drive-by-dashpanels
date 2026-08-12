# Drive by DashPanels

Аддон-мост между [Drive-By-Wire](https://www.curseforge.com/minecraft/mc-mods/drive-by-wire) и [Dashpanels](https://modrinth.com/mod/dashpanels) (`BoxxedDev/control-panels`).

**Minecraft 1.21.1 · NeoForge 21.1.235 · Java 21 · MIT**

---

## Что делает

Мод добавляет один блок — **Панельный интерфейс проводов** (`drivebydashpanels:wire_interface`).
Поставьте его вплотную к любому блоку сети Dashpanels (панель, кабель) — и интерфейс станет
двусторонним шлюзом между модулями панели и проводами Drive-By-Wire:

* **Панель → провод.** Каждый *входной* модуль сети (переключатель, ручка, рычаг, джойстик и т.д.)
  превращается в **канал провода** с именем этого модуля. Значение 0–15 постоянно передаётся в сеть
  Drive-By-Wire, откуда его читают контроллеры Sable/Create.
* **Провод → панель.** Сигнал, пришедший по проводу на интерфейс в канале `X`, подаётся в *выходной*
  модуль с именем `X` (лампа-индикатор, семисегментный индикатор, зуммер и т.п.).

### Правила имён каналов

| Тип модуля Dashpanels | Имя канала |
|---|---|
| `IInput` / `IOutput` (одиночный вход/выход) | имя модуля, например `throttle` |
| `IMultiInput` / `IMultiOutput` (много значений) | `имяМодуля.имяЗаписи`, например `joystick.x` |
| обычный редстоун (без провода DBW) | канал `world` — попадает в выходной модуль с именем `world` |

Имя модуля задаётся в конфиге модуля в Dashpanels (поле `name`). Модули без имени игнорируются.

---

## Требования

* Minecraft **1.21.1**, NeoForge **21.1.235+**
* **Drive-By-Wire** (и его зависимости: Create, Sable)
* **Dashpanels 2.2+** (и его зависимости: Create, Sable Companion, CC: Tweaked, Flywheel, Registrate)

---

## Как пользоваться

1. Соберите панель Dashpanels и дайте нужным модулям **имена** через их конфиг.
2. Поставьте блок **Панельный интерфейс проводов** рядом с панелью или кабелем сети
   (интерфейс сам находит сеть по 6 соседним блокам).
3. Возьмите в руку **Wire** из Drive-By-Wire, наведитесь на интерфейс и **крутите колесо мыши** —
   так выбирается канал; список каналов = имена модулей вашей панели.
4. Протяните провод от интерфейса к контроллеру (или наоборот, чтобы гнать сигнал в панель).
5. **ПКМ по интерфейсу без предмета** — диагностика: список каналов и их текущих значений.

---

## Сборка

```bash
gradle build          # или ./gradlew build, если сгенерировать wrapper: gradle wrapper
```

Готовый jar: `build/libs/drivebydashpanels-0.1.0.jar`.

Каждый push в `main` собирается через GitHub Actions (`.github/workflows/build.yml`):
jar лежит в артефакте `drive-by-dashpanels-jar` и в rolling-пререлизе с тегом `dev`.

### Почему сборка не требует jar-ников чужих модов

API Drive-By-Wire и Dashpanels нет в публичных Maven-репозиториях, поэтому в проекте лежат
**compile-only заглушки** этих API (`src/main/java/edn/**`, `src/main/java/moth/**`) — точные
сигнатуры классов/методов/полей из исходников обоих модов, тела бросают `AssertionError`.
В игре используются настоящие классы, а заглушки **исключаются из jar** (`jar { exclude 'edn/**', 'moth/**' }`),
и задача `verifyNoStubs` (входит в `check`) ломает сборку, если они всё-таки попали внутрь.

Если захотите собирать с настоящими jar-никами — положите их в `libs/` и добавьте
`compileOnly files(...)`, после чего удалите папки-заглушки.

---

## English (short)

Bridge addon between Drive-By-Wire and Dashpanels for MC 1.21.1 / NeoForge 21.1.235.
The `Panel Wire Interface` block exposes every named input module of the adjacent Dashpanels
network as a Drive-By-Wire channel, and drives named output modules from incoming wire signals
(multi-IO entries use `module.entry`; plain redstone arrives on the `world` channel).
Build with `gradle build`; the project ships compile-only API stubs so no external mod jars are needed.

## License

MIT — see [LICENSE](LICENSE). API stubs replicate public signatures only; Dashpanels is MIT,
Drive-By-Wire remains the property of its authors and is **not** redistributed here.
