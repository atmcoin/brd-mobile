package com.fabriik.signup.ui.kyc.idupload

import android.net.Uri
import com.fabriik.signup.ui.base.FabriikContract

interface KycBaseIdUploadContract {

    sealed class Event : FabriikContract.Event {
        object FragmentStarted: Event()
        object NextClicked: Event()
        object RetryClicked: Event()
        object TakePhotoClicked: Event()
        object TakePhotoFailed: Event()
        class TakePhotoCompleted(val uri: Uri): Event()
    }

    sealed class Effect : FabriikContract.Effect {
        object ShowCameraPreview : Effect()
        class ShowImagePreview(val imageUri: Uri) : Effect()
        class GoToNextStep(val imageUri: Uri) : Effect()
    }

    data class State(
        val imageUri: Uri? = null,
        val nextEnabled: Boolean = false,
        val retryEnabled: Boolean = false,
        val takePhotoEnabled: Boolean = true
    ): FabriikContract.State
}