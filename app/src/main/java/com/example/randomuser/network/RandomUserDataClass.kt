package com.example.randomuser.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.squareup.moshi.Json

//all the parellable data classes here

data class RandomUserMain(
    @Json(name = "results") val list:List<RandomUserDataClass>?
    )

@Parcelize
data class RandomUserDataClass(
    @Json(name = "gender") val gender:String?,
    @Json(name = "name") val name: Name?,
    @Json(name = "email") val email:String?,
    @Json(name = "cell") val cell:String?,
    @Json(name = "id") val id:Id?,
    @Json(name = "location") val location: Location?,
    @Json(name = "picture") val picture: Picture?
) : Parcelable

@Parcelize
data class Id(
    @Json(name = "name") val name:String?,
    @Json(name = "value") val value:String?
) : Parcelable

@Parcelize
data class Name(
    @Json(name = "title") val title:String?,
    @Json(name = "first") val first:String?,
    @Json(name = "last") val last:String?
) : Parcelable

@Parcelize
data class Location(
    @Json(name = "city") val city:String?,
    @Json(name = "state") val state:String?,
    @Json(name = "country") val country:String?,
    @Json(name = "coordinates") val coordinates: Coordinates?
) : Parcelable

@Parcelize
data class Coordinates(
    @Json(name = "latitude") val lan:String?,
    @Json(name = "longitude") val lon:String?
) : Parcelable

@Parcelize
data class Picture(
    @Json(name = "large") val large:String?,
    @Json(name = "medium") val medium:String?,
    @Json(name = "thumbnail") val thumbnail:String?
) : Parcelable