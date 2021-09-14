package com.qucoon.todoapp.domain

import com.qucoon.todoapp.domain.model.Order
import com.qucoon.todoapp.domain.model.Priority

/**
 * If Y want create something from base
 * Please code in here
 * UseCase<Type>
 */
abstract class UseCase<in Params, out T> where T : Any {

    abstract fun createObservable(params: Params? = null): T
    abstract fun createPriorityObservable(params: Params? = null, priority: Priority): T
    abstract fun createOrderedObservable(params: Params? = null, order: Order): T

    open fun onCleared() {}
}