import java.util.*;

public class Driver {
	public static void main(String[] args) {
		long[] list = new long[5];
		Random rnd = new Random();

		for (int i = 0; i < 5; i++) {
			list[i] = rnd.nextInt(10);
		}
		KK part = new KK(list);
		System.out.println(part.residue(part.heap));
	}
}