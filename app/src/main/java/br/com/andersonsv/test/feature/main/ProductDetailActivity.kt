package br.com.andersonsv.test.feature.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.andersonsv.test.R
import br.com.andersonsv.test.extension.asBRLNoFraction
import br.com.andersonsv.test.extension.makeVisible
import br.com.andersonsv.test.network.model.product.Product
import br.com.andersonsv.test.util.Constants
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : AppCompatActivity() {

    private val SINGLE_IMAGE = 1
    private var currentImagePosition: Int = 0
   // private var galleryAdapterClickSubscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       val product = intent.getParcelableExtra<Product>(Constants.INTENT_PRODUCT)
            ?: throw IllegalStateException("field ${Constants.INTENT_PRODUCT} missing in Intent")

        configureData(product)

        //configureImages(product.photos)
    }

    private fun configureData(product: Product){
        textViewContent.text = product.content
        textViewOriginalPrice.text = product.originalPrice.asBRLNoFraction(false)

        if(product.discountPorcentage != 0.0) {
            textViewDiscount.makeVisible()
            textViewDiscount.text = String.format(getString(R.string.product_detail_discount), product.discountPorcentage)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_product_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> false
    }

    /*private fun configureImages(images: MutableList<Photo>) {

        var imagesUrl = ArrayList<String>()

        for (item in images){
            imagesUrl.add(item.generateImageUrl())
        }

        if (images.size <= SINGLE_IMAGE) indicatorProductImages.makeGone()
        else indicatorProductImages.makeVisible()

        galleryAdapterClickSubscription?.dispose()

        val adapter = ImageSliderAdapter(
            imagesUrl
        ) { view, content ->
            (view as ImageView).loadImage(content)
        }

        galleryAdapterClickSubscription = adapter.clickItem()
            .subscribe {
                //clickViewPager.onNext(it)
            }

        viewPagerProductImages.adapter = adapter
        viewPagerProductImages.currentItem = currentImagePosition
        indicatorProductImages.setViewPager(viewPagerProductImages)
    }*/
}