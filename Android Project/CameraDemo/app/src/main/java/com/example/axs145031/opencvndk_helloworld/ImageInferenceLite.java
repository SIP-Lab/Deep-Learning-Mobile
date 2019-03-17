package com.example.axs145031.opencvndk_helloworld;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.SparseArray;


import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class ImageInferenceLite {

    /** Dimensions of inputs. */
    private static final int DIM_BATCH_SIZE = 1;

    private static final int DIM_PIXEL_SIZE = 3;

    private Interpreter tflite;
    private String MODEL_FILE = "mobilenet_v1_1.0_224.tflite";
    private static final String LABEL_FILE = "labels.txt";
    private ByteBuffer imgData = null;
    private float[][] ProbArray = null;
    private int INPUT_SIZE = 224;
    private Integer[] indices;
    private SparseArray<String> labels;
    private ArrayIndexCompare sortedLabels;

    ImageInferenceLite(AssetManager assets) {
        try {
            tflite = new Interpreter(loadModelFile(assets));
            imgData = ByteBuffer.allocateDirect(
                    DIM_BATCH_SIZE * INPUT_SIZE * INPUT_SIZE * DIM_PIXEL_SIZE * 4);
            imgData.order(ByteOrder.nativeOrder());
            readLabels(assets);

            ProbArray = new float[1][labels.size()];

            sortedLabels = new ArrayIndexCompare(ProbArray[0]);
            indices = sortedLabels.createIndexArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readLabels(AssetManager assetManager) throws IOException {
        InputStream is = assetManager.open(LABEL_FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String st;
        labels = new SparseArray<>();
        int index = 0;
        while ((st = br.readLine()) != null) {
            labels.put(index++, st);
        }
    }

    public String predict(float[] data) {
        String output = "";
        if (tflite != null) {

            convertToTfLiteInput(data);
            if (imgData != null) {

                tflite.run(imgData, ProbArray);
                Arrays.sort(indices);
                Arrays.sort(indices, sortedLabels);

                for (int i = 1; i <= 5; i++){
                    output = output.concat(
                            String.format(
                                    Locale.getDefault(), "%3.0f%% ",
                                    100 * ProbArray[0][indices[labels.size() -  i]])
                                    + labels.get(indices[labels.size() -  i]) + "\n");
                }
            }
        }
        return output;
    }

    private void convertToTfLiteInput(float[] input) {

        imgData.rewind();
        for (int i = 0; i < input.length; i++) {
            imgData.putFloat(input[i]);
        }

    }

    private MappedByteBuffer loadModelFile(AssetManager assets) throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
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
