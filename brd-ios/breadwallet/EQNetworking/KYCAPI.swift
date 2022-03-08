// 
// Created by Equaleyes Solutions Ltd
//

import Foundation

enum KYCEndpoints: String, URLType {
    static var baseURL: String = "https://one-dev.moneybutton.io/blocksatoshi/one/kyc/%@"
    
    case personalInformation = "pi?%@"
    case uploadSelfieImage = "upload?type=SELFIE%@"
    case uploadFrontBackImage = "upload?type=ID%@"
    
    var url: String {
        return String(format: Self.baseURL, rawValue)
    }
}

class KYCBaseResponseWorker<T: ModelResponse, U: Model, V: ModelMapper<T, U>>: BaseResponseWorker<T, U, V> {
    
}

class KYCBasePlainResponseWorker: BasePlainResponseWorker {
    
}