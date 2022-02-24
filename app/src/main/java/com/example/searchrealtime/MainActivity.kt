package com.example.searchrealtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchrealtime.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val publishSubject = PublishSubject.create<String>()!!

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

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                numberAdapter.numberArray.clear()
                publishSubject.onNext(newText!!)
                return false
            }
        })

        publishSubject.debounce(1000, TimeUnit.MILLISECONDS).filter { it.isNotEmpty() }.distinctUntilChanged().flatMap {
            search(it, numberArray)
        }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                numberAdapter.addData(it)
            }, {
                it.printStackTrace()
            }, {

            })
    }

    private fun search(text: String, numberArray: ArrayList<Int>): Observable<ArrayList<Int>> {
        return Observable.create<ArrayList<Int>> {
            val list = ArrayList<Int>()
            for (item in numberArray) {
                if (item.toString().contains(text)) {
                    list.add(item)
                }
            }
            it.onNext(list)
        }.delay(2000L, TimeUnit.MILLISECONDS)
    }
}

