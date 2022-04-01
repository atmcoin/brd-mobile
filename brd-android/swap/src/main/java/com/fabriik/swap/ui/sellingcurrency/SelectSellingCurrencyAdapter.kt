package com.fabriik.swap.ui.sellingcurrency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.breadwallet.util.formatCryptoForUi
import com.breadwallet.tools.manager.BRSharedPrefs
import com.breadwallet.util.formatFiatForUi
import com.fabriik.swap.R
import com.fabriik.swap.data.model.SellingCurrencyData
import com.fabriik.swap.databinding.ListItemSellingCurrencyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

class SelectSellingCurrencyAdapter(private val callback: (SellingCurrencyData) -> Unit) :
    ListAdapter<SellingCurrencyData, SelectSellingCurrencyAdapter.CurrencyViewHolder>(
        CallbackDiff
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CurrencyViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_selling_currency, parent, false
        )
    )

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(
            item = getItem(position),
            callback = callback
        )
    }

    override fun onViewRecycled(holder: SelectSellingCurrencyAdapter.CurrencyViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    inner class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ListItemSellingCurrencyBinding.bind(view)
        private val boundScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        fun bind(item: SellingCurrencyData, callback: (SellingCurrencyData) -> Unit) {
            binding.apply {
                root.setOnClickListener { callback(item) }

                tvCurrency.text = item.currency.fullName

                val fiatIso = BRSharedPrefs.getPreferredFiatIso()
                tvCryptoBalance.text = item.cryptoBalance.formatCryptoForUi(
                    currencyCode = item.currency.name
                )
                tvFiatBalance.text = item.fiatBalance.formatFiatForUi(
                    currencyCode = fiatIso
                )
                tvTradePrice.text = item.fiatPricePerUnit.formatFiatForUi(
                    currencyCode = fiatIso
                )

                viewIcon.loadIconForCurrency(
                    currencyCode = item.currency.name,
                    scope = boundScope
                )
            }
        }

        fun unbind() {
            boundScope.coroutineContext.cancelChildren()
        }
    }

    private object CallbackDiff : DiffUtil.ItemCallback<SellingCurrencyData>() {

        override fun areItemsTheSame(oldItem: SellingCurrencyData, newItem: SellingCurrencyData) =
            newItem.currency.name == oldItem.currency.name

        override fun areContentsTheSame(
            oldItem: SellingCurrencyData,
            newItem: SellingCurrencyData
        ) =
            newItem == oldItem
    }
}
