package com.turbosokol.mypurchases.core.redux

import com.turbosokol.mypurchases.common.app.AppState
import com.turbosokol.mypurchases.common.app.RootReducer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

interface GeneralState
interface Action
interface Effect

interface Store<S: GeneralState, A: Action, E: Effect> {
    fun observeAsState(): StateFlow<S>
    fun execute(action: A)
    fun getState(): S
    fun observeSideEffect(): Flow<E>
}

@ExperimentalTime
open class ReduxStore(
    private val reducer: RootReducer,
    defaultValue: AppState,
    private val middlewares: List<Middleware<AppState>>
): Store<AppState, Action, Effect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
    protected val state = MutableStateFlow(defaultValue)
    private val sideEffect = MutableSharedFlow<Effect>()

    override fun observeAsState(): StateFlow<AppState> = state.asStateFlow()

    override fun execute(action: Action) {
        val oldState = state.value
        val newState = reducer.reduce(oldState, action)

        middlewares.forEach { middleware ->
            launch {
                middleware.process(newState, action, sideEffect).collect { middlewareAction ->
                    execute(middlewareAction)
                }
            }
        }

        if (oldState != newState) {
            state.value = newState
        }
    }

    override fun getState(): AppState {
        return state.value
    }

    override fun observeSideEffect(): Flow<Effect> = sideEffect
}

@ExperimentalTime
inline fun <reified T: GeneralState> Reducer<T>.getState(
    appState: AppState,
    mapState: (AppState) -> T
): T = mapState(appState)