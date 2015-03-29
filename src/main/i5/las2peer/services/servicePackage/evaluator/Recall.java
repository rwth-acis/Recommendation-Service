/**
 * 
 */
package i5.las2peer.services.servicePackage.evaluator;

import i5.las2peer.services.servicePackage.datamodel.UserEntity;
import i5.las2peer.services.servicePackage.utils.Application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author sathvik
 *
 */
public class Recall {
	private LinkedHashMap<String, Double> userId2score;
	private int count; // To calculate R@count
	private double[] recall_values;
	private static final int collectionSize = 50;
	private Map<Long, UserEntity> userId2userObj;

	public Recall(LinkedHashMap<String, Double> userId2score,
			Map<Long, UserEntity> userId2userObj, int count) {
		if (count == -1) {
			this.count = Integer.MAX_VALUE;
		} else {
			this.count = count;
		}

		this.userId2score = userId2score;
		this.userId2userObj =  userId2userObj;
		recall_values = new double[collectionSize];
	}

	private int getTotalRelevantExpertInCollection() {
		int total_relevant_experts_in_collection = 0;
		int i = 0;

		Iterator<String> iterator = this.userId2score.keySet().iterator();
		while (iterator.hasNext() && i < collectionSize) {
			String setElement = iterator.next();
			UserEntity user_entity = userId2userObj.get(Long
					.valueOf(setElement));
			if (user_entity != null && user_entity.isProbableExpert()) {
				total_relevant_experts_in_collection++;
			}
			i++;
		}

		return total_relevant_experts_in_collection;
	}

	public double getValue() {
		double recall_score = 0;
		Iterator<String> iterator = this.userId2score.keySet().iterator();
		int no_of_relevant_experts = 0;
		int i = 0;

		while (iterator.hasNext() && i < this.count) {
			String setElement = iterator.next();
			UserEntity user_entity = userId2userObj.get(Long
					.valueOf(setElement));
			// System.out.println("Is relevant expert:: "
			// + user_entity.isProbableExpert());
			if (user_entity != null && user_entity.isProbableExpert()) {
				no_of_relevant_experts++;
			}
			i++;
		}
		System.out.println("No of relevant expert:: " + no_of_relevant_experts);

		// TODO: Check this again.
		int total_relevant_expert = getTotalRelevantExpertInCollection();
		total_relevant_expert = total_relevant_expert == 0 ? 1
				: total_relevant_expert;

		recall_score = (double) no_of_relevant_experts / total_relevant_expert;

		return recall_score * 100;
	}

	public void calculateValuesAtEveryPosition() {

		Iterator<String> iterator = this.userId2score.keySet().iterator();
		int no_of_relevant_experts = 0;
		int i = 0;

		// TODO: Check this again.
		int total_relevant_expert = getTotalRelevantExpertInCollection();
		System.out.println("Total Relevant Expert:: " + total_relevant_expert);
		total_relevant_expert = total_relevant_expert == 0 ? 1
				: total_relevant_expert;

		while (iterator.hasNext() && i < this.count) {
			String setElement = iterator.next();
			UserEntity user_entity = userId2userObj.get(Long
					.valueOf(setElement));
			// System.out.println("Is relevant expert:: "
			// + user_entity.isProbableExpert());
			if (user_entity != null && user_entity.isProbableExpert()) {
				no_of_relevant_experts++;
			}

			recall_values[i] = (double) no_of_relevant_experts
					/ total_relevant_expert;

			i++;
		}
		System.out.println("No of relevant expert:: " + no_of_relevant_experts);
	}

	public double[] getValues() {
		return recall_values;
	}

	public double[] getRoundedValues() {

		ArrayList<Double> rounded_recall_values = new ArrayList<Double>();

		for (int i = 0; i < recall_values.length && i < count; i++) {
			rounded_recall_values.add(Application.round(recall_values[i], 2));
		}

		Double[] values = rounded_recall_values
				.toArray(new Double[rounded_recall_values.size()]);
		return ArrayUtils.toPrimitive(values);

	}

	public void saveRecallValuesToFile() {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("recall_list.txt", false)))) {
			for (int i = 0; i < recall_values.length; i++) {
				// System.out.println("Recall Values:: " + recall_values[i]);
				out.println(Application.round(recall_values[i], 2));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getCount() {
		return this.count;
	}
}