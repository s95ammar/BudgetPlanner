package com.s95ammar.budgetplanner.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.s95ammar.budgetplanner.R

typealias Permission = String

val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

fun requestPermission(
    permission: Permission,
    @StringRes rationalMessageResId: Int,
    requestPermissionLauncher: ActivityResultLauncher<Permission>,
    activity: Activity,
    onShowPermissionRationale: () -> Unit = {
        showPermissionRationaleDialog(activity, rationalMessageResId) { requestPermissionLauncher.launch(permission) }
    }
) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        onShowPermissionRationale()
    } else {
        requestPermissionLauncher.launch(permission)
    }
}

fun Context.isPermissionGranted(permission: Permission): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun showPermissionRationaleDialog(context: Context, @StringRes messageResId: Int, onUserConsent: () -> Unit) {
    MaterialAlertDialogBuilder(context)
        .setTitle(R.string.permission_required_title)
        .setMessage(messageResId)
        .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
        .setPositiveButton(R.string.ok) { _, _ -> onUserConsent() }
        .show()
}
