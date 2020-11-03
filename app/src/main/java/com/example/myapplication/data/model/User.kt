package com.example.myapplication.data.model

import android.net.Uri

data class User(var uid: String?,
                var email: String? = null,
                var displayName: String? = null,
                var photoUri: Uri? = null
)