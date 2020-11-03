package com.example.myapplication.ui

import android.os.Bundle
import androidx.annotation.Nullable

interface BasePresenter {

    fun onStart(@Nullable extras: Bundle?)

    fun onDestroy()
}