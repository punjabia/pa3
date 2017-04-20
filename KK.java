/* HUID: 60944733 and 31238714
 * Programming Assignment 3, CS 124, 4/21/17
 * File Description: Java class with main method that takes text file of long numbers from commandline argument
 *					 and outputs Karmarkar-Karp residue computed using heap implementation.
 */

import java.util.*;
import java.io.*;

public class kk {

	public static void main(String[] args) {
		long[] list = new long[100];
		String file = args[0]; // filename argument

        String line;
        int c = 0;

        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffered = new BufferedReader(reader);

            while((line = buffered.readLine()) != null && c < 100) { // store numbers in the list sequentially until 100 numbers have been added
            	list[c] = Long.parseLong(line);
            	c++;
            }   

            buffered.close();         
        }

        catch(FileNotFoundException e) {
            e.printStackTrace();             
        }

        catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println(residue(list)); // print Karmarkar-Karp residue
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
}