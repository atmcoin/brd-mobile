package com.fabriik.swap.ui.sellingcurrency

import android.app.Application
import androidx.lifecycle.*
import com.fabriik.swap.data.SwapApi
import com.fabriik.swap.ui.SwapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SelectSellingCurrencyViewModel(
    application: Application
) : AndroidViewModel(application), LifecycleObserver {

    val actions: Channel<SelectSellingCurrencyAction> = Channel(Channel.UNLIMITED)

    val state: LiveData<SelectSellingCurrencyState>
        get() = _state

    val effect: LiveData<SelectSellingCurrencyEffect?>
        get() = _effect

    private var searchQuery: String = ""
    private val api: SwapApi = SwapApi.create()
    private val _state = MutableLiveData<SelectSellingCurrencyState>().apply { value = SelectSellingCurrencyState() }
    private val _effect = MutableLiveData<SelectSellingCurrencyEffect?>()
    private var currencies: List<SwapViewModel.SellingCurrencyData> = mutableListOf()

    init {
        handleAction()
    }

    private fun handleAction() {
        viewModelScope.launch {
            actions.consumeAsFlow().collect {
                when (it) {
                    is SelectSellingCurrencyAction.LoadCurrencies -> loadCurrencies()
                    is SelectSellingCurrencyAction.SearchChanged -> onSearchChanged(it)
                    is SelectSellingCurrencyAction.CurrencySelected -> onCurrencySelected(it)
                }
            }
        }
    }

    private fun loadCurrencies() {
        if (currencies.isNotEmpty()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateState {
                    it.copy(isLoading = true)
                }

                currencies = api.getCurrencies().map { currency ->
                    SwapViewModel.SellingCurrencyData(
                        currency = currency,
                        cryptoBalance = BigDecimal.ONE,
                        fiatBalance = BigDecimal.TEN
                    )
                }

                updateState {
                    it.copy(
                        isLoading = false,
                        currencies = currencies
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    private fun onCurrencySelected(action: SelectSellingCurrencyAction.CurrencySelected) {
        _effect.postValue(
            SelectSellingCurrencyEffect.NavigateToBuyingCurrencies(
                action.currency.currency
            )
        )
    }

    private fun onSearchChanged(action: SelectSellingCurrencyAction.SearchChanged) {
        searchQuery = action.query

        viewModelScope.launch(Dispatchers.IO) {
            updateState {
                it.copy(
                    currencies = currencies.filter { data ->
                        data.currency.name.contains(searchQuery) ||
                                data.currency.fullName.contains(searchQuery)
                    }
                )
            }
        }
    }

    private suspend fun updateState(handler: suspend (intent: SelectSellingCurrencyState) -> SelectSellingCurrencyState) {
        _state.postValue(handler(state.value!!))
    }
}