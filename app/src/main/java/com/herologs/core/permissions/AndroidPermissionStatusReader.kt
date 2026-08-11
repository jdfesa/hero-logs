package com.herologs.core.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.herologs.domain.permissions.MapPermissionStatusUseCase
import com.herologs.domain.permissions.PermissionGrantSnapshot
import com.herologs.domain.permissions.PermissionOverview
import com.herologs.domain.permissions.PermissionStatusReader

class AndroidPermissionStatusReader internal constructor(
    private val permissionGrantChecker: PermissionGrantChecker,
    private val sdkInt: Int,
    private val mapPermissionStatus: MapPermissionStatusUseCase,
) : PermissionStatusReader {
    constructor(context: Context) : this(
        permissionGrantChecker = PermissionGrantChecker { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        },
        sdkInt = Build.VERSION.SDK_INT,
        mapPermissionStatus = MapPermissionStatusUseCase(),
    )

    override fun read(): PermissionOverview = mapPermissionStatus(
        PermissionGrantSnapshot(
            preciseLocationGranted = permissionGrantChecker.isGranted(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            approximateLocationGranted = permissionGrantChecker.isGranted(
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            activityRecognitionGranted = sdkInt >= Build.VERSION_CODES.Q &&
                permissionGrantChecker.isGranted(Manifest.permission.ACTIVITY_RECOGNITION),
            activityRecognitionPermissionRequired = sdkInt >= Build.VERSION_CODES.Q,
        ),
    )
}

internal fun interface PermissionGrantChecker {
    fun isGranted(permission: String): Boolean
}
