package pe.gadolfolozano.listdetailroombackupapp

import android.app.Application
import pe.gadolfolozano.listdetailroombackupapp.di.Koin

class TaskApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Koin.initialize(this)
    }

}