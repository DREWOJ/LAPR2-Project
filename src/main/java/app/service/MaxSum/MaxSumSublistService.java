package app.service.MaxSum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.ArrayUtils;
import app.domain.shared.Constants;
import app.service.PropertiesUtils;
import java.util.stream.Collectors;

public class MaxSumSublistService {
  private List<Integer> sublist = null;
  private int startIndex = 0;
  private int endIndex = 0;
  private int sum = 0;
  private double timeElapsed;

  public MaxSumSublistService(List<Integer> differenceList) {
    if (differenceList.size() == 0) throw new IllegalArgumentException("The list cannot be empty");

    IMaxSum strategy = getMaxSumStrategy();

    int[] differencesArray = parseListToPrimitiveArray(differenceList);
    long startTime = System.nanoTime();

    int[] subArray = strategy.maxSum(differencesArray);

    long endTime = System.nanoTime();

    sublist = parseArrayToList(subArray);
    this.timeElapsed = (endTime - startTime) / Math.pow(10, 6);

    findIndexes(differenceList);
    calculateSum(differenceList);
  }

  private int[] parseListToPrimitiveArray(List<Integer> list) {
    return ArrayUtils.toPrimitive(list.toArray(new Integer[list.size()]));
  }

  private List<Integer> parseArrayToList(int[] array) {
    return Arrays.stream(array).boxed().collect(Collectors.toList());
  }

  private IMaxSum getMaxSumStrategy() {
    Properties props = PropertiesUtils.getProperties();
    String algorithmName = props.getProperty(Constants.PARAMS_PERFORMANCE_ALGORITHM);

    if (algorithmName.equals("BruteForce")) return new MaxSum();
    else if (algorithmName.equals("Benchmark")) return new SumAdapter();
    else throw new IllegalArgumentException("Unknown algorithm name: " + algorithmName);
  }

  private void findIndexes(List<Integer> list) {
    startIndex = Collections.indexOfSubList(list, sublist);
    endIndex = startIndex + sublist.size() - 1;
  }

  private void calculateSum(List<Integer> list) {
    for (int i = startIndex; i <= endIndex; i++)
      sum += list.get(i);
  }

  public int getStartIndex() {
    return startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  public int getSum() {
    return sum;
  }

  public List<Integer> getMaxSumSubList() {
    return sublist;
  }

  public double getTimeElapsed() {
    return this.timeElapsed;
  }
}
