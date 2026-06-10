# 📦 SoftGest Java – Sistema de Gestión Comercial

**Stack:** Java 17 + Spring Boot 3.4 + Spring Data JPA + Thymeleaf + Bootstrap 5 + MariaDB

---

## 🚀 Instalación paso a paso

### Paso 1: Instalar Java 17 (IMPORTANTE)

Este proyecto requiere **Java 17** (la versión LTS que soporta Spring Boot). Si tiene Java 25 u otra versión, instale Java 17 junto a ella (pueden convivir sin problemas):

```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
```

Verificar que se instaló:

```bash
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -version
```

Debe mostrar: `openjdk version "17.x.x"`

**Configurar Java 17 como predeterminado** (recomendado para este proyecto):

```bash
sudo update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java
sudo update-alternatives --set javac /usr/lib/jvm/java-17-openjdk-amd64/bin/javac

# O si prefiere elegir interactivamente:
sudo update-alternatives --config java
# Seleccione el número que corresponda a java-17
```

Verificar:

```bash
java -version
# Debe decir: openjdk version "17.x.x"

javac -version
# Debe decir: javac 17.x.x
```

> **Nota:** Si no quiere cambiar la versión global, puede configurar Java 17 solo para este proyecto usando la variable JAVA_HOME antes de compilar:
> ```bash
> export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
> export PATH=$JAVA_HOME/bin:$PATH
> ```

### Paso 2: Verificar Maven

```bash
mvn -version
```

Si no está instalado:

```bash
sudo apt install maven -y
```

### Paso 3: Crear la base de datos en MariaDB

```bash
sudo mariadb
```

```sql
CREATE DATABASE softgest CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SHOW DATABASES;
EXIT;
```

> Si su MariaDB tiene contraseña de root, use `mariadb -u root -p` y edite el archivo `src/main/resources/application.properties` con la contraseña.

### Paso 4: Configurar la conexión

Abra `src/main/resources/application.properties` y verifique:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/softgest
spring.datasource.username=root
spring.datasource.password=
```

### Paso 5: Compilar

```bash
cd softgest-java
mvn clean install -DskipTests
```

Debe ver: **BUILD SUCCESS**

### Paso 6: Ejecutar

```bash
mvn spring-boot:run
```

O use el script incluido (auto-detecta Java 17):

```bash
chmod +x run.sh
./run.sh
```

Verá en la consola:

```
Datos semilla cargados exitosamente
Usuarios de prueba (clave: 123456): admin@softgest.com | vendedor@softgest.com
Started SoftgestApplication in X seconds
```

### Paso 7: Abrir en el navegador

**http://localhost:8080**

Login: `admin@softgest.com` / `123456`

---

## 👥 Usuarios de prueba

| Rol | Correo | Contraseña | Permisos |
|---|---|---|---|
| Admin | admin@softgest.com | 123456 | Todo |
| Vendedor | vendedor@softgest.com | 123456 | Clientes, productos, pedidos |
| Bodeguero | bodeguero@softgest.com | 123456 | Productos, inventario |
| Domiciliario | domiciliario@softgest.com | 123456 | Pedidos |

---

## 📁 Estructura del proyecto

```
softgest-java/
├── pom.xml                              # Dependencias (como package.json)
├── run.sh                               # Script para compilar y ejecutar
├── src/main/
│   ├── java/com/softgest/
│   │   ├── SoftgestApplication.java     # Punto de entrada
│   │   ├── config/
│   │   │   ├── SecurityConfig.java      # Autenticación + roles
│   │   │   └── DataSeeder.java          # Datos semilla
│   │   ├── model/                       # Entidades JPA (7 archivos)
│   │   │   ├── Usuario.java
│   │   │   ├── Cliente.java
│   │   │   ├── Categoria.java
│   │   │   ├── Producto.java
│   │   │   ├── Pedido.java
│   │   │   ├── DetallePedido.java
│   │   │   ├── Rol.java                 # Enum
│   │   │   └── EstadoPedido.java        # Enum
│   │   ├── repository/                  # Acceso a datos (6 archivos)
│   │   ├── service/                     # Lógica de negocio (4 archivos)
│   │   └── controller/                  # Controladores MVC (6 archivos)
│   └── resources/
│       ├── application.properties
│       ├── static/css/custom.css
│       └── templates/                   # Vistas Thymeleaf (12 archivos)
```

## 🔄 Equivalencias Node.js → Java

| Concepto | Node.js | Java |
|---|---|---|
| Framework web | Express.js | Spring Boot (Spring MVC) |
| ORM | Sequelize | Spring Data JPA (Hibernate) |
| Autenticación | JWT | Spring Security (sesiones) |
| Cifrado | bcryptjs | BCryptPasswordEncoder |
| Vistas | React (SPA) | Thymeleaf (SSR) |
| Estilos | Tailwind CSS | Bootstrap 5 |
| Dependencias | npm / package.json | Maven / pom.xml |
| Variables config | .env | application.properties |

## 🎯 Arquitectura MVC

```
Navegador → Controller → Service → Repository → MariaDB
                ↓
           Thymeleaf (HTML)
```

## 📊 Ver la BD con DBeaver

Nueva conexión → MariaDB → Host: localhost, Puerto: 3306, BD: softgest, Usuario: root

## 🐛 Problemas comunes

| Problema | Solución |
|---|---|
| `Unsupported class file major version 69` | Está usando Java 25. Instale Java 17 (Paso 1) |
| `mvn: command not found` | `sudo apt install maven` |
| `Access denied for user 'root'` | Ponga la contraseña en application.properties |
| `Unknown database 'softgest'` | Cree la BD (Paso 3) |
| `Port 8080 already in use` | `sudo kill $(sudo lsof -t -i:8080)` |
