package com.example.axs145031.opencvndk_helloworld;

import android.content.res.AssetManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCVCamera;
    private TextView labelsView;
    // Used for logging success or failure messages
    private static final String TAG = "OCVSample::Activity";
    private ImageInference imageInference;

    // For TensorFlow Lite, Still in Beta
    private ImageInferenceLite imageInferenceLite;


    private Thread thread = new Thread() {
        @Override
        public void run() {
            try{
                while(true) {
                    sleep(0);
                    labelsView.setText(imageInference.predict(data));

                    // For TensorFlow Lite; still in Beta
                    //labelsView.setText(imageInferenceLite.predict(data));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };



    Mat mRgba, mRgb, mROI, inputImage;
    Rect region;
    float[] data;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCVCamera.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCVCamera = findViewById(R.id.opencvCameraView);
        mOpenCVCamera.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCVCamera.setCvCameraViewListener(this);

        labelsView = findViewById(R.id.labelsView);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCVCamera != null)
            mOpenCVCamera.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        Imgproc.cvtColor(mRgba, mRgb, Imgproc.COLOR_RGBA2RGB);

        mRgb.submat(region).convertTo(mROI, CvType.CV_32FC3, 1.0);

        Imgproc.resize(mROI, inputImage, new Size(imageInference.INPUT_SIZE, imageInference.INPUT_SIZE));

        Scalar preprocess = new Scalar(-123.68/255.0,-116.78/255.0,-103.94/255.0);

        Core.subtract(inputImage, preprocess, inputImage);

        inputImage.get(0,0, data);

        //labelsView.setText(imageInference.predict(data));

        return mRgba; // This function must return
    }

    public void onCameraViewStarted(int width, int height) {

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgb = new Mat(height, width, CvType.CV_8UC3);
        mROI = new Mat(height, height, CvType.CV_32FC3);
        inputImage = new Mat(ImageInference.INPUT_SIZE, ImageInference.INPUT_SIZE, CvType.CV_32FC3);
        region = new Rect((width - height) / 2, 0, height, height);

        data = new float[inputImage.rows() * inputImage.cols() * inputImage.channels()];

        AssetManager assetManager = getAssets();

        try {
            imageInference = new ImageInference(assetManager);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // For TensorFlow Lite; Still in Beta
        //imageInferenceLite = new ImageInferenceLite(assetManager);

        thread.start();
    }

    public void onCameraViewStopped() {
        mRgba.release();
        mRgb.release();
        mROI.release();
        inputImage.release();
        region = null;
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCVCamera != null)
            mOpenCVCamera.disableView();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void some_function(long matAddress);
    public native void initialize(int height, int width);
    public native void getImageArray();
}
