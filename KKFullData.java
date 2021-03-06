/* HUID: 60944733 and 31238714
 * Programming Assignment 3, CS 124, 4/21/17
 * File Description: Java class with main method that outputs the data analyzed in this assignment. 
 *					 Implements Karmarkar-Karp residue calculation using heap implementation and various 
 *					 random move and local search optimizations.
 */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KKFullData {

	public static void main(String[] args) {
		long[] list = new long[100];
		Random rnd = new Random();
		long[][] results = new long[7][100];
		long[][] times = new long[7][100];
		long startTime;
		long endTime;

		for (int runs = 0; runs < 100; runs++) {
			for (int i = 0; i < 100; i++) {
				list[i] = ThreadLocalRandom.current().nextLong(1, 1000000000001L); // Populates list of numbers with random long uniformly between 1 and 10^12
			}

			System.out.println(runs + 1); // Prints the number of trials out of 100

			startTime = System.currentTimeMillis();
			results[0][runs] = residue(list);
			endTime   = System.currentTimeMillis();
			times[0][runs] = endTime - startTime;
			startTime = System.currentTimeMillis();
			results[1][runs] = repRandStandard(list);
			endTime   = System.currentTimeMillis();
			times[1][runs] = endTime - startTime;
			startTime = System.currentTimeMillis();
			results[2][runs] = repRandPrePart(list);
			endTime   = System.currentTimeMillis();
			times[2][runs] = endTime - startTime;
			startTime = System.currentTimeMillis();
			results[3][runs] = hillClimbStandard(list);
			endTime   = System.currentTimeMillis();
			times[3][runs] = endTime - startTime;
			startTime = System.currentTimeMillis();
			results[4][runs] = hillClimbPrePart(list);
			endTime   = System.currentTimeMillis();
			times[4][runs] = endTime - startTime;
			startTime = System.currentTimeMillis();
			results[5][runs] = simAnnealStandard(list);
			endTime   = System.currentTimeMillis();
			times[5][runs] = endTime - startTime;
			startTime = System.currentTimeMillis();
			results[6][runs] = simAnnealPrePart(list);
			endTime   = System.currentTimeMillis();
			times[6][runs] = endTime - startTime;
		
		}

		// Prints all data serially
		System.out.println("Karmarkar-Karp Residue:");
		printResults(results[0]);
		System.out.println("Times:");
		printResults(times[0]);
		System.out.println("Repeated Random Standard Solution Representation:");
		printResults(results[1]);
		System.out.println("Times:");
		printResults(times[1]);
		System.out.println("Repeated Random Prepartition Solution Representation:");
		printResults(results[2]);
		System.out.println("Times:");
		printResults(times[2]);
		System.out.println("Hill Climbing Standard Solution Representation:");
		printResults(results[3]);
		System.out.println("Times:");
		printResults(times[3]);
		System.out.println("Hill Climbing Prepartition Solution Representation:");
		printResults(results[4]);
		System.out.println("Times:");
		printResults(times[4]);
		System.out.println("Simulated Annealing Standard Solution Representation:");
		printResults(results[5]);
		System.out.println("Times:");
		printResults(times[5]);
		System.out.println("Simulated Annealing Prepartition Solution Representation:");
		printResults(results[6]);
		System.out.println("Times:");
		printResults(times[6]);
	}

	// method to print data points from array
	public static void printResults(long[] list) {
		for (int i = 0; i < list.length; i++) {
			System.out.println(list[i]);
		}
	}

	// method to restore heap invariant in heap with one incorrect parent node
	public static long[] maxHeapify(long[] list, int index) {
		int leftChild = 2 * (index + 1) - 1;
		int rightChild = 2 * (index + 1);
		int largest;


		if (leftChild < 100 && list[leftChild] > list[index]) {
			largest = leftChild;
		}
		else {
			largest = index;
		}
		if (rightChild < 100 && list[rightChild] > list[largest]) {
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

	// method to build a heap from unsorted list of longs
	public static long[] buildHeap(long[] list) {
		long[] heap = list;
		for (int i = list.length / 2; i >= 0; i--) {
			heap = maxHeapify(heap, i);
		}
		return heap;
	}

	// method to calculate Karmarkar-Karp residue for list of longs (using heap data structure)
	public static long residue(long[] list) {
		long[] heap = buildHeap(list.clone());
		long max1;
		long max2;

		while (heap[1] != 0 || heap[2] != 0) {
			max1 = heap[0];
			heap[0] = 0;
			heap = maxHeapify(heap, 0);
			max2 = heap[0];
			heap[0] = Math.abs(max1 - max2);
			heap = maxHeapify(heap, 0);

		}

		return heap[0];
	}

	// returns either 0 or 1 with approximately equal probability
	public static int coinFlip() {
		Random r = new Random();
		return r.nextInt(2);
	}

	// method to calculate residue using repeated random moves with standard solution representation
	public static long repRandStandard(long[] nums) {
		long best_res = Long.MAX_VALUE;
		for (int iter = 0; iter < 25000; iter++) {
			long res = 0;
			for (int i = 0; i < nums.length; i++) {
				res += (2*coinFlip()-1) * nums[i];
			}
			res = Math.abs(res);
			if (iter == 0) {
				best_res = res;
			}
			else if (res < best_res) {
				best_res = res;
			}
		}
		return best_res;
	}

	// method to calculate residue using repeated random moves with prepartition solution representation
	public static long repRandPrePart(long[] nums) {
		Random r = new Random();
		long best_res = Long.MAX_VALUE;
		// prepartitioned sums
		long[] preparts = new long[nums.length];
		for (int iter = 0; iter < 25000; iter++) {
			for (int i = 0; i < nums.length; i++) {
				preparts[r.nextInt(nums.length)] += nums[i];
			}
			long res = residue(preparts);
			if (iter == 0) {
				best_res = res;
			}
			else if (res < best_res) {
				best_res = res;
			}
		}
		return best_res;
	}

	// method to calculate temperature for simulated annealing local search
	public static boolean tempProb(long sum, int iter) {
		double t = Math.pow(10,10) * Math.pow(0.8, iter / 300); // iter / 300 is integer division
		double p = Math.pow(Math.E, - sum / t);
		// return true with prob p
		Random r = new Random();
		return (r.nextDouble() < p);
	}

	// method to calculate residue using local search with standard solution representation
	public static long localSearchStandard(long[] nums, boolean annealing) {
		long best_res = Long.MAX_VALUE;
		long new_res;
		long last_res;

		// always either 0 or 1
		int[] state = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			state[i] = coinFlip();
			best_res += (2*state[i] - 1) * nums[i];
		}
		best_res = Math.abs(best_res);
		last_res = best_res;
		Random r = new Random();
		for (int iter = 0; iter < 25000; iter++) {
			int i = r.nextInt(nums.length);
			state[i] = 1 - state[i];
			int second = coinFlip();
			int j = r.nextInt(nums.length);
			if (second == 1) {
				//j = r.nextInt(nums.length);
				state[j] = 1 - state[j];
			}
			new_res = 0;
			for (int k = 0; k < nums.length; k++) {
				new_res += (2*state[k] - 1) * nums[k];
			}
			new_res = Math.abs(new_res);
			if (new_res < best_res) {
				best_res = new_res;
			}
			else {
				// toggle things back unless we are annealing
				if (annealing && tempProb(last_res + new_res, iter)) {
					last_res = new_res;
					continue;
				}
				state[i] = 1 - state[i];
				if (second == 1) {
					state[j] = 1 - state[j];
				}
			}
			last_res = new_res;
			//System.out.print(best_res + " ");
		}
		return best_res;
	}

	// method to calculate residue using local search with prepartition solution representation
	public static long localSearchPrePart(long[] nums, boolean annealing) {
		// prepartition index for each num
		int[] indices = new int[nums.length];
		long[] prepart = new long[nums.length];
		for (int i = 0; i < nums.length; i++) {
			prepart[indices[i]] += nums[i];
		}
		long best_res = residue(prepart);
		long last_res = best_res;
		Random r = new Random();
		for (int iter = 0; iter < 25000; iter++) {
			int i = r.nextInt(nums.length);
			int j = r.nextInt(nums.length);
			int oldIdx = indices[i];
			prepart[oldIdx] -= nums[i];
			prepart[j] += nums[i];
			indices[i] = j;

			long new_res = residue(prepart);
			if (new_res < best_res) {
				best_res = new_res;
			}
			else {
				// put things back
				if (annealing && tempProb(last_res + new_res, iter)) {
					last_res = new_res;
					continue;
				}
				prepart[oldIdx] += nums[i];
				prepart[j] += nums[i];
				indices[i] = oldIdx;
			}
			last_res = new_res;
		}
		return best_res;
	}

	// bootstrap for hill climbing local search with standard solution representation
	public static long hillClimbStandard(long[] nums) {
		return localSearchStandard(nums, false);
	}

	// bootstrap for hill climbing local search with prepartition solution representation
	public static long hillClimbPrePart(long[] nums) {
		return localSearchPrePart(nums, false);
	}

	// bootstrap for simulated annealing local search with standard solution representation
	public static long simAnnealStandard(long[] nums) {
		return localSearchStandard(nums, true);
	}

	// bootstrap for simulated annealing local search with prepartition solution representation
	public static long simAnnealPrePart(long[] nums) {
		return localSearchPrePart(nums, true);
	}
}