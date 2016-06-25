

package perturbation.perturbator;

public interface Perturbator {
    boolean pboolean(boolean value);

    byte pbyte(byte value);

    short pshort(short value);

    int pint(int value);

    long plong(long value);

    char pchar(char value);

    float pfloat(float value);

    double pdouble(double value);

    @Override
    String toString();
}

