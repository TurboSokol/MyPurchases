package com.turbosokol.mypurchases.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val appDispatcher: CoroutineDispatcher = Dispatchers.IO