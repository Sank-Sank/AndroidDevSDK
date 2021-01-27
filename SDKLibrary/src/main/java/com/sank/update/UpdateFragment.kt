package com.sank.update

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.sank.*
import com.sank.weight.CircularProgressBar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateFragment : DialogFragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(updateBean: UpdateBean) =
                UpdateFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, updateBean)
                    }
                }
    }


    private var updateBean: UpdateBean? = null

    private lateinit var tvTips: TextView
    private lateinit var update_dialog_info: TextView
    private lateinit var update_dialog_title: TextView
    private lateinit var update_dialog_confirm: Button

    //关闭按钮
    private lateinit var update_dialog_close_layout: LinearLayout
    private lateinit var MessageContent: LinearLayout
    private lateinit var progressLayout: ConstraintLayout
    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var progressContent: TextView

    //是否已经更新完成
    private var isComplete = false

    private var updateInterface: OnUpdateInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            updateBean = it.getSerializable(ARG_PARAM1) as UpdateBean
        }
        setStyle(STYLE_NO_TITLE, R.style.Theme_Code_BottomSheetDialog_NoActionBar)
    }

    fun setClickListener(updateInterface: OnUpdateInterface) {
        this.updateInterface = updateInterface
    }

    @SuppressLint("SetTextI18n")
    fun setProgress(progress: Int) {
        circularProgressBar.progress = progress.toFloat()
        progressContent.text = "$progress"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTips = view.findViewById(R.id.tvTips)
        update_dialog_info = view.findViewById(R.id.update_dialog_info)
        update_dialog_title = view.findViewById(R.id.update_dialog_title)
        update_dialog_confirm = view.findViewById(R.id.update_dialog_confirm)
        update_dialog_close_layout = view.findViewById(R.id.update_dialog_close_layout)
        MessageContent = view.findViewById(R.id.MessageContent)
        progressLayout = view.findViewById(R.id.progressLayout)
        circularProgressBar = view.findViewById(R.id.circularProgressBar)
        progressContent = view.findViewById(R.id.progressContent)
        isCancelable = false
        initView()
    }

    fun setComplete() {
        isComplete = true
        progressLayout.visibility = View.GONE
        MessageContent.visibility = View.VISIBLE
        update_dialog_confirm.text = "下载完成,立即更新！"
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        //更新版本号
        tvTips.text = "发现新版本\nV${updateBean?.versionName}"

        // 更新标题
        update_dialog_title.text = updateBean?.title

        // 更新内容
        update_dialog_info.text = updateBean?.Description

        // 确定
        update_dialog_confirm.setOnClickListener {
            isComplete.yes {
                updateInterface?.onCompleteUpdate()
            }.no {
                updateInterface?.onStartUpdate()
                updateBean?.force.yes {
                    //强制更新
                    progressLayout.visibility = View.VISIBLE
                    MessageContent.visibility = View.GONE
                }.no {
                    dismiss()
                }
            }
        }

        // 取消
        update_dialog_close_layout.setOnClickListener {
            updateForce()
        }

    }

    private fun updateForce() {
        updateBean?.force.yes {
            val dialog = android.app.AlertDialog.Builder(activity)
                    .setMessage("本次更新是重要更新，为了不影响您的使用，请同意本次更新")
                    .setPositiveButton("同意") { dialogInterface, i ->

                    }
                    .setNegativeButton("不同意") { dialogInterface, i ->
                        val nm = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                        nm?.cancelAll()
                        exitApp()
                        android.os.Process.killProcess(android.os.Process.myPid())
                    }
                    .create()
            dialog.setOnShowListener {
                val button = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                button.setTextColor(color(R.color.c3273f8))
                val button1 = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                button1.setTextColor(color(R.color.c8b94a2))
            }
            dialog.show()
        }.no {
            dismiss()
        }
    }
}