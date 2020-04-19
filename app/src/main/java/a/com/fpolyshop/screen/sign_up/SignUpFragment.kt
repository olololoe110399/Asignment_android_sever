package a.com.fpolyshop.screen.sign_up

import a.com.fpolyshop.R
import a.com.fpolyshop.data.models.User
import a.com.fpolyshop.data.repository.Repository
import a.com.fpolyshop.data.source.local.LocalDataSource
import a.com.fpolyshop.data.source.remote.RemoteDataSource
import a.com.fpolyshop.screen.container.ContainerFragment
import a.com.fpolyshop.utils.*
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_menu_add_images_bottomsheet.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import java.io.FileNotFoundException
import java.io.InputStream


class SignUpFragment : Fragment(), SignUpContract.View {
    private lateinit var presenter: SignUpContract.Presenter
    private lateinit var encodedImage: String
    private var bitmap: Bitmap? = null
    private var mBottomDialogNotificationAction: BottomSheetDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            val movieRepository: Repository =
                Repository.getInstance(
                    RemoteDataSource.instance,
                    LocalDataSource.instance
                )
            presenter = SignUpPresenter(movieRepository)
        }
        presenter.setView(this)
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.registerBtn.setOnClickListener {
            onClick()
        }
        view.avatar.setOnClickListener {
            menuImage(REQUEST_CODE_CAMERA_IMG, REQUEST_CODE_PHOTO_IMG)
        }
    }

    override fun onSignUpUserSuccess(status: String) {
        if (status == "Success") {
            bitmap = null
            view?.run {
                activity?.rememberUser(
                    usernameEdt.text.toString(),
                    passwordEdt.text.toString(),
                    false
                )
                avatar.setBackgroundResource(R.drawable.placeholder_avatar)
                usernameEdt.text.clear()
                passwordEdt.text.clear()
                confirm_passwordEdt.text.clear()
                full_nameEdt.text.clear()
                birthEdt.text.clear()
                phoneEdt.text.clear()
                addressEdt.text.clear()
            }
            activity?.loadNoBackStack(ContainerFragment.instance)
            activity?.showSnackBarMsg(status)

        } else {
            activity?.showSnackBarMsg(status)
        }
    }

    override fun onError(str: String?) {
        str?.let {
            Toast.makeText(activity, it, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onLoading(isLoad: Boolean) {
        view?.run {
            if (isLoad) {
                frameProgressBar.visibility = View.VISIBLE
            } else {
                frameProgressBar.visibility = View.GONE
            }
        }
    }

    private fun onClick() {

        view?.run {
            activity?.let {
                if (usernameEdt.text.toString().trim() == "" || passwordEdt.text.toString()
                        .trim() == "" || confirm_passwordEdt.text.toString()
                        .trim() == "" || full_nameEdt.text.toString()
                        .trim() == "" || birthEdt.text.toString()
                        .trim() == "" || phoneEdt.text.toString()
                        .trim() == "" || addressEdt.text.toString()
                        .trim() == "" || bitmap == null
                ) {
                    it.showSnackBar(R.string.valid_null)
                } else if (!validateEmail(usernameEdt.text.toString())) {
                    it.showSnackBar(R.string.valid_email)
                } else if (passwordEdt.text.toString().length < 8) {
                    it.showSnackBar(R.string.valid_pass)
                } else if (passwordEdt.text.toString().trim() != confirm_passwordEdt.text.toString()
                        .trim()
                ) {
                    it.showSnackBar(R.string.match_pass)
                } else {
                    onLoading(true)
                    ConvertImageAsyncTask(object : OnConvertImageListener {
                        override fun onImageLoaded(base64: String?) {
                            base64?.let { encodedImage = base64 }
                            val user =
                                User(
                                    usernameEdt.text.toString().trim(),
                                    passwordEdt.text.toString().trim(),
                                    full_nameEdt.text.toString().trim(),
                                    phoneEdt.text.toString().trim(),
                                    addressEdt.text.toString().trim(),
                                    birthEdt.text.toString().trim(),
                                    encodedImage
                                )
                            if (NetworkUtil.isConnectedToNetwork(it)) {
                                presenter.postSignUp(user)
                            } else {
                                val message = getString(R.string.check_internet_fail)
                                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                            }

                        }

                        override fun onImageError(e: Exception?) {
                            e?.run {
                                Toast.makeText(activity, message.toString(), Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }).execute(bitmap)


                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_CAMERA_IMG -> if (requestCode == REQUEST_CODE_CAMERA_IMG && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CODE_CAMERA_IMG)
            } else {
                Toast.makeText(activity, "Do not allow to open CAMERA", Toast.LENGTH_SHORT).show()
            }
            REQUEST_CODE_PHOTO_IMG -> if (requestCode == REQUEST_CODE_PHOTO_IMG && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_PHOTO_IMG)
            } else {
                Toast.makeText(activity, "Do not allow to open PHOTO LIBRARY", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_CAMERA_IMG -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.extras?.let {
                    bitmap = it.get("data") as Bitmap
                    view?.avatar?.setImageBitmap(bitmap)
                }
            }
            REQUEST_CODE_PHOTO_IMG -> if (resultCode == Activity.RESULT_OK && data != null) {
                data.data?.let {
                    try {
                        val inputStream: InputStream? =
                            activity?.contentResolver?.openInputStream(it)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        this.bitmap = bitmap
                        view?.avatar?.setImageBitmap(bitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun menuImage(rq_code_camera: Int, rq_code_photo: Int) {
        val viewBottom =
            layoutInflater.inflate(R.layout.dialog_menu_add_images_bottomsheet, null)
        context?.let {
            viewBottom?.run {
                mBottomDialogNotificationAction = BottomSheetDialog(it)
                mBottomDialogNotificationAction?.setContentView(viewBottom)
                mBottomDialogNotificationAction?.show()

                val bottomSheet =
                    mBottomDialogNotificationAction?.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                bottomSheet?.background = null
                lnPhotolibrary.setOnClickListener {
                    photoLibrary(rq_code_photo)
                    mBottomDialogNotificationAction?.dismiss()
                }
                lnchoosephoto.setOnClickListener {
                    takeAPhoto(rq_code_camera)
                    mBottomDialogNotificationAction?.dismiss()
                }
                lndismiss.setOnClickListener { mBottomDialogNotificationAction?.dismiss() }
            }
        }
    }

    private fun takeAPhoto(REQUEST_CODE: Int) {
        context?.let {
            if (checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    REQUEST_CODE
                )
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }

    private fun photoLibrary(REQUEST_CODE: Int) {
        context?.let {
            if (checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    REQUEST_CODE
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        return false
    }

    private object HOLDER {
        val INSTANCE = SignUpFragment()
    }

    companion object {
        private const val REQUEST_CODE_CAMERA_IMG = 1
        private const val REQUEST_CODE_PHOTO_IMG = 2
        val instance: SignUpFragment by lazy {
            HOLDER.INSTANCE
        }
    }

}