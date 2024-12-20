package com.example.data.remote

import android.graphics.Point
import android.location.Location
import android.util.Log
import com.example.model.NearByCapsule
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.LatLng
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NearByCapsulesDataSource @Inject constructor(
  val firestore: FirebaseFirestore,
) {
  private var fetchJob: Job? = null

  fun fetchNearbyCapsules(
    location: LatLng,
    radius: Double,
    onComplete: (List<NearByCapsule>) -> Unit
  ) {

    fetchJob?.cancel()
    fetchJob = CoroutineScope(Dispatchers.IO).launch {
      val center = GeoLocation(location.latitude, location.longitude)
      val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radius)
      val capsuleSet = mutableSetOf<String>()
      val capsules = mutableListOf<NearByCapsule>()

      bounds.forEach { bound ->
        try {
          val snapshot = firestore.collection("capsules")
            .orderBy("geoHash")
            .startAt(bound.startHash)
            .endAt(bound.endHash)
            .get()
            .await()
          snapshot.documents.forEach { doc ->
            val geoPoint = doc.getGeoPoint("location")
            val distance = GeoFireUtils.getDistanceBetween(
              center,
              GeoLocation(geoPoint!!.latitude, geoPoint.longitude)
            )
            if (distance <= radius && !capsuleSet.contains(doc.id)) {
              capsuleSet.add(doc.id)
              capsules.add(
                NearByCapsule(
                  capsuleId = doc.id,
                  location = doc.getGeoPoint("location")!!,
                  capsuleImageUrl = doc.getString("imageUrl")!!,
                  capsuleTitle = doc.getString("title")!!,
                  modelId = doc.get("modelId").toString(),
                  description = doc.getString("description")!!
                )
              )
            }
          }
        } catch (e: Exception) {
        }
      }
      withContext(Dispatchers.Main) {
        onComplete(capsules)
      }
    }
  }
}
