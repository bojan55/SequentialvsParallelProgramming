public class IntegrationSequential {
    public static double integrate(double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.5 * (f(a) + f(b));
        for (int i = 1; i < n; i++) {
            sum += f(a + i * h);
        }
        return sum * h;
    }

    private static double f(double x) {
        return x * x;
    }
}
