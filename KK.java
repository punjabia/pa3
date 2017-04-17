import java.util.*;

public class KK {
	long[] heap = new long[100];
	int length;

	public KK(long[] list) {
		System.out.println(Arrays.toString(list));
		this.length = list.length;
		this.heap = this.buildHeap(list);
		System.out.println(Arrays.toString(this.heap));
	}

	public long[] maxHeapify(long[] list, int index) {
		int leftChild = 2 * (index + 1) - 1;
		int rightChild = 2 * (index + 1);
		int largest;


		if (leftChild < this.length && list[leftChild] > list[index]) {
			largest = leftChild;
		}
		else {
			largest = index;
		}
		if (rightChild < this.length && list[rightChild] > list[largest]) {
			largest = rightChild;
		}
		if (largest != index) {
			long temp = list[index];
			list[index] = list[largest];
			list[largest] = temp;

			return maxHeapify(list, largest);
		}
		return list;
	}

	public long[] buildHeap(long[] list) {
		long[] heap = list;
		for (int i = list.length / 2; i >= 0; i--) {
			heap = maxHeapify(heap, i);
		}
		return heap;
	}

	public long residue(long[] list) {
		long max1;
		long max2;

		while (list[1] != 0 || list[2] != 0) {
			max1 = list[0];
			list[0] = 0;
			list = this.maxHeapify(list, 0);
			max2 = list[0];
			list[0] = Math.abs(max1 - max2);
			list = this.maxHeapify(list, 0);

			System.out.println(Arrays.toString(list));

		}

		return list[0];
	}
}