## Le projet

Prototype d'une application de transfert d'argent entre utilisateurs. 

## L'application

Les utilisateurs doivent pouvoir :
 - s'inscrire à l'aide d'un identifiant e-mail unique,
 - se connecter avec leurs identifiants personnels,
 - ajouter d'autres utilisateurs à leur bénéficiaires pour leur transférer de l'argent. 

## Modèle Physique de Données

Le diagramme EER ci-dessous décrit la structure des tables et les relations entre elles dans la base de données.

![alt text](paymybuddy.png)

### Tables :
- **user** : contient les informations sur les utilisateurs.<br>  
*id* : identifiant unique de l'utilisateur,<br>
*username* : nom d'utilisateur,<br>
*email* : adresse e-mail (ne peut pas être partagée entre plusieurs utilisateurs),<br>
*password* : mot de passe,<br>
*balance* : solde du compte en euros (ne peut pas être négatif),<br>
*deleted_at* : date de suppression du compte (soft-delete),<br>
*active_email* : colonne générée pour contenir l'adresse e-mail d'un utilisateur actif. Si le compte de l'utilisateur a été supprimé, un nouveau compte avec la même adresse e-mail peut être créé.<br>

**Le champ `active_email` permet de conserver l'unicité de l'adresse e-mail uniquement parmi les comptes utilisateurs actifs** (non supprimés).   
   
- **transfer** : enregistre les transferts entre utilisateurs.<br>  
*id* : identifiant unique de la transaction,<br>
*sender* : identifiant de l'utilisateur qui envoie de l'argent,<br>
*receiver* : identifiant de l'utilisateur qui reçoit de l'argent,<br>
*description* : description de la transaction qui permet de renseigner un motif,
*amount* : montant de la transaction.<br>
  
Les transferts sont liés aux utilisateurs via les clés étrangères `sender` et `receiver` qui font référence à `user.id`. 
Un index sur `amount` permet d'accélérer les recherches, notamment lors de filtres ou tris par montant.
   
- **user_beneficiary** : gère les relations entre utilisateurs.<br>  
*user_id* : identifiant de l'utilisateur qui a ajouté un bénéficiaire,<br>
*beneficiary_id* : identifiant de l'utilisateur ajouté aux bénéficiaires (la réciprocité n'est pas obligatoire).<br>
   
Les relations entre utilisateurs sont gérées via les clés étrangères `user_id` et `beneficiary_id` qui font toutes deux référence à `user.id`.<br>
La table `user_beneficiary` contient ainsi uniquement des associations entre identifiants d'utilisateurs.<br>
**Chaque couple (user_id, beneficiary_id) est unique** : un même bénéficiaire ne peut pas être ajouté plusieurs fois par le même utilisateur.<br>

### Schéma de la base de données et données initiales

[Le schéma](src/main/resources/schema.sql), conçu pour être utilisé avec MySQL, contient non seulement la définition complète des tables et des relations (clés primaires, clés étrangères, contraintes), mais aussi un **jeu de données initial**.  
Ces données permettent de comprendre le modèle en contexte réel et de tester rapidement le fonctionnement de l'application sans avoir à insérer manuellement des enregistrements. 

La base de données doit être créée avant le démarrage de l'application. 

### Création de la base de données

Dans un terminal, se connecter à MySQL avec la commande <code>mysql -u root -p</code>, puis saisir le mot de passe.<br>
Une fois connecté, créer la base de données avec la commande <code>CREATE DATABASE paymybuddy;</code>

### Démarrage de l'application

Créer un profil local en dupliquant le contenu du fichier [application.properties](src/main/resources/application.properties) dans un fichier application-local.properties. 

Renseigner les informations de connexion à la base de données locale dans le fichier applications-local.properties. Ce fichier ne doit pas être commité dans le dépôt.

#### Depuis un terminal

Démarrer l'application avec <code>./mvnw spring-boot:run</code>

#### Depuis un IDE
 - **Eclipse** / **STS**

Importer le projet.  
Run As -> Spring Boot App.

 - **Visual Studio Code**

Installer les extensions "Spring Boot Extension Pack" et "Java Extension Pack" si nécessaire.   
Ouvrir le projet dans VS Code.  
Lancer l'application depuis le Spring Boot Dashboard : Run. 

 - **IntelliJ IDEA**

Ouvrir le projet en tant que Maven project.  
Depuis la classe PayMyBuddyApiApplication : Run. 