// 
// Created by Equaleyes Solutions Ltd
//

import UIKit

protocol KYCAddressBusinessLogic {
    // MARK: Business logic functions
    
    func executeGetDataForPickerView(request: KYCAddress.GetDataForPickerView.Request)
    func executeCheckFieldPickerIndex(request: KYCAddress.CheckFieldPickerIndex.Request)
    func executeCheckFieldType(request: KYCAddress.CheckFieldText.Request)
    func executeSubmitData(request: KYCAddress.SubmitData.Request)
}

protocol KYCAddressDataStore {
    // MARK: Data store
    
    var country: String? { get set }
    var zipCode: String? { get set }
    var address: String? { get set }
    var apartment: String? { get set }
    var city: String? { get set }
    var state: String? { get set }
    var dateOfBirth: String? { get set }
    var taxIdNumber: String? { get set }
    
    var countrySelectedIndex: PickerViewViewController.Index? { get set }
    var countryName: String? { get set }
    
    var stateSelectedIndex: PickerViewViewController.Index? { get set }
    var stateName: String? { get set }
    
    var fieldValidationIsAllowed: [KYCAddress.FieldType: Bool] { get set }
}

class KYCAddressInteractor: KYCAddressBusinessLogic, KYCAddressDataStore {
    var presenter: KYCAddressPresentationLogic?
    
    // MARK: Interactor functions
    
    var country: String?
    var zipCode: String?
    var address: String?
    var apartment: String?
    var city: String?
    var state: String?
    var dateOfBirth: String?
    var taxIdNumber: String?
    
    var countrySelectedIndex: PickerViewViewController.Index?
    var countryName: String?
    
    var stateSelectedIndex: PickerViewViewController.Index?
    var stateName: String?
    
    var fieldValidationIsAllowed = [KYCAddress.FieldType: Bool]()
    
    func executeSubmitData(request: KYCAddress.SubmitData.Request) {
        let worker = KYCPostPersonalInformationWorker()
        let workerUrlModelData = KYCPostPersonalInformationWorkerUrlModelData()
        let workerRequest = KYCPostPersonalInformationWorkerRequest(street: (address ?? "") + " " + (apartment ?? ""),
                                                                    city: city,
                                                                    state: state,
                                                                    zip: zipCode,
                                                                    country: country,
                                                                    dateOfBirth: dateOfBirth,
                                                                    taxIdNumber: taxIdNumber)
        let workerData = KYCPostPersonalInformationWorkerData(workerRequest: workerRequest,
                                                              workerUrlModelData: workerUrlModelData)
        
        worker.execute(requestData: workerData) { [weak self] error in
            guard error == nil else {
                self?.presenter?.presentError(response: .init(error: error))
                return
            }
            
            self?.presenter?.presentSubmitData(response: .init())
        }
    }
    
    func executeGetDataForPickerView(request: KYCAddress.GetDataForPickerView.Request) {
        switch request.type {
        case .country:
            presenter?.presentGetDataForPickerView(response: .init(index: countrySelectedIndex,
                                                                   type: request.type))
            
        case .state:
            presenter?.presentGetDataForPickerView(response: .init(index: stateSelectedIndex,
                                                                   type: request.type))
            
        default:
            break
            
        }
    }
    
    func executeCheckFieldPickerIndex(request: KYCAddress.CheckFieldPickerIndex.Request) {
        let index = request.index
        let fieldValues = request.fieldValues
        let pickerValues = request.pickerValues
        
        switch request.type {
        case .country:
            country = index == nil ? nil : fieldValues[index?.row ?? 0]
            countryName = index == nil ? nil : pickerValues[index?.row ?? 0]
            countrySelectedIndex = index
            
        case .state:
            state = index == nil ? nil : fieldValues[index?.row ?? 0]
            stateName = index == nil ? nil : pickerValues[index?.row ?? 0]
            stateSelectedIndex = index
            
        default:
            break
        }
        
        presenter?.presentSetPickerValue(response: .init(country: countryName ?? "", state: stateName ?? ""))
        
        fieldValidationIsAllowed[request.type] = index != nil
        
        checkCredentials()
    }
    
    func executeCheckFieldType(request: KYCAddress.CheckFieldText.Request) {
        switch request.type {
        case .zipCode:
            zipCode = request.text
            
        case .address:
            address = request.text
            
        case .apartment:
            apartment = request.text
            
        case .city:
            city = request.text
            
        case .state:
            state = request.text
            
        default:
            break
        }
        
        checkCredentials()
    }
    
    private func checkCredentials() {
        var validationValues = [Bool]()
        validationValues.append(!country.isNilOrEmpty)
        validationValues.append(!state.isNilOrEmpty)
        validationValues.append(!city.isNilOrEmpty)
        validationValues.append(!zipCode.isNilOrEmpty)
        validationValues.append(!address.isNilOrEmpty)
        validationValues.append(validateCity())
        validationValues.append(validateZipCode())
        validationValues.append(validateAddress())
        
        let shouldEnable = !validationValues.contains(false)
        
        presenter?.presentShouldEnableSubmit(response: .init(shouldEnable: shouldEnable))
    }
    
    private func validateCity() -> Bool {
        guard let city = city else { return false }
        
        let isViable = !city.isEmpty
        presenter?.presentValidateField(response: .init(isViable: isViable, type: .city, isFieldEmpty: !isViable))
        
        return isViable
    }
    
    private func validateZipCode() -> Bool {
        guard let zipCode = zipCode else { return false }
        
        let isViable = !zipCode.isEmpty
        presenter?.presentValidateField(response: .init(isViable: isViable, type: .zipCode, isFieldEmpty: !isViable))
        
        return isViable
    }
    
    private func validateAddress() -> Bool {
        guard let address = address else { return false }
        
        let isViable = !address.isEmpty
        presenter?.presentValidateField(response: .init(isViable: isViable, type: .address, isFieldEmpty: !isViable))
        
        return isViable
    }
}