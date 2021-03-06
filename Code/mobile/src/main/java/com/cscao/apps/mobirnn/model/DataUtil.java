package com.cscao.apps.mobirnn.model;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by qqcao on 4/5/17Wednesday.
 *
 * Data utility class
 */

public class DataUtil {
    public static float[] parseBias(String fileName) throws IOException {
        List<String> lines = FileUtils.readLines(new File(fileName));
        int size = lines.size();
        float[] b = new float[size];
        for (int i = 0; i < size; i++) {
            b[i] = Float.valueOf(lines.get(i));
        }

        return b;
    }

    public static float[][] parseWeight(String fileName) throws IOException {
        List<String> lines = FileUtils.readLines(new File(fileName));
        int size = lines.size();
        float[][] weights = new float[size][];
        for (int i = 0; i < size; i++) {
            String line = lines.get(i).trim().replaceAll("( )+", ",");
//            System.out.println("line:" + line);
            String[] ws = line.split(",");
            weights[i] = new float[ws.length];
            for (int j = 0; j < ws.length; j++) {
                weights[i][j] = Float.valueOf(ws[j]);
            }
        }
        return weights;
    }

    public static float[][][] parseInputData(String folder) throws IOException {
        String[] inputFileNames = new File(folder).list();
        int input_dim = inputFileNames.length;
        float[][][] data = new float[input_dim][][];

        for (int i = 0; i < input_dim; i++) {
            String filePath = folder + File.separator + inputFileNames[i];
            // System.out.println("filePath: " + filePath);
            data[i] = parseWeight(filePath);
        }

        int samples = data[0].length;
        int steps = data[0][0].length;
        // System.out.println("samples: " + samples + "steps:" + steps);
        float[][][] output = new float[samples][steps][input_dim];
        for (int i = 0; i < input_dim; i++) {
            for (int j = 0; j < samples; j++) {
                for (int k = 0; k < steps; k++) {
                    output[j][k][i] = data[i][j][k];
                    // System.out.println(data[i][j][k]);
                }
            }
        }
        return output;
    }

    public static int[] parseLabel(String fileName) throws IOException {
        List<String> lines = FileUtils.readLines(new File(fileName));

        int size = lines.size();
        int[] label = new int[size];
        for (int i = 0; i < size; i++) {
            label[i] = Integer.valueOf(lines.get(i));
        }

        return label;
    }

    public static float[] alter2Dto1D(float[][] x) {
        int X = x.length;
        int Y = x[0].length;
        float[] converted = new float[X * Y];
        for (int i = 0; i < X; i++) {
            System.arraycopy(x[i], 0, converted, i * Y, Y);
        }
        return converted;
    }

    public static float[] alter3Dto1D(float[][][] x) {
        int X = x.length;
        int Y = x[0].length;
        int Z = x[0][0].length;

        float[] converted = new float[X * Y * Z];
        for (int i = 0; i < X; i++) {
            float[][] layer = x[i];
            for (int j = 0; j < Y; j++) {
                System.arraycopy(layer[j], 0, converted, i * Y * Z + j * Z, Z);
            }
        }
        return converted;
    }

    public static float sigmoid(float x) {
        return 1 / (1 + (float) Math.exp(-x));
    }

    public static float tanh(float x) {
        return (float) Math.tanh(x);
    }

    public static int argmax(float[] x) {
        int max = 0;
        float a = 0.0f;
        for (int i = 0; i < x.length; i++) {
            if (x[i] > a) {
                a = x[i];
                max = i;
            }
        }
        return max;
    }

    public static float[][] relu(float[][] x) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                x[i][j] = Math.max(x[i][j], 0);
            }
        }

        return x;
    }

}
