package com.herologs.domain.permissions

/** Boundary for reading current permission state without exposing Android APIs to the UI. */
fun interface PermissionStatusReader {
    fun read(): PermissionOverview
}
