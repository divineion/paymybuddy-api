## Le projet

Prototype d'une application de transfert d'argent entre utilisateurs. 

## L'application

Les utilisateurs doivent pouvoir :
 - s'inscrire à l'aide d'un identifiant e-mail unique,
 - se connecter avec leurs identifiants personnels,
 - ajouter d'autres utilisateurs à leur bénéficiaires pour leur transférer de l'argent. 

## Modèle Physique de Données

Le diagramme EER ci-dessous décrit la structure des tables et les relations entre elles dans la base de données.

![diagramme EER représentant les tables de la base de données paymybuddy et leurs relations.](paymybuddy.png "Diagramme") 

### Tables :

- **role** : contient les informations sur les rôles des utilisateurs.    
*id* : identifiant unique du rôle, auto-incrémenté,   
*name* : nom du rôle (exemple : user, admin).    
   

- **user** : contient les informations sur les utilisateurs.    
*id* : identifiant unique de l'utilisateur, auto-incrémenté   
*username* : nom d'utilisateur,   
*email* : adresse e-mail (ne peut pas être partagée entre plusieurs utilisateurs),   
*deleted_at* : date de suppression du compte (soft-delete),   
*password* : mot de passe,   
*balance* : solde du compte en euros (ne peut pas être négatif),   
*active_email* : colonne virtuelle calculée dynamiquement à partir des champs `email` et `deleted_at` pour contenir l'adresse e-mail d'un utilisateur actif. Si le compte de l'utilisateur a été supprimé, un nouveau compte avec la même adresse e-mail peut être créé.    
*role* : role attribué à l'utilisateur (définit ses permissions).   
    
**Le champ `role` référence l'identifiant du rôle dans la table role.**    
**Le champ `active_email` permet de conserver l'unicité de l'adresse e-mail uniquement parmi les comptes utilisateurs actifs** (non supprimés).    
   
      
- **transfer** : enregistre les transferts entre utilisateurs.   
*id* : identifiant unique de la transaction, auto-incrémenté,   
*sender* : identifiant de l'utilisateur qui envoie de l'argent,   
*receiver* : identifiant de l'utilisateur qui reçoit de l'argent,   
*description* : description de la transaction (motif),   
*amount_excluding_fees* : montant hors frais de la transaction : c'est le montant qui sera versé au bénéficiaire,   
*fees* : frais de service appliqués à chaque transaction (par défaut : 0,5%),   
*total_amount* : colonne virtuelle générée automatiquement, montant total de la transaction (par défaut, le montant versé au bénéficiaire + les frais).     
  
Les transferts sont liés aux utilisateurs via les clés étrangères `sender` et `receiver` qui font référence à `user.id`. 
   
- **user_beneficiary** : gère les relations entre utilisateurs.     
*user_id* : identifiant de l'utilisateur qui a ajouté un bénéficiaire,   
*beneficiary_id* : identifiant de l'utilisateur ajouté aux bénéficiaires (la réciprocité n'est pas obligatoire).   
   
Les relations entre utilisateurs sont gérées via les clés étrangères `user_id` et `beneficiary_id` qui font toutes deux référence à `user.id`.   
La table `user_beneficiary` contient ainsi uniquement des associations entre identifiants d'utilisateurs.   
**Chaque couple (user_id, beneficiary_id) est unique** : un même bénéficiaire ne peut pas être ajouté plusieurs fois par le même utilisateur.   

### Initialisation du schéma de la base de données et données initiales

Le mot de passe administrateur doit être défini via la propriété `admin.default.password` dans `application.properties`.

La configuration courante du projet dans [application.properties](src/main/resources/application.properties) est définie sur `mysql`.   
[Le schéma](src/main/resources/schema-mysql.sql), conçu pour être utilisé avec MySQL, contient non seulement la définition complète des tables et des relations (clés primaires, clés étrangères, contraintes), et un **jeu de données initial**.

[Une version PostgreSQL du schéma](src/main/resources/schema-postgresql.sql) initial peut être initialisée en définissant le profil `postgresql`.

Une fois l'application lancée et les données initialisées via le schéma SQL, la configuration peut être modifiée pour éviter la réinitialisation de la table à chaque lancement de l'application : 

`spring.sql.init.mode=never`   

### Génération des clés secrètes JWT
L'authentification JWT est signée via une paire de clés secrètes.   
  
Générer une clé privée : <code>openssl genrsa -out private_key.pem 2048</code>   
Génerer la clé publique correspondante : <code>openssl rsa -pubout -in private_key.pem -out public_key.pem</code>   

Les deux fichiers doivent être placés dans dans `src/main/resources/jwt-keys/`.

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