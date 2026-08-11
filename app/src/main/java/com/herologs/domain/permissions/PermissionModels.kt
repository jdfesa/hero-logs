package com.herologs.domain.permissions

enum class PermissionCapability {
    FOREGROUND_LOCATION,
    ACTIVITY_RECOGNITION,
    HEALTH_CONNECT,
}

enum class PermissionAccessStatus {
    GRANTED,
    LIMITED,
    NOT_GRANTED,
    NOT_REQUIRED,
    NOT_CONFIGURED,
}

data class PermissionCapabilityState(
    val capability: PermissionCapability,
    val access: PermissionAccessStatus,
)

/** Android-light facts used to map platform grants into product-facing states. */
data class PermissionGrantSnapshot(
    val preciseLocationGranted: Boolean,
    val approximateLocationGranted: Boolean,
    val activityRecognitionGranted: Boolean,
    val activityRecognitionPermissionRequired: Boolean,
)

data class PermissionOverview(
    val capabilities: List<PermissionCapabilityState>,
) {
    init {
        val representedCapabilities = capabilities.map { it.capability }
        require(representedCapabilities.size == representedCapabilities.distinct().size) {
            "Permission capabilities must not be duplicated"
        }
        require(representedCapabilities.toSet() == PermissionCapability.entries.toSet()) {
            "Every permission capability must have a state"
        }
    }

    operator fun get(capability: PermissionCapability): PermissionCapabilityState =
        capabilities.first { it.capability == capability }
}
