# Deploying Deep Learning Models on Smartphones

This GitHub repository is the code accompaniment of the following paper:
> **Guidelines and Benchmarks for Deployment of Deep Learning Models on Smartphones as Real-Time Apps**<br>
> Abhishek Sehgal and Nasser Kehtarnavaz - University of Texas at Dallas<br>
> https://arxiv.org/abs/1901.02144<br>
> https://www.mdpi.com/2504-4990/1/1/27<br>
>
> **Abstract:** Deep learning solutions are being increasingly used in mobile applications. Although there are many open-source software tools for the development of deep learning solutions, there are no guidelines in one place in a unified manner for using these tools toward real-time deployment of these solutions on smartphones. From the variety of available deep learning tools, the most suited ones are used in this paper to enable real-time deployment of deep learning inference networks on smartphones. A uniform flow of implementation is devised for both Android and iOS smartphones. The advantage of using multi-threading to achieve or improve real-time throughputs is also showcased. A benchmarking framework consisting of accuracy, CPU/GPU consumption, and real-time throughput is considered for validation purposes. The developed deployment approach allows deep learning models to be turned into real-time smartphone apps with ease based on publicly available deep learning and smartphone software tools. This approach is applied to six popular or representative convolutional neural network models, and the validation results based on the benchmarking metrics are reported.

## Resources
Supporting materials related to this work are available via the following links:

|**Link**|Description
|:-------|:----------
|https://arxiv.org/abs/1901.02144| arXiv version of the manuscript
|https://www.mdpi.com/2504-4990/1/1/27| MDPI version of the manuscript
|http://www.utdallas.edu/~kehtar/DNN-apps-demo.mp4| Videoclip showing the deep learning models running in real-time on the Android and iOS smartphone platforms

## Getting Started
These codes contain Python notebooks that guide one through the process of converting the pre-trained model into smartphone-required model files; Android Studio project for deploying models on Android devices and Xcode project for deploying models on iOS devices.

## Prerequisites

As Python is the de-facto coding language used for TensorFlow and Keras, Anaconda is used to manage the environment for creating deployment models. The Anaconda environment used for this repository can be duplicated by running the following code after installing [Anaconda](https://www.anaconda.com/distribution/):
```
conda env create -f Deep-Learning-Mobile.yml
```
The TensorFlow and Keras installed in this environment are the CPU version of the libraries. To install the GPU versions of these libraries, need to change in the .yml file or to update after creating the environment.

## Coding IDE

The apps are developed using Xcode IDE for iOS and Android Studio for Android. For image capturing, OpenCV wrappers are used for both types of devices.

To set up OpenCV for Android, the following links are used:

- https://opencv.org/platforms/android/
- https://blog.codeonion.com/2016/04/09/show-camera-on-android-app-using-opencv-for-android/

To set up OpenCV for iOS, the following links are used:
- https://docs.opencv.org/2.4.13.7/doc/tutorials/introduction/ios_install/ios_install.html
- https://medium.com/@dwayneforde/image-recognition-on-ios-with-swift-and-opencv-b5cf0667b79

## Using the Projects

### Android Studio Project
- Clean the Project
- Make sure that OpenCV has been built and added properly to the Project
- Add the CNN model to use in the assets folder of the Project
- Update the settings in the ImageInference.java file according to the model
- If using a different set of labels, add the labels.txt file for that respective file

### iOS Project
- Build the OpenCV2 framework and add it to the main Project
- Add the model to the Project and update the model name and image size in the ViewController.swift files
- Build the Project

## License and Citation
The codes are licensed under MIT license.

For any utilization of the code content of this repository, the following paper needs to get cited by the user:

- A. Sehgal, and N. Kehtarnavaz, "Guidelines and Benchmarks for Deployment of Deep Learning Models on Smartphones as Real-Time Apps," Machine Learning and Knowledge Extraction, vol. 1, pp. 450-465, Feb 2019 (Open Access).
