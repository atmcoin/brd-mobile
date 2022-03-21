package com.fabriik.buy.data

import com.fabriik.buy.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WyreApi(private val service: WyreService) {

    suspend fun getPaymentUrl(isTestNetwork: Boolean) = service.getPaymentUrl(
        auth = BuildConfig.FABRIIC_CLIENT_TOKEN,
        isTestNetwork = isTestNetwork
    )

    companion object {

        const val REDIRECT_URL = "https://fabriikw.com"

        fun create() = WyreApi(
            Retrofit.Builder()
                .baseUrl(FabriikApiConstants.HOST_WYRE_API)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(WyreService::class.java)
        )
    }
}