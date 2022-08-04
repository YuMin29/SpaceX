package com.yumin.spacex.model


import com.google.gson.annotations.SerializedName

data class Payload(
    @SerializedName("cap_serial")
    val capSerial: String,
    @SerializedName("cargo_manifest")
    val cargoManifest: String,
    @SerializedName("customers")
    val customers: List<String>,
    @SerializedName("flight_time_sec")
    val flightTimeSec: Int,
    @SerializedName("manufacturer")
    val manufacturer: String,
    @SerializedName("mass_returned_kg")
    val massReturnedKg: Double,
    @SerializedName("mass_returned_lbs")
    val massReturnedLbs: Double,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("norad_id")
    val noradId: List<Int>,
    @SerializedName("orbit")
    val orbit: String,
    @SerializedName("orbit_params")
    val orbitParams: OrbitParams,
    @SerializedName("payload_id")
    val payloadId: String,
    @SerializedName("payload_mass_kg")
    val payloadMassKg: Double,
    @SerializedName("payload_mass_lbs")
    val payloadMassLbs: Double,
    @SerializedName("payload_type")
    val payloadType: String,
    @SerializedName("reused")
    val reused: Boolean,
    @SerializedName("uid")
    val uid: String
)