package com.yumin.spacex.model


import com.google.gson.annotations.SerializedName

data class Core(
    @SerializedName("block")
    val block: Int,
    @SerializedName("core_serial")
    val coreSerial: String,
    @SerializedName("flight")
    val flight: Int,
    @SerializedName("gridfins")
    val gridfins: Boolean,
    @SerializedName("land_success")
    val landSuccess: Boolean,
    @SerializedName("landing_intent")
    val landingIntent: Boolean,
    @SerializedName("landing_type")
    val landingType: String,
    @SerializedName("landing_vehicle")
    val landingVehicle: String,
    @SerializedName("legs")
    val legs: Boolean,
    @SerializedName("reused")
    val reused: Boolean
)