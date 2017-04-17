import java.util.*;
import java.io.*;

public class Algorithms {
	final MAX_ITER = 25000

	// returns either 0 or 1 with approximately equal probability
	public static coinFlip() {
		Random r = new Random();
		return r.next();
	}

	public static long repRandStandard(long[] nums) {
		long best_res;
		for (int iter = 0; iter < MAX_ITER; iter++) {
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

	public static long repRandPrePart(long[] nums) {
		Random r = new Random();
		long best_res;
		// prepartitioned sums
		long[] preparts = new long[nums.length];
		for (int iter = 0; iter < MAX_ITER; iter++) {
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
	}

	public static boolean tempProb(long sum, int iter) {
		double t = Math.pow(10,10) * Math.pow(0.8, iter / 300); // iter / 300 is integer division
		double p = Math.pow(Math.e, - sum / t);
		// return true with prob p
		Random r = new Random();
		return (r.nextDouble() < p);
	}

	public static long localSearchStandard(long[] nums, boolean annealing) {
		long best_res = 0;
		long new_res;
		long last_res;

		// always either 0 or 1
		int[] state = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			state[i] = coinFlip();
			best_res += (2*state[i] - 1) * nums[i];
		}
		last_res = best_res;
		Random r = new Random();
		for (int iter = 0; iter < MAX_ITER; iter++) {
			int i = r.nextInt(nums.length);
			state[i] = 1 - state[i];
			int second = coinFlip();
			if (second) {
				int j = r.nextInt(nums.length);
				state[j] = 1 - state[j];
			}
			new_res = 0;
			for (int k = 0; k < nums.length; k++) {
				new_res += (2*state[i] - 1) * nums[i];
			}
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
				if (second) {
					state[j] = 1 - state[j];
				}
			}
			last_res = new_res;
		}
		return best_res;
	}

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
		for (int iter = 0; iter < MAX_ITER; iter++) {
			int i = r.nextInt(nums.length);
			int j = r.nextInt(nums.length);
			oldIdx = indices[i];
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

	public static long hillClimbStandard(long[] nums) {
		return localSearchStandard(nums, false);
	}

	public static long hillClimbPrePart(long[] nums) {
		return localSearchPrePart(nums, false);
	}

	public static long simAnnealStandard(long[] nums) {
		return localSearchStandard(nums, true);
	}

	public static long simAnnealPrePart(long[] nums) {
		return localSearchPrePart(nums, true);
	}
}