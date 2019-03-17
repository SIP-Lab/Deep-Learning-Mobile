#include <jni.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/core.hpp>

using namespace cv;

Mat temp;
Rect r;

int nRows, nCols;
float* p;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_axs145031_opencvndk_1helloworld_MainActivity_some_1function(JNIEnv __unused *env,
                                                                             jobject __unused instance,
                                                                             jlong matAddress) {
    Mat &im = *(Mat *) matAddress;

    im(r).clone().convertTo(temp, CV_32FC4, 1/255.0);
    cvtColor(temp, temp, COLOR_RGBA2RGB, 3);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_axs145031_opencvndk_1helloworld_MainActivity_initialize(JNIEnv __unused *env,
                                                                         jobject __unused instance,
                                                                         jint height, jint width) {
    temp = Mat(height, height, CV_32FC4);

    r = Rect((width - height) / 2, 0, height, height);

    nRows = temp.rows;
    nCols = temp.cols * temp.channels();

    if(temp.isContinuous()){
        nCols *= nRows;
        nRows = 1;
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_axs145031_opencvndk_1helloworld_MainActivity_getImageArray(JNIEnv *env,
                                                                            jobject instance) {

    for (int i = 0; i < nRows; ++i) {
        p = temp.ptr<float>(i);
        for (int j = 0; j < nCols; ++j) {
            p[j] = rand();
        }
    }

}