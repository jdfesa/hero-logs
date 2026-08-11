package com.herologs.domain.permissions

import org.junit.Assert.assertEquals
import org.junit.Test

class MapPermissionStatusUseCaseTest {
    private val mapPermissionStatus = MapPermissionStatusUseCase()

    @Test
    fun `precise location maps to granted access`() {
        val overview = mapPermissionStatus(
            snapshot(
                preciseLocationGranted = true,
                approximateLocationGranted = true,
            ),
        )

        assertEquals(
            PermissionAccessStatus.GRANTED,
            overview[PermissionCapability.FOREGROUND_LOCATION].access,
        )
    }

    @Test
    fun `approximate location maps to limited access`() {
        val overview = mapPermissionStatus(
            snapshot(approximateLocationGranted = true),
        )

        assertEquals(
            PermissionAccessStatus.LIMITED,
            overview[PermissionCapability.FOREGROUND_LOCATION].access,
        )
    }

    @Test
    fun `missing location grants map to not granted`() {
        val overview = mapPermissionStatus(snapshot())

        assertEquals(
            PermissionAccessStatus.NOT_GRANTED,
            overview[PermissionCapability.FOREGROUND_LOCATION].access,
        )
    }

    @Test
    fun `activity recognition maps runtime permission state when required`() {
        val granted = mapPermissionStatus(
            snapshot(
                activityRecognitionGranted = true,
                activityRecognitionPermissionRequired = true,
            ),
        )
        val notGranted = mapPermissionStatus(
            snapshot(activityRecognitionPermissionRequired = true),
        )

        assertEquals(
            PermissionAccessStatus.GRANTED,
            granted[PermissionCapability.ACTIVITY_RECOGNITION].access,
        )
        assertEquals(
            PermissionAccessStatus.NOT_GRANTED,
            notGranted[PermissionCapability.ACTIVITY_RECOGNITION].access,
        )
    }

    @Test
    fun `activity recognition maps to not required on legacy Android`() {
        val overview = mapPermissionStatus(
            snapshot(activityRecognitionPermissionRequired = false),
        )

        assertEquals(
            PermissionAccessStatus.NOT_REQUIRED,
            overview[PermissionCapability.ACTIVITY_RECOGNITION].access,
        )
    }

    @Test
    fun `health connect remains explicitly not configured`() {
        val overview = mapPermissionStatus(snapshot())

        assertEquals(
            PermissionAccessStatus.NOT_CONFIGURED,
            overview[PermissionCapability.HEALTH_CONNECT].access,
        )
    }

    private fun snapshot(
        preciseLocationGranted: Boolean = false,
        approximateLocationGranted: Boolean = false,
        activityRecognitionGranted: Boolean = false,
        activityRecognitionPermissionRequired: Boolean = false,
    ) = PermissionGrantSnapshot(
        preciseLocationGranted = preciseLocationGranted,
        approximateLocationGranted = approximateLocationGranted,
        activityRecognitionGranted = activityRecognitionGranted,
        activityRecognitionPermissionRequired = activityRecognitionPermissionRequired,
    )
}
