# SpaceInvahess

SpaceInvahess est un jeu inspiré de Space Invaders, avec une jouabilité à l'horizontale. Ce jeu est développé en Java et est jouable via l'environnement de développement IntelliJ IDEA, avec la version JDK 21. Ce README contient des instructions pour lancer le projet, une description des modes de jeu disponibles, et des informations supplémentaires.

## Description du jeu

### Style de jeu

- **Demo** : L'objectif de la demo et de permettre au joueur de découvrir le jeu. Il pourra ensuite se familiariser avec les commandes afin de définir les touches qui lui conviennent via les paramètres -> `Settings`.
- **Arcade** : L'objectif est de réaliser le meilleur score possible. Le joueur commence avec trois vies et doit éviter les ennemis ainsi qu'esquiver les projectiles pour survivre. Le joueur perd une vie s'il subit trop de dégâts ou s'il entre en collision avec un vaisseau ennemi. Le jeu se termine quand le joueur perd toutes ses vies.

## Prérequis

- **Java Development Kit (JDK)** : Assurez-vous d'avoir la version 21 du JDK installée sur votre machine.
- **IDE** : Utilisez IntelliJ IDEA pour exécuter le projet.

## Installation et lancement du projet

1. **Clonez le projet** : Récupérez le code source en clonant ce dépôt avec la commande suivante :
   ```bash
   git clone https://github.com/EpitechMscProPromo2027/T-JAV-501-LIL_9.git
   ```
2. **Ouvrez le projet dans IntelliJ** :
   Lancez IntelliJ IDEA et choisissez l'option `Ouvrir` pour sélectionner le dossier contenant les fichiers du projet.

3. **Configurer le JDK** :

- Allez dans les `Settings` d'IntelliJ.
- Sous `Project Structure`, sélectionnez `Project SDK` et choisissez `JDK 21`.

4. **Exécuter le projet** : Localisez la classe principale dans le dossier `src` (souvent nommée `Main` ou similaire). Faites un clic droit dessus et sélectionnez `Run 'Main'` pour lancer le jeu.

5. **UML Diagram** : Un diagramme UML décrivant la structure du projet est disponible dans le fichier `SpaceInvahess.drawio`. Ce diagramme peut être ouvert avec Draw.io pour une vue d’ensemble de l'architecture et des relations entre les classes.

6. **Javadoc** : Une Javadoc est disponnible si on lance la page HTML grâce au chemin suivant `./javadoc/index.html`.

## Commandes de jeu

Utilisez les touches suivantes pour contrôler votre vaisseau (par défaut) :

- `Z` pour voler vers le haut
- `S` pour voler vers le bas
- `Q` pour voler vers l'arrière
- `D` pour voler vers l'avant
- `SPACE` pour tirer 

Vous pouvez également personnaliser vos commandes via les **Settings** dans l'interface du jeu.

***ATTENTION : le jeu est configuré en `QWERTY` de base***

## Choix de vaisseau et autres paramètres

Dans les **Settings** du jeu, vous pouvez choisir le type de vaisseau que vous souhaitez piloter :

- Le **Rafale**, qui tire des roquettes.
- Sinon, le **Warthog**, qui tire des balles.

Il suffit de séléctionner la case correspondante dans les paramètres du jeu.

## Choix de résolution

Dans les **Settings**, vous avez également la possibilité de sélectionner la résolution de la fenêtre de jeu selon vos préférences.

## Position de la fenêtre

Notez que la fenêtre du jeu ne se positionne pas automatiquement en haut à gauche de l'écran. Il est donc recommandé de **placer manuellement la fenêtre dans le coin supérieur gauche** pour une meilleure expérience de jeu.

