package com.example.tienda.adapter

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tienda.databinding.ItemRecyclerProductBinding
import com.example.tienda.model.Product
import com.google.android.material.snackbar.Snackbar

class ProductsAdapter(var listaProductos: MutableList<Product> = mutableListOf(), var contexto: Context, val onProductAdded: (Product) -> Unit): RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {
    inner class ProductHolder(var binding: ItemRecyclerProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductHolder {
        val binding: ItemRecyclerProductBinding = ItemRecyclerProductBinding.inflate(LayoutInflater.from(contexto), parent, false)
        return ProductHolder(binding)
    }

    // Aquí le indicamos qué datos tiene que mostrar en pantalla (en cada fila: título, precio, foto y botón para añadir el producto al carrito).
    override fun onBindViewHolder(
        holder: ProductHolder,
        position: Int
    ) {
        val product = listaProductos[position]

        holder.binding.productTitleRecycler.text = product.title
        holder.binding.productPriceRecycler.text= String.format("%.2f", product.price)
        Glide.with(contexto).load(product.thumbnail).into(holder.binding.productThumbnailRecycler)

        holder.binding.addToCartBtnRecycler.setOnClickListener {
            onProductAdded(product)
            Snackbar.make(it, "${product.title} añadido al carrito.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    fun addProduct(product: Product){
        listaProductos.add(product)
        notifyItemInserted(listaProductos.size - 1)
    }

}