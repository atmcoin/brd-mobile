// 
// Created by Equaleyes Solutions Ltd
//

import UIKit

protocol Roundable {
    var isCircle: Bool { get set }
    var cornerRadius: CGFloat { get set }
    var borderWidth: CGFloat { get set }
    var borderColor: UIColor { get set }
}

extension Roundable where Self: UIView {
    func setupLayer() {
        clipsToBounds = true

        if isCircle {
            layer.cornerRadius = frame.width < frame.height ? frame.width / 2 : frame.height / 2
        } else {
            layer.cornerRadius = cornerRadius
        }

        layer.borderWidth = borderWidth
        layer.borderColor = borderColor.cgColor
        
        if #available(iOS 13.0, *) {
            layer.cornerCurve = .continuous
        }
    }
}
