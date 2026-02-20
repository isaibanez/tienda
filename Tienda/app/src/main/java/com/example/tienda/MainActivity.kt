package com.example.tienda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tienda.adapter.ProductsAdapter
import com.example.tienda.databinding.ActivityMainBinding
import com.example.tienda.model.Product
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterProducts: ProductsAdapter

    private lateinit var listaCategories: MutableList<String>
    private lateinit var adapterSpinner: ArrayAdapter<String>
    private var cart = ArrayList<Product>()
    private var listaProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSpinner()
        peticionJSON()
        peticionJSONCategories()
        }

    // Inicializamos y conectamos con el recycler de productos.
    private fun setupRecyclerView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Tienda"

        adapterProducts = ProductsAdapter(listaProducts, this) {
            producto -> cart.add(producto)
        }
        binding.recyclerProducts.adapter = adapterProducts

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    // Spinner de las categorías.
    private fun setupSpinner() {
        listaCategories = mutableListOf()
        adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategories)

        binding.spinnerProducts.adapter = adapterSpinner
    }

    // Petición para obtener el JSON de los productos.
    private fun peticionJSON() {
        val url = "https://dummyjson.com/products"

        val request: JsonObjectRequest = JsonObjectRequest(url, {
            Log.v("conexion", "Conexión correcta.")
            procesarRespuesta(it)
        }, { Log.v("conexion", "Error en la conexión con el servidor.")
        })

        Volley.newRequestQueue(applicationContext).add(request)
    }

    // Petición para obtener el JSON de las categorías.
    private fun peticionJSONCategories() {
        val url = "https://dummyjson.com/products/categories"

        val request = JsonArrayRequest(url, {
            Log.v("conexion", "Conexión correcta (categories).")
            procesarRespuestaCategories(it)
        }, {
            Log.v("conexion", "Error en la conexión con el servidor (categories).")
        })

        Volley.newRequestQueue(applicationContext).add(request)
    }

    // Procesar respuesta para el JSON de los productos.
    private fun procesarRespuesta(param: JSONObject) {
        val gson = Gson()
        val productArray: JSONArray = param.getJSONArray("products")
        for(i in 0..productArray.length()-1){
            val productJSON: JSONObject = productArray.getJSONObject(i)
            val product: Product = gson.fromJson(productJSON.toString(), Product::class.java)

            adapterProducts.addProduct(product)

            Log.v("conexion", "Producto: ${product.title} | Precio: ${product.price} €")
        }
    }

    // Procesar respuesta para el JSON (array) de las categorías.
    private fun procesarRespuestaCategories(arrayCategories: JSONArray) {
        for(i in 0..arrayCategories.length()-1){
            val categoryJSON: JSONObject = arrayCategories.getJSONObject(i)
            val categoryName = categoryJSON.getString("name")

            listaCategories.add(categoryName)

            Log.v("conexion", "Categoría añadida: $categoryName")
        }

        adapterSpinner.notifyDataSetChanged()
    }

    // Menú.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Aquí indicamos que el botón del carrito llevará a la Second Activity.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_cart -> {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("carrito", cart)
                startActivity(intent)
            }
        }
        return true
    }
}


