package br.com.andersonsv.test.feature.main

import android.os.Bundle
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import br.com.andersonsv.test.R
import br.com.andersonsv.test.adapter.ImageSliderAdapter
import br.com.andersonsv.test.extension.asBRLNoFraction
import br.com.andersonsv.test.extension.generateImageUrl
import br.com.andersonsv.test.extension.loadImage
import br.com.andersonsv.test.extension.makeVisible
import br.com.andersonsv.test.network.model.product.Photo
import br.com.andersonsv.test.network.model.product.Product
import br.com.andersonsv.test.util.Constants
import kotlinx.android.synthetic.main.activity_product_detail.*
import java.text.MessageFormat

class ProductDetailActivity : AppCompatActivity() {

    private var currentImagePosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       val product = intent.getParcelableExtra<Product>(Constants.INTENT_PRODUCT)
            ?: throw IllegalStateException("field ${Constants.INTENT_PRODUCT} missing in Intent")

        configureData(product)

        configureImages(product.photos)
    }

    private fun configureData(product: Product){

        textViewTitle.text = product.title
        textViewContent.text = product.content
        textViewPrice.text = product.originalPrice.asBRLNoFraction(false)

        if(product.originalPrice !=  product.price){
            textViewOriginalPrice.makeVisible()

            val originalPrice = SpannableString(product.price.asBRLNoFraction(true))
            originalPrice.setSpan(StrikethroughSpan(), 0, originalPrice.length, 0)

            textViewOriginalPrice.text = originalPrice
        }

        if(product.discountPorcentage != 0.0) {
            textViewDiscount.makeVisible()
            val discount = MessageFormat.format(getString(R.string.product_detail_discount), product.discountPorcentage.asBRLNoFraction())
            textViewDiscount.text = discount
        }

        if(product.maximumInstallment != 0){
            textViewInstallments.makeVisible()
            val installments = MessageFormat.format(getString(R.string.product_detail_installments), product.maximumInstallment)
            textViewInstallments.text = installments
        }

        if(product.likesCount != 0){
            fabLikes.setBadgeNum(product.likesCount.toString())
        }

        if(product.publishedCommentsCount != 0){
            fabComments.setBadgeNum(product.publishedCommentsCount.toString())
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

    private fun configureImages(images: MutableList<Photo>) {

        val imagesUrl = ArrayList<String>()

        for (item in images){
            imagesUrl.add(item.generateImageUrl())
        }

        val adapter = ImageSliderAdapter(
            imagesUrl
        ) { view, content ->
            (view as ImageView).loadImage(content)
        }

        viewPagerProductImages.adapter = adapter
        viewPagerProductImages.currentItem = currentImagePosition
        indicatorProductImages.setViewPager(viewPagerProductImages)
    }
}