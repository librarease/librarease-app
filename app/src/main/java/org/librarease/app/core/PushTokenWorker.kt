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
            Log.e(WORKER_TAG, "FCM token is missing in worker input data.")
            return Result.failure()
        }

        if (authRepository.currentUser == null) {
            Log.d(WORKER_TAG, "User not authenticated, skipping token send.")
            return Result.success()
        }

        return try {
            Log.d(WORKER_TAG, "Sending FCM token to backend...")
            val success = authRepository.sendFcmToken(token)
            if (success) {
                Log.d(WORKER_TAG, "FCM token sent successfully.")
                Result.success()
            } else {
                Log.e(WORKER_TAG, "Backend failed to store FCM token. Retrying...")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e(WORKER_TAG, "Error sending FCM token", e)
            Result.retry()
        }
    }

    companion object {
        const val KEY_TOKEN = "fcm_token"
        const val WORKER_TAG = "PushTokenWorker"

        fun enqueue(context: Context, token: String) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORKER_TAG)

            val workRequest = OneTimeWorkRequestBuilder<PushTokenWorker>()
                .setInputData(workDataOf(KEY_TOKEN to token))
                .addTag(WORKER_TAG)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
            Log.d(WORKER_TAG, "Work request enqueued for token.")
        }
    }
}
