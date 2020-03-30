package ru.mertsalovda.firebase.model

class Message() {
    var name: String = ""
    var text: String = ""
    var date: Long = 0
    var photoUrl: String = ""

    constructor(name: String, text: String, date: Long, photoUrl: String) : this(){
        this.name = name
        this.text = text
        this.date = date
        this.photoUrl = photoUrl
    }

    override fun toString(): String {
        return "Message(name='$name', text='$text', date=$date, photoUrl='$photoUrl')"
    }


}