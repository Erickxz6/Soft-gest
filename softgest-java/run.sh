#!/bin/bash
echo "╔══════════════════════════════════════════╗"
echo "║     SoftGest – Compilando y ejecutando   ║"
echo "╚══════════════════════════════════════════╝"
echo ""

# Forzar Java 17 si está instalado
if [ -d "/usr/lib/jvm/java-17-openjdk-amd64" ]; then
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
    export PATH=$JAVA_HOME/bin:$PATH
fi

echo "Java: $(java -version 2>&1 | head -1)"
echo ""

mvn clean install -DskipTests && echo "" && mvn spring-boot:run
