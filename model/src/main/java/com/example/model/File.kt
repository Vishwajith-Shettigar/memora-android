package com.example.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class FileUploadProgress(
  val fileName: String,
  val uri: Uri,
  var progress: Double = 0.0,
  var totalSize: Long = 0,
  var uploadedSize: Long = 0,
  var isFailed: Boolean = false,
  val fileType: String = "unknown"
)

data class FileUploaded(
  val fileName: String,
  val uri: Uri,
  val fileUri:Uri,
  var totalSize: Long = 0,
  val fileType: String = "unknown"
)

data class TempUploaded(
  val uri: Uri,
  val userId: String,
  val timeStamp: Timestamp,
)



data class DownloadFile(
  val url: String,
  val fileType: String,
  val name: String,
  val size: String,
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString() ?: "",
    parcel.readString() ?: "",
    parcel.readString() ?: "",
    parcel.readString()?:""
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(url)
    parcel.writeString(fileType)
    parcel.writeString(name)
    parcel.writeString(size)

  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<DownloadFile> {
    override fun createFromParcel(parcel: Parcel): DownloadFile {
      return DownloadFile(parcel)
    }

    override fun newArray(size: Int): Array<DownloadFile?> {
      return arrayOfNulls(size)
    }
  }
}
