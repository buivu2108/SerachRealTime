package com.example.searchrealtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchrealtime.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView: RecyclerView = findViewById(R.id.rcvNumber)
        val layoutManager = LinearLayoutManager(this)
        val numberAdapter = NumberAdapter()
        recyclerView.adapter = numberAdapter
        recyclerView.layoutManager = layoutManager

        val numberArray = ArrayList<Int>()
        for (i in 1..100) {
            numberArray.add(i)

        }

        Observable.create<ArrayList<Int>> {
            binding.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    numberAdapter.numberArray.clear()
                    val list = ArrayList<Int>()
                    for (item in numberArray) {
                        if (item.toString().contains(newText!!)) {
                            list.add(item)
                        }
                    }

                    it.onNext(list)

                    return false
                }
            })
        }.debounce(2000,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread()).subscribe({
            numberAdapter.addData(it)
        },{
            it.printStackTrace()
        },{

        })
    }


}