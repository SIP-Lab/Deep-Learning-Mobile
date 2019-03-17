# Deploying Deep Learning Models on Smartphones

This GitHub repository is the code accompaniment of the following paper:
> **Guidelines and Benchmarks for Deployment of Deep Learning Models on Smartphones as Real-Time Apps**<br>
> Abhishek Sehgal and Nasser Kehtarnavaz, The University of Texas at Dallas<br>
> https://arxiv.org/abs/1901.02144<br>
> https://www.mdpi.com/2504-4990/1/1/27<br>
>
> **Abstract:** Deep learning solutions are being increasingly used in mobile applications. Although there are many open-source software tools for the development of deep learning solutions, there are no guidelines in one place in a unified manner for using these tools toward real-time deployment of these solutions on smartphones. From the variety of available deep learning tools, the most suited ones are used in this paper to enable real-time deployment of deep learning inference networks on smartphones. A uniform flow of implementation is devised for both Android and iOS smartphones. The advantage of using multi-threading to achieve or improve real-time throughputs is also showcased. A benchmarking framework consisting of accuracy, CPU/GPU consumption, and real-time throughput is considered for validation purposes. The developed deployment approach allows deep learning models to be turned into real-time smartphone apps with ease based on publicly available deep learning and smartphone software tools. This approach is applied to six popular or representative convolutional neural network models, and the validation results based on the benchmarking metrics are reported.

## Resources
All material related to our paper is available via the following links:

|**Link**|Description
|:-------|:----------
|https://arxiv.org/abs/1901.02144| arXiv version of Manuscript
|https://www.mdpi.com/2504-4990/1/1/27| MDPI version of Manuscript
|http://www.utdallas.edu/~kehtar/DNN-apps-demo.mp4| Video of the deep learning models on the Android and iOS Smartphones

## Getting Started
These codes contain python notebooks that will guide you through the process of converting the pre-trained model into smartphone-required model files, Android Studio project for deploying the models on android devices and Xcode project for deploying the models on iOS devices.

## Prerequisites

As Python is the de-facto coding language used for TensorFlow and Keras, we used Anaconda to manage the environment for creating mobile deployment models. The Anaconda environment used for the development of this repository can be duplicated by running the following code after installing [Anaconda](https://www.anaconda.com/distribution/):
```
conda env create -f Deep-Learning-Mobile.yml
```
The TensorFlow and Keras installed in this environment are the CPU version of the libraries. To install the GPU versions of these libraries, you can change it in the .yml file or update it after creating the environment.

## Coding IDE

The applications were developed using Xcode IDE for iOS and Android Studio for Android. For image processing OpenCV wrappers were used for both devices. On Android, the image capturing API used was also developed using OpenCV.

To set up OpenCV for android, the following links were used:

- https://opencv.org/platforms/android/
- https://blog.codeonion.com/2016/04/09/show-camera-on-android-app-using-opencv-for-android/

To set up OpenCV for iOS, the following links were used:
- https://docs.opencv.org/2.4.13.7/doc/tutorials/introduction/ios_install/ios_install.html
- https://medium.com/@dwayneforde/image-recognition-on-ios-with-swift-and-opencv-b5cf0667b79
