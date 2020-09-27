package com.example.takecare.ui.advice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.adapter.InformationAdapter
import com.example.takecare.model.Tip
import kotlinx.android.synthetic.main.activity_information.*

class InformationActivity : AppCompatActivity() {

    lateinit var tipList: ArrayList<Tip>
    private lateinit var recyclerTipAdapter : InformationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        tipList = ArrayList<Tip>()
        createData()

        recyclerTipAdapter = InformationAdapter(tipList)

        recyclerview_tips.apply {
            adapter = recyclerTipAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun createData(){
        tipList.addAll(ArrayList(listOf<Tip>(
            Tip(
                title = getString(R.string.title1),
                content =  getString(R.string.text1),
                image = R.drawable.info_img_1
            ),
            Tip(
                title = getString(R.string.title2),
                content =  "${getString(R.string.text2)}\n\n${getString(R.string.text3)}",
                image = R.drawable.info_img_2
            ),
            Tip(
                title = getString(R.string.title3),
                content = "${getString(R.string.text4)}\n\n${getString(R.string.title4)}\n\n" +
                        "${getString(R.string.text5)}\n\n${getString(R.string.text6)}\n\n" +
                        "${getString(R.string.text7)}\n\n${getString(R.string.text8)}",
                image = R.drawable.info_img_3
            ),
            Tip(
                title = getString(R.string.title5),
                content = "${getString(R.string.text9)}\n\n${getString(R.string.text10)}\n\n" +
                        getString(R.string.text11),
                image = R.drawable.info_img_5
            ),
            Tip(
                title = getString(R.string.title6),
                content = "${getString(R.string.text12)}\n\n${getString(R.string.text13)}",
                image = R.drawable.info_img_6
            ),
            Tip(
                title = getString(R.string.title7),
                content = getString(R.string.text14),
                image = R.drawable.info_img_7
            )
        )))
    }
}
