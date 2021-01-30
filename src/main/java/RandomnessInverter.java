public class RandomnessInverter {
    private final RandomnessSource source;

    public RandomnessInverter(RandomnessSource source) {
        this.source = source;
    }

    public double nextInvert() {
        double randomNumber = source.nextUniformRandom();
        return 1.0 / randomNumber;
    }
}
