package com.example.repairkz.Navigation

object Routes {
    const val MAIN_WINDOW = "main_window"
    const val PHOTO_PREVIEW = "camera_preview"
    const val SEARCH = "search"
    const val CAMERA = "camera"
    const val DETAILS = "details"
    const val USERINFO = "user_info"
    const val SIGN_IN_SCREEN = "sign_in"
    const val SIGN_UP_SCREEN = "sign_up"
    const val PROFILE_GROUP = "profile_group"

    fun userInfoRoute(id:Int?): String{
        return if(id!=null){
            "${USERINFO}?userId=${id}"
        } else{
            USERINFO
        }
    }
}