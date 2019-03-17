package com.example.axs145031.opencvndk_helloworld;

import android.content.res.AssetManager;
import android.util.SparseArray;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;


public class ImageInference {

    private static final String MODEL_FILE =
            "file:///android_asset/ResNet50.pb";
    private static final String LABEL_FILE = "labels.txt";
    private static final String INPUT_NAME = "input_1";
    private static final String OUTPUT_NAME = "fc1000/Softmax";
    private static final String[] OUTPUT_NAMES = {OUTPUT_NAME};
    public static final int INPUT_SIZE = 224;

    private float[] preds;
    private int numClasses;
    private Integer[] indices;
    private SparseArray<String> labels;
    private ArrayIndexCompare sortedLabels;

    private TensorFlowInferenceInterface inferenceInterface;

    public ImageInference(AssetManager assetManager) throws IOException {
        inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
        numClasses = (int) inferenceInterface.graphOperation(OUTPUT_NAME)
                .output(0)
                .shape()
                .size(1);
        preds = new float[numClasses];

        sortedLabels = new ArrayIndexCompare(preds);
        indices = sortedLabels.createIndexArray();

        InputStream is = assetManager.open(LABEL_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String st;
        labels = new SparseArray<>();
        int index = 0;
        while ((st = br.readLine()) != null) {
            labels.put(index++, st);
        }
    }

    public String predict(float[] data){
        inferenceInterface.feed(INPUT_NAME, data, 1, INPUT_SIZE, INPUT_SIZE, 3);
        inferenceInterface.run(OUTPUT_NAMES);
        inferenceInterface.fetch(OUTPUT_NAME, preds);

        Arrays.sort(indices);
        Arrays.sort(indices, sortedLabels);

        String output = "";

        for (int i = 1; i <= 5; i++){
            output = output.concat(
                    String.format(Locale.getDefault(), "%3.0f%% ", 100 * preds[indices[numClasses -  i]])
                            + labels.get(indices[numClasses -  i] + 1) + "\n");
        }

        return output;
    }

    private class ArrayIndexCompare implements Comparator<Integer> {
        private final float[] array;

        private ArrayIndexCompare(float[] array) {
            this.array = array;
        }

        private Integer[] createIndexArray() {
            Integer[] indices = new Integer[array.length];
            for (int i = 0; i < array.length; i++) {
                indices[i] = i;
            }
            return indices;
        }

        @Override
        public int compare(Integer index1, Integer index2) {
            return Float.compare(array[index1], array[index2]);
        }
    }

}

