package org.librarease.app.core

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.librarease.app.domain.repository.AuthRepository


@HiltWorker
class PushTokenWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val authRepository: AuthRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val token = inputData.getString(KEY_TOKEN)
        if (token.isNullOrEmpty()) {
            Log.e("PushTokenWorker", "FCM token is missing in worker input data.")
            return Result.failure()
        }

        return try {
            val success = authRepository.sendFcmToken(token)
            if (success) {
                Result.success()
            } else {
                Log.e("PushTokenWorker", "Backend failed to store FCM token, retrying.")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("PushTokenWorker", "Error sending FCM token", e)
            Result.retry()
        }
    }

    companion object {
        const val KEY_TOKEN = "fcm_token"
        const val WORKER_TAG = "PushTokenWorker"

        fun enqueue(context: Context, token: String) {
            val workRequest = OneTimeWorkRequestBuilder<PushTokenWorker>()
                .setInputData(workDataOf(KEY_TOKEN to token))
                .addTag(WORKER_TAG)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
