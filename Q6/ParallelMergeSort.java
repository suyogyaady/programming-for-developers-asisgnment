package Q6;
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort extends RecursiveAction {
    private final int[] array;
    private final int left;
    private final int right;

    public ParallelMergeSort(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (right - left <= 1) {
            Arrays.sort(array, left, right); // Sequential sort for small subarrays
        } else {
            int mid = left + (right - left) / 2;
            invokeAll(
                    new ParallelMergeSort(array, left, mid),
                    new ParallelMergeSort(array, mid, right)
            );
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) {
        int[] leftArray = Arrays.copyOfRange(array, left, mid);
        int[] rightArray = Arrays.copyOfRange(array, mid, right);

        int leftIndex = 0, rightIndex = 0, index = left;
        while (leftIndex < leftArray.length && rightIndex < rightArray.length) {
            if (leftArray[leftIndex] <= rightArray[rightIndex]) {
                array[index++] = leftArray[leftIndex++];
            } else {
                array[index++] = rightArray[rightIndex++];
            }
        }

        while (leftIndex < leftArray.length) {
            array[index++] = leftArray[leftIndex++];
        }

        while (rightIndex < rightArray.length) {
            array[index++] = rightArray[rightIndex++];
        }
    }

    public static void main(String[] args) {
        int[] inputArray = {5, 2, 9, 1, 5, 6, 8, 4};
        System.out.println("Unsorted Array: " + Arrays.toString(inputArray));

        ParallelMergeSort parallelMergeSort = new ParallelMergeSort(inputArray, 0, inputArray.length);
        parallelMergeSort.invoke(); // Initiating the sorting process

        System.out.println("Sorted Array: " + Arrays.toString(inputArray));
    }
}
