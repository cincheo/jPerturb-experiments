#!/usr/bin/env bash

jPerturb=jPerturb/target/jPerturb-0.0.1-SNAPSHOT-jar-with-dependencies.jar
perturbation=jPerturb/src/main/java/perturbation/
processors=processor.AssignmentProcessor:processor.VariableCaster:processor.PerturbationProcessor
m2_repo=$HOME/.m2/repository

#Install Spoon
git clone https://github.com/INRIA/spoon.git
cd spoon
mvn install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

cd ..

#Install jPerturb
git clone http://github.com/Spirals-Team/jPerturb.git
cd jPerturb
mvn package -Dmaven.test.skip=true

cd ..

#Spoon little exp
i=(md5/MD5.java quicksort/QuickSort.java mersenne/MersenneTwister.java sudoku/Sudoku.java zip/LZW.java)
for file in "${i[@]}"
do
    echo "java -jar $jPerturb -i src/main/java/$file:$perturbation -o src/main/java --with-imports -p $processors"
    java -jar $jPerturb -i src/main/java/$file:$perturbation -o src/main/java --with-imports -p processor.RenameProcessor:$processors
done

mvn compile