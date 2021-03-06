package bitcoin;

import experiment.Oracle;
import experiment.Tuple;

/**
 * Created by spirals on 25/04/16.
 */
public class BitcoinOracle implements Oracle<Tuple,Integer> {

    private BitcoinManager manager;

    public BitcoinOracle(BitcoinManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean assertPerturbation(Tuple input, Integer output) {
        if (output == -1) {
            return false;
        }

        int currentAmountSender = BitcoinManager.btcStringToBtcInt(this.manager.getWalletAppKit((int)input.get(0)).wallet().getBalance().toFriendlyString());
        int currentAmountReceiver = BitcoinManager.btcStringToBtcInt(this.manager.getWalletAppKit((int)input.get(1)).wallet().getBalance().toFriendlyString());

        int oldAmountSender = BitcoinManager.amountOfWalletBeforeTask[(int)input.get(0)];
        int oldAmountReceiver = BitcoinManager.amountOfWalletBeforeTask[(int)input.get(1)];

//        System.out.println(currentAmountSender);
//        System.out.println(currentAmountReceiver);
//
//        System.out.println(oldAmountSender);
//        System.out.println(oldAmountReceiver);
//
//
//        System.out.println(input);
//        System.out.println(input.get(2));

        boolean assertion = oldAmountSender == currentAmountSender + input.get(2) + BitcoinManager.FEE_AMOUNT
            && oldAmountReceiver == currentAmountReceiver - input.get(2);

//        System.out.println(assertion);

        BitcoinManager.amountOfWalletBeforeTask[(int)input.get(0)] = currentAmountSender;
        BitcoinManager.amountOfWalletBeforeTask[(int)input.get(1)] = currentAmountReceiver;

        return assertion;
    }

}
