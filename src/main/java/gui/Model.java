package gui;

import experiment.CallableImpl;
import experiment.Manager;
import experiment.Tuple;
import perturbation.PerturbationEngine;
import perturbation.enactor.NeverEnactorImpl;
import perturbation.enactor.RandomEnactorImpl;
import perturbation.location.PerturbationLocation;
import perturbation.log.LoggerImpl;
import perturbation.perturbator.AddNPerturbatorImpl;
import perturbation.perturbator.InvPerturbatorImpl;
import quicksort.QuickSortManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by bdanglot on 13/05/16.
 */
public class Model extends Observable {

    private float rnd = 0.0f;

    private Manager manager;

    private List<PerturbationLocation> locations;

    private int numberOfTask;

    private int size;

    private float step = 0.001f;

    private int accExec;

    private int accExecSuccess;

    private double avgPerturbationPerExec;

    private List<String> classOfLocation;

    private List<String> currentTypeOfLocation;

    private List<Integer> antifragileLocation;// == 100%

    private List<Integer> robustLocation;//100% to 50%

    private List<Integer> weakLocation;//<50%

    public int getAccExec() {
        return accExec;
    }

    public int getAccExecSuccess() {
        return accExecSuccess;
    }

    public double getAvgPerturbationPerExec() {
        return avgPerturbationPerExec;
    }

    public int getNumberOfTask() {
        return numberOfTask;
    }

    public void setNumberOfTask(int numberOfTask) {
        this.numberOfTask = numberOfTask;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addType(String type) {
        this.currentTypeOfLocation.add(type);
        this.setUpLocations();
    }

    public void removeType(String type) {
        this.currentTypeOfLocation.remove(type);
        this.setUpLocations();
    }

    public void addClassLocation(String classLocation) {
        this.classOfLocation.add(classLocation);
        this.setUpLocations();
    }

    public void removeClassLocation(String classLocation) {
        this.classOfLocation.remove(classLocation);
        this.setUpLocations();
    }

    public void addRand(float value) {
        this.rnd = this.rnd + value >= 1.0f ? 1.0f : this.rnd + value;
        this.setUpLocations();
    }

    public void minusRand(float value) {
        this.rnd = this.rnd - value <= 0.0f ? 0.0f : this.rnd - value;
        this.setUpLocations();
    }

    public Tuple getConfig() {
        Tuple config = new Tuple(3);
        int cptAntiFragile = 0, cptRobust = 0, cptWeak = 0;
        for (PerturbationLocation location : this.locations) {
            if (this.currentTypeOfLocation.contains(location.getType())) {
                if (this.antifragileLocation.contains(location.getLocationIndex()))
                    cptAntiFragile++;
                else if (this.robustLocation.contains(location.getLocationIndex()))
                    cptRobust++;
                else if (this.weakLocation.contains(location.getLocationIndex()))
                    cptWeak++;
            }
        }
        config.set(0, cptAntiFragile);
        config.set(1, cptRobust);
        config.set(2, cptWeak);
        return config;
    }

    public Model() {
        this.accExec = 0;
        this.accExecSuccess = 0;
        this.size = 100;
        this.numberOfTask = 40;
        this.avgPerturbationPerExec = 0.0d;
        this.classOfLocation = new ArrayList<>();
        this.classOfLocation.add("Antifragile");
        this.currentTypeOfLocation = new ArrayList<>();
        this.currentTypeOfLocation.add("Numerical");
        this.manager = new QuickSortManager(this.numberOfTask, this.size, (int) System.currentTimeMillis());
        this.initLocations();
        this.setUpLocations();
        PerturbationEngine.loggers.put("gui", new LoggerImpl());
    }

    private void initLocations() {
        this.locations = this.manager.getLocations();//TODO

        for (PerturbationLocation location : this.locations) {
            if (location.getType().equals("Boolean"))
                location.setPerturbator(new InvPerturbatorImpl());
            else
                location.setPerturbator(new AddNPerturbatorImpl(1));
        }

        this.antifragileLocation = new ArrayList<>();
        this.robustLocation = new ArrayList<>();
        this.weakLocation = new ArrayList<>();

        this.readFile("results/" + this.manager.getName() + "/IntegerAddOne_RandomExplorer_analysis_graph_data.txt");
        this.readFile("results/" + this.manager.getName() + "/BooleanNegation_RandomExplorer_analysis_graph_data.txt");

        System.out.println(this.antifragileLocation);
        System.out.println(this.robustLocation);
        System.out.println(this.weakLocation);
    }

    private void readFile(String path) {
        try {

            BufferedReader br = new BufferedReader(new FileReader(path));
            br.readLine();
            br.readLine();
            br.readLine();//Trash unused lines
            int nbRandomRate = (br.readLine().split(" ").length - 2);
            br.readLine();
            br.readLine();
            br.readLine();//Trash unused lines
            int nbLocation = Integer.parseInt(br.readLine().split(" ")[0]);
            br.readLine();//Trash unused lines

            for (int loc = 0; loc < nbLocation; loc++) {
                boolean isAntifragile = true;
                boolean added = false;
                boolean used = false;
                int indexLoc = -1;
                for (int random = 0; random < nbRandomRate; random++) {
                    String[] line = br.readLine().trim().replaceAll(" +", " ").split(" ");
                    indexLoc = Integer.parseInt(line[2]);
                    if (Integer.parseInt(line[6]) > 0) {
                        if (Integer.parseInt(line[7]) > 0) {
                            float success = Float.parseFloat(line[line.length - 1].replace(",", "."));
                            used = true;
                            if (success < 50.0) {
                                if (!this.weakLocation.contains(indexLoc))
                                    this.weakLocation.add(indexLoc);
                                added = true;
                            } else if (success < 100.0) {
                                isAntifragile = false;
                            }
                        }
                    }
                }
                if (used && !added) {
                    if (isAntifragile)
                        this.antifragileLocation.add(indexLoc);
                    else
                        this.robustLocation.add(indexLoc);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpLocations() {
        for (PerturbationLocation location : this.locations) {
            location.setEnactor(new NeverEnactorImpl());
        }

        List<Integer> indices = new ArrayList<>();
        if (this.classOfLocation.contains("Antifragile"))
            indices.addAll(this.antifragileLocation);
        if (this.classOfLocation.contains("Robust"))
            indices.addAll(this.robustLocation);
        if (this.classOfLocation.contains("Weak"))
            indices.addAll(this.weakLocation);

        for (PerturbationLocation location : this.locations) {
            if (this.currentTypeOfLocation.contains(location.getType()) && indices.contains(location.getLocationIndex()))
                location.setEnactor(new RandomEnactorImpl(rnd));
        }

        this.setChanged();
        this.notifyObservers();

    }

    public void incRnd() {
        this.rnd = this.rnd + this.step < 1.0f ? this.rnd + this.step : 1.0f;
        this.setUpLocations();
    }

    public void decRnd() {
        this.rnd = this.rnd - this.step > 0.0f ? this.rnd - this.step : 0.0f;
        this.setUpLocations();
    }

    public float getRnd() {
        return this.rnd;
    }

    public float runAllTask() {

        PerturbationEngine.loggers.get("gui").reset();
        for (PerturbationLocation location : this.locations)
            PerturbationEngine.loggers.get("gui").logOn(location);
        this.manager = new QuickSortManager(this.numberOfTask, this.size, (int) System.currentTimeMillis());

        int nbSuccess = 0;
        for (int i = 0; i < numberOfTask; i++)
            nbSuccess += runPerturbation(i);

        int acc = 0;
        for (PerturbationLocation location : this.locations)
            acc += PerturbationEngine.loggers.get("gui").getEnactions(location);

        this.avgPerturbationPerExec = (double) acc / (double) numberOfTask;

        return (float) nbSuccess / (float) (numberOfTask) * 100;
    }

    private int runPerturbation(int indexTask) {
        accExec++;
        try {
            CallableImpl callable = this.manager.getCallable(manager.getTask(indexTask));
            Object output = callable.call();
            if (this.manager.getOracle().assertPerturbation(manager.getTask(indexTask), output)) {
                accExecSuccess++;
                return 1;
            } else
                return 0;
        } catch (Exception | Error e) {
            return 0;
        }
    }

}
