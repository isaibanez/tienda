package com.example.tienda.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tienda.databinding.ItemRecyclerCartBinding
import com.example.tienda.model.Product

class CartAdapter(var listaCarrito: List<Product>, var contexto: Context) : RecyclerView.Adapter<CartAdapter.CartHolder>() {
    inner class CartHolder(var binding: ItemRecyclerCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartHolder {
        val binding: ItemRecyclerCartBinding = ItemRecyclerCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartHolder(binding)
    }

    // Aquí le indicamos qué datos tiene que mostrar en pantalla (en cada fila: título, precio y foto).
    override fun onBindViewHolder(
        holder: CartHolder,
        position: Int
    ) {
        val productCart = listaCarrito[position]

        holder.binding.productTitleRecyclerCart.text = productCart.title
        holder.binding.productPriceRecyclerCart.text = String.format("%.2f", productCart.price)
        Glide.with(contexto).load(productCart.thumbnail).into(holder.binding.productThumbnailRecyclerCart)

    }

    override fun getItemCount(): Int {
        return listaCarrito.size
    }

}