package com.raytalktech.storyapp.ui.addstory

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raytalktech.storyapp.R
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.data.source.remote.StatusResponse
import com.raytalktech.storyapp.databinding.FragmentAddStoryBinding
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.utils.ViewModelFactory
import com.raytalktech.storyapp.utils.checkPermission
import com.raytalktech.storyapp.utils.getCurrentLocation
import com.raytalktech.storyapp.utils.requestPermission
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class AddStoryFragment : Fragment() {

    private lateinit var _binding: FragmentAddStoryBinding
    private val binding get() = _binding
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]


            if (checkPermission(Manifest.permission.CAMERA)) {
                startTakePhoto()
            } else {
                requestPermission(Manifest.permission.CAMERA, 12) { isGranted ->
                    if (isGranted) {
                        startTakePhoto()
                    } else {
                        Toast.makeText(requireActivity(), "kenapa dah", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireActivity(),
                "com.raytalktech.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                binding.apply {
                    rotateFile(file)
                    val reduceFile = reduceFileImage(file)

                    ivResultPhoto.setImageBitmap(BitmapFactory.decodeFile(reduceFile.path))
                    val requestImageFile = reduceFile.asRequestBody("image/jpeg".toMediaType())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        reduceFile.name,
                        requestImageFile
                    )

                    setupPostStories(imageMultipart)
                }
            }
        }
    }

    private fun setupPostStories(imageMultipart: MultipartBody.Part) {
        binding.apply {
            etDescription.doOnTextChanged { _, _, _, _ -> validate() }

            var latitude = 0f
            var longitude = 0f

            requireActivity().getCurrentLocation { location ->
                location.let {
                    latitude = it.latitude.toFloat()
                    longitude = it.longitude.toFloat()
                }
            }

            btnUpload.setOnClickListener {
                pbView.isVisible = true
                viewModel.postStories(
                    imageMultipart,
                    etDescription.text.toString(),
                    latitude,
                    longitude
                ).observe(viewLifecycleOwner, postStories)
            }
        }
    }

    private val postStories =
        androidx.lifecycle.Observer<ApiResponse<DataResponse>> { result ->
            when (result.status) {
                StatusResponse.EMPTY -> {}
                StatusResponse.ERROR -> {}
                StatusResponse.SUCCESS -> {
                    if (!result.body.error)
                        findNavController().navigate(R.id.navigation_home)

                    binding.pbView.isGone = true
                    Toast.makeText(requireActivity(), result.body.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun validate() {
        binding.apply {
            var isValid = true

            if (etDescription.text.isEmpty()) isValid = false

            btnUpload.isEnabled = isValid
        }
    }

    private val FILENAME_FORMAT = "dd-MMM-yyyy"

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun rotateFile(file: File) {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)

        // Determine the camera position from the EXIF data
        val exif = ExifInterface(file.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val isBackCamera = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> true
            ExifInterface.ORIENTATION_ROTATE_270 -> false
            else -> null // Unknown camera position
        }

        // Apply rotation and/or mirror transformation based on the camera position
        if (isBackCamera != null) {
            val rotation = if (isBackCamera) 90f else -90f
            matrix.postRotate(rotation)
            if (!isBackCamera) {
                matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            }
        }

        // Apply the transformation to the bitmap
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // Save the transformed bitmap back to the file
        FileOutputStream(file).use { out ->
            result.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
        }

        // Recycle the bitmap to release memory
        bitmap.recycle()
        result.recycle()
    }

    private val MAXIMAL_SIZE = 1000000
    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }
}