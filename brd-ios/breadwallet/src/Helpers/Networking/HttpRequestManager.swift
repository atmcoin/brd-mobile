//
//  EQNetworking
//  Copyright © 2022 Equaleyes Ltd. All rights reserved.
//

import Foundation

final class HTTPRequestManager {
    func request(_ method: EQHTTPMethod,
                 url: String,
                 headers: [String: String] = [:],
                 media: [MultiPart] = [],
                 parameters: [String: Any] = [:],
                 encoding: Encoding = .json) -> HTTPRequest {
        return HTTPRequest(method: method, url: url, headers: headers, media: media, parameters: parameters, encoding: encoding)
    }
}
