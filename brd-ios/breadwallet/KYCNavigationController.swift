// 
// Created by Equaleyes Solutions Ltd
// 

import UIKit

class KYCNavigationController: UINavigationController, UINavigationControllerDelegate {
    override func viewDidLoad() {
        super.viewDidLoad()
        
        delegate = self
        
        setup()
        setupHeader()
    }
    
    private func setup() {
        if #available(iOS 13.0, *) {
            let appearance = UINavigationBarAppearance()
            appearance.configureWithOpaqueBackground()
            appearance.backgroundColor = .clear
            appearance.shadowColor = nil
            
            navigationBar.scrollEdgeAppearance = appearance
            navigationBar.standardAppearance = appearance
            navigationBar.compactAppearance = appearance
        }
        
        navigationBar.tintColor = .kycCompletelyWhite
        navigationBar.barTintColor = .kycCompletelyWhite
        navigationBar.shadowImage = UIImage()
        navigationBar.prefersLargeTitles = false
        
        view.backgroundColor = .almostBlack
    }
    
    private func setupHeader() {
        let headerImageView = UIImageView(image: UIImage(named: "KYC Header Logo"))
        headerImageView.contentMode = .scaleAspectFit
        headerImageView.frame = navigationBar.frame
        navigationBar.addSubview(headerImageView)
    }
    
    func navigationController(_ navigationController: UINavigationController, willShow viewController: UIViewController, animated: Bool) {
        let item = KYCBackBarButtonItem(title: "        ", style: .plain, target: nil, action: nil)
        viewController.navigationItem.backBarButtonItem = item
    }
}

class KYCBackBarButtonItem: UIBarButtonItem {
    @available(iOS 14.0, *)
    override var menu: UIMenu? {
        get {
            return super.menu
        }
        set {}
    }
}
