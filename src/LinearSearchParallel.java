import java.util.concurrent.*;

public class LinearSearchParallel {
    private static class SearchTask extends RecursiveTask<Boolean> {
        private final int[] arr;
        private final int target;
        private final int start, end;
        private static final int THRESHOLD = 10_000;

        public SearchTask(int[] arr, int target, int start, int end) {
            this.arr = arr;
            this.target = target;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Boolean compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    if (arr[i] == target) {
                        return true;
                    }
                }
                return false;
            } else {
                int mid = (start + end) / 2;
                SearchTask leftTask = new SearchTask(arr, target, start, mid);
                SearchTask rightTask = new SearchTask(arr, target, mid, end);
                leftTask.fork();
                boolean rightResult = rightTask.compute();
                boolean leftResult = leftTask.join();
                return leftResult || rightResult;
            }
        }

        public static boolean parallelSearch(int[] arr, int target) {
            ForkJoinPool pool = new ForkJoinPool();
            return pool.invoke(new SearchTask(arr, target, 0, arr.length));
        }
    }
}
