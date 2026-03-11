package com.example.repairkz.Navigation

object Routes {
    const val MAIN_WINDOW = "main_window"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP_EMAIL = "sign_up_email"
    const val SIGN_UP_CODE = "sign_up_code"
    const val SIGN_UP_DATA = "sign_up_data"
    const val PHOTO_PREVIEW = "camera_preview"
    const val SEARCH = "search"
    const val CAMERA = "camera"
    const val DETAILS = "details"
    const val USERINFO = "user_info"
    const val SIGN_IN_SCREEN = "sign_in"
    const val SIGN_UP_SCREEN = "sign_up"
    const val PROFILE_GROUP = "profile_group"
    const val REG_GROUP = "reg_group"

    const val REG_PHOTO_GROUP = "reg_photo_group"
    const val PROFILE_PHOTO_GROUP = "profile_photo_group"

    fun userInfoRoute(id:Int?): String{
        return if(id!=null){
            "${USERINFO}?userId=${id}"
        } else{
            USERINFO
        }
    }
}