package com.dominicwrieden.sampleapp.splash

import android.animation.Animator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dominicwrieden.sampleapp.MainActivity
import com.dominicwrieden.sampleapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    companion object {
        val ANIMATION_MIN_PROGRESS = 0f
        val ANIMATION_MAX_PROGRESS = 0.65f
        val ANIMATION_SPEED = 3f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashAnimation.setAnimation(R.raw.welcome_animation)
        splashAnimation.setMinAndMaxProgress(ANIMATION_MIN_PROGRESS, ANIMATION_MAX_PROGRESS)
        splashAnimation.speed = ANIMATION_SPEED

        splashAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                startMainActivity()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

        })

        splashAnimation.playAnimation()

    }


    private fun startMainActivity() {
        MainActivity.openActivity(this)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

}