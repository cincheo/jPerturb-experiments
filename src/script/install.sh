#!/usr/bin/env bash

#spoon=spoon-core-5.1.0-jar-with-dependencies.jar


jPerturb=jPerturb/target/jPerturb-0.0.1-SNAPSHOT-jar-with-dependencies.jar
perturbation=jPerturb/src/main/java/perturbation/
processors=processor.AssignmentProcessor:processor.VariableCaster:processor.RenameProcessor:processor.PerturbationProcessor

path=src/main/java/

git clone https://github.com/INRIA/spoon.git
cd spoon
mvn install -q -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

git clone http://github.com/Spirals-Team/jPerturb.git
cd jPerturb
mvn package -Dmaven.test.skip=true -q
cd ..

#wget https://gforge.inria.fr/frs/download.php/latestzip/86/Spoon-latest.zip
#unzip Spoon-latest.zip 2>/dev/null 1>/dev/null

wget http://mirror.trisect.eu/Apache//commons/math/source/commons-math3-3.6.1-src.zip
unzip commons-math3-3.6.1-src.zip 2>/dev/null 1>/dev/null

cp -R commons-math3-3.6.1-src/src/main/java/org/ $path
rm -Rf commons-math3-3.6.1-src

#files=(md5/MD5.java quicksort/QuickSort.java mersenne/MersenneTwister.java org/apache/commons/math3/optim/linear/SimplexSolver.java sudoku/Sudoku.java zip/LZW.java)
files=(org/apache/commons/math3/optim/linear/SimplexSolver.java)
for i in "${files[@]}"
do
    echo $i
    java -jar $jPerturb -i $path/$i:$perturbation -o src/main/java -x --with-imports -p $processors
done