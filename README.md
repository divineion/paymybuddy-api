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
*active_email* : colonne calculée dynamiquement à partir des champs èmail`et `deleted_at` pour contenir l'adresse e-mail d'un utilisateur actif. Si le compte de l'utilisateur a été supprimé, un nouveau compte avec la même adresse e-mail peut être créé.<br>

**Le champ `active_email` permet de conserver l'unicité de l'adresse e-mail uniquement parmi les comptes utilisateurs actifs** (non supprimés).   
   
- **transfer** : enregistre les transferts entre utilisateurs.<br>  
*id* : identifiant unique de la transaction,<br>
*sender* : identifiant de l'utilisateur qui envoie de l'argent,<br>
*receiver* : identifiant de l'utilisateur qui reçoit de l'argent,<br>
*description* : description de la transaction qui permet de renseigner un motif,
*amount_excluding_fees* : montant hors frais de la transaction : c'est le montant qui sera versé au bénéficiaire,
*fees* : frais de service appliqués à chaque transaction (par défaut : 0,5%),
*total_amount* : montant total de la transaction (par défaut, le montant versé au bénéficiaire + les frais).<br>
  
Les transferts sont liés aux utilisateurs via les clés étrangères `sender` et `receiver` qui font référence à `user.id`. 
   
- **user_beneficiary** : gère les relations entre utilisateurs.<br>  
*user_id* : identifiant de l'utilisateur qui a ajouté un bénéficiaire,<br>
*beneficiary_id* : identifiant de l'utilisateur ajouté aux bénéficiaires (la réciprocité n'est pas obligatoire).<br>
   
Les relations entre utilisateurs sont gérées via les clés étrangères `user_id` et `beneficiary_id` qui font toutes deux référence à `user.id`.<br>
La table `user_beneficiary` contient ainsi uniquement des associations entre identifiants d'utilisateurs.<br>
**Chaque couple (user_id, beneficiary_id) est unique** : un même bénéficiaire ne peut pas être ajouté plusieurs fois par le même utilisateur.<br>

### Initialisation du schéma de la base de données et données initiales

#### Avec script SQL

La configuration courante du projet dans [application.properties](src/main/resources/application.properties) est définie sur `mysql`.
[Le schéma](src/main/resources/schema-mysql.sql), conçu pour être utilisé avec MySQL, contient non seulement la définition complète des tables et des relations (clés primaires, clés étrangères, contraintes), et un **jeu de données initial**.

[Une version PostgreSQL du schéma](src/main/resources/schema-postgresql.sql) initial peut être initialisée en définissant le profil `postgresql`.

#### Avec Hibernate

Le schéma de la base de données peut également être généré automatiquement par Hibernate au démarrage de l'application, sur la base des entités JPA du projet, en définissant le profil sur `hibernate-init`. 

La base de données doit être créée avant le démarrage de l'application. 
Le premier démarrage de l'application entraîne la génération du schéma de données et l'insertion de données. 

### Création de la base de données

Avec MySQL ou PostgreSQL : 
<code>CREATE DATABASE paymybuddy;</code>

### Démarrage de l'application

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