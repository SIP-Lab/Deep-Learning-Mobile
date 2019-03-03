# Deploying Deep Learning Models on Smartphones

This GitHub repository is the code accompaniment of the following paper:
> **"Guidelines and Benchmarks for Deployment of Deep Learning Models on Smartphones as Real-Time Apps"**<br>
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

As Python is the de-facto coding language used for TensorFlow and Keras, we used Anaconda. The Anaconda environments used for the development of this repository can be duplicated by running the following code:
