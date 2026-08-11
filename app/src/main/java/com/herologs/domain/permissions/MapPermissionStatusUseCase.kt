package com.herologs.domain.permissions

class MapPermissionStatusUseCase {
    operator fun invoke(snapshot: PermissionGrantSnapshot): PermissionOverview = PermissionOverview(
        capabilities = listOf(
            PermissionCapabilityState(
                capability = PermissionCapability.FOREGROUND_LOCATION,
                access = when {
                    snapshot.preciseLocationGranted -> PermissionAccessStatus.GRANTED
                    snapshot.approximateLocationGranted -> PermissionAccessStatus.LIMITED
                    else -> PermissionAccessStatus.NOT_GRANTED
                },
            ),
            PermissionCapabilityState(
                capability = PermissionCapability.ACTIVITY_RECOGNITION,
                access = when {
                    !snapshot.activityRecognitionPermissionRequired -> PermissionAccessStatus.NOT_REQUIRED
                    snapshot.activityRecognitionGranted -> PermissionAccessStatus.GRANTED
                    else -> PermissionAccessStatus.NOT_GRANTED
                },
            ),
            PermissionCapabilityState(
                capability = PermissionCapability.HEALTH_CONNECT,
                access = PermissionAccessStatus.NOT_CONFIGURED,
            ),
        ),
    )
}
