package com.example.movil_ctl.hilt

import android.content.Context
import androidx.room.Room
import com.example.movil_ctl.data.AppDatabase
import com.example.movil_ctl.repositories.CtlRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CtlModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCtlRepository(db: AppDatabase): CtlRepository {
        return CtlRepository(db)
    }
}