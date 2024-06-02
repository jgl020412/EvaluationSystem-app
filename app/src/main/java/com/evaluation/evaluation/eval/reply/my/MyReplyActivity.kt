package com.evaluation.evaluation.eval.reply.my

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.evaluation.evaluation.App
import com.evaluation.evaluation.R
import com.evaluation.evaluation.base.BaseActivity
import com.evaluation.evaluation.databinding.ActivityMyReplyBinding
import com.evaluation.evaluation.databinding.ActivityReplyBinding
import com.evaluation.evaluation.eval.reply.ReplyAdapter
import com.evaluation.evaluation.eval.reply.ReplyViewModel
import com.evaluation.evaluation.model.model.EvaluationModel
import com.evaluation.evaluation.util.MLog
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyReplyActivity : BaseActivity() {

    private lateinit var binding: ActivityMyReplyBinding
    private val viewModel by viewModels<MyReplyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReplyBinding.inflate(layoutInflater)
        initData()
        viewModel.getReplyList{ initView() }
        setContentView(binding.root)
    }

    fun initData() {
        val stringExtra = intent.getStringExtra("evaluation")
        viewModel.evaluationModel.value = Gson().fromJson(stringExtra, EvaluationModel::class.java)
    }

    fun initView() {
        val star = binding.ratingBar.progressDrawable as LayerDrawable
        star.getDrawable(0).setColorFilter(ContextCompat.getColor(App.context, R.color.emptyStarColor), PorterDuff.Mode.SRC_ATOP)
        star.getDrawable(1).setColorFilter(ContextCompat.getColor(App.context, R.color.fillStartColor), PorterDuff.Mode.SRC_ATOP)
        star.getDrawable(2).setColorFilter(ContextCompat.getColor(App.context, R.color.fillStartColor), PorterDuff.Mode.SRC_ATOP)
        viewModel.evaluationModel.value?.let {
            binding.ratingBar.rating = it.level.toFloat()
            binding.evaluationContent.text = it.content
            binding.evaluationTvTime.text = it.time
            binding.replyListBar.setTitle(it.name)
        }
        val adapter = ReplyAdapter(viewModel.replyList.value!!)
        binding.replyRecyclerView.adapter = adapter
        binding.replyRecyclerView.layoutManager = LinearLayoutManager(App.context)
        MLog.d(TAG, "${viewModel.replyList.value!!}")
        binding.replyButton.setOnClickListener {
            viewModel.content.value = binding.replyEdit.text.toString()
            viewModel.createReply()
        }
    }

    companion object {
        fun actionStart(context: Context, evaluationViewModel: EvaluationModel) {
            val intent = Intent(context, MyReplyActivity::class.java)
            intent.putExtra("evaluation", Gson().toJson(evaluationViewModel))
            context.startActivity(intent)
        }

        private const val TAG = "MyReplyActivity"
    }

}