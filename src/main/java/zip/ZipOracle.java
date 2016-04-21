package zip;

import experiment.Oracle;
import experiment.OracleManager;
import experiment.Runner;

/**
 * Created by spirals on 05/04/16.
 */
public class ZipOracle implements Oracle<String,String> {

    @Override
    public boolean assertPerturbation(String input, String output) {
        return input.equals(output);
    }

}