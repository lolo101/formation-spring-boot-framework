public class Framework {
    public static void main(String[] args) {
        RandomnessInverter inverter = new RandomnessInverter(new RandomnessHttpSource());
        System.out.println(inverter.nextInvert());
    }
}
