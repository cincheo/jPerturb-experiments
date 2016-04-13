#!/usr/bin/env bash

git clone http://github.com/Spirals-Team/jPerturb.git
cd jPerturb
mvn install
cd ..
wget https://gforge.inria.fr/frs/download.php/latestzip/86/Spoon-latest.zip
unzip Spoon-latest.zip
java -classpath spoon-core-5.1.0-jar-with-dependencies.jar:jPerturb/target/jPerturb-0.0.1-SNAPSHOT.jar spoon.Launcher -i resources/java:jPerturb/src/main/java/perturbation/ -o src/main/java --with-imports --compile -d bin -p processor.AssignmentProcessor:processor.VariableCaster:processor.PerturbationProcessor
javac -d bin -cp bin src/main/java/*/*.java