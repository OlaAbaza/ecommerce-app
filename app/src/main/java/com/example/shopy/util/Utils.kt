package com.example.shopy.util

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import java.util.regex.Matcher
import java.util.regex.Pattern


class Utils {

    companion object{
        //toolbar Image
        lateinit var toolbarImg: ImageView
        //toolbar cart
        lateinit var cartView : View

        fun validateEmail(userEmail: String):Boolean{
            val patternEmail: Pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
            val matcherEmail: Matcher = patternEmail.matcher(userEmail)
            val emailMatchResult: Boolean = matcherEmail.matches()

            return emailMatchResult
        }
        fun validatePassword(password: String):Boolean{

            //Minimum eight characters, at least one letter and one number
            val patternPassword: Pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
            val matcherPassword: Matcher = patternPassword.matcher(password)
            val passowordMatchResult : Boolean = matcherPassword.matches()

            return passowordMatchResult
        }
        fun validatePhone(Phone: String):Boolean{

         //   "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$"
            //Minimum eight characters, at least one letter and one number
            val patternPhone: Pattern = Pattern.compile("^[+]?[0-9]{10,13}$")
            val matcherPhone: Matcher = patternPhone.matcher(Phone)
            val PhoneMatchResult : Boolean = matcherPhone.matches()

            return PhoneMatchResult
        }
    }




}