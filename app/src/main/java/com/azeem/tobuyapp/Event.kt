package com.azeem.tobuyapp

open class Event<out T>(private val content:T) {
    var hasBeenHandled = false
        private set

    //returns the content and prevents its use again
    fun getContentIfNotHandledOrReturnNull():T?{
        return if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    //returns the content, even if it's already been handled.
    fun peekContent():T = content

}