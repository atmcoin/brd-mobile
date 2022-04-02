// 
// Created by Equaleyes Solutions Ltd
//

import UIKit

class KYCProgressView: BaseView, GenericSettable {
    typealias Model = ViewModel
    
    struct ViewModel: Hashable {
        let text: String
        let progress: Progress
    }
    
    enum Progress: Int, CaseIterable {
        case address
        case personalInfo
        case idFront
        case idBack
        case idSelfie
        case complete
        
        var value: Float {
            let percent: Float = Float(100 / Progress.allCases.count) + 1
            let realValue: Float = Float(rawValue + 1) * percent
            
            return realValue / 100
        }
    }
    
    private lazy var titleLabel: UILabel = {
        let titleLabel = UILabel()
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.textAlignment = .center
        titleLabel.textColor = .almostBlack
        titleLabel.font = UIFont(name: "AvenirNext-Bold", size: 20)
        
        return titleLabel
    }()
    
    private lazy var progressView: UIProgressView = {
        let progressView = UIProgressView()
        progressView.translatesAutoresizingMaskIntoConstraints = false
        progressView.transform = progressView.transform.scaledBy(x: 1, y: 1.6)
        progressView.trackTintColor = .kycGray3
        progressView.progressTintColor = .almostBlack
        
        return progressView
    }()
    
    override func setupSubviews() {
        super.setupSubviews()
        
        addSubview(titleLabel)
        titleLabel.topAnchor.constraint(equalTo: topAnchor, constant: 36).isActive = true
        titleLabel.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 10).isActive = true
        titleLabel.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -10).isActive = true
        
        addSubview(progressView)
        progressView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 28).isActive = true
        progressView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 40).isActive = true
        progressView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -40).isActive = true
        progressView.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -28).isActive = true
    }
    
    func setup(with model: Model) {
        titleLabel.text = model.text
        progressView.progress = model.progress.value
    }
}