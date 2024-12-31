package com.example.geminichatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import com.google.ai.client.generativeai.type.content

class ChatViewModel : ViewModel() {
    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constant.apiKey
    )
    fun senMessage(question : String){
        viewModelScope.launch {
            val chat = generativeModel.startChat(
                history = messageList.map {
                    content(it.role){text(it.message)}
                }.toList()
            )
            messageList.add(MessageModel(question,"user"))
            messageList.add(MessageModel("typing...", "model"))
            val response = chat.sendMessage(question)
            messageList.removeAt(messageList.lastIndex)
            messageList.add(MessageModel(response.text.toString(),"model"))

        }

    }
}