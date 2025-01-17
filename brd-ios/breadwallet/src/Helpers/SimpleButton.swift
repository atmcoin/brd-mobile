// 
// Created by Equaleyes Solutions Ltd
//

import UIKit

class SimpleButton: RoundedView {
    enum ButtonStyle {
        // TODO: Use configurations
        case kycEnabled
        case kycDisabled
        case swapEnabled
        case swapDisabled
        
        var borderColor: UIColor? {
            switch self {
            case .kycEnabled:
                return .clear
                
            case .kycDisabled:
                return .gray3
                
            case .swapEnabled:
                return .clear
                
            case .swapDisabled:
                return .clear
                
            }
        }
        
        var backgroundColor: UIColor {
            switch self {
            case .kycEnabled:
                return .vibrantYellow
                
            case .kycDisabled:
                return Theme.primaryBackground
                
            case .swapEnabled:
                return .swapButtonEnabledColor
                
            case .swapDisabled:
                return .swapButtonDisabledColor
                
            }
        }
        
        var titleColor: UIColor {
            switch self {
            case .kycEnabled:
                return .almostBlack
                
            case .kycDisabled:
                return .gray3
                
            case .swapEnabled:
                return Theme.primaryBackground
                
            case .swapDisabled:
                return Theme.primaryBackground
                
            }
        }
    }
    
    private let button = BaseButton(type: .system)
    private var buttonStyle: ButtonStyle?
    
    var didTap: (() -> Void)?
    
    init() {
        super.init(frame: .zero)
        
        setupElements()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        setupElements()
    }
    
    func setup(as buttonStyle: ButtonStyle, title: String) {
        self.buttonStyle = buttonStyle
        
        button.setTitle(title, for: .normal)
        
        style()
    }
    
    func changeStyle(with buttonStyle: ButtonStyle) {
        self.buttonStyle = buttonStyle
        
        style()
    }
    
    private func style() {
        button.layer.masksToBounds = true
        button.layer.cornerRadius = 13
        button.layer.borderColor = buttonStyle?.borderColor?.cgColor
        button.layer.borderWidth = 2
        
        button.backgroundColor = buttonStyle?.backgroundColor
        button.setTitleColor(buttonStyle?.titleColor, for: .normal)
        
        switch buttonStyle {
        case .kycEnabled, .swapEnabled:
            button.isEnabled = true
            
        case .kycDisabled, .swapDisabled, .none:
            button.isEnabled = false
            
        }
    }
    
    private func setupElements() {
        backgroundColor = .clear
        
        addSubview(button)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.topAnchor.constraint(equalTo: topAnchor).isActive = true
        button.bottomAnchor.constraint(equalTo: bottomAnchor).isActive = true
        button.leadingAnchor.constraint(equalTo: leadingAnchor).isActive = true
        button.trailingAnchor.constraint(equalTo: trailingAnchor).isActive = true
        
        button.titleLabel?.font = UIFont(name: "AvenirNext-Bold", size: 14)
        button.addTarget(self, action: #selector(buttonTapped), for: .touchUpInside)
    }
    
    @objc private func buttonTapped() {
        didTap?()
    }
}
