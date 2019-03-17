//
//  ViewController.swift
//  CameraDemo
//
//  Created by Sehgal, Abhishek on 5/3/18.
//  Copyright © 2018 Sehgal, Abhishek. All rights reserved.
//

import UIKit
import AVFoundation
import CoreML

class ViewController: UIViewController, FrameExtractorDelegate {
    
    var frameExtractor: FrameExtractor!
    let openCVWrapper = OpenCVWrapper()
    
//    Update the model name here to which one you intend on using and its input image size
    let model = InceptionV3()
    let inputSize: Int32 = 299
    var cgImage: CGImage!
    
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var predictLabel: UITextView!
    
    let updateLabel = { (probs: Dictionary<String, Double>) -> (String) in
        var label: String = ""
        let probs_sorted = probs.sorted(by: {$0.value > $1.value})
        
        for index in 0...4 {
            label = label + "\(index+1). " + probs_sorted[index].key + " " + String(format: "%.0f%% \n", probs_sorted[index].value * 100)
        }
        return label
    }
    
    // This function loads the Frame Extractor class that extracts frames on the fly from the smartphone
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        frameExtractor = FrameExtractor()
        frameExtractor.delegate = self
    }
    
    // This function preprocesses the image to the appropriate size for the Neural Network
    // and then predicts the class
    func captured(image: UIImage) {
        imageView.image = image
        cgImage = openCVWrapper.preprocessImage(image, image_size: inputSize)?.cgImage
        DispatchQueue.global(qos: .background).async {
            self.predict()
        }
    }
    
    func predict() {
            let pixelBuffer = pixelBufferFromImage(image: cgImage)
            let output = try? model.prediction(image: pixelBuffer!)
            DispatchQueue.main.async {
                self.predictLabel.text = self.updateLabel((output?.classLabelProbs)!)
            }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func pixelBufferFromImage(image: CGImage) -> CVPixelBuffer? {
        let frameSize = CGSize(width: image.width, height: image.height)
        var pixelBuffer: CVPixelBuffer? = nil
        let status = CVPixelBufferCreate(kCFAllocatorDefault,
                                         Int(frameSize.width),
                                         Int(frameSize.height),
                                         kCVPixelFormatType_32BGRA,
                                         nil,
                                         &pixelBuffer)
        if(status != kCVReturnSuccess) { return nil }
        
        CVPixelBufferLockBaseAddress(pixelBuffer!, CVPixelBufferLockFlags(rawValue: 0))
        let data = CVPixelBufferGetBaseAddress(pixelBuffer!)
        let rgbColorSpace = CGColorSpaceCreateDeviceRGB()
        let context = CGContext(data: data,
                                width: Int(frameSize.width),
                                height: Int(frameSize.height),
                                bitsPerComponent: 8,
                                bytesPerRow: CVPixelBufferGetBytesPerRow(pixelBuffer!),
                                space: rgbColorSpace,
                                bitmapInfo: CGBitmapInfo(rawValue: CGImageAlphaInfo.premultipliedFirst.rawValue).rawValue | CGBitmapInfo(rawValue: CGImageByteOrderInfo.order32Little.rawValue).rawValue)
        context?.draw(image, in: CGRect(x: 0, y: 0, width: frameSize.width, height: frameSize.height))
        
        CVPixelBufferUnlockBaseAddress(pixelBuffer!, CVPixelBufferLockFlags(rawValue: 0))
        
        return pixelBuffer
    }


}

