# SAÉ2.01: Développement orienté objets</h1>
BRIAULT Maxence, JARNOUX Antoine, DE MOURA Thomas, Lotto Maxime, GELIS Adrien et PARISSE Chloé (Info Groupe C)

&nbsp;

## 1. Préface

Implémenter une classe pour représenter le graphe du réseau
de métro, ainsi que des méthodes pour calculer le plus court chemin
entre deux stations à l'aide des algorithmes de Dijkstra ou de BellmanFord.

Il est possible de :

* Visualiser le graphe sur une carte en permettant aux utilisateur de mieux comprendre la géographie du réseau et de visualiser le trajet optimal proposé par l'algorithme.

&nbsp;

## 2. Cahier des charges

### Fonctionnalités pour le sujet #2 – Métro parisien

1. [x] **Être capable de lire le réseau des lignes de métro avec leurs caractéristiques, stockés dans un fichier. Un format est proposé en annexe et le jeu de données est fourni.**
    1. [x] Bonus : compléter les lignes de métro par les lignes RER en se limitant aux stations intra-muros.

2. [x] **Afficher les éléments du réseau métropolitain à la demande de l’utilisateur :**
    1. [x] La liste des stations d’une ligne donnée.
    2. [x] La ou les correspondances possibles entre deux lignes.
    3. [x] Les trajets possibles entre deux stations, en indiquant le nombre de stations, le nombre de correspondances, le temps estimé du trajet (on comptera 3 min entre deux stations et 6 min par correspondance).

3. [x] **Être capable de fournir, sous forme de texte, le trajet le plus court, ou utilisant le moins de correspondances, entre deux stations.**
    1. [x] Bonus : afficher le graphe et saisir les 2 stations sur le graphe ; afficher visuellement les chemins le plus court en durée ou celui utilisant le moins de correspondances.

4. [x] **Être capable de fournir le trajet, le plus court en temps, entre deux stations passant par une troisième station étape.**
    1. [x] Bonus : afficher le graphe et saisir les 2 stations et la station étape sur le graphe ; afficher visuellement le chemin le plus courts en durée ou celui utilisant le moins de correspondances.

5. [x] **Analyse plus poussée du graphe des lignes de métro :**
    1. [x] Les stations proches d’une station donnée ou reliées par une arête donnée (analyse 1-distance).
    2. [x] Dire si deux stations sont reliées à 𝑝𝑝-distance.
    3. [x] Comparer 2 stations A et B : pour chacune, dire laquelle est la plus ACCESSIBLE que l’autre (la plus proche d’une correspondance), laquelle est la plus CENTRALE (possède le plus de correspondances à 𝑝𝑝-distance, la valeur de 𝑝𝑝 étant donnée par l’utilisateur) et laquelle est la plus TERMINALE (plus proche en temps d’un terminus).

6. [x] **Bonus : proposer un algorithme qui permet de trouver l’arbre couvrant minimum (ACM) reliant toutes les stations. En quoi ce réseau serait-il avantageux ou pas pour la RATP ? et pour les utilisateurs ?**

&nbsp;

## 3. Informations

Pour ce projet nous avons décidé d'utiliser le language `java`. Nous utilisons `maven` pour la gestion des dépendances (`Spring`, `JUnit`, etc.) et de la compilation.

> 📌 Pour copier les dépendances dans le fichier de sorti, utiliser la commande `mvndependency:copy-dependencies`.

Pour la gestion de version, nous avons utilisé `Git` avec `Github`. A chaque push, Github test le code pour s'assurer qu'aucune erreur n'a été introduite dans le code. Si les test ne se sont pas dérouler comme prévu une croix rouge s'affichera à coté du commit.

> 📌 Si jamais vous voyez plein d'erreurs en ouvrant le projet. Cela signifie que votre maven (ou votre IDE) est mal configuré.

&nbsp;

## 4. Déploiement

Utiliser la commande `mvn install` dans le dossier `sae201`. Un fichier .jar sera généré dans le dossier `sae201/target` *(Il s'agit de la partie Java Server)*.

> 📌 `mvn javadoc:javadoc` permet de mettre à jour la documentation.

Partie FrontEnd :

* `npm install` pour installer les dépendances.
* `npm run dev` pour lancer en mode DEV ou `npm run build` pour compiler.
* `npm run start` pour lancer l'execution compilée.

&nbsp;

## 5. Dossiers

```bash
├───.github                             Tout ce qui est lié à GitHub (Scripts Auto et badges)
│   ├───badges
│   └───workflows
├───CSV                                 CSV chargés dans la migration base de données
├───DbMigration                         Le script python qui prend le CSV et génere la BDD
├───DemoPython                          Script donné en exemple
├───frontend                            Toute la partie affichage du trajet
│   ├───components
│   ├───public
│   │   ├───images
│   │   │   └───metroIcon
│   │   └───svgLines
│   ├───src
│   │   └───app
│   │       └───Map
│   ├───types
│   └───utils
├───jsscripts                           Tous les scripts aidant à générer la carte
├───sae201                              Dossier principal du projet
│   ├───src
│   │   ├───main
│   │   │   └───java
│   │   │       └───fr
│   │   │           └───uwu
│   │   │               └───utils
│   │   └───test
│   │       ├───java
│   │       │   └───fr
│   │       │       └───uwu
│   │       └───resources
│   └───target
│       ├───classes
│       │   └───fr
│       │       └───uwu
│       │           └───utils
│       └───test-classes
│           └───fr
│               └───uwu
└───SVGMetros                           Carte véctorielle des lignes de métro et RER
```
