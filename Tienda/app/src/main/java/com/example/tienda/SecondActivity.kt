package com.example.tienda

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tienda.adapter.CartAdapter
import com.example.tienda.databinding.ActivityMainBinding
import com.example.tienda.databinding.ActivitySecondBinding
import com.example.tienda.model.Product
import com.google.android.material.snackbar.Snackbar

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private var listaCart: ArrayList<Product> = ArrayList()
    private lateinit var adapterCart: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCart)
        supportActionBar?.title = "Carrito"

        listaCart = intent.getSerializableExtra("carrito") as? ArrayList<Product> ?: arrayListOf()

        setupRecyclerView()
        calcularTotalCarrito()
    }

    // Inicializamos y conectamos con el recycler de la lista de productos añadidos al carrito.
    private fun setupRecyclerView() {
        adapterCart = CartAdapter(listaCart, this)

        binding.recyclerCart.adapter = adapterCart

        binding.recyclerCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    // Aquí calculamos el importe total del carrito.
    private fun calcularTotalCarrito() {
        var totalCart = 0.0
        for (product in listaCart) {
            totalCart += product.price ?: 0.0
        }

        binding.recyclerTotalCart.text = "Total: ${String.format("%.2f", totalCart)} €"
    }

    // Menú.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.second_menu, menu)
        return true
    }

    // Aquí indicamos lo que hará al confirmar el carrito + al borrar el carrito.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_confirm_cart -> {
                var total = listaCart.sumOf {
                    it.price ?: 0.0
                }
                Snackbar.make(binding.root, "Enhorabuena, compra por valor de ${String.format("%.2f", total)} € realizada.", Snackbar.LENGTH_SHORT).show()
            }
            R.id.item_empty_cart -> {
                listaCart.clear()

                adapterCart.notifyDataSetChanged()

                calcularTotalCarrito()

                Snackbar.make(binding.root, "Carrito vaciado.", Snackbar.LENGTH_SHORT).show()
            }
        }
        return true
    }
}