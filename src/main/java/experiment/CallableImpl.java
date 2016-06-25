package experiment;

/**
 * Created by spirals on 08/04/16.
 */
public abstract class CallableImpl<T,P> {

    protected T input;

    public CallableImpl(T input) {
        this.input = input;
    }

    public abstract P call() throws Exception;
}
