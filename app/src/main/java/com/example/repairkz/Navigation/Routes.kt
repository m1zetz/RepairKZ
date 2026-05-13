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
    const val USER_INFO = "user_info"
    const val MASTER_INFO = "master_info"
    const val SIGN_IN_SCREEN = "sign_in"
    const val SIGN_UP_SCREEN = "sign_up"
    const val PROFILE_GROUP = "profile_group"
    const val REG_GROUP = "reg_group"

    const val REG_PHOTO_GROUP = "reg_photo_group"
    const val ORDER_REG = "order_reg"
    const val PROFILE_PHOTO_GROUP = "profile_photo_group"

    fun userInfoRoute(id:Long?): String{
        return if(id!=null){
            "${USER_INFO}?userId=${id}"
        } else{
            USER_INFO
        }
    }
    fun masterInfoRoute(id:Long?): String{
        return if(id!=null){
            "${MASTER_INFO}?userId=${id}"
        } else{
            USER_INFO
        }
    }
}