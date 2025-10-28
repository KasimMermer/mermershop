# Mermer Shop 🛒

Ein vollständiges E-Commerce-System entwickelt mit Spring Boot, Thymeleaf und MySQL.

## 📋 Projektbeschreibung

Mermer Shop ist eine Web-Anwendung für Online-Shopping mit Benutzer- und Admin-Funktionen. Das System ermöglicht es Benutzern, Produkte zu durchsuchen, in den Warenkorb zu legen und Bestellungen aufzugeben. Administratoren können Produkte verwalten (hinzufügen, bearbeiten, löschen).

## 🚀 Features

### Für Benutzer (USER)
- ✅ Registrierung und Login
- ✅ Produktkatalog durchsuchen mit Suchfunktion
- ✅ Produktdetailseiten ansehen
- ✅ Produkte in den Warenkorb legen
- ✅ Warenkorbverwaltung (Menge ändern, Produkte entfernen)
- ✅ Bestellungen aufgeben
- ✅ Bestellhistorie einsehen
- ✅ Profilbearbeitung

### Für Administratoren (ADMIN)
- ✅ Admin-Dashboard
- ✅ Produkte hinzufügen mit Bildupload
- ✅ Produkte bearbeiten
- ✅ Produkte löschen
- ✅ Produktverwaltung mit Bildverwaltung

## 🛠️ Technologie-Stack

- **Backend**: Spring Boot 3.5.7
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Datenbank**: MySQL
- **Build-Tool**: Maven
- **Java Version**: 21
- **Weitere Dependencies**:
  - Spring Data JPA
  - Spring Boot Starter Web
  - Spring Boot Starter Validation
  - Lombok
  - MySQL Connector

## 📦 Installation

### Voraussetzungen
- Java 21 oder höher
- MySQL 8.0 oder höher
- Maven 3.6 oder höher

### Schritt 1: Repository klonen
```bash
git clone https://github.com/KasimMermer/mermershop.git
cd mermershop
```

### Schritt 2: Datenbank einrichten
1. MySQL-Datenbank erstellen:
```sql
CREATE DATABASE mermer_shop;
```

2. Datenbankkonfiguration anpassen (falls nötig) in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mermer_shop
spring.datasource.username=root
spring.datasource.password=
```

### Schritt 3: Anwendung starten
```bash
./mvnw spring-boot:run
```

Die Anwendung läuft unter: `http://localhost:8081`

## 👤 Standard-Benutzer

Die Anwendung erstellt automatisch Test-Benutzer beim ersten Start:

### Admin-Account
- **Benutzername**: `admin`
- **Passwort**: `12340000`

### Benutzer-Account
- **Benutzername**: `user`
- **Passwort**: `12340000`

## 📁 Projektstruktur

```
mermershop/
├── src/
│   ├── main/
│   │   ├── java/com/mermershop/
│   │   │   ├── config/          # Konfigurationen (DataSeeder)
│   │   │   ├── controller/      # REST-Controller
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── enums/            # Enumerationen (UserRole)
│   │   │   ├── model/            # Entitätsklassen (User, Product, Order)
│   │   │   ├── repository/       # JPA Repositories
│   │   │   ├── service/          # Business-Logik
│   │   │   └── MermershopApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/          # Stylesheets
│   │       │   └── images/       # Produktbilder
│   │       ├── templates/        # Thymeleaf Templates
│   │       └── application.properties
│   └── test/                     # Tests
├── pom.xml
└── README.md
```

## 🔧 Konfiguration

### application.properties
```properties
server.port=8081
spring.application.name=Mermer Shop
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mermer_shop
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```

## 📸 Screenshots

### Benutzer-Dashboard
Zeigt alle verfügbaren Produkte mit Suchfunktion

### Produktdetailseite
Detaillierte Produktinformationen mit Warenkorb-Funktion

### Warenkorb
Übersicht aller ausgewählten Produkte

### Admin-Dashboard
Produktverwaltung für Administratoren

## 🔐 Sicherheit

- Session-basierte Authentifizierung
- Rollenbasierte Zugriffskontrolle (USER/ADMIN)
- Passwörter werden als Klartext gespeichert (für Entwicklungszwecke)
  - **Hinweis**: In Produktion sollte Password-Hashing implementiert werden!

## 📝 API-Endpunkte

### Authentifizierung
- `GET /login` - Login-Seite
- `POST /login` - Login-Verarbeitung
- `GET /register` - Registrierungsseite
- `POST /register` - Registrierung
- `GET /logout` - Logout

### Produkte
- `GET /products/detail/{id}` - Produktdetails
- `GET /products/add` - Produkt hinzufügen (Admin)
- `POST /products/add` - Produkt speichern (Admin)
- `GET /products/edit/{id}` - Produkt bearbeiten (Admin)
- `POST /products/edit/{id}` - Produkt aktualisieren (Admin)
- `POST /products/delete/{id}` - Produkt löschen (Admin)

### Warenkorb
- `GET /cart` - Warenkorb anzeigen
- `POST /cart/add/{productId}` - Produkt hinzufügen
- `POST /cart/update/{productId}` - Menge aktualisieren
- `POST /cart/remove/{productId}` - Produkt entfernen
- `POST /cart/clear` - Warenkorb leeren

### Bestellungen
- `GET /orders` - Bestellhistorie
- `POST /orders/checkout` - Bestellung aufgeben

### Profil
- `GET /profile` - Profil anzeigen
- `POST /profile/update` - Profil aktualisieren

## 🚧 Zukünftige Erweiterungen

- [ ] Password-Hashing mit BCrypt
- [ ] Spring Security Integration
- [ ] Produktkategorien
- [ ] Produktbewertungen
- [ ] Zahlungsintegration
- [ ] E-Mail-Benachrichtigungen
- [ ] Erweiterte Suchfilter
- [ ] Produktvarianten (Größe, Farbe)

## 👨‍💻 Entwickler

Kasim Mermer

## 📄 Lizenz

Dieses Projekt wurde für Assessment Day Zwecke erstellt.

## 🤝 Beitragen

Contributions sind willkommen! Bitte erstelle einen Pull Request oder öffne ein Issue.

## 📞 Kontakt

Bei Fragen oder Problemen bitte ein Issue auf GitHub erstellen.

---
