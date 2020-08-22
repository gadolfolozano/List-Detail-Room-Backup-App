package pe.gadolfolozano.listdetailroombackupapp.di

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import pe.gadolfolozano.listdetailroombackupapp.data.TaskDatabase
import pe.gadolfolozano.listdetailroombackupapp.ui.MainViewModel
import pe.gadolfolozano.listdetailroombackupapp.ui.taskList.TaskListViewModel
import pe.gadolfolozano.listdetailroombackupapp.ui.taskdetail.TaskDetailViewModel

object Koin {
    @JvmField
    val MODULES: List<Module> = listOf(taskModule, databaseModule)

    @JvmStatic
    fun initialize(application: Application) {
        startKoin {
            androidContext(application)
            modules(MODULES)
        }
    }
}

val taskModule = module {
    viewModel { TaskListViewModel() }
    viewModel { TaskDetailViewModel() }
    viewModel { MainViewModel(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            TaskDatabase::class.java,
            TaskDatabase.NAME
        ).fallbackToDestructiveMigration().build()
    }

    single { get<TaskDatabase>().userDAO() }

    single { get<TaskDatabase>().taskDAO() }

    single { get<TaskDatabase>().taskDetailDAO() }
}