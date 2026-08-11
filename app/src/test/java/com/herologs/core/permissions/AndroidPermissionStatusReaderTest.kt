package com.herologs.core.permissions

import android.Manifest
import android.os.Build
import com.herologs.domain.permissions.MapPermissionStatusUseCase
import com.herologs.domain.permissions.PermissionAccessStatus
import com.herologs.domain.permissions.PermissionCapability
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class AndroidPermissionStatusReaderTest {
    @Test
    fun `reader maps current precise location and activity grants`() {
        val reader = reader(
            sdkInt = Build.VERSION_CODES.Q,
            grantedPermissions = setOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION,
            ),
        )

        val overview = reader.read()

        assertEquals(
            PermissionAccessStatus.GRANTED,
            overview[PermissionCapability.FOREGROUND_LOCATION].access,
        )
        assertEquals(
            PermissionAccessStatus.GRANTED,
            overview[PermissionCapability.ACTIVITY_RECOGNITION].access,
        )
    }

    @Test
    fun `reader preserves approximate-only location as limited`() {
        val reader = reader(
            sdkInt = Build.VERSION_CODES.Q,
            grantedPermissions = setOf(Manifest.permission.ACCESS_COARSE_LOCATION),
        )

        assertEquals(
            PermissionAccessStatus.LIMITED,
            reader.read()[PermissionCapability.FOREGROUND_LOCATION].access,
        )
    }

    @Test
    fun `reader does not inspect activity permission before Android Q`() {
        val inspectedPermissions = mutableListOf<String>()
        val reader = AndroidPermissionStatusReader(
            permissionGrantChecker = PermissionGrantChecker { permission ->
                inspectedPermissions += permission
                false
            },
            sdkInt = Build.VERSION_CODES.P,
            mapPermissionStatus = MapPermissionStatusUseCase(),
        )

        val overview = reader.read()

        assertEquals(
            PermissionAccessStatus.NOT_REQUIRED,
            overview[PermissionCapability.ACTIVITY_RECOGNITION].access,
        )
        assertFalse(inspectedPermissions.contains(Manifest.permission.ACTIVITY_RECOGNITION))
    }

    @Test
    fun `reader reports missing modern runtime grants without claiming denial`() {
        val overview = reader(
            sdkInt = Build.VERSION_CODES.Q,
            grantedPermissions = emptySet(),
        ).read()

        assertEquals(
            PermissionAccessStatus.NOT_GRANTED,
            overview[PermissionCapability.FOREGROUND_LOCATION].access,
        )
        assertEquals(
            PermissionAccessStatus.NOT_GRANTED,
            overview[PermissionCapability.ACTIVITY_RECOGNITION].access,
        )
    }

    private fun reader(
        sdkInt: Int,
        grantedPermissions: Set<String>,
    ) = AndroidPermissionStatusReader(
        permissionGrantChecker = PermissionGrantChecker(grantedPermissions::contains),
        sdkInt = sdkInt,
        mapPermissionStatus = MapPermissionStatusUseCase(),
    )
}
