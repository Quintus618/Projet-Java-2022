# Projet-Java-2022

Projet de "clavardage" (messagerie ou chat informatique) du semestre 7 de la formation INSA.
Sous Linux, utilisez simplement la commande "java -jar insatact.jar" pour démarrer.
Relancez si une fenêtre vide apparaît. Si une photo de chat s'affiche, tout va bien.
Si vous souhaitez recréer un .jar, utilisez dans le répertoire insatact "mvn clean compile" puis "mvn package".

Ce projet a pour but la communication sur un même réseau interne de manière décentralisée; une base de donnée commune est néanmoins requise pour l'authentification ainsi que la récupération des messages échangés lors d'une ancienne session.

La messagerie permet l'envoi et la réception de messages jusqu'à  4095 caractères en TCP ainsi que leur affichage, le choix d'un identifiant unique et d'un mot de passe personnels. L'utilisateur peut échanger avec  n'importe quel autre utilisateur connecté sur le même réseau et récupérer un historique des anciens messages à sa connexion.
L'utilisateur choisit également un pseudo personnel unique et modifiable qui est l'identitée qui sera présentée aux autres.

La présence de chaque utilisateur est signalée régulièrement via broadcast UDP.

Aucune connaissance en informatique n'est requise pour l'utilisation de ce programme.


Les concepteurs sont au courant de la faiblesse de l'application en matière de sécurité (notamment, chaque utilisateur peut obtenir le contrôle de la base de données), cependant cela reste un projet étudiant et le choix a été fait de concentrer le travail sur d'autres parties.