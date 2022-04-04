package com.fabriik.swap.ui.sellingcurrency

import android.app.Application
import androidx.lifecycle.*
import com.fabriik.swap.data.SwapApi
import com.fabriik.swap.data.SwapCurrenciesRepository
import com.fabriik.swap.data.model.SellingCurrencyData
import com.fabriik.swap.ui.base.SwapViewModel
import com.fabriik.swap.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.direct
import org.kodein.di.erased.instance

class SelectSellingCurrencyViewModel(
    application: Application
) : AndroidViewModel(application),
    SwapViewModel<SelectSellingCurrencyState, SelectSellingCurrencyAction, SelectSellingCurrencyEffect?>,
    LifecycleObserver, KodeinAware {

    override val kodein by closestKodein { application.applicationContext }
    override val actions: Channel<SelectSellingCurrencyAction> = Channel(Channel.UNLIMITED)

    override val state: LiveData<SelectSellingCurrencyState>
        get() = _state

    override val effect: LiveData<SelectSellingCurrencyEffect?>
        get() = _effect

    private val currenciesRepository = SwapCurrenciesRepository(
        swapApi = SwapApi.create(),
        breadBox = direct.instance(),
        ratesRepository = direct.instance()
    )

    private val _state = MutableLiveData<SelectSellingCurrencyState>().apply {
        value = SelectSellingCurrencyState()
    }
    private val _effect = SingleLiveEvent<SelectSellingCurrencyEffect?>()

    private var searchQuery: String = ""
    private var currencies: List<SellingCurrencyData> = mutableListOf()

    init {
        handleAction()
    }

    private fun handleAction() {
        viewModelScope.launch {
            actions.consumeAsFlow().collect {
                when (it) {
                    is SelectSellingCurrencyAction.Close -> {
                        _effect.postValue(
                            SelectSellingCurrencyEffect.GoToHome
                        )
                    }
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

                currencies = currenciesRepository.getSellingCurrenciesData()

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
            SelectSellingCurrencyEffect.GoToBuyingCurrencySelection(
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
                        data.currency.name.contains(searchQuery, true) ||
                                data.currency.fullName.contains(searchQuery, true)
                    }
                )
            }
        }
    }

    private suspend fun updateState(handler: suspend (intent: SelectSellingCurrencyState) -> SelectSellingCurrencyState) {
        _state.postValue(handler(state.value!!))
    }
}