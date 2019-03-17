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
    let model = InceptionV3()
    var cgImage: CGImage!
    
    var frameCount = 0;
    var movingAverageBuffer = MovingAverageBuffer(period: 10)
    
    var nFramesPredicted = 0
    var startTime: DispatchTime!
    var endTime: DispatchTime!
    var elapsedTime: UInt64?
    
    
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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        frameExtractor = FrameExtractor()
        frameExtractor.delegate = self
        //print(model.model.modelDescription.description)
//        Timer.scheduledTimer(withTimeInterval: 0.7,
//                                         repeats: true,
//                                         block: {_ in
//                                            guard let pixelBuffer = OpenCVWrapper.pixelBuffer(from: self.cgImage)?.takeRetainedValue() else {return}
//                                            guard let output = try? self.model.prediction(image: pixelBuffer) else {return}
//                                            let probs = output.classLabelProbs.sorted(by:{$0.value > $1.value})
//                                            var label: String = ""
//
//                                            for index in 0...4 {
//                                                label = label + "\(index+1). " + probs[index].key + " " + String(format: "%.0f%% \n", probs[index].value * 100)
//                                            }
//
//                                            self.predictLabel.text = label
//        })
        
        
//        startTime = DispatchTime.now()
//        DispatchQueue.global(qos: .background).async {
//            while(true){
//                if(self.cgImage != nil){
//                    self.predict()
//                }
//            }
//        }

    }
    
    func captured(image: UIImage) {
        //autoreleasepool{
            imageView.image = image
            cgImage = openCVWrapper.preprocessImage(image, image_size: 299)?.cgImage
//            self.predict()
            DispatchQueue.global(qos: .background).async {
                self.predict()
//                self.nFramesPredicted += 1
//                if self.nFramesPredicted == 100 {
//                    self.endTime = DispatchTime.now()
//                    self.elapsedTime = self.endTime.uptimeNanoseconds - self.startTime.uptimeNanoseconds
//                    print("\(Double(self.elapsedTime!)/(100 * 1_000_000_000))")
//                    self.nFramesPredicted = 0
//                    self.startTime = DispatchTime.now()
//                }
            }
//        }
        
    }
    
    func predict() {
 //       autoreleasepool{
            let pixelBuffer = pixelBufferFromImage(image: cgImage)
            let output = try? model.prediction(image: pixelBuffer!)
//            self.predictLabel.text = self.updateLabel((output?.classLabelProbs)!)
            DispatchQueue.main.async {
                self.predictLabel.text = self.updateLabel((output?.classLabelProbs)!)
            }
//        }
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

