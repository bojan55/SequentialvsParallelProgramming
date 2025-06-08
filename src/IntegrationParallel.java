import java.util.concurrent.*;

public class IntegrationParallel extends RecursiveTask<Double> {
    private final double a, h;
    private final int start, end;
    private static final int THRESHOLD = 1_000_000;

    public IntegrationParallel(double a, double h, int start, int end) {
        this.a = a;
        this.h = h;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Double compute() {
        if (end - start <= THRESHOLD) {
            double sum = 0;
            for (int i = start; i < end; i++) {
                sum += f(a + i * h);
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            IntegrationParallel left = new IntegrationParallel(a, h, start, mid);
            IntegrationParallel right = new IntegrationParallel(a, h, mid, end);
            left.fork();
            double rightSum = right.compute();
            double leftSum = left.join();
            return leftSum + rightSum;
        }
    }

    private static double f(double x) {
        return x * x;
    }

    public static double integrate(double a, double b, int n) {
        double h = (b - a) / n;
        ForkJoinPool pool = new ForkJoinPool();
        double sum = pool.invoke(new IntegrationParallel(a, h, 1, n));
        pool.shutdown();
        return h * (0.5 * (f(a) + f(b)) + sum);
    }
}
