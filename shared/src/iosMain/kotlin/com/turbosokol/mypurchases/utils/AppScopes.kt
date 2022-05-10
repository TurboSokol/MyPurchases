package com.turbosokol.mypurchases.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

val applicationNetworkScope = CoroutineScope(appDispatcher + Job())