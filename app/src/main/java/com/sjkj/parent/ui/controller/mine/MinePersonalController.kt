package com.sjkj.parent.ui.controller.mine

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.RegexUtils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.sjkj.parent.R
import com.sjkj.parent.common.ChatType
import com.sjkj.parent.common.GlideApp
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.data.server.MinePersonalModifyRequestBody
import com.sjkj.parent.mvp.contract.UpLoadContract
import com.sjkj.parent.mvp.contract.mine.MinePersonalModifyContract
import com.sjkj.parent.mvp.presenter.UploadPresenter
import com.sjkj.parent.mvp.presenter.mine.MinePersonalModifyPresenter
import com.sjkj.parent.ui.controller.base.BaseController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.mine.MineChangePDFragment
import com.sjkj.parent.utils.getUser
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils
import kotlinx.android.synthetic.main.controller_mine_personal.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.io.File

/**
 * @author by dingl on 2017/9/23.
 * @desc MinePersonalController
 */
class MinePersonalController(context: Context) : BaseController(context), MinePersonalModifyContract.BaseView, UpLoadContract.BaseView {
    override fun upLoadSuccess(fileName: String, type: Int, tag: Long) {
        val param = MinePersonalModifyRequestBody()
        param.HeadImg = fileName
        minePersonalModifyPresenter.modify(param)
    }

    override fun upLoadError(tag: Long) {
        showToast(context.getString(R.string.modify_head_error))
    }

    override fun modifySuccess(param: MinePersonalModifyRequestBody) {
        when (mTag) {
            0 -> {
                showToast(context.getString(R.string.modify_head_success))
                getUser().HeadImg = param.HeadImg
                modifyHead()
                EventBus.getDefault().post(MessageEvent(Which.MODIFY_HEAD, null))
            }
            2 -> {
                showToast(context.getString(R.string.modify_name_success))
                getUser().Name = String.format("${param.Name}家长")
                (mine_group?.getChildAt(3) as QMUICommonListItemView).setDetailText(getUser().Name)
                EventBus.getDefault().post(MessageEvent(Which.MODIFY_NAME, null))
            }
            4 -> {
                showToast(context.getString(R.string.modify_tel_success))
                getUser().Tel = param.Tel
                (mine_group?.getChildAt(5) as QMUICommonListItemView).setDetailText(getUser().Tel)
            }
            5 -> {
                showToast(context.getString(R.string.modify_qq_success))
                getUser().QQ = param.QQ
                (mine_group?.getChildAt(6) as QMUICommonListItemView).setDetailText(getUser().QQ)
            }
            6 -> {
                showToast(context.getString(R.string.modify_email_success))
                getUser().Email = param.Email
                (mine_group?.getChildAt(7) as QMUICommonListItemView).setDetailText(getUser().Email)
            }
        }
    }

    override fun modifyFailed() {
        when (mTag) {
            0 -> showToast(context.getString(R.string.modify_head_error))
            2 -> showToast(context.getString(R.string.modify_name_error))
            4 -> showToast(context.getString(R.string.modify_tel_error))
            5 -> showToast(context.getString(R.string.modify_qq_error))
            6 -> showToast(context.getString(R.string.modify_email_error))
        }
    }

    private lateinit var iv_head: QMUIRadiusImageView

    private var minePersonalModifyPresenter: MinePersonalModifyPresenter

    private var upLoadPresenter: UploadPresenter

    private var imgPath: Uri? = null

    private var mTag = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.controller_mine_personal, this)
        minePersonalModifyPresenter = MinePersonalModifyPresenter(this)
        upLoadPresenter = UploadPresenter(this)
        initData()
    }

    private fun initData() {

        val itemHead = mine_group?.createItemView(context?.getString(R.string.mine_head))
        itemHead?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM

        val headView = LayoutInflater.from(context).inflate(R.layout.view_head, null)

        iv_head = headView.findViewById(R.id.view_head)

        modifyHead()

        itemHead?.addAccessoryCustomView(headView)

        val params = headView.layoutParams as FrameLayout.LayoutParams?

        params?.width = FrameLayout.LayoutParams.MATCH_PARENT

        params?.topMargin = ConvertUtils.dp2px(5.0f)

        params?.bottomMargin = ConvertUtils.dp2px(5.0f)

        headView.layoutParams = params

        itemHead?.tag = 0

        val itemName = mine_group?.createItemView(context?.getString(R.string.mine_name))
        itemName?.setDetailText(getUser().UserName)
        itemName?.tag = 1

        val itemNickname = mine_group?.createItemView(context?.getString(R.string.mine_nickname))
        itemNickname?.setDetailText(getUser().Name)
        itemNickname?.tag = 2

        val itemPassword = mine_group?.createItemView(context?.getString(R.string.mine_password))
        itemPassword?.setDetailText("******")
        itemPassword?.tag = 3

        val itemTel = mine_group?.createItemView(context?.getString(R.string.mine_tel))
        itemTel?.setDetailText(getUser().Tel)
        itemTel?.tag = 4

        val itemQQ = mine_group?.createItemView(context?.getString(R.string.mine_qq))
        itemQQ?.setDetailText(getUser().QQ)
        itemQQ?.tag = 5

        val itemEmail = mine_group?.createItemView(context?.getString(R.string.mine_email))
        itemEmail?.setDetailText(getUser().Email)
        itemEmail?.tag = 6

        val onClickListener = View.OnClickListener { v ->
            if (v is QMUICommonListItemView) {
                showEditDailog(v.tag as Int)
            }
        }

        QMUIGroupListView.newSection(context)
                .addItemView(itemHead, {
                    mMinePersonalControlListener?.modifyHead()
                })
                .addItemView(itemName, null)
                .addItemView(itemNickname, onClickListener)
                .addItemView(itemPassword, {
                    mMinePersonalControlListener?.startFragment(MineChangePDFragment())
                })
                .addItemView(itemTel, onClickListener)
                .addItemView(itemQQ, onClickListener)
                .addItemView(itemEmail, onClickListener)
                .addTo(mine_group)

    }

    private fun modifyHead() {
        GlideApp.with(context)
                .asBitmap()
                .load(getUser()._getHeadImg())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                        iv_head.imageBitmap = resource
                    }

                })
    }

    fun initHead(imgPath: Uri) {
        this.imgPath = imgPath
        upLoadPresenter.upload(File(PhotoMetadataUtils.getPath(context.contentResolver, imgPath)), ChatType.MSGTYPE_IMG)
    }

    private fun showEditDailog(tag: Int) {
        var placeholderHint: String? = null
        when (tag) {
            2 -> placeholderHint = context?.getString(R.string.mine_nickname_tip)
            4 -> placeholderHint = context?.getString(R.string.mine_tel_tip)
            5 -> placeholderHint = context?.getString(R.string.mine_qq_tip)
            6 -> placeholderHint = context?.getString(R.string.mine_email_tip)
        }
        val builder = QMUIDialog.EditTextDialogBuilder(context)
        builder.setTitle(context?.getString(R.string.dialog_tip))
                .setPlaceholder(placeholderHint)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(context?.getString(R.string.dialog_cancle)) { dialog, _ ->
                    dialog.dismiss()
                }
                .addAction(context?.getString(R.string.dialog_confirm)) { dialog, _ ->
                    val text = builder.editText.text
                    if (text != null && text.isNotEmpty()) {
                        val param = MinePersonalModifyRequestBody(getUser().UserID)
                        mTag = tag
                        when (tag) {
                            2 -> {
                                param.Name = text.toString()
                                minePersonalModifyPresenter.modify(param)
                                dialog.dismiss()
                            }
                            4 -> {
                                val tel = RegexUtils.isTel(text.toString())
                                if (tel) {
                                    param.Tel = text.toString()
                                    minePersonalModifyPresenter.modify(param)
                                    dialog.dismiss()
                                } else
                                    showToast(context.getString(R.string.tel_regex))
                            }
                            5 -> {
                                param.QQ = text.toString()
                                minePersonalModifyPresenter.modify(param)
                                dialog.dismiss()
                            }
                            6 -> {
                                val email = RegexUtils.isEmail(text.toString())
                                if (email) {
                                    param.Email = text.toString()
                                    minePersonalModifyPresenter.modify(param)
                                    dialog.dismiss()
                                } else
                                    showToast(context.getString(R.string.email_regex))
                            }
                        }
                    } else {
                        when (tag) {
                            2 -> context?.toast(context?.getString(R.string.mine_nickname_tip) ?: "")
                            4 -> context?.toast(context?.getString(R.string.mine_tel_tip) ?: "")
                            5 -> context?.toast(context?.getString(R.string.mine_qq_tip) ?: "")
                            6 -> context?.toast(context?.getString(R.string.mine_email_tip) ?: "")
                        }
                    }
                }
                .show()

    }

    private var mMinePersonalControlListener: MinePersonalControlListener? = null

    interface MinePersonalControlListener {
        fun startFragment(fragment: BackBaseFragment)
        fun modifyHead()
    }

    fun startFragment(fragment: BackBaseFragment) {
        if (mMinePersonalControlListener != null) {
            mMinePersonalControlListener?.startFragment(fragment)
        }
    }

    fun setMinePersonalControlListener(minePersonalControlListener: MinePersonalControlListener) {
        mMinePersonalControlListener = minePersonalControlListener
    }
}
